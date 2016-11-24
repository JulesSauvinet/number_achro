package solver;

import graphmodel.ColoredGraph;
import org.chocosolver.solver.variables.IntVar;
import search.SearchType;
import utils.ColorMapping;


/**
 * * Created by teamgraphe
 *
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

    private final static int TIME_LIMIT = 30;
    private AchroSolver_k solveur;
    private boolean hasBeenComplete = false;
    private Integer bInfNbAchro = 0;
    private Integer bSupNbAchro;
    private ColoredGraph g;
    private int N;

    public AchroSolver(ColoredGraph g) {
            this.g=g;
            this.N = g.getNodeCount();
            //Determination des bornes pour k, le nombre achromatique
            //La borne inf est la taille de la clique maximale (grossier)
            //La borne sup est le nombre de noeud (grossier)
            this.bSupNbAchro = g.getNodeSet().size();
            this.bInfNbAchro = g.getMaximalClique().size();
            this.solveur = new AchroSolver_k(g);
        }
    
    public void setConstraintSupp(Boolean UseHeuristicMaxClique, 
                               Boolean UseHeuristicNValue){
        solveur.setUseHeuristicMaxClique(UseHeuristicMaxClique);
        solveur.setUseHeuristicNValue(UseHeuristicNValue);
    }
    
    public int solve(){
        return solve(SearchType.DEFAULT);
    }
    public int solve(SearchType st){
        for (int k = bInfNbAchro; k <= bSupNbAchro; k++){
            solveur.setK(k);


            if(solveur.solve(st)){
                hasBeenComplete = true;
                System.out.println("Une solution a été trouvé pour le nombre achromatique " + k);
                for (int i = 0; i < N ; i++) {
                    int color = ((IntVar)(solveur.bestModel.getVars()[i])).getValue();
                    //System.out.println("Le somment "+i+" a été affecté de la couleur "+ColorMapping.colorsMap[color%32]);
                    //g.getNode(i).addAttribute("ui.class", "color" + i);

                    g.getNode(i).addAttribute("ui.style", "fill-color: " + ColorMapping.colorsMap[color%32]+";");
                }

                //Affichage en continu
                // g.display();
                if (k==bSupNbAchro){
                    System.out.println("Le nombre achromatique du graphe est " + "egal a " + bSupNbAchro);
                    return bSupNbAchro;
                }
            }
            else {
                int runtime = solveur.runtime;
                if (runtime >= TIME_LIMIT){
                    if (k>bInfNbAchro && hasBeenComplete) {
                        int nbachro = k-1;
                        System.out.print("Le solveur n'a pas pu trouver de solutions dans le temps limite de " + (TIME_LIMIT) + " secondes, ");
                        System.out.println("le nombre achromatique est " + "au moins egal a " + nbachro);

                        return nbachro;
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
                    }
                }
            }
        }
        return -1;
    }


}
