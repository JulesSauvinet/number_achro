package graphmodel;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.implementations.SingleNode;

import java.awt.*;

/**
 * Created by jules on 16/11/2016.
 */
public class ColoredNode extends SingleNode{

    protected Color c = Color.black;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColoredNode that = (ColoredNode) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public ColoredNode(SingleGraph graph, String name, Color c) {
        super(graph, name);
        this.c = c;

    }


    public ColoredNode(Graph graph, String name) {
        super((SingleGraph)graph, name);
        this.c = Color.black;
    }

    @Override
    public String toString() {
        return "ColoredNode{" +
                "id='" + id + '\'' +
                '}';
    }


    public Color getC() {
        return c;
    }

    public void setC(Color c) {
        this.c = c;
    }
}
