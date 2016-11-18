package graphmodel;

import org.graphstream.graph.Graph;
import org.graphstream.graph.NodeFactory;
import org.graphstream.graph.implementations.AdjacencyListGraph;
import org.graphstream.graph.implementations.SingleGraph;
import utils.GraphReader;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by jules on 18/11/2016.
 */
public class ColoredGraph extends SingleGraph {

    public HashMap<String, Color> vertexColoring;

    public ColoredGraph(String name) {
        super(name);

        setNodeFactory(new NodeFactory<ColoredNode>() {
            public ColoredNode newInstance(String id, Graph graph) {
                return new ColoredNode(graph, id) ;
            }
        });


    }

    public void setDefaultColors(){
        for (Object v : this.getNodeSet()){
            String s = (String) v;
            vertexColoring.put(s,Color.black);
        }
    }

    public void setColor(String vertex, Color c){
        vertexColoring.put(vertex,c);
    }

    public HashMap<String, Color> getVertexColoring() {
        return vertexColoring;
    }

    public void setVertexColoring(HashMap<String, Color> vertexColoring) {
        this.vertexColoring = vertexColoring;
    }
}
