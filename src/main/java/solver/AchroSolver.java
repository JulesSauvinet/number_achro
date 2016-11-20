package solver;

import graphmodel.ColoredNode;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.limits.TimeCounter;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.impl.FixedIntVarImpl;
import org.chocosolver.util.criteria.Criterion;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import utils.ColorMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jules on 16/11/2016.
 * Cette classe code la résolution de la coloration, 
 *      -> c'est dans cette classe que nous utilisons le solveur.
 * Il faut y décrire le modèle, les données, les sorties et les containtes.
 * Voici les deux principales contraintes :
 *      - la coloration propre
 *      - la coloration complete
 */

public class AchroSolver {

    private final static int TIME_LIMIT = 60;

    public int solve(SingleGraph g){
        boolean hasBeenComplete = false;
        Integer nbEdges = g.getEdgeCount();
        Integer nbVertexes = g.getNodeCount();

        //Determination des bornes pour k, le nombre achromatique
        //La borne sup est le nombre de noeud (grossier)
        Integer bSupNbAchro = g.getNodeSet().size();
        //La borne inf est la taille de la clique maximale (grossier)
        Integer bInfNbAchro = 0;
        //Pour respecter la contrainte de coloration propre
        List<Node> maximalClique = new ArrayList<Node>();
        List<List<Node>> maximalCliques = new ArrayList<>();
        for (List<Node> clique : Toolkit.getMaximalCliques(g)) {
            maximalCliques.add(clique);
            if (clique.size() > maximalClique.size())
                maximalClique = clique;
        }

        bInfNbAchro = maximalClique.size();

        int N = nbVertexes;

        for (int k = bInfNbAchro; k <= bSupNbAchro; k++){
            Model model = new Model("Complete coloring of size " + k);
            //les contraintes
            IntVar[] B = model.intVarArray("the vertex associated with the index has the color c",N, 0,k-1, true);

            //Construction d'une matrice d'adjacence
            int[][] matAdj = Toolkit.getAdjacencyMatrix(g);
            //community?

            //Petite OPTI on a la droit?
            // car pas toutes les solutions avec ça et puis quand la taille augmente ca devient négligeable
            model.arithm(B[Toolkit.randomNode(g).getIndex()],"=",0).post();

            //OPTI SUR Les clique max : fonctionne un petit peu..
            for (List<Node> nodes : maximalCliques){
                int sizeClique = nodes.size();
                IntVar[] C = model.intVarArray("the maximal clique vertices color",sizeClique, 0, Math.max(k-1,sizeClique), true);
                int idx=0;
                for (Node node : nodes){
                    C[idx]=B[node.getIndex()];
                    idx++;
                }
                model.allDifferent(C).post();
            }

            //coloration propre
            //Opti? on a la droit? car pas toutes les solution avec ça et puis quand la taille augmente ca devient negligeable
//            model.arithm(B[maxNode.getIndex()],"=",0).post();

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

            //model.allDifferentUnderCondition();model.among();
            //for (int i=0; i<k; i++)
            //    model.count(i, B, N/k);

            //optimisation 1
            IntVar nValues  = new FixedIntVarImpl("nValues",k,model);
            //model.atLeastNValues(B,nValues,false).post();
            model.atMostNValues(B,nValues,false).post();
            model.setObjective(Model.MAXIMIZE, nValues);
            Solver solver = model.getSolver();
            //solver.setL

            //Limite de 60 secondes
            solver.limitTime(TIME_LIMIT+"s");
            solver.setNoLearning();//(true,false);
            long time = System.currentTimeMillis();
            //TODO regarder les stratégies
            //solver.setSearch(Search.defaultSearch(model));//minDomLBSearch(C));
            //solver.setSearch(Search.activityBasedSearch(B));
            //Vraiment mieux
            solver.setSearch(Search.domOverWDegSearch(B));

            //PROPAGATION de contrainte
            try {
                solver.propagate();
            } catch (ContradictionException e) {
                e.printStackTrace();
            }

            //solver.addStopCriterion()?;

            if(solver.solve()){
                hasBeenComplete = true;
                System.out.println("Une solution a été trouvé pour le nombre achromatique " + k);
                for (int i = 0; i < N ; i++) {
                    int color = B[i].getValue();
                    System.out.println("L'arete "+i+" est de couleur "+ColorMapping.colorsMap[color%32]);
                    //g.getNode(i).addAttribute("ui.class", "color" + i);
                    g.getNode(i).addAttribute("ui.style", "fill-color: " + ColorMapping.colorsMap[color%32]+";");
                }

                //Affichage personnalise selon le graphe
                //clebshLayout(g);

                //Affichage de toutes les solutions : nécessaire? Attention JAVA HEAP SPACE
                /*solver.findAllSolutions();
                solver.showSolutions();
                solver.showStatistics();*/

                //Affichage en continu
                // g.display();
            }
            else {
                int runtime = (int)((System.currentTimeMillis()-time)/1000);
                if (runtime >= TIME_LIMIT){
                    if (k>bInfNbAchro && hasBeenComplete) {
                        int nbachro = k-1;
                        System.out.print("Le solveur n'a pas pu trouver de solutions dans le temps limite de " + (TIME_LIMIT) + " secondes, ");
                        System.out.println("le nombre achromatique est " + "au moins egal a " + nbachro);
                        return k - 1;
                    }
                    else{
                        System.out.println("Le solveur n'a pas pu determiner s'il existait une coloration complete" + " en " + (TIME_LIMIT) +" secondes");
                        return -1;
                    }
                }
                else {
                    if (k > bInfNbAchro && hasBeenComplete) {
                        int nbachro = k - 1;
                        System.out.println("Le nombre achromatique du graphe est " + "egal a " + nbachro);
                        return nbachro;
                    } else if (k == bSupNbAchro) {
                        System.out.println("Le graphe n'admet pas de coloration complete");
                        return -1;
                    }
                }
                //TODO traiter le else
            }

            //TODO donner toutes les solutions? ou c'est juste une permutation?
        }
        return bSupNbAchro;
    }
}
