package test;

import graphmodel.ColoredGraph;
import solver.AchroSolverBase;
import utils.GraphReader;

import java.io.IOException;
import solver.AchroSolverHeuris;
import solver.AchroSolverk;


/**
 * Created by jules on 16/11/2016.
 */
public class TestHeuris {

    static String GRAPHNAME = "benchmark/marine";
    public static void main (String[] args) throws IOException {
        System.out.println("test");
        //AchroSolver.testSolver1();
        ColoredGraph g = new ColoredGraph(GRAPHNAME);
        GraphReader.buildGraphFromFile(g,  GRAPHNAME);
        g.setUiProps();
        //g.addAttribute("ui.stylesheet", Test.class.getClassLoader().getResource("colors.css"));
        //g.display();

        AchroSolverHeuris solver = new AchroSolverHeuris(g);
        System.out.println("test");
        int achroNumber = solver.solve();
        System.out.println("test");
        System.out.println("Achromatic number : " + achroNumber);
        g.display();
    }
}
