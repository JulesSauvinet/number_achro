package solver;

import graphmodel.ColoredNode;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.impl.FixedIntVarImpl;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import utils.ColorMapping;

/**
 * Created by jules on 16/11/2016.
 * Dans cette classe, il faut coder les deux principales contraintes :
 *      - la coloration propre
 *      - la coloration complete
 */

public class AchroSolver {
    public int solve(SingleGraph g){
        boolean hasBeenComplete = false;
        Integer bSupNbAchro = g.getNodeSet().size();
        Integer bInfNbAchro = 0;
        Integer nbEdges = g.getEdgeCount();
        Integer nbVertexes = g.getNodeCount();

        Integer maxDegree = 0;

        ColoredNode maxNode = null;
        //Pour respecter la contrainte de coloration propre
        for (Object v :g.getNodeSet()){
            ColoredNode s = (ColoredNode) v;
            if (s.getDegree() > maxDegree) {
                maxDegree = s.getDegree();
                maxNode = s;
            }
        }

        bInfNbAchro = maxDegree;

        int N = nbVertexes;

        for (int k = bInfNbAchro; k <= bSupNbAchro; k++){
            Model model = new Model("All complete coloring of size " + k);
            //les contraintes
            IntVar[] B = model.intVarArray("the vertex associated with the index has the color c",N, 0,k-1, true);

            for (Node v1 :g.getNodeSet()){

            }

            //IntVar[] CNS = model.intVarArray("the nValues of the vertexes",nbVertexes, 0,k-1, true);

            Integer[][] matAdj = new Integer[N][N];
            //Construction d'une matrice d'adjacence
            int i1 = 0, i2 = 0, i3=0;
            for (Node v1 :g.getNodeSet()){
                ColoredNode s1 = (ColoredNode) v1;
                //IntVar[] CN = model.intVarArray("the color of the neighbours",v1.getDegree(), 0,k-1, false);
                i3=0;
                for (Node v2 :g.getNodeSet()) {

                    ColoredNode s2 = (ColoredNode) v2;
                    if (s1.hasEdgeBetween(s2)){
                        //CN[i3] = B[i2]; i3++;
                        matAdj[i1][i2] = 1;
                    }
                    else{
                        matAdj[i1][i2] = 0;
                    }
                    i2++;
                }
                //OPTI 2 NE SERT A RIEN
                /*IntVar nValues = new FixedIntVarImpl("nValuesi",v1.getDegree(),model);
                model.atMostNValues(CN, nValues,true).post();
                model.setObjective(Model.MAXIMIZE,nValues);*/
                i2=0;
                i1++;
            }

            //Opti? on a la droit? car pas toutes les solution avec ça et puis quand la taille augmente ca devient néegligeable
            model.arithm(B[maxNode.getIndex()],"=",0).post();

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
                                    if (contrainte1 == null) {
                                        contrainte1 = conj;
                                    } else {
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
                            contrainte3= model.and(contrainte3,contrainte2);
                        }
                    }
                }

