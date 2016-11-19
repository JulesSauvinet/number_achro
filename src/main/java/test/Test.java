package test;

import graphmodel.ColoredGraph;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.ListenableUndirectedGraph;
import solver.AchroSolver;
import utils.GraphReader;

import java.io.IOException;


/**
 * Created by jules on 16/11/2016.
 */
public class Test {
    public static void main (String[] args) throws IOException {

        //AchroSolver.testSolver1();
        ColoredGraph g = new ColoredGraph("clebsh");
        GraphReader.buildGraphFromFile(g, "data_raw/clebsh.txt");
        g.setUiProps();
        //g.addAttribute("ui.stylesheet", Test.class.getClassLoader().getResource("colors.css"));
        //g.display();
        AchroSolver solver = new AchroSolver();
        int achroNumber = solver.solve(g);
	System.out.println("Achromatic number : " + achroNumber);
        g.display();
    }
}
