package graphmodel;

import org.graphstream.graph.implementations.SingleGraph;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by jules on 18/11/2016.
 */
public class GraphModel {

    public SingleGraph g;
    public HashMap<String, Color> vertexColoring;

    public GraphModel(SingleGraph g) {
        this.g = g;
        for (Object v : g.getNodeSet()){
            String s = (String) v;
            vertexColoring.put(s,Color.black);
        }
    }

    public void setColor(String vertex, Color c){
        vertexColoring.put(vertex,c);
    }

    public SingleGraph getG() {
        return g;
    }

    public void setG(SingleGraph g) {
        this.g = g;
    }

    public HashMap<String, Color> getVertexColoring() {
        return vertexColoring;
    }

    public void setVertexColoring(HashMap<String, Color> vertexColoring) {
        this.vertexColoring = vertexColoring;
    }
}
