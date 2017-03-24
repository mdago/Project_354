package iteration1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;



/**
 * Class implementation of a graphing mechanism using JavaFX. This verion displays all possible time spans and periods as a proof of concept for interation 1
 * @author Stephen Prizio, Marco Dagostino, Ming Tsai, Maximilien Le Clei. Chris McArthur, Himmet Arican, Athanasios Babouras
 * @version 1.0
 */
public class Graph extends Application {
	/**
	 * Stock reader to extract data from stocks file
	 */
	private StockReader readStockInfo;
	
	/**
	 * Array to store the closing prices of the stocks
	 */
	private double[] closingPrices0;
	private double[] closingPrices1;
	private double[] closingPrices5;
	private double[] closingPrices10;
	
	/**
	 * Calculator to calculate moving averages
	 */
	private Calculator calculator;
	
	/**
	 * Arrays to store moving averages that will be used to display the data
	 */
	private double[] movingAverages0;
	private double[] movingAverages1;
	private double[] movingAverages5;
	private double[] movingAverages10;
	
	/**
	 * Overrides Application start method. This method creates the GUI
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(Stage stage) {
		// set title of window
        stage.setTitle("Stock Program");

        //	create and name the axis for the graph
        NumberAxis x = new NumberAxis();
        x.setLabel("Elapsed Time (in days)");
        NumberAxis y = new NumberAxis();
        y.setLabel("Daily Closing Price (in $)");
        
        //	create chart
        LineChart<Number,Number> lineChart = new LineChart<Number,Number>(x,y);
                
        //	give chart a title
        lineChart.setTitle("Moving Averages, 2016 - 1962");
        
        //	create data series to represent the periods
        XYChart.Series ma20 = new XYChart.Series();
        XYChart.Series ma50 = new XYChart.Series();
        XYChart.Series ma100 = new XYChart.Series();
        XYChart.Series ma200 = new XYChart.Series();
        
        //	give the series their names
        ma20.setName("All-time (20 Day Moving Average)");
        ma50.setName("1 year (50 Day Moving Average)");
        ma100.setName("5 years (100 Day Moving Average)");
        ma200.setName("10 years (200 Day Moving Average)");
        
        // *** add data to graph ***
        
        //	read the stock data
        readStockInfo = new StockReader();
        
        //	get array of closing prices from stocks
        closingPrices0 = readStockInfo.getClosingPrices();
        closingPrices1 = readStockInfo.getClosingPrices(1);
        closingPrices5 = readStockInfo.getClosingPrices(5);
        closingPrices10 = readStockInfo.getClosingPrices(10);
        
        //	initialize calculator
        calculator = new Calculator();
        
        //	calculate moving averages
        movingAverages0 = calculator.calculateMovingAverage(closingPrices0, 20);
        movingAverages1 = calculator.calculateMovingAverage(closingPrices1, 50);
        movingAverages5 = calculator.calculateMovingAverage(closingPrices5, 100);
        movingAverages10 = calculator.calculateMovingAverage(closingPrices10, 200);
        
        //	add data to graph
        for (int i = 0; i < movingAverages0.length; ++i) {
        	ma20.getData().add(new XYChart.Data((i + 1), movingAverages0[i]));
        }
        for (int i = 0; i < movingAverages1.length; ++i) {
        	ma50.getData().add(new XYChart.Data((i + 1), movingAverages1[i]));
        }
        for (int i = 0; i < movingAverages5.length; ++i) {
        	ma100.getData().add(new XYChart.Data((i + 1), movingAverages5[i]));
        }
        for (int i = 0; i < movingAverages10.length; ++i) {
        	ma200.getData().add(new XYChart.Data((i + 1), movingAverages10[i]));
        }
        
        // 	disable chart symbols
        lineChart.setCreateSymbols(false);
        
        //	and scene to window and give the window a default size
        Scene scene  = new Scene(lineChart,1200,800);
        
        //	add custom css to chart
        scene.getStylesheets().add(getClass().getResource("graph.css").toExternalForm());
        
        //	add data series to chart
        lineChart.getData().add(ma20);
        lineChart.getData().add(ma200);
        lineChart.getData().add(ma100);
        lineChart.getData().add(ma50);
       
        //	make the chart visible and not resizeable
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
 
	/**
	 * Main that runs the entire project and displays the graph
	 * @param args Standard Main method parameter
	 */
    public static void main(String[] args) {
    	//	run program
        launch(args);
    }

}
