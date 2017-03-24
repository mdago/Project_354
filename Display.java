package iteration2;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import iteration1.Calculator;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

@SuppressWarnings({ "unchecked", "rawtypes" })
/**
 * The main driver. Generates the GUI and uses mouse click event handlers to display user selected information. Information is gathered using the Yahoo Finance API
 * @author Stephen Prizio, Marco Dagostino, Ming Tsai, Maximilien Le Clei. Chris McArthur, Himmet Arican, Athanasios Babouras
 * @version 2.0
 */
public class Display extends Application {	
	/**
	 * Calculator to calculate moving averages
	 */
	private Calculator calculator;
	
	/**
	 * Array of Strings representing the 30 DOW stocks as tickers
	 */
	private final String[] tickers = {"AAPL", "AXP", "BA", "CAT", "CSCO", "CVX", "KO", "DD", "XOM", "GE", "GS", "HD", "IBM", "INTC", "JNJ", "JPM", "MCD", "MMM", "MRK", "MSFT"
			, "NKE", "PFE", "PG", "TRV", "UNH", "UTX", "V", "VZ", "WMT", "DIS"};
	
	/**
	 * Array of Strings representing the 30 DOW stocks as names
	 */
	private final String[] names = {"Apple", "American Express", "Boeing", "Caterpillar", "Cisco Systems", "Chevron", "Coca-Cola", "DuPont", "ExxonMobil", "General Electric", "Goldman Sachs", "Home Depot", "IBM", "Intel", "Johnson & Johnson", "JPMorgan Chase", "McDonald's", "3M Company", "Merck", "Microsoft"
			, "Nike", "Pfizer", "Procter & Gamble", "The Travelers", "UnitedHealth", "United Technologies", "Visa", "Verizon", "Wal-Mart", "Walt Disney"};
	
	/**
	 * Number Axis representing the x-axis, elapsed time with varying in days, weeks or months
	 */
    NumberAxis x;
    
    /**
     * Number Axis representing the y-axis, closing price in dollars
     */
    NumberAxis y;
	
	/**
	 * Data Series representing daily closing prices
	 */
    XYChart.Series closingPrices;
    
    /**
     * Data Series representing 20 day moving average
     */
    XYChart.Series twentyDayMA = new XYChart.Series();
	
    /**
     * Data Series representing 50 day moving average
     */
    XYChart.Series fiftyDayMA = new XYChart.Series();
    
    /**
     * Data Series representing 100 day moving average
     */
    XYChart.Series oneHundredDayMA = new XYChart.Series();
    
    /**
     * Data Series representing 200 day moving average
     */
    XYChart.Series twoHundredDayMA = new XYChart.Series();
	
	/**
	 * JavaFX List of Stocks as a ListView
	 */
	ListView<String> stockList;
	
	/**
	 * Index of selected stock in list view menu
	 */
	private int stockIndex = 0;
	
	/**
	 * Stock object representing the currently selected stock by the user
	 */
	private Stock stock;
	
	/**
	 *  Double array of closing prices
	 */
	double[] closings;
	
	/**
	 * Yahoo Finance Calendar instance used to determine time spans (start of span)
	 */
	Calendar start = Calendar.getInstance();
	
	/**
	 * Yahoo Finance Calendar instance used to determine time spans (end of span)
	 */
	Calendar end = Calendar.getInstance();
	
