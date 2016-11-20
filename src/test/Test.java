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

    static String GRAPHNAME = "izipizi3";
    public static void main (String[] args) throws IOException {

        //AchroSolver.testSolver1();
        ColoredGraph g = new ColoredGraph(GRAPHNAME);
        GraphReader.buildGraphFromFile(g, GRAPHNAME);
        g.setUiProps();
        //g.addAttribute("ui.stylesheet", Test.class.getClassLoader().getResource("colors.css"));
        //g.display();
        AchroSolver solver = new AchroSolver();
        solver.solve(g);
        //g.display();
    }
}
