package solver;

import graphmodel.ColoredGraph;

import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.ParallelPortfolio;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.nary.nValue.PropAtMostNValues;
import org.chocosolver.solver.constraints.nary.nValue.PropAtMostNValues_BC;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.assignments.DecisionOperator;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMin;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainRandom;
import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;
import org.chocosolver.solver.variables.impl.FixedIntVarImpl;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Node;
import search.IntValueSelect;
import search.SearchType;

import static org.chocosolver.solver.search.strategy.Search.defaultSearch;
import static org.chocosolver.solver.search.strategy.Search.intVarSearch;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainRandom;

/**
 ** Created by teamgraphe
 *
 */
public class AchroSolver_k {

    private final static int TIME_LIMIT = 60;

    private ColoredGraph g;
    private int k;

    public int runtime;
    private int N;
    private int[][] matAdj;

    Boolean UseHeuristicMaxClique = true;
    Boolean UseHeuristicNValue = true;

    public AchroSolver_k(ColoredGraph g) {
        this.g = g;
        this.N = g.getNodeCount();

        this.matAdj = Toolkit.getAdjacencyMatrix(g);
    }

    public void setK(int k) {
        this.k = k;
    }


    public void setUseHeuristicNValue(Boolean UseHeuristicNValue) {
        this.UseHeuristicNValue = UseHeuristicNValue;
    }

    public void setUseHeuristicMaxClique(Boolean UseHeuristicMaxClique) {
        this.UseHeuristicMaxClique = UseHeuristicMaxClique;
    }


    private void heuristicMaxClique(Model model, IntVar[] B){
        for (List<Node> nodes : g.getMaximalCliques()){
            int sizeClique = nodes.size();
            IntVar[] C = model.intVarArray("the maximal clique vertices color",sizeClique, 0, Math.max(k-1,sizeClique), true);
            int idx=0;
            for (Node node : nodes){
                C[idx]=B[node.getIndex()];
                idx++;
            }
            model.allDifferent(C).post();
        }
    }

    private void heuristicNValue(Model model, IntVar[] B){
        IntVar nValues  = new FixedIntVarImpl("nValues",k,model);
        model.atLeastNValues(B,nValues,true).post();
        model.atMostNValues(B,nValues,true).post();
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
            //printStat();
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
            case DOMOVERWDEG:
                solver.setSearch(Search.domOverWDegSearch(B));
                break;
            case ACTIVITY:
                solver.setSearch(Search.activityBasedSearch(B));
                break;
            case MINDOM:
                solver.setSearch(Search.minDomLBSearch(B));
                break;
            case GREEDY:
                solver.setSearch(Search.greedySearch(defaultSearch(model)));
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
                        new IntDomainMin(),
//                        new IntValueSelect(B),

                        // variables to branch on
                        B
                ), Search.defaultSearch(model));
            case RANDOM:
                solver.setSearch(intVarSearch(
                        // variable selector
                        (VariableSelector<IntVar>) variables -> {
                            IntVar next = null;
                            for(int i = 0; i < variables.length; i++){
                                if(!variables[i].isInstantiated()){
                                    next = variables[i];break;
                                }
                            }
                            if(next == null){
                                return null;
                            }
                            return next;
                        },
                        // value selector
                        new IntDomainRandom(242353595353L),
                        // variables to branch on
                        B
                ), Search.defaultSearch(model));
                break;
            case MAXCONS_RAND:
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
                        new IntDomainRandom(242353595353L),

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