package utils;

import org.graphstream.graph.Graph;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by jules on 16/11/2016.
 */
public class GraphReader {
    public static void buildGraphFromFile(Graph g, String fileName) throws IOException, NullPointerException {

        if (fileName == null)
            fileName = "benchmark/clebsh";

        String[] fileSplit = fileName.split("/");
        int nbVertex = 0;
        int nbEdges = 0;

        ClassLoader classLoader = GraphReader.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile()); // Throws NullPointerException if file does not exist

        Scanner scanner = new Scanner(file);

        StringBuilder sb = new StringBuilder();
        String line = scanner.nextLine();

        //first line : number of vertexes
        nbVertex = Integer.parseInt(line);

        for (int i=0; i<nbVertex; i++){
            //vertexes names
            line = scanner.nextLine();
            g.addNode(line);
        }

        line = scanner.nextLine();
        //line : number of edges
        nbEdges = Integer.parseInt(line);

        for (int i=0; i<nbEdges; i++){
            line = scanner.nextLine();
            String[] edge = line.split(" ");
            // add some sample data (graph manipulated via JGraphT)
            g.addEdge(edge[0]+edge[1],edge[0], edge[1]);
        }

        scanner.close();
    }

}
