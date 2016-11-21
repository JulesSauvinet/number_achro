package test;

import graphmodel.ColoredGraph;
import solver.AchroSolverBase;
import utils.GraphReader;

import java.io.IOException;


/**
 * Created by jules on 16/11/2016.
 */
public class TestHeuris {

    static String GRAPHNAME = "benchmark/marine";
    public static void main (String[] args) throws IOException {

        //AchroSolver.testSolver1();
        ColoredGraph g = new ColoredGraph(GRAPHNAME);
        GraphReader.buildGraphFromFile(g,  GRAPHNAME);
        g.setUiProps();
        //g.addAttribute("ui.stylesheet", Test.class.getClassLoader().getResource("colors.css"));
        //g.display();

        AchroSolverBase solver = new AchroSolverBase();
        int achroNumber = solver.solve(g);
        System.out.println("Achromatic number : " + achroNumber);
        g.display();
    }
}
