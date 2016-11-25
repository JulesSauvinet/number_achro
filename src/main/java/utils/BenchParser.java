package utils;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by jules on 22/11/2016.
 */
/*debut pour parser un gros set de graphe*/
public class BenchParser {
    public static void main(String[] args) throws FileNotFoundException {
        try {
            String fileName = "mathematica/graphs";
            ClassLoader classLoader = BenchParser.class.getClassLoader();
            URL urlFile = classLoader.getResource(fileName);
            File file = new File(urlFile.getFile()); // Throws NullPointerException if file does not exist

            Scanner sc = new Scanner(file);

            int nbGraph = 0;
            while (sc.hasNextLine() && nbGraph < 1000 ) {

                String graphLine = sc.nextLine();
                String[] graphInfos = graphLine.split("\"");

                String arcs = graphInfos[2];
                String[] edges = arcs.split("\\}, \\{");

                File newFile = new File("graph"+nbGraph);
                nbGraph++;

                StringBuilder sb = new StringBuilder("");
                for (int i =1; i< edges.length; i++){
                    String edge = edges[i];
                    edge = edge.replaceAll("\\{","");
                    edge = edge.replaceAll("\\}","");
                    edge = edge.replaceAll(",","");
                    sb.append(edge);
                    sb.append("\n");
                }
                // if file doesnt exists, then create it
                if (!newFile.exists() && nbGraph > 500) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(newFile.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(sb.toString());
                bw.close();

            }

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
