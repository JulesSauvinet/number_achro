/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solver;

import graphmodel.ColoredNode;
import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.impl.FixedIntVarImpl;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import utils.ColorMapping;

/**
 *
 * @author howard
 */
public class AchroSolverk {
    
    private final static int TIME_LIMIT = 10;
    
    private SingleGraph g;
    private int k;
    
    public Model model;
    public IntVar[] B;
    
    public int runtime;
    private int N;
    private int[][] matAdj;
    private List<List<Node>> maximalCliques;
    private ColoredNode[] sortedNodes;
    

    public AchroSolverk(SingleGraph g, int N, List<List<Node>> maximalCliques,ColoredNode[] sortedNodes) {
        this.g = g;
        this.N = N;
        this.maximalCliques = maximalCliques;
        this.sortedNodes =sortedNodes;
        
        
        //Construction d'une matrice d'adjacence
        matAdj = Toolkit.getAdjacencyMatrix(g);
    }
    Boolean UseHeuristicFirstAffectation = true;
    Boolean UseHeuristicMaxClique = true;
    Boolean UseHeuristicNValue = true;

    public void setK(int k) {
        this.k = k;
    }
    

    public void setUseHeuristicFirstAffectation(Boolean UseHeuristicFirstAffectation) {
        this.UseHeuristicFirstAffectation = UseHeuristicFirstAffectation;
    }

    public void setUseHeuristicNValue(Boolean UseHeuristicNValue) {
        this.UseHeuristicNValue = UseHeuristicNValue;
    }

    public void setUseHeuristicMaxClique(Boolean UseHeuristicMaxClique) {
        this.UseHeuristicMaxClique = UseHeuristicMaxClique;
    }
    
    
    private void heuristicFirstAffectation(){
        model.arithm(B[sortedNodes[0].getIndex()],"=",0).post();
    }
    private void heuristicMaxClique(){
        for (List<Node> nodes : maximalCliques){
            int sizeClique = nodes.size();
            IntVar[] C = model.intVarArray("the maximal clique vertices color",sizeClique, 0, Math.max(k-1,sizeClique), true);
            int idx=0;
            for (Node node : nodes){
                C[idx]=B[/*mapping[*/node.getIndex()/*]*/];
                idx++;
            }
            model.allDifferent(C).post();
        }
    }    
    private void heuristicNValue(){
        IntVar nValues  = new FixedIntVarImpl("nValues",k,model);
        //model.atLeastNValues(B,nValues,false).post();
        model.atMostNValues(B,nValues,false).post();
        model.setObjective(Model.MAXIMIZE, nValues);
    }
    
    private void constraintProperColoring(){
        // On code ici la coloration propre
        // si deux noeuds sont voisins, ils ne peuvent pas avoir la meme couleur
        for (int i = 0; i < N-1 ; i++) { // pour chaque noeud
            for (int j = 0; j < N ; j++) { // pour chaque couleur
                if (matAdj[i][j] == 1 && i != j) {
                    Constraint constr = model.arithm(B[i], "!=", B[j]);
                    constr.post();
                }
            }
        }
    }
    
    private void constraintCompleteColoring(){
        // On code ici la coloration complete
        int idxN1 = 0, idxN2 = 0;
        Constraint contrainte4 = null;
        for (int c1 = 0; c1 < k-1; c1++) {
            Constraint contrainte3 = null;
            for (int c2 = c1+1; c2 < k; c2++) {
                Constraint contrainte2 = null;
                idxN1 = 0;
                for (Node n1 : g.getNodeSet()) {
                    Constraint contrainte1= null;
                    idxN2 = 0;
                    for (Node n2 : g.getNodeSet()) {
                        if (matAdj[idxN1][idxN2]==1) {
                            Constraint c1n1 = model.arithm(B[idxN1], "=", c1);
                            Constraint c2n2 = model.arithm(B[idxN2], "=", c2);
                            if (!n1.equals(n2)) {
                                Constraint conj = model.and(c1n1, c2n2);
                                if (contrainte1 == null) {
                                    contrainte1 = conj;
                                } 
                                else {
                                    contrainte1 = model.or(contrainte1, conj);
                                }
                            }
                        }
                        idxN2++;
                    }
                    if (contrainte1 != null){
                        if (contrainte2 == null){
                            contrainte2 = contrainte1;
                        }
                        else {
                            contrainte2= model.or(contrainte2,contrainte1);
                        }
                    }
                    idxN1++;
                }
                if (contrainte2 != null){
                    if (contrainte3 == null){
                        contrainte3 = contrainte2;
                    }
                    else {
                        contrainte3 = model.and(contrainte3,contrainte2);
                    }
                }
            }

            if (contrainte3 != null){
                contrainte3.post();
            }
        }
    }
    
    public Boolean solve(){
        
        this.model = new Model("Complete coloring of size " + k);        
        this.B = model.intVarArray("the vertex associated with the index has the color c",N, 0,k-1, true);

       

        constraintProperColoring();

        constraintCompleteColoring();



        //optimisation 1
        if(UseHeuristicNValue) heuristicNValue();
       
        
         //Petite OPTI on a la droit?
        // car pas toutes les solutions avec ça et puis quand la taille augmente ca devient négligeable
        if(UseHeuristicFirstAffectation)   heuristicFirstAffectation();


        //OPTI SUR Les clique max : fonctionne un petit peu..
        if(UseHeuristicMaxClique) heuristicMaxClique();
        

        Solver solver = model.getSolver();

        //Limite de 60 secondes
        solver.limitTime(TIME_LIMIT+"s");
        solver.setNoLearning();//(true,false);
        //Vraiment mieux
        solver.setSearch(Search.domOverWDegSearch(B));
        
        long time = System.currentTimeMillis();
        //TODO regarder les stratégies
        //solver.setSearch(Search.defaultSearch(model));//minDomLBSearch(C));
        //solver.setSearch(Search.activityBasedSearch(B));
        
        //solver.setSearch(Search.inputOrderLBSearch(B));

        //PROPAGATION de contrainte
        try {
            solver.propagate();
        } catch (ContradictionException e) {
            e.printStackTrace();
        }

        //solver.addStopCriterion()?;

        Boolean res = solver.solve();
        runtime =  (int)((System.currentTimeMillis()-time)/1000);
        return res;
    }
    
    
    
    
    
    
}
