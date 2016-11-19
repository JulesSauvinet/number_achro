package solver;

import graphmodel.ColoredNode;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import utils.ColorMapping;

/**
 * Created by jules on 16/11/2016.
 */
public class AchroSolver {

    /**
     *
     * @param g
     * @return achromatic number
     */
    public int solve(SingleGraph g){
        Integer bSupNbAchro = g.getNodeSet().size();
        Integer bInfNbAchro = 0;
        Integer nbEdges = g.getEdgeCount();
        Integer nbVertexes = g.getNodeCount();

        Integer maxDegree = 0;

        //Pour respecter la contrainte de coloration propre
        for (Object v :g.getNodeSet()){
            ColoredNode s = (ColoredNode) v;
            if (s.getDegree() > maxDegree)
                maxDegree = s.getDegree();
        }

        bInfNbAchro = maxDegree;

        int N = nbVertexes;

        for (int k = bInfNbAchro; k <= bSupNbAchro; k++){
            Model model = new Model("All complete coloring of size " + k);
            //les contraintes
            IntVar[] B = model.intVarArray("the vertex associated with the index has the color c",nbVertexes, 0,k-1, false);


            Integer[][] matAdj = new Integer[N][N];
            //Construction d'une matrice d'adjacence
            int i1 = 0, i2 = 0;
            for (Node v1 :g.getNodeSet()){
                ColoredNode s1 = (ColoredNode) v1;
                for (Node v2 :g.getNodeSet()) {
                    ColoredNode s2 = (ColoredNode) v2;
                    if (s1.hasEdgeBetween(s2)){
                        matAdj[i1][i2] = 1;
                    }
                    else{
                        matAdj[i1][i2] = 0;
                    }

                    i2++;
                }
                i2=0;
                i1++;
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

            Solver solver = model.getSolver();
            //TODO regarder les stratégies
            solver.setSearch(Search.defaultSearch(model));//minDomLBSearch(C));

            if(solver.solve()){
                System.out.println("All complete coloring of number achromatic :" + k);
                for (int i = 0; i < N ; i++) {
                    int color = B[i].getValue();
                    System.out.println("L'arete "+i+" est de couleur "+ColorMapping.colorsMap[color%32]);
                    //g.getNode(i).addAttribute("ui.class", "color" + i);
                    g.getNode(i).addAttribute("ui.style", "fill-color: " + ColorMapping.colorsMap[color%32]+";");
                }
//                g.display();
            }
            else {
                System.out.println("No higher solution for this graph");
                return k - 1;
            }
            //TODO donner toutes les solutions??! ou c'est juste une permutation?
        }
	return 0;
    }
}
