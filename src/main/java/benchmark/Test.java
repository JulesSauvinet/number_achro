package benchmark;

import graphmodel.ColoredGraph;
import solver.AchroSolver;
import utils.GraphEReader;

import java.io.IOException;


/**
 ** Created by teamgraphe
 */
public class Test {

    static String GRAPHNAME = "benchmark/graph1";
    public static void main (String[] args) throws IOException {
        //AchroSolver.testSolver1();
        ColoredGraph g = GraphEReader.buildGraphFromFile(GRAPHNAME);
        g.setUiProps();
        //g.addAttribute("ui.stylesheet", Test.class.getClassLoader().getResource("colors.css"));
        //g.display();

        AchroSolver solver = new AchroSolver(g);
        int achroNumber = solver.solve();
        System.out.println("Achromatic number : " + achroNumber);
        g.display();
    }
}
