package iteration2;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Calendar;

import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.ChartZoomManager;

import iteration1.Calculator;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

@SuppressWarnings({ "rawtypes", "unchecked" })
/**
 * The main driver. Generates the GUI and uses mouse click event handlers to display user selected information. Information is gathered using the Yahoo Finance API
 * @author Stephen Prizio, Marco Dagostino, Ming Tsai, Maximilien Le Clei, Chris McArthur, Himmet Arican, Athanasios Babouras
 * @version 2.0
 */
public class Display extends Application {	
	/**
	 * Calculator to calculate moving averages
	 */
	private Calculator calculator;
	
	/**
	 * Top button bar for navigation
	 */
	private ButtonBar bar;
	
	/**
	 * Array containing all dates for the stock's prices
	 */
	private DateObject[] dates;
	
	/**
	 * Help button for tutorial
	 */
	private Button help;
	
	/**
	 * Reset button to reset chart view
	 */
	private Button reset;
	
	/**
	 * Array of Strings representing the 30 DOW stocks as tickers
	 */
	private final String[] tickers = {"AAPL", "AXP", "BA", "CAT", "CSCO", "CVX", "KO", "DD", "XOM", "GE", "GS", "HD", "IBM", "INTC", "JNJ", "JPM", "MCD", "MMM", "MRK", "MSFT"
			, "NKE", "PFE", "PG", "TRV", "UNH", "UTX", "V", "VZ", "WMT", "DIS"};
	
	/**
	 * Array of Strings representing the 30 DOW stocks as names
	 */
	private final String[] names = {"Apple", "American Express", "Boeing", "Caterpillar", "Cisco Systems", "Chevron", "Coca-Cola", "DuPont", "ExxonMobil", "General Electric", "Goldman Sachs", "Home Depot", "IBM", "Intel"
			, "Johnson & Johnson", "JPMorgan Chase", "McDonald's", "3M Company", "Merck", "Microsoft", "Nike", "Pfizer", "Procter & Gamble", "The Travelers", "UnitedHealth", "United Technologies", "Visa", "Verizon"
			, "Wal-Mart", "Walt Disney"};
	
	/**
	 * Array storing all stock logos
	 */
	private Image[] imgs = createImages();
	
	/**
	 * Number Axis representing the x-axis, elapsed time with varying in days, weeks or months
	 */
    private NumberAxis x;
    
    /**
     * Number Axis representing the y-axis, closing price in dollars
     */
    private NumberAxis y;
	
	/**
	 * Data Series representing daily closing prices
	 */
    private XYChart.Series<Number,Number> closingPrices;
    
    /**
     * Data Series representing 20 day moving average
     */
    private XYChart.Series<Number,Number> twentyDayMA = new XYChart.Series<Number, Number>();
	
    /**
     * Data Series representing 50 day moving average
     */
    private XYChart.Series<Number,Number> fiftyDayMA = new XYChart.Series<Number, Number>();
    
    /**
     * Data Series representing 100 day moving average
     */
    private XYChart.Series<Number,Number> oneHundredDayMA = new XYChart.Series<Number, Number>();
    
    /**
     * Data Series representing 200 day moving average
     */
    private XYChart.Series<Number,Number> twoHundredDayMA = new XYChart.Series<Number, Number>();
	
	/**
	 * JavaFX List of Stocks as a ListView
	 */
    private ListView<String> stockList;
	
	/**
	 * Index of selected stock in list view menu
	 */
	private int stockIndex = -1;
	
	/**
	 * Stock object representing the currently selected stock by the user
	 */
	private Stock stock;
	
	/**
	 * Double array representing closing prices of stock
	 */
	double[] closings;
	
	/**
	 * Integer value representing time span for stock
	 */
	private int timeSpan = 1;
	
	/**
	 * Toggle group for radio buttons for timespan section
	 */
	private ToggleGroup group;
	
	/**
	 * Area chart representing closing prices of stocks
	 */
	private AreaChart<Number, Number> show;
	
	/**
	 * Interval at which the data will be displayed
	 */
	private Interval interval;
	
	/**
	 * String that will be concatenated to x-axis to display scale
	 */
	private String delimiter;
	
	/**
	 * Boolean to determine if a stock was selected
	 */
	private boolean stockSelected = false;
	
	/**
	 * 20 day moving average checkbox
	 */
	private CheckBox twenty;
	
	/**
	 * 50 day moving average checkbox
	 */
	private CheckBox fifty;
	
	/**
	 * 100 day moving average checkbox
	 */
	private CheckBox oneHundred;
	
	/**
	 * 200 day moving average checkbox
	 */
	private CheckBox twoHundred;
	
	/**
	 * List of recently viewed stocks
	 */
	private ObservableList recents = FXCollections.observableArrayList();
	
