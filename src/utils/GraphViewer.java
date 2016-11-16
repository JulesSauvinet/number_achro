package utils;

import graphmodel.ColoredVertex;
import org.jgraph.JGraph;
import org.jgraph.graph.*;
import org.jgrapht.Graph;
import org.jgrapht.graph.ListenableUndirectedGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

/**
 * Created by jules on 16/11/2016.
 */
public class GraphViewer{
    private final Color DEFAULT_BG_COLOR = Color.decode( "#FAFBFF" );
    private static final Dimension DEFAULT_SIZE = new Dimension( 830, 620 );
    public static HashMap<String,DefaultGraphCell> cellsMap = new HashMap();


    //TODO refaire tout
    public static void showGraph(ListenableUndirectedGraph g) {
        // Construct Model and Graph
        GraphModel model = new DefaultGraphModel();
        JGraph graph = new JGraph(model);
        // Control-drag should clone selection
        graph.setCloneable(true);

        // Enable edit without final RETURN keystroke
        graph.setInvokesStopCellEditing(true);

        // When over a cell, jump to its default port (we only have one, anyway)
        graph.setJumpToDefaultPort(true);

        // Insert all three cells in one call, so we need an array to store them
        DefaultGraphCell[] cells = new DefaultGraphCell[g.vertexSet().size()+g.edgeSet().size()];

        int i = 0;
        for (Object v : g.vertexSet()){
            ColoredVertex cv = (ColoredVertex) v;

            cells[i] = createVertex(cv.getName(), 400, 400, 400, 400,
                    ((ColoredVertex) v).getC(), true);
            i++;

            cellsMap.put(cv.getName(), cells[i]);
        }

        for (Object v : g.edgeSet()){

            org.jgrapht.graph.DefaultEdge e = (org.jgrapht.graph.DefaultEdge) v;

            ColoredVertex source = (ColoredVertex) g.getEdgeSource(e);
            ColoredVertex target = (ColoredVertex) g.getEdgeTarget(e);

            DefaultEdge edge = new DefaultEdge(source.getName() + "-" + target.getName());

            edge.setSource(cellsMap.get(source.getName()));
            edge.setTarget(cellsMap.get(target.getName()));
            cells[i] = edge;

            // Set Arrow Style for edge
            GraphConstants.setLineEnd(edge.getAttributes(),GraphConstants.ARROW_NONE);
            GraphConstants.setEndFill(edge.getAttributes(), true);
            i++;
        }

        // Insert the cells via the cache, so they get selected
        graph.getGraphLayoutCache().insert(cells);

        JFrame frame = new JFrame();

        JPanel jp = new JPanel();
        jp.setPreferredSize(DEFAULT_SIZE);// changed it to preferredSize, Thanks!
        jp.add(new JScrollPane(graph));
        frame.getContentPane().add(jp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    //TODO Ellipse2D
    public static DefaultGraphCell createVertex(String name, double x,
                                                double y, double w, double h, Color bg, boolean raised) {

        // Create vertex with the given name
        DefaultGraphCell cell = new DefaultGraphCell(name);

        // Set bounds
        GraphConstants.setBounds(cell.getAttributes(),
                new Rectangle2D.Double(x, y, w, h));

        // Set fill color
        if (bg != null) {
            GraphConstants.setGradientColor(cell.getAttributes(), bg);
            GraphConstants.setOpaque(cell.getAttributes(), true);
        }

        // Set raised border
        if (raised) {
            GraphConstants.setBorder(cell.getAttributes(),
                    BorderFactory.createRaisedBevelBorder());
        } else // Set black border
        {
            GraphConstants.setBorderColor(cell.getAttributes(),
                    Color.black);
        }
        // Add a Floating Port
        cell.addPort();

        return cell;
    }
}