	/**
	 * Overrides Application start method. This method creates and runs the GUI
	 * @throws IOException Throw exception if error with connection to Yahoo
	 */
	@Override
	public void start(Stage stage) throws IOException {	
		//	temporarily set time span ** THIS -1 SHOULD BE CALCULATED BY A METHOD TO DETERMINE TIME SPAN
		start.add(Calendar.YEAR, -1);
		
		// set title of window
        stage.setTitle("Stock Program");
        
        //	create layout of window
        BorderPane bp = new BorderPane();
        
        // 	add default graph to window layout
        bp.setCenter(createChart());
        
        // 	LEFT PART OF LAYOUT :: STOCK MENU
        
        //	create layout for left side of border pane
        VBox left = new VBox();
        left.getStyleClass().add("left-style");
        
        //	format vbox
        left.setPadding(new Insets(50, 50, 50, 50));
        left.setSpacing(15);
        
        //	create label
        Label stockLabel = new Label("List of Stocks");
        
        //	create a list view to list all stocks
        stockList = new ListView<String>();
        
        //	add custom css class
        stockList.getStyleClass().add("list-view-style");
        
        ObservableList<String> items = FXCollections.observableArrayList (
        	  tickers[0] + " - " + names[0], tickers[1] + " - " + names[1], tickers[2] + " - " + names[2], tickers[3] + " - " + names[3], tickers[4] + " - " + names[4]
        	, tickers[5] + " - " + names[5], tickers[6] + " - " + names[6], tickers[7] + " - " + names[7], tickers[8] + " - " + names[8], tickers[9] + " - " + names[9]
        	, tickers[10] + " - " + names[10], tickers[11] + " - " + names[11], tickers[12] + " - " + names[12], tickers[13] + " - " + names[13], tickers[14] + " - " + names[14]
        	, tickers[15] + " - " + names[15], tickers[16] + " - " + names[16], tickers[17] + " - " + names[17], tickers[18] + " - " + names[18], tickers[19] + " - " + names[19]
        	, tickers[20] + " - " + names[20], tickers[21] + " - " + names[21], tickers[22] + " - " + names[22], tickers[23] + " - " + names[23], tickers[24] + " - " + names[24]
        	, tickers[25] + " - " + names[25], tickers[26] + " - " + names[26], tickers[27] + " - " + names[27], tickers[28] + " - " + names[28], tickers[29] + " - " + names[29]);
        
        //	set menu items to list view
        stockList.setItems(items);
        
        //	set preferred dimensions for list
        stockList.setPrefWidth(180);
        stockList.setPrefHeight(700);
        	
        //	add elements to VBox
        left.getChildren().add(stockLabel);
        left.getChildren().add(stockList);
        	
        bp.setLeft(left);
        
        //	CENTER PART OF LAYOUT :: GRAPH
        
        //  mouse clicked event handler that returns index of selected item
        //	this will create and display a new chart each time new stock is selected
        stockList.setOnMouseClicked(new EventHandler<MouseEvent>() {
        	@Override
            public void handle(MouseEvent arg0) {
               stockIndex = stockList.getSelectionModel().getSelectedIndex();
               try {
				   stock = YahooFinance.get(tickers[stockIndex], start, end, Interval.DAILY);
				   
				   //	create a chart
				   LineChart<Number, Number> show = createChart(names[stockIndex]);
	
				   addDataToGraph(stock, show);	//	add data to graph
				   
				   bp.setCenter(show);
				   
               } catch (IOException e) {
            	   System.out.println("Connection Error");
            	   System.exit(1);
               }
            }
        });
        
        // 	RIGHT PART OF LAYOUT :: UI CONTROLS
        
        // 	create layout for right side of border pane
        VBox right = new VBox();
        
        //	format vbox
        right.setPadding(new Insets(50, 50, 50, 50));
        right.setSpacing(15);
        
        //	create labels
        Label rightTitle = new Label("Graph Controls");
        Label timeSpanTitle = new Label("Time Spans");
        Label movingAverageTitle = new Label("Moving Averages");
        
        //	create a toggle group so only one choice can be selected
        ToggleGroup group = new ToggleGroup();
        
        //	create radio buttons for time span
        RadioButton oneYear = new RadioButton("1 year");
        oneYear.setToggleGroup(group);
        RadioButton fiveYears = new RadioButton("5 years");
        fiveYears.setToggleGroup(group);
        RadioButton tenYears = new RadioButton("10 years");
        tenYears.setToggleGroup(group);
        RadioButton allTime = new RadioButton("All-time");
        allTime.setToggleGroup(group);
        
        //	create check boxes for moving averages
        CheckBox twenty = new CheckBox("20 Day Moving Average");
        CheckBox fifty = new CheckBox("50 Day Moving Average");
        CheckBox oneHundred = new CheckBox("100 Day Moving Average");
        CheckBox twoHundred = new CheckBox("200 Day Moving Average");
        
        
        //	add label
        right.getChildren().add(rightTitle);
        
        //	add label
        right.getChildren().add(timeSpanTitle);
        
        //	add radio buttons boxes
        right.getChildren().add(oneYear);
        right.getChildren().add(fiveYears);
        right.getChildren().add(tenYears);
        right.getChildren().add(allTime);
        
        //	add label
        right.getChildren().add(movingAverageTitle);
        
        //	add check boxes
        right.getChildren().add(twenty);
        right.getChildren().add(fifty);
        right.getChildren().add(oneHundred);
        right.getChildren().add(twoHundred);
        
        //	set right side of borderpane to be VBox
        bp.setRight(right);
        
        // 	and scene to window and give the window a default size
        Scene scene  = new Scene(bp,1600,1000);
        
        //	add custom font
        Font.loadFont(Display.class.getResource("Montserrat-Regular.ttf").toExternalForm(), 10);
        
        // 	add custom css to chart
        scene.getStylesheets().add(getClass().getResource("graph.css").toExternalForm());
       
        //	give the application an icon
        //stage.getIcons().add(new Image(this.getClass().getResourceAsStream("graph-icon.png")));
        stage.getIcons().add(new Image(Display.class.getResourceAsStream("graph-icon.png")));
        
        //	make the chart visible and fixed
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
	
	/**
	 * Create a blank chart as a place holder while awaiting for user's selection
	 * @return a linechart
	 */
	private LineChart<Number, Number> createChart() {
		//	instantiate axis
        x = new NumberAxis();
        y = new NumberAxis();
		
		LineChart<Number, Number> lc = new LineChart<Number,Number>(x,y);
        
        //	give axis names
        x.setLabel("Elapsed Time");
        y.setLabel("Daily Closing Price ($)");
        
        // 	disable chart symbols
        lc.setCreateSymbols(false);
        
        lc.setAnimated(true);
        
        return lc;		
	}
	
	/**
	 * Create a a new linechart that displays the title of the selected stock
	 * @return a linechart
	 */
	private LineChart<Number, Number> createChart(String stockName) {
		//	instantiate axis
        x = new NumberAxis();
        y = new NumberAxis(135, 195, 2);
		
		LineChart<Number, Number> lc = new LineChart<Number,Number>(x,y);
        
        //	give axis names
        x.setLabel("Elapsed Time");
        y.setLabel("Daily Closing Price ($)");
                
        //	give chart a title
        lc.setTitle(stockName + " Stock History");
        
        // 	disable chart symbols
        lc.setCreateSymbols(false);
        
        lc.setAnimated(true);
        
        return lc;		
	}
	
	/**
	 * Adds data to graph by retrieving information from the Yahoo Finance API
	 * @param aStock the selected stock
	 * @param aLine the line chart on which to add the data
	 * @throws IOException Throws exception if connection error
	 */
	private void addDataToGraph(Stock aStock, LineChart<Number, Number> aLine) throws IOException {     		
        //	instantiate data series
        closingPrices = new XYChart.Series();
        twentyDayMA = new XYChart.Series();
        
        //	give the series their names
        closingPrices.setName("Daily Closing Prices");
        twentyDayMA.setName("20 Day Moving Average");
        
        //	set up display such that it shows monthly stocks for 1 year COULD CHANGE THIS SUCH THAT THE START AND END CAN BE REPLACED BY METHODS THAT DETERMINE TIMESPAN
        aStock = YahooFinance.get(tickers[stockIndex], start, end, Interval.DAILY);
        //	create list of stocks
        List<HistoricalQuote> quotes = stock.getHistory();
        
        //	initialize calculator
        calculator = new Calculator();
        
        //	create new array of closing prices
        closings = new double[quotes.size()];
        
        //	add data to array 
        /*
        for (int i = 0; i < quotes.size(); ++i)
        	closings[i] = quotes.get(i).getClose().doubleValue();
        */
        for (int i = 0; i < quotes.size(); ++i)
        	closings[i] = quotes.get(quotes.size() - i - 1).getClose().doubleValue();
        
        //	calculate moving average
        closings = calculator.calculateMovingAverage(closings, 20);
        
        //	add data to series
        for (int i = 0; i < quotes.size(); ++i) {
        	closingPrices.getData().add(new XYChart.Data(i, quotes.get(quotes.size() - i - 1).getClose()));
        }
        for (int k = 0; k < closings.length; ++k)
        	twentyDayMA.getData().add(new XYChart.Data(k, closings[k]));
       
        //	add closing prices to graph
        aLine.getData().add(closingPrices);
        
        //	add moving average to graph
        aLine.getData().add(twentyDayMA);
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
