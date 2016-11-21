package graphmodel;

import org.graphstream.graph.Graph;
import org.graphstream.graph.NodeFactory;
import org.graphstream.graph.implementations.SingleGraph;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by jules on 18/11/2016.
 * On crée dans cette classe un graphe de noeuds colorés que l'on stockera dans une HashMap
 */
public class ColoredGraph extends SingleGraph {

    public HashMap<String, Color> vertexColoring;

    public ColoredGraph(String name) {
        super(name);

        setNodeFactory(new NodeFactory<ColoredNode>() {
            @Override
            public ColoredNode newInstance(String id, Graph graph) {
                return new ColoredNode(graph, id) ;
            }
        });
    }

    public HashMap<String, Color> getVertexColoring() {
        return vertexColoring;
    }

    public void setVertexColoring(HashMap<String, Color> vertexColoring) {
        this.vertexColoring = vertexColoring;
    }

    public void setColor(String vertex, Color c){
        vertexColoring.put(vertex,c);
    }

    public void setUiProps() {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        //this.addAttribute("ui.stylesheet", "graph { fill-color: red; }");
        this.addAttribute("ui.stylesheet", "node { size: 18px; }");
    }

    public void setDefaultColors(){
        for (Object v : this.getNodeSet()){
            String s = (String) v;
            vertexColoring.put(s,Color.black);
        }
    }
}