                if (contrainte3 != null){
                    contrainte3.post();
                }
            }

            //OPTI 1
            IntVar nValues  = new FixedIntVarImpl("nValues",k,model);
            //model.atLeastNValues(B,nValues,false).post();
            model.atMostNValues(B,nValues,false).post();
            model.setObjective(Model.MAXIMIZE, nValues);

            Solver solver = model.getSolver();
            //TODO regarder les stratégies
            solver.setSearch(Search.defaultSearch(model));//minDomLBSearch(C));

            //PROPAGATION
            try {
                solver.propagate();
            } catch (ContradictionException e) {
                e.printStackTrace();
            }
            solver.limitTime("60s");

            if(solver.solve()){
                hasBeenComplete = true;
                //TODO find all the complete coloring and display all the solutions
                System.out.println("All complete coloring of number achromatic :" + k);
                for (int i = 0; i < N ; i++) {
                    int color = B[i].getValue();
                    //System.out.println("L'arete "+i+" est de couleur "+ColorMapping.colorsMap[color%32]);
                    //g.getNode(i).addAttribute("ui.class", "color" + i);
                    g.getNode(i).addAttribute("ui.style", "fill-color: " + ColorMapping.colorsMap[color%32]+";");
                }
                //clebshLayout(g);
                //JAVA HEAP SPACE!
                /*solver.findAllSolutions();
                solver.showSolutions();
                solver.showStatistics();*/
//                g.display();
            }else if(solver.hasEndedUnexpectedly()){
                if (k>bInfNbAchro) {
                    int nbachro = k-1;
                    System.out.println("Le solveur n'a pas pu trouvé de solutions dans le temps limite; le nombre achromatique est " +
                            "au moins egal a " + nbachro);
                }
                else{
                    System.out.println("le solveur n'a pas pu determiner s'il existait une coloration complete");
                }
                return k - 1;
            }
            else {
                if (k>bInfNbAchro && hasBeenComplete) {
                    int nbachro = k-1;
                    System.out.println("Le nombre achromatique du graphe est " +
                            "egal a " + nbachro);
                    return nbachro;
                }
                else if (hasBeenComplete){
                    System.out.println("le graphe n'admet pas de coloration complete");
                    return k - 1;
                }
                else if (k==bSupNbAchro){
                    System.out.println("le graphe n'admet pas de coloration complete");
                    return -1;
                }
                //TODO traiter le else
            }

            //TODO donner toutes les solutions??! ou c'est juste une permutation?
        }
	return bSupNbAchro;
    }


    public static void clebshLayout(SingleGraph g){
        /*Test*/
        g.getNode(0).setAttribute("xyz", 4, 0, 0);
        g.getNode(1).setAttribute("xyz", 16, 0, 0);
        g.getNode(2).setAttribute("xyz", 20, 11.5, 0);
        g.getNode(3).setAttribute("xyz", 10, 20, 0);
        g.getNode(4).setAttribute("xyz", 0, 11.5, 0);

        g.getNode(9).setAttribute("xyz", 10,1, 0);
        g.getNode(7).setAttribute("xyz", 17, 6.5, 0);
        g.getNode(8).setAttribute("xyz", 14, 15, 0);
        g.getNode(6).setAttribute("xyz", 6, 15, 0);
        g.getNode(5).setAttribute("xyz", 3, 6.5, 0);

        g.getNode(10).setAttribute("xyz", 9, 7.5, 0);
        g.getNode(11).setAttribute("xyz", 11, 7.5, 0);

        g.getNode(12).setAttribute("xyz", 8, 10, 0);
        g.getNode(13).setAttribute("xyz", 12, 10, 0);
        g.getNode(14).setAttribute("xyz", 10, 12, 0);

        g.getNode(15).setAttribute("xyz", 10, 9.5, 0);

        /*g.getNode(0).setAttribute("xyz", 0, 4, 0);
        g.getNode(1).setAttribute("xyz", 1, 5, 0);
        g.getNode(2).setAttribute("xyz", 2, 6, 0);
        g.getNode(3).setAttribute("xyz", 3, 7, 0);
        g.getNode(4).setAttribute("xyz", 4, 8, 0);
        g.getNode(9).setAttribute("xyz", 5, 7, 0);
        g.getNode(7).setAttribute("xyz", 6, 6, 0);
        g.getNode(8).setAttribute("xyz", 7, 5, 0);
        g.getNode(6).setAttribute("xyz", 8, 4, 0);
        g.getNode(5).setAttribute("xyz", 7, 3, 0);
        g.getNode(10).setAttribute("xyz", 6, 2, 0);
        g.getNode(11).setAttribute("xyz", 5, 1, 0);
        g.getNode(12).setAttribute("xyz", 4, 0, 0);
        g.getNode(13).setAttribute("xyz", 3, 1, 0);
        g.getNode(14).setAttribute("xyz", 2, 2, 0);
        g.getNode(15).setAttribute("xyz", 1, 3, 0);*/
        g.display(false);
    }
}
