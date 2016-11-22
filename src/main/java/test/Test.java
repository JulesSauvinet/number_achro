package test;

import graphmodel.ColoredGraph;
import solver.AchroSolver;
import utils.GraphReader;

import java.io.IOException;


/**
 * Created by jules on 16/11/2016.
 */
public class Test {

    static String GRAPHNAME = "benchmark/clebsh";
    public static void main (String[] args) throws IOException {
        //AchroSolver.testSolver1();
        ColoredGraph g = new ColoredGraph(GRAPHNAME);
        GraphReader.buildGraphFromFile(g,  GRAPHNAME);
        g.setUiProps();
        //g.addAttribute("ui.stylesheet", Test.class.getClassLoader().getResource("colors.css"));
        //g.display();

        AchroSolver solver = new AchroSolver(g);
        int achroNumber = solver.solve();
        System.out.println("Achromatic number : " + achroNumber);
        g.display();
    }
}
