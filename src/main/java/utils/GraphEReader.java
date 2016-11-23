package utils;

import graphmodel.ColoredGraph;
import org.graphstream.graph.Graph;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by jules on 16/11/2016.
 */
public class GraphEReader {
    public static ColoredGraph buildGraphFromFile(String fileName) throws IOException, NullPointerException {

        ColoredGraph g = new ColoredGraph(fileName.split("/")[fileName.split("/").length-1]);
        Set<String> nodes = new HashSet();

        if (fileName == null)
            fileName = "benchmark/contiguous-usa";


        ClassLoader classLoader = GraphReader.class.getClassLoader();
        URL urlFile = classLoader.getResource(fileName);
        File file = new File(urlFile.getFile()); // Throws NullPointerException if file does not exist

        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] edge = line.split(" ");
            String node1=edge[0];
            String node2=edge[1];

            if (!nodes.contains(node1)){
                g.addNode(node1);
                nodes.add(node1);
            }

            if (!nodes.contains(node2)){
                g.addNode(node2);
                nodes.add(node2);
            }
            g.addEdge(node1+"-"+node2,node1, node2);
        }

        sc.close();

        g.buildGraph();

        return g;
    }

}