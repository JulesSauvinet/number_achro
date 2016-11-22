package graphmodel;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.*;
import java.util.List;

/**
 * Created by teamgraphe
 * On crée dans cette classe un graphe de noeuds colorés que l'on stockera dans une HashMap
 */
public class ColoredGraph extends SingleGraph {

    private List<Node> maximalClique = new ArrayList<>();
    private List<List<Node>> maximalCliques = new ArrayList<>();
    private ColoredNode[] sortedNodes;

    public ColoredGraph(String name) {
        super(name);
        setNodeFactory((String id1, Graph graph) -> new ColoredNode(graph, id1));
    }

    public void buildGraph() {
        int N = this.getNodeCount();
        sortedNodes = new ColoredNode[N];
        int cpt=0;
        for (Node node : this.getNodeSet()){
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

        for (List<Node> clique : Toolkit.getMaximalCliques(this)) {
            this.maximalCliques.add(clique);
            //System.out.println("maxclique"+ clique.size());
            if (clique.size() > this.maximalClique.size())
                this.maximalClique = clique;
        }
    }

    public void setUiProps() {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        this.addAttribute("ui.stylesheet", "graph { fill-color: #1a1713;\n" +
                "canvas-color: black;\n" +
                "fill-mode: gradient-vertical;\n" +
                "fill-color: black, #004;\n" +
                "padding: 40px; } " + "\n" +
                "node { size: 18px; fill-mode :gradient-horizontal; shadow-mode: gradient-radial;\n" +
                "shadow-color: #FFF5, #FFF0;\n" +
                "shadow-width: 10px;\n" +
                "shadow-offset: 3px, 3px;}" + "\n" +
                "edge { /*shape: L-square-line;*/\n" +
                "size: 1px;\n" +
                "fill-color: #FFF3;\n" +
                "fill-mode: plain;\n" +
                "arrow-shape: none;}" +
                "\n" +
                "sprite {\n" +
                "shape: circle;\n" +
                "fill-mode: gradient-radial;\n" +
                "fill-color: #FFF8, #FFF0;\n" +
                "}\n");
    }

    public java.util.List<Node> getMaximalClique() {
        return maximalClique;
    }

    public ColoredNode[] getSortedNodes() {
        return sortedNodes;
    }

    public java.util.List<java.util.List<Node>> getMaximalCliques() {
        return maximalCliques;
    }
}
