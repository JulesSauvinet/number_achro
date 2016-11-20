package utils;

import org.graphstream.graph.implementations.SingleGraph;

/**
 * Created by jules on 20/11/2016.
 */
public class GraphLayouts {
    public static void clebshLayout(SingleGraph g){
        /*Test*/
        g.getNode(0).setAttribute("xyz", 4, 0, 0);
        g.getNode(1).setAttribute("xyz", 16, 0, 0);
        g.getNode(2).setAttribute("xyz", 20, 11.5, 0);
        g.getNode(3).setAttribute("xyz", 10, 20, 0);
        g.getNode(4).setAttribute("xyz", 0, 11.5, 0);

        g.getNode(9).setAttribute("xyz", 10,1, 0);
        g.getNode(7).setAttribute("xyz", 17, 6.5, 0);
        g.getNode(8).setAttribute("xyz", 14, 15, 0);
        g.getNode(6).setAttribute("xyz", 6, 15, 0);
        g.getNode(5).setAttribute("xyz", 3, 6.5, 0);

        g.getNode(10).setAttribute("xyz", 9, 7.5, 0);
        g.getNode(11).setAttribute("xyz", 11, 7.5, 0);

        g.getNode(12).setAttribute("xyz", 8, 10, 0);
        g.getNode(13).setAttribute("xyz", 12, 10, 0);
        g.getNode(14).setAttribute("xyz", 10, 12, 0);

        g.getNode(15).setAttribute("xyz", 10, 9.5, 0);

        /*g.getNode(0).setAttribute("xyz", 0, 4, 0);
        g.getNode(1).setAttribute("xyz", 1, 5, 0);
        g.getNode(2).setAttribute("xyz", 2, 6, 0);
        g.getNode(3).setAttribute("xyz", 3, 7, 0);
        g.getNode(4).setAttribute("xyz", 4, 8, 0);
        g.getNode(9).setAttribute("xyz", 5, 7, 0);
        g.getNode(7).setAttribute("xyz", 6, 6, 0);
        g.getNode(8).setAttribute("xyz", 7, 5, 0);
        g.getNode(6).setAttribute("xyz", 8, 4, 0);
        g.getNode(5).setAttribute("xyz", 7, 3, 0);
        g.getNode(10).setAttribute("xyz", 6, 2, 0);
        g.getNode(11).setAttribute("xyz", 5, 1, 0);
        g.getNode(12).setAttribute("xyz", 4, 0, 0);
        g.getNode(13).setAttribute("xyz", 3, 1, 0);
        g.getNode(14).setAttribute("xyz", 2, 2, 0);
        g.getNode(15).setAttribute("xyz", 1, 3, 0);*/
        g.display(false);
    }

}