	/**
	 * Log of recently viewed stocks
	 */
	private TableView table = new TableView();
	
	/**
	 * Stock Popover message box
	 */
	private PopOver stockOver;
	
	/**
	 * Graph popover
	 */
	private PopOver graphOver;
	
	/**
	 * Radio button popover
	 */
	private PopOver radioOver;
	
	/**
	 * MA button popover
	 */
	private PopOver checkOver;
	
	/**
	 * Log popover
	 */
	private PopOver logOver;
	
	/**
	 * Layout Pane set as a border pane
	 */
	private BorderPane bp;
	
	/**
	 * Manager to manage chart panning
	 */
	private ChartPanManager panner;
	
	/**
	 * Manager to manage chart zooming
	 */
	private ChartZoomManager zoomManager;
	
	/**
	 * Overrides Application start method. This method creates and runs the GUI
	 * @throws IOException Throw exception if error with connection to Yahoo
	 */
	@Override
	public void start( Stage stage) throws IOException {		
		// set title of window
        stage.setTitle("Stocky");
        
        //	create layout of window
        bp = new BorderPane();
        
        //	BORDER LAYOUT :: TOP
        
        //	create button bar   
        bar = new ButtonBar();
        
        //	create help button
        help = createAndAddButton(bar, "Help", ButtonData.HELP);
        
        //	create reset button
        reset = createAndAddButton(bar, "Reset", ButtonData.OTHER);
        
        //	display button bar
        bp.setTop(bar);
        
        // 	LEFT PART OF LAYOUT :: STOCK MENU
        
        //	create layout for left side of border pane
        VBox left = new VBox();
        left.setAlignment(Pos.TOP_CENTER);
        
        //	add custom css class for left side
        left.getStyleClass().add("left-style");
        
        //	format vbox
        left.setPadding(new Insets(50, 50, 50, 50));
        left.setSpacing(15);
        
        //	create label
        Label stockLabel = new Label("Stocks List");
        
        //	create a list view to list all stocks
        stockList = new ListView<String>();
        
        //	add custom css class
        stockList.getStyleClass().add("list-view-style");
        
        ObservableList<String> items = FXCollections.observableArrayList (
          	  names[0], names[1], names[2], names[3], names[4]
          	, names[5], names[6], names[7], names[8],  names[9]
          	, names[10], names[11], names[12], names[13], names[14]
          	, names[15], names[16], names[17], names[18], names[19]
          	, names[20], names[21], names[22], names[23], names[24]
          	, names[25], names[26], names[27], names[28], names[29]);
        
        //	set menu items to list view
        stockList.setItems(items);
        
        //	add logos next to each stock 
        stockList.setCellFactory(param -> new ListCell<String>() {
        	ImageView viewer = new ImageView();
        	{
        		prefWidthProperty().bind(stockList.widthProperty().subtract(2));
        		setMaxWidth(Control.USE_PREF_SIZE);
        	}
        	@Override
        	public void updateItem(String name, boolean empty) {
        		super.updateItem(name, empty);
        		if (empty) {
        			setText(null);
        			setGraphic(null);
        		}
        		else {
        			if (name.equals("Apple"))
        				viewer.setImage(imgs[0]);
        			else if (name.equals("American Express"))
        				viewer.setImage(imgs[1]);
        			else if (name.equals("Boeing"))
        				viewer.setImage(imgs[2]);
        			else if (name.equals("Caterpillar"))
        				viewer.setImage(imgs[3]);
        			else if (name.equals("Cisco Systems"))
        				viewer.setImage(imgs[4]);
        			else if (name.equals("Chevron"))
        				viewer.setImage(imgs[5]);
        			else if (name.equals("Coca-Cola"))
        				viewer.setImage(imgs[6]);
        			else if (name.equals("DuPont"))
        				viewer.setImage(imgs[7]);
        			else if (name.equals("ExxonMobil"))
        				viewer.setImage(imgs[8]);
        			else if (name.equals("General Electric"))
        				viewer.setImage(imgs[9]);
        			else if (name.equals("Goldman Sachs"))
        				viewer.setImage(imgs[10]);
        			else if (name.equals("Home Depot"))
        				viewer.setImage(imgs[11]);
        			else if (name.equals("IBM"))
        				viewer.setImage(imgs[12]);
        			else if (name.equals("Intel"))
        				viewer.setImage(imgs[13]);
        			else if (name.equals("Johnson & Johnson"))
        				viewer.setImage(imgs[14]);
        			else if (name.equals("JPMorgan Chase"))
        				viewer.setImage(imgs[15]);
        			else if (name.equals("McDonald's"))
        				viewer.setImage(imgs[16]);
        			else if (name.equals("3M Company"))
        				viewer.setImage(imgs[17]);
        			else if (name.equals("Merck"))
        				viewer.setImage(imgs[18]);
        			else if (name.equals("Microsoft"))
        				viewer.setImage(imgs[19]);
        			else if (name.equals("Nike"))
        				viewer.setImage(imgs[20]);
        			else if (name.equals("Pfizer"))
        				viewer.setImage(imgs[21]);
        			else if (name.equals("Procter & Gamble"))
        				viewer.setImage(imgs[22]);
        			else if (name.equals("The Travelers"))
        				viewer.setImage(imgs[23]);
        			else if (name.equals("UnitedHealth"))
        				viewer.setImage(imgs[24]);
        			else if (name.equals("United Technologies"))
        				viewer.setImage(imgs[25]);
        			else if (name.equals("Visa"))
        				viewer.setImage(imgs[26]);
        			else if (name.equals("Verizon"))
        				viewer.setImage(imgs[27]);
        			else if (name.equals("Wal-Mart"))
        				viewer.setImage(imgs[28]);
        			else if (name.equals("Walt Disney"))
        				viewer.setImage(imgs[29]);
        			
        			setText(name);
        			setGraphic(viewer);
        		}
        	}
        });
        
        //	set preferred dimensions for list
        stockList.setPrefWidth(220);
        stockList.setPrefHeight(700);
        	
        //	add elements to VBox
        left.getChildren().add(stockLabel);
        left.getChildren().add(stockList);
        	
        bp.setLeft(left);
        
        //	CENTER PART OF LAYOUT :: GRAPH
        
        // 	add default graph to window layout
        bp.setCenter(createChart());
        
        //  mouse clicked listener that returns index of selected item
        //	this will create and display a new chart each time new stock is selected
        stockList.setOnMouseClicked(new EventHandler<MouseEvent>() {
        	@Override
            public void handle(MouseEvent arg0) {
        		//	reset all check boxes
    			twenty.setSelected(false);
    			fifty.setSelected(false);
    			oneHundred.setSelected(false);
    			twoHundred.setSelected(false);
        		
        		//	get calendar instances
        		Calendar start = Calendar.getInstance();
        		Calendar end = Calendar.getInstance();
        		
               stockIndex = stockList.getSelectionModel().getSelectedIndex();
               try {   
            	   //	set stock selected to true
            	   stockSelected = true;
            	   
            	   start.add(Calendar.YEAR, (timeSpan * -1));
            	   
            	   //   adjust x-axis scale based on selected time span              
       				if (timeSpan == 1) {
       					interval = Interval.DAILY;
       					delimiter = " (in Days)";
       				}
       				else if (timeSpan == 2 || timeSpan == 5) {
       					interval = Interval.WEEKLY;
       					delimiter = " (in Weeks)";
       				}
       				else {
       					interval = Interval.MONTHLY;
       					delimiter = " (in Months)";
       				}
            	   
				   stock = YahooFinance.get(tickers[stockIndex], start, end, interval);
				   
				   closings = new double[stock.getHistory().size()];
				   dates = new DateObject[stock.getHistory().size()];
				   
				   //	add data to array 
			        for (int i = 0; i < stock.getHistory().size(); ++i) {
			        	int year = stock.getHistory().get(stock.getHistory().size() - i - 1).getDate().get(Calendar.YEAR);
				        int month = stock.getHistory().get(stock.getHistory().size() - i - 1).getDate().get(Calendar.MONTH);
				        int day = stock.getHistory().get(stock.getHistory().size() - i - 1).getDate().get(Calendar.DAY_OF_MONTH);
				        
				        double temp = Math.round(stock.getHistory().get(stock.getHistory().size() - i - 1).getAdjClose().doubleValue() * 100);
			        	closings[i] = temp /  100;
			        	dates[i] = new DateObject(day, month, year);
			        }
				   
				   //	create a chart
				   show = createChart(names[stockIndex], closings, timeSpan);
				   
				   //	add data to graph
				   addDataToGraph(closings, show);	
				   
				   //	remove animations to increase speed
				   show.setAnimated(false);
				   
				   //	display chart
				   bp.setCenter(show);
				   
				   //	add stock to log
				   addToLog(stock, recents, table, tickers[stockIndex]);
				   
               } catch (IOException e) {
            	   //	display connection error
            	   displayException();
               }
            }
        });

        
        // 	RIGHT PART OF LAYOUT :: UI CONTROLS
        
        // 	create layout for right side of border pane
        VBox right = new VBox();
        //right.setAlignment(Pos.TOP_CENTER);
        
        right.getStyleClass().add("right-style");
        
        //	format vbox
        right.setPadding(new Insets(50, 50, 50, 50));
        right.setSpacing(20);
        
        //	create labels
        Label rightTitle = new Label("Graph Controls");
        Label timeSpanTitle = new Label("Time Spans");
        Label movingAverageTitle = new Label("Moving Averages");
        
        //	create a toggle group so only one choice can be selected
        group = new ToggleGroup();
        
        //	create radio buttons for time span and set ID to number of years to display. All time is represented as 0. These strings will be parsed to integers at runtime
        RadioButton oneYear = new RadioButton("1 year");
        oneYear.setToggleGroup(group);
        oneYear.setId("1");
        oneYear.setSelected(true);
        RadioButton twoYears = new RadioButton("2 years");
        twoYears.setToggleGroup(group);
        twoYears.setId("2");
        RadioButton fiveYears = new RadioButton("5 years");
        fiveYears.setToggleGroup(group);
        fiveYears.setId("5");
        RadioButton allTime = new RadioButton("All-time");
        allTime.setToggleGroup(group);
        allTime.setId("100");
        
        //	listener for radio button selection
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
            	if (group.getSelectedToggle() != null) {
            		//	display warning if stock was not yet selected
            		if (stockIndex == -1) {
            			//	update timespan so that if the stock is selected next, the correct information is displayed
            			displayWarning("No Stock Selected", "Please select a stock before choosing a time span.");
            			
            			//	update timespan so if a stock is then selected, correct display is shown
            			RadioButton selected = (RadioButton) group.getSelectedToggle();
            			timeSpan = Integer.parseInt(selected.getId());
            		}
            		else {            			
            			//	reset all check boxes
            			twenty.setSelected(false);
            			fifty.setSelected(false);
            			oneHundred.setSelected(false);
            			twoHundred.setSelected(false);
            			
            			//	get calendar instances
            			Calendar start = Calendar.getInstance();
            			Calendar end = Calendar.getInstance();
            		
            			//	get number value of selected radio button
            			RadioButton selected = (RadioButton) group.getSelectedToggle();
            			timeSpan = Integer.parseInt(selected.getId());
                    
            			//	adjust x-axis scale based on selected time span              
            			if (timeSpan == 1) {
            				interval = Interval.DAILY;
            				delimiter = " (in Days)";
            			}
            			else if (timeSpan == 2 || timeSpan == 5) {
            				interval = Interval.WEEKLY;
            				delimiter = " (in Weeks)";
            			}
            			else {
            				interval = Interval.MONTHLY;
            				delimiter = " (in Months)";
            			}
                    
            			try {                 	   
            				//	update calendar year starting point
            				start.add(Calendar.YEAR, (timeSpan * -1));
                 	   
            				//	get new info from Yahoo Finance
            				stock = YahooFinance.get(tickers[stockIndex], start, end, interval);
     				   
            				//	update value of closing prices
            				closings = new double[stock.getHistory().size()];
            				dates = new DateObject[stock.getHistory().size()];
     				   
            				//	add data to array 
            				for (int i = 0; i < stock.getHistory().size(); ++i) {
            					int year = stock.getHistory().get(stock.getHistory().size() - i - 1).getDate().get(Calendar.YEAR);
        				        int month = stock.getHistory().get(stock.getHistory().size() - i - 1).getDate().get(Calendar.MONTH);
        				        int day = stock.getHistory().get(stock.getHistory().size() - i - 1).getDate().get(Calendar.DAY_OF_MONTH);
        				        
        				        double temp = Math.round(stock.getHistory().get(stock.getHistory().size() - i - 1).getAdjClose().doubleValue() * 100);
        			        	closings[i] = temp /  100;
        			        	dates[i] = new DateObject(day, month, year);
            				}
     				   
            				//	create a chart
            				show = createChart(names[stockIndex], closings, timeSpan);
     				   
            				//	add data to graph
            				addDataToGraph(closings, show);
     				   
            				//	update graph in center
            				bp.setCenter(show);
     				   
            			} catch (IOException e) {
            				//	display connection error
            				displayException();
            			}
            		}
                }
            }
        });
        
        //	create check boxes for moving averages
        twenty = new CheckBox("20 Day Moving Average");
        fifty = new CheckBox("50 Day Moving Average");
        oneHundred = new CheckBox("100 Day Moving Average");
        twoHundred = new CheckBox("200 Day Moving Average");        
        
        //	add listeners to each check box
        twenty.selectedProperty().addListener(new ChangeListener<Boolean>() {
        	@Override
        	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        		if (stockSelected) {
        			if (twenty.isSelected()) {
        				//	add moving averages to graph
        				double[] averages = calculator.calculateMovingAverage(closings, 20);
        				addAverageToGraph(averages, show, 20);
        			}
        			else {
        				//	clear data from series and remove from graph
        				twentyDayMA.getData().clear();
        				show.getData().remove(twentyDayMA);
        			}
        		}
        		else {
        			if (twenty.isSelected()) {
        				displayWarning("No Stock Selected", "Please select a stock before choosing a moving average.");
        			}
        		}
        	}
        });
        fifty.selectedProperty().addListener(new ChangeListener<Boolean>() {
        	@Override
        	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        		if (stockSelected) {
        			if (fifty.isSelected()) {
        				//	check if computer average extends beyond limit of graph
        				if (closings.length < 50) {
        					displayError("Moving Average Error", "There aren't enough data points to compute a 50 day moving average");
        					twoHundred.setSelected(false);
        				}
        				else {
        					//	add moving averages to graph
        					double[] averages = calculator.calculateMovingAverage(closings, 50);
        					addAverageToGraph(averages, show, 50);
        				}
        			}
        			else {
        				//	clear data from series and remove from graph
        				fiftyDayMA.getData().clear();
        				show.getData().remove(fiftyDayMA);
        			}
        		}
        		else {
        			if (fifty.isSelected()) {
        				displayWarning("No Stock Selected", "Please select a stock before choosing a moving average.");
        			}
        		}
        	}
        });
        oneHundred.selectedProperty().addListener(new ChangeListener<Boolean>() {
        	@Override
        	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        		if (stockSelected) {
        			if (oneHundred.isSelected()) {
        				//	check if computed average extends beyond limit of graph
        				if (closings.length < 100) {
        					displayError("Moving Average Error", "There aren't enough data points to compute a 100 day moving average");
        					twoHundred.setSelected(false);
        				}
        				else {
        					//	add moving averages to graph
        					double[] averages = calculator.calculateMovingAverage(closings, 100);
        					addAverageToGraph(averages, show, 100);
        				}
        			}
        			else {
        				//	clear data from series and remove from graph
        				oneHundredDayMA.getData().clear();
        				show.getData().remove(oneHundredDayMA);
        			}
        		}
        		else {
        			if (oneHundred.isSelected()) {
        				displayWarning("No Stock Selected", "Please select a stock before choosing a moving average.");
        			}
        		}
        	}
        });
        twoHundred.selectedProperty().addListener(new ChangeListener<Boolean>() {
        	@Override
        	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        		if (stockSelected) {
        			if (twoHundred.isSelected()) {        				
        				//	check if computed average extends beyond limit of graph
        				if (closings.length < 200) {
        					displayError("Moving Average Error", "There aren't enough data points to compute a 200 day moving average");
        					twoHundred.setSelected(false);
        				}
        				else {
        					//	add moving averages to graph
        					double[] averages = calculator.calculateMovingAverage(closings, 200);
        					addAverageToGraph(averages, show, 200);
        				}
        			}
        			else {
        				//	clear data from series and remove from graph
        				twoHundredDayMA.getData().clear();
        				show.getData().remove(twoHundredDayMA);
        			}
        		}
        		else {
        			if (twoHundred.isSelected()) {
        				displayWarning("No Stock Selected", "Please select a stock before choosing a moving average.");
        			}
        		}
        	}
        });
        
        //	add label
        right.getChildren().add(rightTitle);
        
        //	add label
        right.getChildren().add(timeSpanTitle);
        
        //	add radio buttons boxes
        right.getChildren().add(oneYear);
        right.getChildren().add(twoYears);
        right.getChildren().add(fiveYears);
        right.getChildren().add(allTime);
        
        //	add label
        right.getChildren().add(movingAverageTitle);
        
        //	add check boxes
        right.getChildren().add(twenty);
        right.getChildren().add(fifty);
        right.getChildren().add(oneHundred);
        right.getChildren().add(twoHundred);
        
        // 	create label for log
        Label log = new Label("Recently Viewed Stocks");
        right.getChildren().add(log);
        
        //	create a table column
        TableColumn stockName = new TableColumn<>("Name");
        stockName.setResizable(false);
        stockName.setPrefWidth(300);
        
        //	add column to table
        table.getColumns().add(stockName);
        
        //	add table to VBox
        right.getChildren().add(table);
        
        //	enable data modification from YahooFinance Stock class
        stockName.setCellValueFactory(
        		new PropertyValueFactory<>("name")
        );
        
        //	set right side of borderpane to be VBox
        bp.setRight(right);
        
        //	add listener to help button
        help.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
        	@Override
        	public void handle(MouseEvent e) {        		
        		//	run tutorial
        		tutorial(stockList, show, oneYear, twenty, table);
        	}
        });
        
        //	add listener to reset button
        reset.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
        	@Override
        	public void handle(MouseEvent e) {
        		//	stop panning and zooming for current chart
        		stopFunction();
        		
        		//	recreate chart at particular instance
        		show = createChart(names[stockIndex], closings, timeSpan);
				addDataToGraph(closings, show);
				
				//	uncheck check boxes
				twenty.setSelected(false);
				fifty.setSelected(false);
				oneHundred.setSelected(false);
				twoHundred.setSelected(false);
        		
				//	display recreated chart
        		bp.setCenter(show);
        	}
        });
        
        //	add custom css to borderpane
        bp.getStyleClass().add("bp");
        
        // 	add scene to window and give the window a default size
        Scene scene  = new Scene(bp, 1500,900);
        
        //	add custom font
        Font.loadFont(Display.class.getResource("Montserrat-Regular.ttf").toExternalForm(), 20);
        
        // 	add custom css to chart
        scene.getStylesheets().add(getClass().getResource("graph.css").toExternalForm());
       
        //	give the application an icon
        stage.getIcons().add(new Image(Display.class.getResourceAsStream("graph-icon.png")));
        
        //	display the gui
        stage.setScene(scene);
        
        stage.setResizable(true);
        stage.show();
    }
	
	/**
	 * Method that creates a new button while also adding it to a given button bar. Gives a drop shadow effect as well
	 * @param b Buttonbar where the button will be added
	 * @param buttonLabel Text to be displayed inside button
	 * @param data Buttonbar data
	 * @return A new button
	 */
	private Button createAndAddButton(ButtonBar b, String buttonLabel, ButtonData data) {
		//	create help button
		Button temp = new Button(buttonLabel);
		
		//	set button bar data
        ButtonBar.setButtonData(temp, data);
        
        //	add shadow effect to button on mouse enter
        DropShadow shadow = new DropShadow();
        temp.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
        	@Override
        	public void handle(MouseEvent e) {
        		temp.setEffect(shadow);
        	}
        });
        //	remove shadow effect on mouse leave
        temp.addEventHandler(MouseEvent.MOUSE_EXITED, 
        	    new EventHandler<MouseEvent>() {
        	        @Override public void handle(MouseEvent e) {
        	            temp.setEffect(null);
        	        }
        	});
        
        //	add help button to bar
        b.getButtons().add(temp);
        
        //	return button
        return temp;
	}
	
	/**
	 * Create a blank chart as a place holder while awaiting for user's selection
	 * @return a linechart
	 */
	private AreaChart<Number, Number> createChart() {
		//	instantiate axis
        x = new NumberAxis();
        y = new NumberAxis();
		
		AreaChart<Number, Number> ac = new AreaChart<Number,Number>(x,y);
        
        //	give axis names
        x.setLabel("Elapsed Time");
        y.setLabel("Daily Closing Price ($)");
        
        // 	disable chart symbols
        ac.setCreateSymbols(false);
        
        ac.setAnimated(true);
        
        return ac;		
	}
	
	/**
	 * Create a a new linechart that displays the title of the selected stock
	 * @param stockName Name of selected stock
	 * @param arr Array containing data values for graph
	 * @param span Span of time that the data values cover
	 * @return a linechart
	 */
	private AreaChart<Number, Number> createChart(String stockName, double[] arr, int span) {
		//	when creating a new graph, stop and panning and zooming, in order to properly delete old chart
		stopFunction();
		
		//	initialize calculator
        calculator = new Calculator();
		
        //	determine tick length based on timespan
        int tick = 0;
        
        if (timeSpan == 1)
        	tick = 15;
        else if (timeSpan == 2)
        	tick = 10;
        else if (timeSpan == 5)
        	tick = 20;
        else
        	tick = 30;
        	
        
		//	instantiate axis with bounds matching the limits of the array holding the values       
        x = new NumberAxis(0, arr.length - 1, tick);
        // 	set scale for y axis using min and max values of closing prices
        y = new NumberAxis(calculator.getMin(closings) - 1, calculator.getMax(closings) + 1, 2);
		
		AreaChart<Number, Number> ac = new AreaChart<Number,Number>(x,y);
        
        //	give axis names
        x.setLabel("Elapsed Time" + delimiter);
        y.setLabel("Daily Closing Price ($)");
                
        //	give chart a title
        ac.setTitle(stockName + " Stock History");
        
        //	disallow animations
        ac.setAnimated(false);  
        
        //	add panning and zooming functionality to chart
        addFunction(ac);
        
        //	return chart
        return ac;		
	}
	
	/**
	 * Adds data to graph by retrieving information from the Yahoo Finance API
	 * @param prices Array containing closing prices to be added to graph
	 * @param aLine Line to which the data will be added
	 */
	private void addDataToGraph(double[] prices, AreaChart<Number, Number> aLine) {   		
        //	instantiate data series
        closingPrices = new XYChart.Series<Number,Number>();
        
        //	give the series a name
        closingPrices.setName("Daily Closing Prices");
        
        //	add data to series
        for (int i = 0; i < prices.length; ++i) {
        	closingPrices.getData().add(new XYChart.Data<Number,Number>(i, prices[i]));
        	closingPrices.getData().get(i).setNode(new HoverPane(dates[i], prices[i]));
        }
        
        //	add closing prices to graph
        aLine.getData().add(closingPrices);
        
        aLine.setAnimated(false);
	}	
	
	/**
	 * Displays exception if connection error
	 */
	private void displayException() {
		Alert exception = new Alert(AlertType.ERROR);
		exception.setTitle("Exception Dialog");
		exception.setHeaderText("A Connection Error was Detected");
		exception.setContentText("Could not establish a connection with Yahoo Finance. Please check that you are properly connected to the Internet.");
		
		exception.showAndWait();
	}
	
	/**
	 * Displays warning if a stock isn't selected while making choices
	 * @param warningTitle Title of the notification
	 * @param warningMessage Accompanying warning message for notification
	 */
	private void displayWarning(String warningTitle, String warningMessage) {
		//	create new warning
		Notifications.create().position(Pos.TOP_CENTER).title(warningTitle).text(warningMessage).darkStyle().showWarning();
	}
	
	/**
	 * Displays error if an error occurs (ex: Selected MA exceeds number of data points
	 * @param errorTitle Title of error notification
	 * @param errorMessage Error message to be displayed
	 */
	private void displayError(String errorTitle, String errorMessage) {
		//	create error notification
		Notifications.create().position(Pos.TOP_CENTER).title(errorTitle).text(errorMessage).darkStyle().showError();
	}
	
	/**
	 * Adds moving averages to graph
	 * @param averages Array of moving averages
	 * @param chart Chart which will contain the data
	 * @param period Moving average period
	 */
	private void addAverageToGraph(double[] averages, AreaChart<Number, Number> chart, int period) {
		//	call add method with variable parameters depending on requested period
		if (period == 20)
			add(averages, twentyDayMA, chart, period);
		else if (period == 50)
			add(averages, fiftyDayMA, chart, period);
		else if (period == 100)
			add(averages, oneHundredDayMA, chart, period);
		else
			add(averages, twoHundredDayMA, chart, period);
		
		//	remove symbols from moving average lines
		for (XYChart.Series<Number, Number> s : show.getData()) {
			if (!(s.getName().equals("Daily Closing Prices"))) {
				for (XYChart.Data<Number, Number> d : s.getData()) {
					StackPane sp = (StackPane) d.getNode();
					sp.setVisible(false);
				}
			}
		}
	}
	
	/**
	 * Adds values to series
	 * @param averages Array of moving averages
	 * @param chart Chart which will contain the data
	 * @param series Data series to which the data will be added
	 */
	private void add(double[] averages, XYChart.Series<Number,Number> series, AreaChart<Number, Number> chart, int period) {
		//	name series
		series.setName(period + " Day Moving Average");
		
		//	add data to series
		for (int i = 0; i < averages.length; ++i) {
			series.getData().add(new XYChart.Data<Number,Number>(i+period-1, averages[i]));
		}
		
		//	add series to chart
		chart.getData().add(series);
		
		//	ensure that the closing prices are on top of the Ma's, in order to have their information viewable
		series.getNode().toBack();
		
		//	disallow animations
		chart.setAnimated(false);
	}
	
	/**
	 * Create images for stocks
	 * @return Array of images
	 */
	private  Image[] createImages() {
		//	create array
		imgs = new Image[30];
		for (int i = 0; i < 30; ++i) {
			if (i == 11)
				imgs[11] = new Image("file:src/imgs/img" + i + ".jpg", 50.0, 50.0, true, true);
			else
				imgs[i] = new Image("file:src/imgs/img" + i + ".png", 50.0, 50.0, true, true); 
		}
		
		return imgs;
	}
	
	/**
	 * Displays popup for stock list help
	 */
	private void displayStockTutorial(ListView<?> list) {
		if (stockOver == null) {
			stockOver = new PopOver();
			stockOver.setArrowLocation(ArrowLocation.BOTTOM_CENTER);
			stockOver.setContentNode(new Label("Select a stock from this list\n by clicking on an item."));
			stockOver.setAutoFix(true);
			stockOver.setAutoHide(true);
			stockOver.setHideOnEscape(true);
			stockOver.setDetachable(false);
		}
		
		stockOver.show(list);
	}
	
	/**
	 * Displays popup for graph help
	 */
	private void displayGraphTutorial(AreaChart<Number, Number> chart) {
		if (graphOver == null) {
			graphOver = new PopOver();
			graphOver.setArrowLocation(ArrowLocation.RIGHT_CENTER);
			graphOver.setContentNode(new Label("The daily closings prices will be displayed here \nonce a stock is selected."));
			graphOver.setAutoFix(true);
			graphOver.setAutoHide(true);
			graphOver.setHideOnEscape(true);
			graphOver.setDetachable(false);
		}
		graphOver.show(chart);
	}

	/**
	 * Displays popup for radio button help
	 */
	private void displayTimeSpanTutorial(RadioButton r) {
		if (radioOver == null) {
			radioOver = new PopOver();
			radioOver.setArrowLocation(ArrowLocation.RIGHT_CENTER);
			radioOver.setContentNode(new Label("The timespan for the closing prices can be\nselected from these options. The graph\ndisplays the information from left (least recent)\nto right (most recent)."));
			radioOver.setAutoFix(true);
			radioOver.setAutoHide(true);
			radioOver.setHideOnEscape(true);
			radioOver.setDetachable(false);
		}
		radioOver.show(r);
	}

	/**
	 * Displays popup for ma checkboxes
	 */
	private void displayMATutorial(CheckBox cb) {
		if (checkOver == null) {
			checkOver = new PopOver();
			checkOver.setArrowLocation(ArrowLocation.RIGHT_CENTER);
			checkOver.setContentNode(new Label("Various Moving Averages can be shown on the graph by\nselecting one or multiple options here."));
			checkOver.setAutoFix(true);
			checkOver.setAutoHide(true);
			checkOver.setHideOnEscape(true);
			checkOver.setDetachable(false);
		}
		checkOver.show(cb);
	}
	
	/**
	 * Displays popup for log
	 */
	private void displayLogTutorial(TableView t) {
		if (logOver == null) {
			logOver = new PopOver();
			logOver.setArrowLocation(ArrowLocation.RIGHT_CENTER);
			logOver.setContentNode(new Label("A log of recently viewed stocks can be seen here.\nClicking on 'name' will sort the entries alphabetically."));
			logOver.setAutoFix(true);
			logOver.setAutoHide(true);
			logOver.setHideOnEscape(true);
			logOver.setDetachable(false);
		}
		
		logOver.show(t);
	}

	/**
	 * Runs an interactive tutorial for the user anytime 'help' is clicked
	 */
	private void tutorial(ListView<?> list, AreaChart<Number, Number> chart, RadioButton r, CheckBox cb, TableView t) {
		displayStockTutorial(list);
		displayGraphTutorial(chart);
		displayTimeSpanTutorial(r);
		displayMATutorial(cb);
		displayLogTutorial(t);
	}
	
	/**
	 * Adds zooming and panning functionality to the given chart
	 * @param ac Chart to be zoomed or panned
	 */
	private void addFunction(AreaChart<Number, Number> ac) {
		//Panning works via either secondary (right) mouse or primary with ctrl held down 
        panner = new ChartPanManager( ac ); 
        panner.setMouseFilter( new EventHandler<MouseEvent>() { 
           @Override 
           public void handle( MouseEvent mouseEvent ) { 
           if ( mouseEvent.getButton() == MouseButton.SECONDARY || 
              ( mouseEvent.getButton() == MouseButton.PRIMARY && 
                mouseEvent.isShortcutDown() ) ) { 
            //let it through 
            } else { 
             mouseEvent.consume(); 
            } 
           } 
          } ); 
          panner.start(); 
          
          Rectangle rect = new Rectangle();
          zoomManager = new ChartZoomManager( bp, rect, ac );
          zoomManager.start(); 
	}
	
	/**
	 * Removes zooming and panning function from chart by invoking manager's stop method.
	 * Used when updating graphs
	 */
	private void stopFunction() {
		if (zoomManager != null && panner != null) {
			panner.stop();
			zoomManager.stop();
		}
	}
	
	/**
	 * Check for duplicates in an observable list and removes them to only show one instance
	 * @param list Observable list containing items to be checked
	 */
	private void addToLog(Stock s, ObservableList list, TableView t, String ticker) {
		//	check if there aren't any stocks already in list
		if (list.size() >= 1) {
			//	scan list for duplicate stock, if found remove all instances of duplicate and add to top
			for (int i = 0; i < list.size(); ++i) {
				for (int j = 0; j < list.size(); ++j) {
					Stock temp = (Stock) list.get(j);
					if (temp.getName().equals(s.getName())) {
						list.remove(j);
					}
				}
			}
			//	adds to top once duplicates are removed
			list.add(0, s);
		}
		else {
			list.add(0, s);
		}
		t.setItems(list);
	}
	
	
	/**
	 * Main that runs the entire project and displays the graph
	 * @param args Standard Main method parameter
	 */
    public static void main(String[] args) {
    	//	POTENTIAL LOGIN HERE
    	
    	//	run program
        launch(args);
    }
}
