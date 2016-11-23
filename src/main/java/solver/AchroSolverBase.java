package solver;

import graphmodel.ColoredNode;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import utils.ColorMapping;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.variables.BoolVar;

/**
 ** Created by teamgraphe
 *
 * Cette classe code la résolution de la coloration, 
 *      -> c'est dans cette classe que nous utilisons le solveur.
 * Il faut y décrire le modèle, les données, les sorties et les containtes.
 * Voici les deux principales contraintes :
 *      - la coloration propre
 *      - la coloration complete
 */

//TODO créer un deuxieme modele avec une matrice extrait de l'idée presente dans cette classe
public class AchroSolverBase {

    private final static int TIME_LIMIT = 60;

    public int solve(SingleGraph g) {
        // booleen pour savoir si nous avons fini l'attribution de couleurs
        boolean hasBeenComplete = false;
        
        // recuperation de donnees sur le graphe
        Integer nbEdges = g.getEdgeCount();
        Integer nbVertexes = g.getNodeCount();

        //Determination des bornes pour k, le nombre achromatique
        Integer bSupNbAchro = g.getNodeSet().size();
        Integer bInfNbAchro = 0;
        
        //Pour respecter la contrainte de coloration propre
        Integer maxDegree = 0;
        for (Object v :g.getNodeSet()) {
            ColoredNode s = (ColoredNode) v;
            if (s.getDegree() > maxDegree)
                maxDegree = s.getDegree();
        }
        bInfNbAchro = maxDegree;

        int N = nbVertexes;

        for (int k = bInfNbAchro; k <= bSupNbAchro; k++){
            Model model = new Model("All complete coloring of size " + k);

            // On definit le tableau rempli en respectant les contraintes
            BoolVar[] B = model.boolVarArray("is the vertex has the color c", nbEdges*k);

            // On definit une matrice d'adjacence que l'on construit
            Integer[][] matAdj = new Integer[N][N];

            int i1 = 0, i2 = 0;
            for (Node v1 :g.getNodeSet()){
                ColoredNode s1 = (ColoredNode) v1;
                for (Node v2 :g.getNodeSet()) {
                    ColoredNode s2 = (ColoredNode) v2;
                    if (s1.hasEdgeBetween(s2))
                        matAdj[i1][i2] = 1;
                    else
                        matAdj[i1][i2] = 0;
                    i2++;
                }
                i2=0;
                i1++;
            }
            
            // A partir de la matrice d'adjacence on peut coder les contraintes de la coloration propre

            // Chaque sommet ne peut avoir qu'une couleur !!
            for (int i = 0; i < N ; i++) {
                for (int c = 0; c < k; c++){
                    for(int g2 = 0; g2 < k; g2++){
                        if(g2 != c) {
                            Constraint constr = model.arithm(B[(i * k) + c], "!=", B[(i * k) + g2]);
                            model.ifThen(B[(i * k) + c], constr);
                        }
                    }
                }
            }

            // Codage de la coloration propre
            for (int i = 0; i < N-1; i++) { 
                for (int j = i+1; j < N; j++) {
                    for (int c = 0; c < k; c++) {
                        if (matAdj[i][j] == 1) {
                            Constraint constr = model.arithm(B[(i*k)+c], "!=", B[(j*k)+c]);
                            model.ifThen(B[(i*k)+c], constr);
                        }
                    }
                }
            }

            // On verifie ici avec cette contrainte que chaque noeud a bien une couleur a la fin
            for (int i = 0; i < N; i++){
                BoolVar[] BTemp = new BoolVar[k];
                for (int coul=0; coul < k; coul++) {
                    BTemp[coul] = B[i*k+coul];
                }
                model.notAllEqual(BTemp).post();
            }

            int idxN1 = 0;
            int idxN2 = 0;
            LogOp contrainte4 = null;
            for (int c1 = 0; c1 < k; c1++) {
                LogOp contrainte3 = null;
                for (int c2 = 0; c2 < k; c2++) {
                    LogOp contrainte2 = null;
                    idxN1=0;
                    for (Node n1 : g.getNodeSet()) {
                        LogOp contrainte1= null;
                        idxN2=0;
                        for (Node n2 : g.getNodeSet()) {
                            if (matAdj[idxN1][idxN2]==1) {
                                if (!n1.equals(n2)) {
                                    LogOp conj = LogOp.and(B[idxN1*c1], B[idxN2*c2]);
                                    if (contrainte1 == null)
                                        contrainte1 = conj;
                                    else
                                        contrainte1 = LogOp.or(contrainte1, conj);
                                }
                            }
                            idxN2++;
                        }
                        if (contrainte1 != null) {
                            if (contrainte2 == null)
                                contrainte2 = contrainte1;
                            else 
                                contrainte2= LogOp.or(contrainte2,contrainte1);
                        }
                        idxN1++;
                    }
                    if (contrainte2 != null) {
                        if (contrainte3 == null)
                            contrainte3 = contrainte2;
                        else
                            contrainte3= LogOp.and(contrainte3,contrainte2);
                    }
                }

                if (contrainte3 != null) {
                    if (contrainte4 == null)
                        contrainte4= contrainte3;
                    else
                        contrainte4= LogOp.and(contrainte4,contrainte3);
                }
            }

            if (contrainte4 != null)
                model.addClauses(contrainte4);


            Solver solver = model.getSolver();

            if(solver.solve()) {
                System.out.println("All complete coloring of number achromatic :" + k);
                for (int i = 0; i < N; i++) {
                    int color = 0;
                    for (int c = 0; c < k; c++){
                        System.out.println(B[(i*k)+c].getValue());
                        if (B[(i*k)+c].getValue() == 1)
                            color = c;
                    }
                    System.out.println("Le sommet "+i+" est de couleur "+ColorMapping.colorsMap[color]);
                    g.getNode(i).addAttribute("ui.style", "fill-color: " + ColorMapping.colorsMap[color]+";");
                }
                g.display();
            }
            else {
                System.out.println("il n'y a pas de solutions");
            }
        }
        return bSupNbAchro;
    }
}
