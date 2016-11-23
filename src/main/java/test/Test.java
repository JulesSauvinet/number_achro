package test;

import graphmodel.ColoredGraph;
import solver.AchroSolver;
import utils.GraphEReader;
import utils.GraphReader;

import java.io.IOException;


/**
 ** Created by teamgraphe
 */
public class Test {

    static String GRAPHNAME = "benchmark2/graph1";
    public static void main (String[] args) throws IOException {
        //AchroSolver.testSolver1();
        ColoredGraph g = GraphEReader.buildGraphFromFile(GRAPHNAME);
        g.setUiProps();
        //g.addAttribute("ui.stylesheet", Test.class.getClassLoader().getResource("colors.css"));
        //g.display();

        AchroSolver solver = new AchroSolver(g, true);
        int achroNumber = solver.solve();
        System.out.println("Achromatic number : " + achroNumber);
        g.display();
    }
}
