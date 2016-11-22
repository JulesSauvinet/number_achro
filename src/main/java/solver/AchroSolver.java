package solver;

import graphmodel.ColoredNode;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import utils.ColorMapping;

import java.util.*;

/**
 * Cette classe code la résolution de la coloration, 
 *      -> c'est dans cette classe que nous utilisons le solveur.
 * Il faut y décrire le modèle, les données, les sorties et les contraintes.
 * Voici les deux principales contraintes :
 *      - la coloration propre
 *      - la coloration complete
 * Nous avons ajouté des heuristiques 
 * par exemple pour ordonner le tableau de traitement des noeuds
 * et ainsi traiter en premiers les noeuds au degres le + élevé
 */

public class AchroSolver {

    private final static int TIME_LIMIT = 60;

    public int solve(SingleGraph g){
        boolean hasBeenComplete = false;
        Integer nbVertexes = g.getNodeCount();

        //Determination des bornes pour k, le nombre achromatique
        //La borne sup est le nombre de noeud (grossier)
        Integer bSupNbAchro = g.getNodeSet().size();

        //La borne inf est la taille de la clique maximale (grossier)
        Integer bInfNbAchro = 0;

        //Pour respecter la contrainte de coloration propre
        List<Node> maximalClique = new ArrayList<Node>();
        List<List<Node>> maximalCliques = new ArrayList<>();
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
            maximalCliques.add(clique);
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

        int N = nbVertexes;

        for (int k = bInfNbAchro; k <= bSupNbAchro; k++){
            AchroSolverk solveur = new AchroSolverk(g, k, maximalCliques, sortedNodes);

            if(solveur.solve()){
                hasBeenComplete = true;
                System.out.println("Une solution a été trouvé pour le nombre achromatique " + k);
                for (int i = 0; i < N ; i++) {
                    int color = solveur.B[i].getValue();
                    //System.out.println("Le somment "+i+" a été affecté de la couleur "+ColorMapping.colorsMap[color%32]);
                    //g.getNode(i).addAttribute("ui.class", "color" + i);
                    g.getNode(i).addAttribute("ui.style", "fill-color: " + ColorMapping.colorsMap[color%32]+";");
                }

                //Affichage personnalise selon le graphe
                //clebshLayout(g);

                //Affichage de toutes les solutions : nécessaire?
                /*solver.findAllSolutions();
                solver.showSolutions();
                solver.showStatistics();*/

                //Affichage en continu
                // g.display();
            }
            else {
                int runtime = solveur.runtime;
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
            }
        }

        System.out.println("Le nombre achromatique du graphe est " + "egal a " + bSupNbAchro);
        return bSupNbAchro;
    }
}
