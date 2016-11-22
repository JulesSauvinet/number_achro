package solver;

import graphmodel.ColoredNode;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import utils.ColorMapping;

import java.util.*;

/**
 * Created by jules on 16/11/2016.
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

public class AchroSolverHeuris {

    private final static int TIME_LIMIT = 60;
    private AchroSolverk solveur;
    
    
    
    boolean hasBeenComplete = false;
    
    //La borne inf est la taille de la clique maximale (grossier)
    Integer bInfNbAchro = 0;
    //Pour respecter la contrainte de coloration propre
    List<Node> maximalClique = new ArrayList<Node>();
    List<List<Node>> maximalCliques = new ArrayList<>();
    List<ColoredNode> unionClique = new ArrayList<>();
    int maxAppClique = 0;
    boolean getMax = true;
    ColoredNode maxNode = null;
    
    Integer nbEdges ;
    Integer nbVertexes ;
    int N;

    //Determination des bornes pour k, le nombre achromatique
    //La borne sup est le nombre de noeud (grossier)
    Integer bSupNbAchro;
    
    SingleGraph g;

    public AchroSolverHeuris(SingleGraph g) {
        this.g=g;
        nbEdges = g.getEdgeCount();
        nbVertexes = g.getNodeCount();

        //Determination des bornes pour k, le nombre achromatique
        //La borne sup est le nombre de noeud (grossier)
        bSupNbAchro = g.getNodeSet().size();
        
        for (List<Node> clique : Toolkit.getMaximalCliques(g)) {
            if (getMax){
                maxNode = (ColoredNode) clique.get(0);
                getMax = false;
            }
            if (clique.contains(maxNode))
                maxAppClique++;
            maximalCliques.add(clique);
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

        N = nbVertexes;

        /*Integer[] mapping = new Integer[nbVertexes];
        Integer[] mapping2 = new Integer[nbVertexes];
        int cpt2 = 0;
        for (Node sortedNode: sortedNodes){
            mapping[sortedNode.getIndex()] = cpt2;
            mapping2[cpt2] = sortedNode.getIndex();
            cpt2++;
        }*/

        solveur = new AchroSolverk(g, N, maximalCliques, sortedNodes);
        
    }
    
    public int solve(){

        
        for (int k = bInfNbAchro; k <= bSupNbAchro; k++){
            solveur.setK(k);

            if(solveur.solve()){
                hasBeenComplete = true;
                System.out.println("Une solution a été trouvé pour le nombre achromatique " + k);
                for (int i = 0; i < N ; i++) {
                    int color = solveur.B[i].getValue();
                    System.out.println("L'arete "+i+" est de couleur "+ColorMapping.colorsMap[color%32]);
                    //g.getNode(i).addAttribute("ui.class", "color" + i);
                    g.getNode(/*mapping2[*/i/*]*/).addAttribute("ui.style", "fill-color: " + ColorMapping.colorsMap[color%32]+";");
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
                //TODO traiter le else
            }

            //TODO donner toutes les solutions? ou c'est juste une permutation?
        }
        return bSupNbAchro;
    }
}
