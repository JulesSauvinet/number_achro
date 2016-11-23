package solver;

import graphmodel.ColoredGraph;
import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.ParallelPortfolio;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.assignments.DecisionOperator;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMin;
import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.impl.FixedIntVarImpl;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Node;
import search.IntValueSelect;
import search.SearchType;

import static org.chocosolver.solver.search.strategy.Search.intVarSearch;

/**
 ** Created by teamgraphe
 *
 */
public class AchroSolver_k {

    private final static int TIME_LIMIT = 10;

    private ColoredGraph g;
    private int k;

    public int runtime;
    private int N;
    private int[][] matAdj;
    public Integer[] mapping;
    public Integer[] mappingInv;

    Boolean UseConstraintFirstAffectation = true;
    Boolean UseHeuristicMaxClique = true;
    Boolean UseHeuristicNValue = true;
    Boolean UseHeuristicSortedNode = true;

    public AchroSolver_k(ColoredGraph g, boolean UHSN) {
        this.g = g;
        this.N = g.getNodeCount();

        //Construction d'une matrice d'adjacence
        UseHeuristicSortedNode = UHSN;
        if (UseHeuristicSortedNode){
            matAdj = new int[N][N];
            for (int i1=0; i1 < g.getSortedNodes().length; i1++){
                for (int i2=0; i2 < g.getSortedNodes().length; i2++) {
                    int hasEdge = g.getSortedNodes()[i1].hasEdgeBetween(g.getSortedNodes()[i2]) ? 1 : 0;
                    matAdj[i1][i2]= hasEdge;
                }
            }
            this.mapping = new Integer[N];
            this.mappingInv = new Integer[N];

            int cpt2 = 0;
            for (Node sortedNode: g.getSortedNodes()){
                mapping[sortedNode.getIndex()] = cpt2;
                mappingInv[cpt2] = sortedNode.getIndex();
                cpt2++;
            }
        }
        else{
            matAdj = Toolkit.getAdjacencyMatrix(g);
        }
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setUseConstraintFirstAffectation(Boolean UseHeuristicFirstAffectation) {
        this.UseConstraintFirstAffectation = UseHeuristicFirstAffectation;
    }

    public void setUseHeuristicNValue(Boolean UseHeuristicNValue) {
        this.UseHeuristicNValue = UseHeuristicNValue;
    }

    public void setUseHeuristicMaxClique(Boolean UseHeuristicMaxClique) {
        this.UseHeuristicMaxClique = UseHeuristicMaxClique;
    }
    
    public void setUseHeuristicSortedNode(Boolean UseHeuristicSortedNode) {
        this.UseHeuristicSortedNode = UseHeuristicSortedNode;
    }
    /*
    * Optimisation    
    */
    private void ConstraintFirstAffectation(Model model, IntVar[] B){
        model.arithm( B[g.getSortedNodes()[0].getIndex()],"=",0).post();
    }

    private void heuristicMaxClique(Model model, IntVar[] B){
        for (List<Node> nodes : g.getMaximalCliques()){
            int sizeClique = nodes.size();
            IntVar[] C = model.intVarArray("the maximal clique vertices color",sizeClique, 0, Math.max(k-1,sizeClique), true);
            int idx=0;
            for (Node node : nodes){
                if (UseHeuristicSortedNode){
                    C[idx]=B[mapping[node.getIndex()]];
                }
                else{
                    C[idx]=B[node.getIndex()];
                }
                idx++;
            }
            model.allDifferent(C).post();
        }
    }

    private void heuristicNValue(Model model, IntVar[] B){
        IntVar nValues  = new FixedIntVarImpl("nValues",k,model);
        //model.atLeastNValues(B,nValues,false).post();
        model.atMostNValues(B,nValues,false).post();
        model.setObjective(Model.MAXIMIZE, nValues);
    }

    private void constraintProperColoring(Model model, IntVar[] B){
        // On code ici la coloration propre
        // si deux noeuds sont voisins, ils ne peuvent pas avoir la meme couleur
        for (int i = 0; i < N-1 ; i++) { // pour chaque noeud
            for (int j = 0; j < N ; j++) { // pour chaque noeud voisin
                if (matAdj[i][j] == 1 && i != j) {
                    Constraint constr = model.arithm(B[i], "!=", B[j]);
                    constr.post();
                }
            }
        }
    }

    private void constraintCompleteColoring(Model model, IntVar[] B){
        // On code ici la coloration complete
        int idxN1 = 0, idxN2 = 0;
        Constraint constraint4 = null;
        for (int c1 = 0; c1 < k-1; c1++) {
            Constraint constraint3 = null;
            for (int c2 = c1+1; c2 < k; c2++) {
                Constraint constraint2 = null;
                idxN1 = 0;
                for (Node n1 : g.getNodeSet()) {
                    Constraint constraint1= null;
                    idxN2 = 0;
                    for (Node n2 : g.getNodeSet()) {
                        if (matAdj[idxN1][idxN2]==1) {
                            Constraint c1n1 = model.arithm(B[idxN1], "=", c1);
                            Constraint c2n2 = model.arithm(B[idxN2], "=", c2);
                            if (!n1.equals(n2)) {
                                Constraint conj = model.and(c1n1, c2n2);
                                if (constraint1 == null) {
                                    constraint1 = conj;
                                }
                                else {
                                    constraint1 = model.or(constraint1, conj);
                                }
                            }
                        }
                        idxN2++;
                    }
                    if (constraint1 != null){
                        if (constraint2 == null){
                            constraint2 = constraint1;
                        }
                        else {
                            constraint2= model.or(constraint2,constraint1);
                        }
                    }
                    idxN1++;
                }
                if (constraint2 != null){
                    if (constraint3 == null){
                        constraint3 = constraint2;
                    }
                    else {
                        constraint3 = model.and(constraint3,constraint2);
                    }
                }
            }

            if (constraint3 != null){
                constraint3.post();
            }
        }
    }

    Model bestModel = null;
    
    public Boolean solve(){
        return solve(SearchType.DEFAULT);
    }

    public Boolean solve(SearchType strategy){
        
        System.out.println("Recherche d'une solution pour le nombre achromatique " + k);

        long time = System.currentTimeMillis();

        ParallelPortfolio portfolio = new ParallelPortfolio();

        /*for(SearchType st : SearchType.values()){
            portfolio.addModel(makeModel(st));
        }*/

        portfolio.addModel(makeModel(strategy));

        Boolean res = portfolio.solve();
        runtime =  (int)((System.currentTimeMillis()-time)/1000);


        if (res){
            bestModel = portfolio.getBestModel();
            printStat();
        }
        
        return res;
    }

    private Model makeModel(SearchType st) {
        Model model;
        model = new Model("Complete coloring of size " + k);
        IntVar[] B = model.intVarArray("the vertex associated with the index has the color c",N, 0,k-1, true);

        constraintProperColoring(model , B);

        constraintCompleteColoring(model , B);

        //OPTI1
        if(UseHeuristicNValue)
            heuristicNValue(model , B);

        //Petite OPTI on a la droit?
        if(UseConstraintFirstAffectation)
            ConstraintFirstAffectation(model , B);

        //Contraintes sur les cliques
        if(UseHeuristicMaxClique)
            heuristicMaxClique(model , B);

        Solver solver = model.getSolver();

        //Limite de 60 secondes
        solver.limitTime(TIME_LIMIT+"s");
        solver.setNoLearning();//(true,false);


        //TODO regarder les stratégies
        switch (st){
            case DEFAULT:
                solver.setSearch(Search.defaultSearch(model));
                break;
            case GREEDY:
                solver.setSearch(Search.domOverWDegSearch(B));
                break;
            case ACTIVITY:
                solver.setSearch(Search.activityBasedSearch(B));
                break;
            case MINDOM:
                solver.setSearch(Search.minDomLBSearch(B));
                break;
            case CUSTOM:
                solver.setSearch(intVarSearch(
                        // variable selector
                        (VariableSelector<IntVar>) variables -> {
                            for(IntVar v:variables){
                                if(!v.isInstantiated()){
                                    return v;
                                }
                            }
                            return null;
                        },
                        // value selector
                        new IntValueSelect(B),
                        // variables to branch on
                        B
                ));
                break;
            case INTVAR:
                solver.setSearch(intVarSearch(
                        // selects the variable of smallest domain size
                        new FirstFail(model),
                        // selects the smallest domain value (lower bound)
                        new IntDomainMin(),
                        // apply equality (var = val)
                        DecisionOperator.int_eq,
                        // variables to branch on
                        B
                ));
                break;
            case MAXCONSTRAINTS:
                solver.setSearch(intVarSearch(
                        // variable selector
                        (VariableSelector<IntVar>) variables -> {
                            int maxCount = Integer.MIN_VALUE;
                            int maxVar = -1;
                            for(int i = 0; i < variables.length; i++){
                                if(!variables[i].isInstantiated()){
                                    int countInstanciatedNeighbours = 0;
                                    for (int j = 0; j < variables.length; j++) {
                                        if(i != j && matAdj[i][j] == 1 && variables[j].isInstantiated()){
                                            countInstanciatedNeighbours++;
                                        }
                                    }
                                    if(countInstanciatedNeighbours > maxCount){
                                        maxCount = countInstanciatedNeighbours;
                                        maxVar = i;
                                    }
                                }
                            }
                            if(maxVar != -1){
                                return variables[maxVar];
                            } else{
                                return null;
                            }
                        },
                        // value selector
                        new IntValueSelect(B),
                        // variables to branch on
                        B
                ), Search.defaultSearch(model));
        }
        //propagation de contraintes
        try {
            solver.propagate();
        } catch (ContradictionException e) {
            e.printStackTrace();
        }
        return model;
    }

    private void printStat() {
        bestModel.getSolver().showStatistics();
        bestModel.getSolver().printStatistics();
    }
}