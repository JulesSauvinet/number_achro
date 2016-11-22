package solver;

import graphmodel.ColoredNode;
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

import java.util.*;

/**
 * Created by Marine
 */
public class AchroSolverOrdered {
    private final static int TIME_LIMIT = 60;

    public int solve(SingleGraph g){
        boolean hasBeenComplete = false;
        Integer nbEdges = g.getEdgeCount();
        Integer nbVertexes = g.getNodeCount();

        //Determination des bornes pour k, le nombre achromatique
        Integer bSupNbAchro = g.getNodeSet().size();
        Integer bInfNbAchro = 0;
        
        //Pour respecter la contrainte de coloration propre        
        List<Node> maximalClique = new ArrayList<>();
        List<List<Node>> maximalesCliques = new ArrayList<>();
        List<ColoredNode> unionClique = new ArrayList<>();
        int maxAppClique = 0;
        boolean getMax = true;
        ColoredNode maxNode = null;
        
        for (List<Node> clique : Toolkit.getMaximalCliques(g)) {
            if (getMax){
                maxNode = (ColoredNode) clique.get(0);
                getMax = false;
            }
            if (clique.contains(maxNode))
                maxAppClique++;
            maximalesCliques.add(clique);
            System.out.println("maxclique"+ clique.size());
            if (clique.size() > maximalClique.size())
                maximalClique = clique;
            for (Node node : clique){
                ColoredNode cNode = (ColoredNode) node;
                if (!cNode.equals(maxNode))
                    unionClique.add(cNode);
            }
        }
        bInfNbAchro = maximalClique.size();

        ColoredNode[] sortedNodes = new ColoredNode[nbVertexes];
        int cpt=0;
        for (Node node : g.getNodeSet()){
            sortedNodes[cpt]=(ColoredNode) node;
            cpt++;
        }
        Arrays.sort(sortedNodes, (o1, o2) -> {
            if (o1.getDegree()>o2.getDegree()){
                return -1;
            }
            else if (o1.getDegree()<o2.getDegree()){
                return 1;
            }
            return 0;
        });
        //System.out.println(Arrays.toString(sortedNodes));

        int N = nbVertexes;

        // mapping permet de relier le sommet du rang i du tableau ordonné au sommet de rang j du graphe G
        // mapping2 permet de relier le sommet du rang i du graphe G au sommet de rang j du tableau ordonné       
        Integer[] mapping = new Integer[nbVertexes];
        Integer[] mapping2 = new Integer[nbVertexes];
        int cpt2 = 0;
        for (Node sortedNode: sortedNodes){
            mapping[sortedNode.getIndex()] = cpt2;
            mapping2[cpt2] = sortedNode.getIndex();
            cpt2++;
        }

        for (int k = bInfNbAchro; k <= bSupNbAchro; k++){
            Model model = new Model("Complete coloring of size " + k);
            
            //les contraintes
            IntVar[] B = model.intVarArray("the vertex associated with the index has the color c",N, 0,k-1, true);

            //Construction d'une matrice d'adjacence
            int[][] matAdj = Toolkit.getAdjacencyMatrix(g);
            for (int i1=0; i1 < sortedNodes.length; i1++){
                for (int i2=0; i2 < sortedNodes.length; i2++) {
                    int hasEdge = sortedNodes[i1].hasEdgeBetween(sortedNodes[i2]) ? 1 : 0;
                    matAdj[i1][i2]= hasEdge;
                }
            }
            model.arithm(B[sortedNodes[0].getIndex()],"=",0).post();

            for (List<Node> nodes : maximalesCliques){
                int sizeClique = nodes.size();
                IntVar[] C = model.intVarArray("the maximal clique vertices color",sizeClique, 0, Math.max(k-1,sizeClique), true);
                int idx=0;
                for (Node node : nodes){
                    C[idx]=B[mapping[node.getIndex()]];
                    idx++;
                }
                model.allDifferent(C).post();
            }

            //coloration propre
            for (int i = 0; i < N-1 ; i++) { // pour chaque noeud
                for (int j = 0; j < N ; j++) { // pour chaque couleur
                    if (matAdj[i][j]==1 && i != j) {
                        Constraint constr = model.arithm(B[i], "!=", B[j]);
                        constr.post();
                    }
                }
            }

            //coloration complete
            int idxN1 = 0;
            int idxN2 = 0;
            Constraint contrainte4 = null;
            for (int c1 = 0; c1 < k-1; c1++) {
                Constraint contrainte3 = null;
                for (int c2 = c1+1; c2 < k; c2++) {
                    Constraint contrainte2 = null;
                    idxN1=0;
                    for (Node n1 : g.getNodeSet()) {
                        Constraint contrainte1= null;
                        idxN2=0;
                        for (Node n2 : g.getNodeSet()) {
                            if (matAdj[idxN1][idxN2]==1) {
                                Constraint c1n1 = model.arithm(B[idxN1], "=", c1);
                                Constraint c2n2 = model.arithm(B[idxN2], "=", c2);
                                if (!n1.equals(n2)) {
                                    Constraint conj = model.and(c1n1, c2n2);
                                    if (contrainte1 == null)
                                        contrainte1 = conj;
                                    else
                                        contrainte1 = model.or(contrainte1, conj);
                                }
                            }
                            idxN2++;
                        }
                        if (contrainte1 != null) {
                            if (contrainte2 == null)
                                contrainte2 = contrainte1;
                            else
                                contrainte2= model.or(contrainte2,contrainte1);
                        }
                        idxN1++;
                    }
                    if (contrainte2 != null) {
                        if (contrainte3 == null)
                            contrainte3 = contrainte2;
                        else
                            contrainte3= model.and(contrainte3,contrainte2);
                    }
                }

                if (contrainte3 != null)
                    contrainte3.post();
            }

            //optimisation 1
            IntVar nValues  = new FixedIntVarImpl("nValues",k,model);
            
            //model.atLeastNValues(B,nValues,false).post();
            model.atMostNValues(B,nValues,false).post();
            model.setObjective(Model.MAXIMIZE, nValues);

            Solver solver = model.getSolver();

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
                    g.getNode(mapping2[i]).addAttribute("ui.style", "fill-color: " + ColorMapping.colorsMap[color%32]+";");
                }
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