/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import search.SearchType;
 
/**
 *
 * @author howard
 */

 
public class DisplayResults extends Application {
 
    @Override 
    public void start(Stage stage) {
        stage.setTitle("Line Chart benchmark");
        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("strategy");
        //creating the chart
        final LineChart<String,Number> lineChart = 
                new LineChart<String,Number>(xAxis,yAxis);
                
        lineChart.setTitle("benchmark");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        //populating the series with data
        series.getData().add(new XYChart.Data(SearchType.DEFAULT.toString(), 23));
        series.getData().add(new XYChart.Data(SearchType.GREEDY.toString(), 14));
        series.getData().add(new XYChart.Data(SearchType.MINDOM.toString(), 15));
        series.getData().add(new XYChart.Data(SearchType.MAXCONSTRAINTS.toString(), 24));
        
        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);
       
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
