package solver;

import graphmodel.ColoredNode;
import org.chocosolver.sat.SatFactory;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.variables.impl.FixedBoolVarImpl;
import org.chocosolver.solver.constraints.IIntConstraintFactory;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.impl.BoolVarImpl;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import utils.ColorMapping;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by jules on 16/11/2016.
 */
public class AchroSolver {
    public void solve(SingleGraph g){
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


        for (int k = bInfNbAchro; k <= bInfNbAchro/*bSupNbAchro*/; k++){
            Model model = new Model("All complete coloring of size " + k);
            //le numéro des sommets
            IntVar[] I = model.intVarArray("id domain",N,1, N, false);
            //le domaine des couleurs
            IntVar[] C = model.intVarArray("color vertex domain",N,0, k-1, false);
            //le domaines des arêtes, inutile?
            IntVar[] A = model.intVarArray("edges domain",N*N,0, 1, false);

            //System.out.println("A.size " + A.length);
            //les contraintes
            model.allDifferent(I).post();

            Integer[][] matAdj = new Integer[nbVertexes][nbVertexes];


            //Construction d'une matrice d'adjacence
            int i1 = 0, i2 = 0, idx=0;
            for (Node v1 :g.getNodeSet()){
                ColoredNode s1 = (ColoredNode) v1;
                for (Node v2 :g.getNodeSet()) {
                    ColoredNode s2 = (ColoredNode) v2;
                    if (s1.hasEdgeBetween(s2)){
                        matAdj[i1][i2] = 1;
                        //model.arithm(A[idx], "==", 1).post();
                    }
                    else{
                        matAdj[i1][i2] = 0;
                        //model.arithm(A[idx], "==", 0).post();
                    }

                    i2++;
                    idx++;
                }
                i2=0;
                i1++;
            }
            //Constraint c = IIntConstraintFactory.arithm(, "==", );
            //model.
            //les sommets adjacents n'ont pas la même couleur => couleur prore
            for (int i = 0; i < N ; i++) {
                for (int j = 0; j < N; j++) {
                    if ((i != j) && (matAdj[i][j] == 1))
                        model.arithm(C[i], "!=", C[j]).post();
                }
            }

            //test d'élimination du couleur
            //for (int i = 0; i < N ; i++) {
            //    model.arithm(C[i], "!=", 1).post();
            //}

            //BoolVar a = model.boolVar("a");
            //Pour chaque paire de couleur


            /*
            COntrainte4 = {}
            Pour chaque couleur Ci1
                Contrainte3 = {}
                Pour chaque couleur Ci2
                    Contrainte2 = {};
                    Pour chaque noeud Ni1
                        Contrainte1 + {}
                        Pour chaque Noeud voisin Ni2 de Ni1
                            Contrainte1 = Contrainte1 Union (Ni1==Ci1) ET (Ni2==Ci2)
                        Fin Pour
                        Contrainte2 = Contrainte2 iNTER Contrainte1
                    Fin Pour
                    Contrainte3 = Contrainte3 INTER Contrainte2
                Fin Pour
                Contrainte4 = Contrainte4 INTER Contrainte3
             Fin Pour
             */

            LogOp contrainte4 = null;
            for (int c1 = 0; c1 < k; c1++) {
                LogOp contrainte3 = null;
                for (int c2 = 0; c2 < k; c2++) {
                    LogOp contrainte2 = null;
                    for (Node n1 : g.getNodeSet()) {
                        LogOp contrainte1= null;
                        Iterator<Node> itNode = n1.getNeighborNodeIterator();
                        while (itNode.hasNext()){
                            Node n2 = itNode.next();
                            BoolVar c1n1 = model.boolVar(""+c1+n1);
                            // creation de la contrainte
                            //model.arithm(C[i], "!=", 1).post();

                            BoolVar c2n2 = model.boolVar(""+c2+n2);
                            if (!n1.equals(n2)) {
                                LogOp conj = LogOp.and(c1n1,c2n2);
                                if (contrainte1 == null){
                                    contrainte1 = conj;
                                }
                                else {
                                    contrainte1= LogOp.or(contrainte1,conj);
                                }

                            }

                        }
                        if (contrainte1 != null){
                            if (contrainte2 == null){
                                contrainte2 = contrainte1;
                            }
                            else {
                                contrainte2= LogOp.and(contrainte2,contrainte1);
                            }
                        }

                    }
                    if (contrainte2 != null){
                        if (contrainte3 == null){
                            contrainte3 = contrainte2;
                        }
                        else {
                            contrainte3= LogOp.and(contrainte3,contrainte2);
                        }
                    }
                }

                if (contrainte3 != null){
                    if (contrainte4 == null){
                        contrainte4= contrainte3;
                    }
                    else {
                        contrainte4= LogOp.and(contrainte4,contrainte3);
                    }
                }
            }

            if (contrainte4 != null)
                model.addClauses(contrainte4);


            Solver solver = model.getSolver();
            //TODO regarder les stratégies
            solver.setSearch(Search.minDomLBSearch(C));

            if(solver.solve()){
                System.out.println("All complete coloring of number achromatic :" + k);
                for (int i = 0; i < N - 1; i++) {
                    System.out.println("L'arete "+i+" est de couleur "+ColorMapping.colorsMap[C[i].getValue()]);
                    //g.getNode(i).addAttribute("ui.class", "color" + i);
                    g.getNode(i).addAttribute("ui.style", "fill-color: " + ColorMapping.colorsMap[C[i].getValue()]+";");
                }
                g.display();
            }
            //TODO donner toutes les solutions??! ou c'est juste une permutation?
            //TODO REMOVE
            return;
        }



    }

    public static void testSolver1(){
        int N = 100;
        // 1. Modelling part
        Model model = new Model("all-interval series of size "+ N);
        // 1.a declare the variables
        IntVar[] S = model.intVarArray("s", N, 0, N - 1, false);
        IntVar[] V = model.intVarArray("V", N - 1, 1, N - 1, false);
        // 1.b post the constraints
        for (int i = 0; i < N - 1; i++) {
            model.distance(S[i + 1], S[i], "=", V[i]).post();
        }
        model.allDifferent(S).post();
        model.allDifferent(V).post();
        S[1].gt(S[0]).post();
        V[1].gt(V[N - 2]).post();

        // 2. Solving part
        Solver solver = model.getSolver();
        // 2.a define a search strategy
        solver.setSearch(Search.minDomLBSearch(S));
        if(solver.solve()){
            System.out.printf("All interval series of size %d%n", N);
            for (int i = 0; i < N - 1; i++) {
                System.out.printf("%d <%d> ",
                        S[i].getValue(),
                        V[i].getValue());
            }
            System.out.printf("%d", S[N - 1].getValue());
        }
    }
}
