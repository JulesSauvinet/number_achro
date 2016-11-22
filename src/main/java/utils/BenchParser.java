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
            ClassLoader classLoader = GraphReader.class.getClassLoader();
            URL urlFile = classLoader.getResource(fileName);
            File file = new File(urlFile.getFile()); // Throws NullPointerException if file does not exist

            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {

                String graphLine = sc.nextLine();
                String[] graphInfos = graphLine.split(",");
                String graphName = graphInfos[0].replaceAll("\"", "").replaceAll("\\{","").replaceAll("\\}","");
                String content = "grge";

                File newFile = new File(urlFile.getFile());
                // if file doesnt exists, then create it
                if (!newFile.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(newFile.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();

            }

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
