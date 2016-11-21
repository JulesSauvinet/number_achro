package graphmodel;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.implementations.SingleNode;

import java.awt.*;

/**
 * Created by jules on 16/11/2016.
 * Classe qui décrit un noeud de notre graphe coloré
 */
public class ColoredNode extends SingleNode {
    
    /* on donne par défaut la couleur noire a chaque noeud
     * cela nous permet de savoir quel(s) noeud(s) ne sont pas colorés lors de notre coloration
     */
    protected Color c = Color.black;

    public ColoredNode(Graph graph, String name) {
        super((SingleGraph)graph, name);
        this.c = Color.black;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ColoredNode that = (ColoredNode) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "ColoredNode{" +
                "id='" + id + '\'' +
                "degree=" + this.getDegree() + '\'' +
                '}';
    }
}
