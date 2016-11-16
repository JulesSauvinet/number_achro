package utils;

import graphmodel.ColoredVertex;
import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by jules on 16/11/2016.
 */
public class GraphReader {
    public static ListenableUndirectedGraph buildGraphFromFile(String fileName) throws IOException {

        if (fileName == null)
            fileName = "data_raw/clebsh.txt";

        ListenableUndirectedGraph g = new ListenableUndirectedGraph( DefaultEdge.class );
        int nbVertex = 0;
        int nbEdges = 0;

        ClassLoader classLoader = GraphReader.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        Scanner scanner = new Scanner(file);

        StringBuilder sb = new StringBuilder();
        String line = scanner.nextLine();

        //first line : number of vertexes
        nbVertex = Integer.parseInt(line);

        for (int i=0; i<nbVertex; i++){
            //vertexes names
            line = scanner.nextLine();
            g.addVertex(new ColoredVertex(line));
        }

        line = scanner.nextLine();
        //line : number of edges
        nbEdges = Integer.parseInt(line);

        for (int i=0; i<nbEdges; i++){
            line = scanner.nextLine();
            String[] edge = line.split(" ");
            // add some sample data (graph manipulated via JGraphT)
            g.addEdge(new ColoredVertex(edge[0]), new ColoredVertex(edge[1]));
        }

        scanner.close();
        return g;
    }

}
