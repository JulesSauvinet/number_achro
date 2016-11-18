package solver;

import graphmodel.ColoredNode;
import org.chocosolver.sat.SatFactory;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import utils.ColorMapping;

import java.util.Arrays;

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

        Integer[][] matAdj = new Integer[nbVertexes][nbVertexes];

        //Construction d'une matrice d'adjacence
        int i1 = 0, i2 = 0;
        for (Object v1 :g.getNodeSet()){
            ColoredNode s1 = (ColoredNode) v1;
            for (Object v2 :g.getNodeSet()) {
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


        System.out.println(Arrays.deepToString(matAdj));
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
            //le numéro des sommets
            IntVar[] I = model.intVarArray("id domain",N,1, N, false);
            //le domaine des couleurs
            IntVar[] C = model.intVarArray("color vertex domain",N,1, N, false);
            //le domaines des arêtes
            //BoolVar[] V = model.boolVarArray("edges domain",nbEdges);

            //les contraintes
            model.allDifferent(I).post();

            for (int i = 0; i < N ; i++) {
                for (int j = 0; j < N; j++) {
                    //OK de faire pas CSP les aretes?
                    if ((i != j) && (matAdj[i][j] == 1))
                        model.arithm(C[i], "!=", C[j]).post();
                }
            }
            for (int i = 0; i < N ; i++) {
                for (int j = 0; j < N; j++) {
                    //model.addClauses(and(nand(nor(a, b), or(c, d)), e), model);
                }
            }

            Solver solver = model.getSolver();
            //TODO regarder les stratégies
            solver.setSearch(Search.minDomLBSearch(C));

            if(solver.solve()){
                System.out.println("All complete coloring of number achromatic :" + k);
                for (int i = 0; i < N - 1; i++) {
                    System.out.println("L'arete "+i+" est de couleur "+ColorMapping.colorsMap[C[i].getValue()%16]);
                    //g.getNode(i).addAttribute("ui.class", "color" + i);
                    g.getNode(i).addAttribute("ui.style", "fill-color: " + ColorMapping.colorsMap[C[i].getValue()%16]+";");
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
