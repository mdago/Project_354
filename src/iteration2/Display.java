package iteration2;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.ChartZoomManager;

import iteration2.Flag.Status;
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
	 * Number of checked boxes at any one time
	 */
	private int checkedCount = 0;
	
	/**
	 * Label that will hold live time
	 */
	private Label date = new Label();
	
	/**
	 * Calculator to calculate moving averages
	 */
	private Calculator calculator;
	
	/**
	 * Array containing all dates for the stock's prices
	 */
	private DateObject[] dates;
	
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
     * List of flags for buying or selling signals
     */
    private ListView<String> flags;
    
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
	private double[] closings;
	
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
	 * String that is used to help change the password of the user
	 */
	private String oldPassString;
	
	/**
	 * String that is used to help change the password of the user
	 */
	private String newPassString;
	
	/**
	 * Boolean to determine if a stock was selected
	 */
	private boolean stockSelected = false;
	
	/**
	 * Boolean to check if old password is correct when changing passwords
	 */
	boolean oldPassValidated = false;
	
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
	 * Stocky's main window with stock information and graphing
	 */
	private void openMainWindow(Stage stage, String username){
		// set title of window
        stage.setTitle("Stocky");
        
        //	create layout of window
        bp = new BorderPane();
        
        //	BORDER LAYOUT :: TOP MENU BAR
        
        //	create menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: #414042;");
        
        //	menu section 'File'
        Menu file = new Menu("_File");
        file.setAccelerator(KeyCombination.keyCombination("SHORTCUT+F"));
        
        //	add items to file
        MenuItem logout = new MenuItem("_Log out  ");
        logout.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
		MenuItem changePassword = new MenuItem("_Change Password  ");
        changePassword.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
        MenuItem exit = new MenuItem("Exit");
        
        //	exit to login screen on click
        logout.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent t) {
        		openLoginWindow(stage);
        	}
        });
		
        //	change password
		changePassword.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent t) {
        		openChangePassWindow(stage, username);
        	}
        });
        
        //	exit window on click
        exit.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent t) {
        		System.exit(0);
        	}
        });
        
        //	add tabs to 'file'
        file.getItems().addAll(logout, changePassword, new SeparatorMenuItem(), exit);
        
        //	create chart tab
        Menu chart = new Menu("_Chart");
        
        //	create items
        MenuItem newChart = new MenuItem("_New Chart  ");
        newChart.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        // 	create new chart when it is clicked
        newChart.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent t) {
        		//	check if new chart has just been created or the program just started
        		if (show != null) {
        			zoomManager.stop();
        			panner.stop();
        			stockList.getSelectionModel().select(-1);
        			group.getToggles().get(0).setSelected(true);
        			table.getItems().clear();
        			show = null;
        			bp.setCenter(createChart());
        			checkedCount = 0;
        		}
        	}
        });
        
        //	create a reset button (listener is located at the end of the file)
        MenuItem reset = new MenuItem("_Reset view  ");
        reset.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        
        //	show flags
        MenuItem showF = new MenuItem("_Show Flags");
        showF.setAccelerator(KeyCombination.keyCombination("Ctrl+F"));
        
        //	hide flags
        MenuItem hideF = new MenuItem("_Hide Flags");
        
        //	add items to 'chart'
        chart.getItems().addAll(newChart, reset, showF, hideF);
        
        //	menu section 'Help'
        Menu help = new Menu("_Help");
        
        //	create menu items for 'help' listener is at bottom of file
        MenuItem showHints = new MenuItem("_Show Help  ");
        showHints.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
        
        //	create option for all controls
        MenuItem controls = new MenuItem("_Controls  ");
        controls.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        
        //	add handler for when controls tab is clicked on
        controls.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent a) {
        		Alert cont = new Alert(AlertType.INFORMATION);
        		cont.setTitle("Controls");
        		cont.setHeaderText("This window can be accessed at any time by selecting 'Controls' or by hitting 'Ctrl+C'");
        		cont.setContentText("Stock, moving average or timespan selections: Left Mouse-click\nChart zooming: Mouse-wheel\nChart panning: Hold Right-click and move mouse\n"
        				+ "Reset chart view: Ctrl+R\nNew Chart: Ctrl+N\nShow Help: Ctrl+H");
        		
        		Stage s = (Stage) cont.getDialogPane().getScene().getWindow();
        		s.getIcons().add(new Image("file:src/imgs/graph-icon.png"));
        		
        		cont.showAndWait();
        	}
        });
        
        //	about menu item
        MenuItem about = new MenuItem("_About Stocky  ");
        about.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        
        //	add handler
        about.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent a) {
        		Alert abt = new Alert(AlertType.INFORMATION);
        		abt.setTitle("About Stocky");
        		abt.setHeaderText("2017 Copyright Stocky All Rights Reserved");
        		abt.setContentText("- Programmers, Designers and Creators: \nStephen Prizio, Marco Dagostino, Ming Tsai, Maximilien Le Clei, Chris McArthur, Himmet Arican, Athanasios Babouras\n\n"
        				+ "- ControlsFX: Jonathan Giles 2017 \nhttp://fxexperience.com/\n\n"
        				+ "- JavaFX Utilities: Gillius \nhttps://github.com/gillius/jfxutils/ under Apache License\n\n"
        				+ "- Yahoo Finance API: Stijn Strickx \nhttp://financequotes-api.com/ under MIT License");
        		
        		Stage s = (Stage) abt.getDialogPane().getScene().getWindow();
        		s.getIcons().add(new Image("file:src/imgs/graph-icon.png"));
        		
        		abt.showAndWait();
        	}
        });        
        
        //	add items to help tab
        help.getItems().addAll(showHints, controls, about);
        
        //	add menu tabs to menu bar
        menuBar.getMenus().addAll(file, chart, help);
        
        //	display button bar
        bp.setTop(menuBar);
        
        // 	LEFT PART OF LAYOUT :: STOCK MENU
        
        //	create layout for left side of border pane
        VBox left = new VBox();
        
        //	ensure that the table is empty
        table.getItems().clear();
        
        //	add custom css class for left side
        left.getStyleClass().add("left-style");
        left.setAlignment(Pos.TOP_CENTER);
        
        //	format vbox
        left.setPadding(new Insets(25, 0, 25, 10));
        left.setSpacing(15);
        
        Label welcome = new Label("Welcome, " + username);
        welcome.getStyleClass().add("username-style");
        welcome.setStyle("-fx-text-fill: #81d4fa;");
        
        //	create label
        Label stockLabel = new Label("Stocks List");
        stockLabel.setStyle("-fx-text-fill: #ffffff;");
        
        //	create a list view to list all stocks
        stockList = new ListView<String>();
        
        //	add custom css class
        stockList.getStyleClass().add("list-view-style");
        
        //	list containing stock names
        ObservableList<String> items = FXCollections.observableArrayList (
          	  "     " + names[0], "     " + names[1], "     " + names[2], "     " + names[3], "     " + names[4]
          	, "     " + names[5], "     " + names[6], "     " + names[7], "     " + names[8], "     " + names[9]
          	, "     " + names[10], "     " + names[11], "     " + names[12], "     " + names[13], "     " + names[14]
          	, "     " + names[15], "     " + names[16], "     " + names[17], "     " + names[18], "     " + names[19]
          	, "     " + names[20], "     " + names[21], "     " + names[22], "     " + names[23], "     " + names[24]
          	, "     " + names[25], "     " + names[26], "     " + names[27], "     " + names[28], "     " + names[29]);
        
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
        			if (name.equals("     " + "Apple"))
        				viewer.setImage(imgs[0]);
        			else if (name.equals("     " + "American Express"))
        				viewer.setImage(imgs[1]);
        			else if (name.equals("     " + "Boeing"))
        				viewer.setImage(imgs[2]);
        			else if (name.equals("     " + "Caterpillar"))
        				viewer.setImage(imgs[3]);
        			else if (name.equals("     " + "Cisco Systems"))
        				viewer.setImage(imgs[4]);
        			else if (name.equals("     " + "Chevron"))
        				viewer.setImage(imgs[5]);
        			else if (name.equals("     " + "Coca-Cola"))
        				viewer.setImage(imgs[6]);
        			else if (name.equals("     " + "DuPont"))
        				viewer.setImage(imgs[7]);
        			else if (name.equals("     " + "ExxonMobil"))
        				viewer.setImage(imgs[8]);
        			else if (name.equals("     " + "General Electric"))
        				viewer.setImage(imgs[9]);
        			else if (name.equals("     " + "Goldman Sachs"))
        				viewer.setImage(imgs[10]);
        			else if (name.equals("     " + "Home Depot"))
        				viewer.setImage(imgs[11]);
        			else if (name.equals("     " + "IBM"))
        				viewer.setImage(imgs[12]);
        			else if (name.equals("     " + "Intel"))
        				viewer.setImage(imgs[13]);
        			else if (name.equals("     " + "Johnson & Johnson"))
        				viewer.setImage(imgs[14]);
        			else if (name.equals("     " + "JPMorgan Chase"))
        				viewer.setImage(imgs[15]);
        			else if (name.equals("     " + "McDonald's"))
        				viewer.setImage(imgs[16]);
        			else if (name.equals("     " + "3M Company"))
        				viewer.setImage(imgs[17]);
        			else if (name.equals("     " + "Merck"))
        				viewer.setImage(imgs[18]);
        			else if (name.equals("     " + "Microsoft"))
        				viewer.setImage(imgs[19]);
        			else if (name.equals("     " + "Nike"))
        				viewer.setImage(imgs[20]);
        			else if (name.equals("     " + "Pfizer"))
        				viewer.setImage(imgs[21]);
        			else if (name.equals("     " + "Procter & Gamble"))
        				viewer.setImage(imgs[22]);
        			else if (name.equals("     " + "The Travelers"))
        				viewer.setImage(imgs[23]);
        			else if (name.equals("     " + "UnitedHealth"))
        				viewer.setImage(imgs[24]);
        			else if (name.equals("     " + "United Technologies"))
        				viewer.setImage(imgs[25]);
        			else if (name.equals("     " + "Visa"))
        				viewer.setImage(imgs[26]);
        			else if (name.equals("     " + "Verizon"))
        				viewer.setImage(imgs[27]);
        			else if (name.equals("     " + "Wal-Mart"))
        				viewer.setImage(imgs[28]);
        			else if (name.equals("     " + "Walt Disney"))
        				viewer.setImage(imgs[29]);
        			
        			setText(name);
        			setGraphic(viewer);
        		}
        	}
        });
        
        //	set preferred dimensions for list (bind list view to be same size as parent container)
        left.setPrefWidth(250);
        left.setPrefHeight(700);
        stockList.prefHeightProperty().bind(left.heightProperty());
        stockList.prefWidthProperty().bind(left.widthProperty());
        	
        //	add elements to VBox
        left.getChildren().add(welcome);
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
    			checkedCount = 0;
        		
        		//	get calendar instances
        		Calendar start = Calendar.getInstance();
        		Calendar end = Calendar.getInstance();
        		
        		//	get index of selected stock
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
            	   
       				//	create new stock
				   stock = YahooFinance.get(tickers[stockIndex], start, end, interval);
				   
				   //	closing prices 
				   closings = new double[stock.getHistory().size()];
				   
				   //	dates of closing prices
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
				   
				   //	display central display
				   bp.setCenter(show);
				   
				   //	add stock to log
				   addToLog(stock, recents, table, tickers[stockIndex]);
				   
               } catch (IOException e) {
            	   //	display connection error
            	   ExceptionHandler.displayException();
               }
            }
        });

        
        // 	RIGHT PART OF LAYOUT :: UI CONTROLS
        
        // 	create layout for right side of border pane
        VBox right = new VBox();
        right.setAlignment(Pos.CENTER);
        
        right.getStyleClass().add("right-style");
        
        //	format vbox
        right.setPadding(new Insets(15, 10, 25, 10));
        right.setSpacing(15);
        right.setPrefWidth(250);
        
        //	date label
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy z");
        Date d = new Date();
        Label da = new Label(dateFormat.format(d));
        da.setStyle("-fx-font-size: 0.95em; -fx-text-fill: #ff5722;");
        
        //	show live time
        time();
        date.setStyle("-fx-font-size: 0.95em; -fx-text-fill: #ff5722;");
        
        //	create controls labels
        Label rightTitle = new Label("Graph Controls");
        rightTitle.prefWidthProperty().bind(right.widthProperty());
        rightTitle.setAlignment(Pos.TOP_CENTER);
        rightTitle.setStyle("-fx-font-size: 1.25em; -fx-text-fill: #ffffff;");
        
        Label timeSpanTitle = new Label("Time Spans");
        timeSpanTitle.prefWidthProperty().bind(right.widthProperty());
        timeSpanTitle.setAlignment(Pos.TOP_CENTER);
        timeSpanTitle.setStyle("-fx-font-size: 1.05em; -fx-text-fill: #81d4fa;"); 
        
        Label movingAverageTitle = new Label("Moving Averages");
        movingAverageTitle.prefWidthProperty().bind(right.widthProperty());
        movingAverageTitle.setAlignment(Pos.TOP_CENTER);
        movingAverageTitle.setStyle("-fx-font-size: 1.05em; -fx-text-fill: #81d4fa;"); 
        
        //	create a toggle group so only one choice can be selected
        group = new ToggleGroup();
        
        //	create radio buttons for time span and set ID to number of years to display. All time is represented as 0. These strings will be parsed to integers at runtime
        RadioButton oneYear = new RadioButton("   1 year");
        oneYear.setToggleGroup(group);
        oneYear.setId("1");
        oneYear.setSelected(true);
        oneYear.prefWidthProperty().bind(right.widthProperty());
        RadioButton twoYears = new RadioButton("   2 years");
        twoYears.setToggleGroup(group);
        twoYears.setId("2");
        twoYears.prefWidthProperty().bind(right.widthProperty());
        RadioButton fiveYears = new RadioButton("   5 years");
        fiveYears.setToggleGroup(group);
        fiveYears.setId("5");
        fiveYears.prefWidthProperty().bind(right.widthProperty());
        RadioButton allTime = new RadioButton("   All-time");
        allTime.setToggleGroup(group);
        allTime.setId("100");
        allTime.prefWidthProperty().bind(right.widthProperty());
        
        //	listener for radio button selection
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
            	if (group.getSelectedToggle() != null) {
            		//	display warning if stock was not yet selected
            		if (stockIndex == -1) {
            			//	update timespan so that if the stock is selected next, the correct information is displayed
            			ExceptionHandler.displayWarning("No Stock Selected", "Please select a stock before choosing a time span.");
            			
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
            			checkedCount = 0;
            			
            			//	get calendar instances
            			Calendar start = Calendar.getInstance();
            			Calendar end = Calendar.getInstance();
            		
            			//	get number value of selected radio button
            			RadioButton selected = (RadioButton) group.getSelectedToggle();
            			timeSpan = Integer.parseInt(selected.getId());
                    
            			//	adjust x-axis scale based on selected time span              
            			if (timeSpan == 1|| timeSpan == 2) {
            				interval = Interval.DAILY;
            				delimiter = " (in Days)";
            			}
            			else if (timeSpan == 5) {
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
            			} 
            			catch (IOException e) {
            				//	display connection error
            				ExceptionHandler.displayException();
            			}
            		}
                }
            }
        });
        
        //	create check boxes for moving averages
        twenty = new CheckBox("  20 Day");
        fifty = new CheckBox("   50 Day");
        oneHundred = new CheckBox(" 100 Day");
        twoHundred = new CheckBox(" 200 Day");      
        
        //	add listeners to each check box
        twenty.selectedProperty().addListener(new ChangeListener<Boolean>() {
        	@Override
        	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        		//	check if a stock is selected
        		if (stockSelected) {
        			//	check if the 20 check box is selected
        			if (twenty.isSelected()) {
        				//	increment checkbox checked counter
        				++checkedCount;
        				//	special case for timespan of all time
        				if (timeSpan == 100) {
        					double[] averages = calculator.calculateMovingAverage(closings, 20);
        					addAverageToGraph(averages, show, 20, timeSpan);
        				}
        				else {
        					try {
    							double[] test = getStockData(stock, tickers[stockIndex], timeSpan, interval, 20);
    							double[] averages = calculator.calculateMovingAverage(test, 20);
    							addAverageToGraph(averages, show, 20, timeSpan);	
    						} 
            				catch (IOException e) {
            					ExceptionHandler.displayException();
    						}
        				}
        			}
        			else {
        				--checkedCount;
        				//	clear data from series and remove from graph
        				twentyDayMA.getData().clear();
        				show.getData().remove(twentyDayMA);
        				//bp.setCenter(show);
        			}
        		}
        		else {
        			if (twenty.isSelected()) {
        				ExceptionHandler.displayWarning("No Stock Selected", "Please select a stock before choosing a moving average.");
        			}
        		}
        	}
        });
        fifty.selectedProperty().addListener(new ChangeListener<Boolean>() {
        	@Override
        	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        		if (stockSelected) {
        			if (fifty.isSelected()) {
        				++checkedCount;
        				if (timeSpan == 100) {
        					double[] averages = calculator.calculateMovingAverage(closings, 50);
        					addAverageToGraph(averages, show, 50, timeSpan);
        				}
        				else {
        					try {
    							double[] test = getStockData(stock, tickers[stockIndex], timeSpan, interval, 50);
    							double[] averages = calculator.calculateMovingAverage(test, 50);
    							addAverageToGraph(averages, show, 50, timeSpan);    							
    						} 
            				catch (IOException e) {
            					ExceptionHandler.displayException();
    						}
        				}
        			}
        			else {
        				--checkedCount;
        				//	clear data from series and remove from graph
        				fiftyDayMA.getData().clear();
        				show.getData().remove(fiftyDayMA);
        				//bp.setCenter(show);
        			}
        		}
        		else {
        			if (fifty.isSelected()) {
        				ExceptionHandler.displayWarning("No Stock Selected", "Please select a stock before choosing a moving average.");
        			}
        		}
        	}
        });
        oneHundred.selectedProperty().addListener(new ChangeListener<Boolean>() {
        	@Override
        	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        		if (stockSelected) {
        			if (oneHundred.isSelected()) {
        				++checkedCount;
        				if (timeSpan == 100) {
        					double[] averages = calculator.calculateMovingAverage(closings, 100);
        					addAverageToGraph(averages, show, 100, timeSpan);
        				}
        				else {
        					try {
    							double[] test = getStockData(stock, tickers[stockIndex], timeSpan, interval, 100);
    							double[] averages = calculator.calculateMovingAverage(test, 100);
    							addAverageToGraph(averages, show, 100, timeSpan);
    							
    						} 
            				catch (IOException e) {
            					ExceptionHandler.displayException();
    						}
        				}
        			}
        			else {
        				--checkedCount;
        				//	clear data from series and remove from graph
        				oneHundredDayMA.getData().clear();
        				show.getData().remove(oneHundredDayMA);
        				//bp.setCenter(show);
        			}
        		}
        		else {
        			if (oneHundred.isSelected()) {
        				ExceptionHandler.displayWarning("No Stock Selected", "Please select a stock before choosing a moving average.");
        			}
        		}
        	}
        });
        twoHundred.selectedProperty().addListener(new ChangeListener<Boolean>() {
        	@Override
        	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        		if (stockSelected) {
        			if (twoHundred.isSelected()) { 
        				++checkedCount;
        				if (timeSpan == 100) {
        					double[] averages = calculator.calculateMovingAverage(closings, 200);
        					addAverageToGraph(averages, show, 200, timeSpan);
        				}
        				else {
        					try {
    							double[] test = getStockData(stock, tickers[stockIndex], timeSpan, interval, 200);
    							double[] averages = calculator.calculateMovingAverage(test, 200);
    							addAverageToGraph(averages, show, 200, timeSpan);
    							
    						} 
            				catch (IOException e) {
            					ExceptionHandler.displayException();
    						}
        				}
        			}
        			else {
        				--checkedCount;
        				//	clear data from series and remove from graph
        				twoHundredDayMA.getData().clear();
        				show.getData().remove(twoHundredDayMA);
        				//bp.setCenter(show);
        			}
        		}
        		else {
        			if (twoHundred.isSelected()) {
        				ExceptionHandler.displayWarning("No Stock Selected", "Please select a stock before choosing a moving average.");
        			}
        		}
        	}
        });
        
        //	add label
        right.getChildren().add(da);
        right.getChildren().add(date);
        
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
        log.prefWidthProperty().bind(right.widthProperty());
        log.setAlignment(Pos.TOP_CENTER);
        log.setStyle("-fx-font-size: 1.05em; -fx-text-fill: #81d4fa;"); 
        right.getChildren().add(log);
        
        //	set default text in table
        table.setPlaceholder(new Label("No recently viewed stocks"));
        table.prefWidthProperty().bind(right.widthProperty());
        
        //	create a table column
        TableColumn stockName = new TableColumn<>("Name");
        stockName.setResizable(false);
        stockName.prefWidthProperty().bind(table.widthProperty());
        
        //	add column to table
        table.getColumns().add(stockName);
        
        //	add table to VBox
        right.getChildren().add(table);
        right.getStyleClass().add("right");
        
        //	enable data modification from YahooFinance Stock class
        stockName.setCellValueFactory(
        		new PropertyValueFactory<>("name")
        );
        
        //	set right side of borderpane to be VBox
        bp.setRight(right);
        
        //	add listener to help button
        showHints.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent a) {   
        		if (show == null) {
        			ExceptionHandler.displayWarning("Not enough information", "To show help, select a stock then select 'Show Help'.");
        		}
        		else {
        			//	display hints
        			Tutorial.tutorial(menuBar, stockList, show, oneYear, twenty, table);
        		}
        	}
        });
        
        //	add listener to reset button
        reset.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent a) {
        		if (show != null) {
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
        			checkedCount = 0;
        			
        			//	display recreated chart
        			bp.setCenter(show);
        		}
        	}
        });
        
        //	add listener for show flags
        showF.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent a) {
        		//	cases for which boxes are checked and make sure that only 2 are selected at time of menu invocation
        		if (twenty.isSelected() && fifty.isSelected() && (checkedCount == 2)) {
        			showFlags(twentyDayMA, fiftyDayMA, 20, 50);
        		}
        		else if (twenty.isSelected() && oneHundred.isSelected() && (checkedCount == 2)) {
        			showFlags(twentyDayMA, oneHundredDayMA, 20, 100);
        		}
        		else if (twenty.isSelected() && twoHundred.isSelected() && (checkedCount == 2)) {
        			showFlags(twentyDayMA, twoHundredDayMA, 20, 200);
        		}
        		else if (fifty.isSelected() && oneHundred.isSelected() && (checkedCount == 2)) {
        			showFlags(fiftyDayMA, oneHundredDayMA, 50, 100);
        		}
        		else if (fifty.isSelected() && twoHundred.isSelected() && (checkedCount == 2)) {
        			showFlags(fiftyDayMA, twoHundredDayMA, 50, 200);
        		}
        		else if (oneHundred.isSelected() && twoHundred.isSelected() && (checkedCount == 2)) {
        			showFlags(oneHundredDayMA, twoHundredDayMA, 100, 200);
        		}
        		else
        			ExceptionHandler.displayWarning("Flags can only be shown for 2 series", "Please select only two moving averages");
        	}
        });
        
        // 	add listener for show flags
        hideF.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent a) {
        		bp.setCenter(show);
        	}
        });
        
        //	add custom css to borderpane
        bp.getStyleClass().add("bp");
            
        //	fade animation on window start
        FadeTransition t = new FadeTransition(Duration.millis(1250), bp);
        t.setFromValue(0.6);
        t.setToValue(1);
        //	start fade
        t.play();
        
        //	get size of screen
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        
        //	set window to be size of current screen
        Scene scene  = new Scene(bp, screenSize.getWidth(), screenSize.getHeight());
        
        //	add custom font
        Font.loadFont("file:src/assets/Montserrat-Regular.ttf", 20);
        
        // 	add custom css to chart
        scene.getStylesheets().add("file:src/assets/graph.css");
       
        //	give the application an icon
        stage.getIcons().add(new Image("file:src/imgs/graph-icon.png"));
        
        //	maximize window
        stage.setMaximized(true);
        
        //	display the gui
        stage.setScene(scene);
        
        //	allow window to be resized
        stage.setResizable(true);
        
        //	show window
        stage.show();
	}
	
	/**
	 * Method which opens the login window
	 */
	private void openLoginWindow(Stage stage){
		//	title window
		stage.setTitle("Stocky");

        //	create layout of window
        GridPane grid = new GridPane();
        
        //  set grid layout
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(15);
        grid.setHgap(10);
        
        //  create button container
        HBox hbButtons = new HBox();
        hbButtons.setPadding(new Insets(15, 12, 15, 12));
        hbButtons.setSpacing(20);
        
        hbButtons.setAlignment(Pos.BASELINE_CENTER);
        VBox imgContainer = new VBox();
        
        imgContainer.setAlignment(Pos.TOP_CENTER);
        
        Button submitButton = new Button("Log In");
        Button clearButton = new Button("Start Over");
        Button registerButton = new Button("New User");
        
        //  create username and password labels and text fields
        final Label usernameLabel = new Label("Username:");
        final TextField usernameTextField = new TextField();
        final Label passwordLabel = new Label("Password:");
        final PasswordField passwordTextField = new PasswordField();
        final Label errorLabel = new Label();
        
        //  add everything to grid
        hbButtons.getChildren().addAll(submitButton, clearButton, registerButton);
        
        Image graphImage = new Image("file:src/imgs/graph-icon.png");
        ImageView pic = new ImageView();
        pic.setImage(graphImage);
        
        imgContainer.getChildren().add(pic);
        
        //	title label
        Label title = new Label("Stocky");
        title.setStyle("-fx-font-size: 3em; -fx-text-fill: #81d4fa;");
      
        //	horizontally center label
        GridPane.setHalignment(title, HPos.CENTER);
        
        grid.add(title, 0, 0, 2, 1);
        grid.add(imgContainer, 0, 2, 2, 1);
        grid.add(usernameLabel, 0, 3);
        grid.add(usernameTextField, 1, 3);
        grid.add(passwordLabel, 0, 4);
        grid.add(passwordTextField, 1, 4);
        grid.add(hbButtons, 0, 5, 2, 1);
        grid.add(errorLabel, 0, 6, 2, 1);
        
        //  handler which checks if login info is correct
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e){
        		if(!usernameTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()){
        			try{
        				if(LoginManager.verifyLogin(usernameTextField.getText(), passwordTextField.getText()))
        					openMainWindow(stage, usernameTextField.getText());
        				else{
        					passwordTextField.clear();
                			errorLabel.setText("Login info not correct.");
                			errorLabel.setTextFill(Color.rgb(210, 50, 35));
        				}
        			}
        			catch(IOException ioe){
        				ioe.printStackTrace();
        			}
        		}
        		else{
        			passwordTextField.clear();
        			errorLabel.setText("Username or password is empty.");
        			errorLabel.setTextFill(Color.rgb(210, 50, 35));
        		}
        	}
        	
        });
        
        //  handler which clears the text fields
        clearButton.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e){
        		usernameTextField.clear();
        		passwordTextField.clear();
        		errorLabel.setText(null);
        	}
        });
        
        //  handler which opens the register window
        registerButton.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e){
        		openRegisterWindow(stage);
        	}
        });
        
        //	custom transtion between windows
        FadeTransition t = new FadeTransition(Duration.millis(1500), grid);
        t.setFromValue(0.6);
        t.setToValue(1);
    
        t.play();
        
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        
        Scene scene  = new Scene(grid, screenSize.getWidth(), screenSize.getHeight());
        
        // 	add custom css to chart
        scene.getStylesheets().add("file:src/assets/login.css");
        
        stage.getIcons().add(new Image("file:src/imgs/graph-icon.png"));
        
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
	}
	
	/**
	 * Method used to open the registration window.
	 */
	public void openRegisterWindow(Stage primaryStage){
		Stage registerStage = new Stage();
		registerStage.setTitle("Registation");
		registerStage.getIcons().add(new Image("file:src/imgs/graph-icon.png"));
		registerStage.initModality(Modality.WINDOW_MODAL);
		registerStage.initOwner(primaryStage);
		
		GridPane registerGrid = new GridPane();
		registerGrid.setAlignment(Pos.CENTER);
		registerGrid.setPadding(new Insets(10, 10, 10, 10));
		registerGrid.setVgap(5);
		registerGrid.setHgap(10);
		
		HBox hbButtons = new HBox();
        hbButtons.setAlignment(Pos.BASELINE_CENTER);
        hbButtons.setPadding(new Insets(10, 10, 10, 10));
        hbButtons.setSpacing(20);
        
        Button submitButton = new Button("Submit");
        Button clearButton = new Button("Start Over");
        
        final Label usernameLabel = new Label("Username:");
        final TextField usernameTextField = new TextField();
        final Label passwordLabel = new Label("Password:");
        final PasswordField passwordTextField = new PasswordField();
        final Label confirmPassLabel = new Label("Confirm Password:");
        final PasswordField confirmPassTextField = new PasswordField();
        final Label errorLabel = new Label();
        
        hbButtons.getChildren().addAll(submitButton, clearButton);
        
        registerGrid.add(usernameLabel, 0, 0);
        registerGrid.add(usernameTextField, 1, 0);
        registerGrid.add(passwordLabel, 0, 1);
        registerGrid.add(passwordTextField, 1, 1);
        registerGrid.add(confirmPassLabel, 0, 2);
        registerGrid.add(confirmPassTextField, 1, 2);
        registerGrid.add(hbButtons, 0, 3, 2, 1);
        registerGrid.add(errorLabel, 0, 4, 2, 1);
        
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e){
        		errorLabel.setText("");
        		if(!usernameTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty() && 
        				passwordTextField.getText().equals(confirmPassTextField.getText())){
        			LoginManager.register(usernameTextField.getText(), passwordTextField.getText());
        			
        			registerStage.close();
        		}
        		else{
        			passwordTextField.clear();
        			confirmPassTextField.clear();
        			errorLabel.setText("There was an issue with registering.");
        			errorLabel.setTextFill(Color.rgb(210, 50, 35));
        		}
        	}
        	
        });
        
        clearButton.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e){
        		usernameTextField.clear();
        		passwordTextField.clear();
        		confirmPassTextField.clear();
        		errorLabel.setText(null);
        	}
        });
        
        Scene registerScene  = new Scene(registerGrid, 460, 300);
        registerScene.getStylesheets().add("file:src/assets/login.css");
        
        registerStage.setScene(registerScene);
        registerStage.setResizable(false);
        registerStage.show();
	}
	
 	/**
	 * Opens the window in which the user can change their password
	 * @param primaryStage the background window used to attach the pop-up to it
	 * @param username the username of the current user
	 */
	public void openChangePassWindow(Stage primaryStage, String username) {
		//  the main stage of the password change window
		Stage changePassStage = new Stage();
		changePassStage.setTitle("Change Password");
		changePassStage.getIcons().add(new Image("file:src/imgs/graph-icon.png"));
		changePassStage.initModality(Modality.WINDOW_MODAL);
		changePassStage.initOwner(primaryStage);
		
		//  this grid holds all of the text fields, labels and buttons
		GridPane changePassGrid = new GridPane();
		changePassGrid.setAlignment(Pos.CENTER);
		changePassGrid.setPadding(new Insets(10, 10, 10, 10));
		changePassGrid.setVgap(5);
		changePassGrid.setHgap(10);
		
		HBox hbButtons = new HBox();
        hbButtons.setAlignment(Pos.BASELINE_CENTER);
        hbButtons.setPadding(new Insets(10, 10, 10, 10));
        hbButtons.setSpacing(20);
        
        Button submitButton = new Button("Submit");
        Button clearButton = new Button("Start Over");
        
        final Label passwordLabel = new Label("Old Password:");
        final PasswordField passwordTextField = new PasswordField();
        final Label confirmPassLabel = new Label("Confirm Password:");
        final PasswordField confirmPassTextField = new PasswordField();
        final Label errorLabel = new Label();
        
        hbButtons.getChildren().addAll(submitButton, clearButton);
        
        changePassGrid.add(passwordLabel, 0, 0);
        changePassGrid.add(passwordTextField, 1, 0);
        changePassGrid.add(confirmPassLabel, 0, 1);
        changePassGrid.add(confirmPassTextField, 1, 1);
        changePassGrid.add(hbButtons, 0, 2, 2, 1);
        changePassGrid.add(errorLabel, 0, 3, 2, 1);
        
        //  the implementation of the submit button which checks the user's credentials, if correct
        //  it provides the user with the ability to change their password
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e){
        		errorLabel.setText("");
        		
        		//  check if the old password has been validated
        		if(!oldPassValidated){
        			if(passwordTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()){
        				passwordTextField.clear();
        				confirmPassTextField.clear();
        				errorLabel.setText("Fields not properly filled.");
        			} else
						try {
							if(passwordTextField.getText().equals(confirmPassTextField.getText()) &&
									LoginManager.verifyLogin(username, passwordTextField.getText())){
								oldPassValidated = true;
								oldPassString = passwordTextField.getText();
								passwordTextField.clear();
								confirmPassTextField.clear();
								passwordLabel.setText("New Password:");
							}
							else{
								passwordTextField.clear();
								confirmPassTextField.clear();
								errorLabel.setText("Could not change password.");
								errorLabel.setTextFill(Color.rgb(210, 50, 35));
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
       			}
        		//  this statement is run when the password is validated
        		else{
        			if(passwordTextField.getText().equals(confirmPassTextField.getText())){
        				newPassString = passwordTextField.getText();
       					String message = LoginManager.changePassword(username, oldPassString, newPassString);   
       					passwordTextField.clear();
       					confirmPassTextField.clear();
       					errorLabel.setText(message);
       					errorLabel.setTextFill(Color.rgb(255, 255, 255));
       					passwordLabel.setText("Old password:");
            			oldPassValidated = false;
        			}
        			else{
        				passwordTextField.clear();
       					confirmPassTextField.clear();
       					errorLabel.setText("Could not change password.");
       					errorLabel.setTextFill(Color.rgb(210, 50, 35));
        			}
        		}
        	}
        	
        });
        
        //  the clear button empties the text fields and the error label
        clearButton.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e){
        		passwordTextField.clear();
        		confirmPassTextField.clear();
        		errorLabel.setText(null);
        	}
        });
        
        //  the scene is attached to the stage
        Scene changePasswordScene  = new Scene(changePassGrid, 460, 300);
        changePasswordScene.getStylesheets().add("file:src/assets/login.css");
        
        changePassStage.setScene(changePasswordScene);
        changePassStage.setResizable(false);
        changePassStage.show();
	}
	
	/**
	 * Overrides Application start method. This method creates and runs the GUI
	 * @throws IOException Throw exception if error with connection to Yahoo
	 */
	@Override
	public void start(Stage stage) throws IOException {	
		stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
			@Override
			public void handle(WindowEvent e){
				Platform.exit();
			}
		});
        openLoginWindow(stage);
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
        
        //	return chart
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
        	tick = 20;
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
        ac.setTitle(stockName + " Stock Prices");
        
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
	 * Adds moving averages to graph
	 * @param averages Array of moving averages
	 * @param chart Chart which will contain the data
	 * @param period Moving average period
	 */
	private void addAverageToGraph(double[] averages, AreaChart<Number, Number> chart, int period, int timeSpan) {
		//	call add method with variable parameters depending on requested period
		if (period == 20)
			add(averages, twentyDayMA, chart, period, timeSpan);
		else if (period == 50)
			add(averages, fiftyDayMA, chart, period, timeSpan);
		else if (period == 100)
			add(averages, oneHundredDayMA, chart, period, timeSpan);
		else
			add(averages, twoHundredDayMA, chart, period, timeSpan);
		
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
	private void add(double[] averages, XYChart.Series<Number,Number> series, AreaChart<Number, Number> chart, int period, int timeSpan) {
		//	name series
		series.setName(period + " Day Moving Average");
		
		if (timeSpan != 100) {
			//	add data to series
			for (int i = 0; i < averages.length; ++i) {
				series.getData().add(new XYChart.Data<Number,Number>(i, averages[i]));
			}
		}
		else {
			for (int i = 0; i < averages.length; ++i) {
				series.getData().add(new XYChart.Data<Number,Number>(i + period, averages[i]));
			}
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
		//	create array of images
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
	 * Removes zooming and panning functionality from chart by invoking managers' stop method.
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
	 * Gets additional closing prices in order to calculate a full moving average. 
	 * @param st Stock being calculated
	 * @param ticker Ticker symbol of stock
	 * @param ts timespan of stock
	 * @param interval interval of retrived data
	 * @param period period for moving average
	 * @return returns array of data
	 * @throws IOException throws exception if connection error
	 */
	private double[] getStockData(Stock st, String ticker, int ts, Interval interval, int period) throws IOException {
		//	adjust dates to get extra info for ma calculation
		Calendar front = Calendar.getInstance();
		Calendar back = Calendar.getInstance();
		
		//	if the time span is 5 years, additional data is required to compute averages since the scale has switched from days to weeks
		if (ts == 5)
			front.add(Calendar.YEAR, ((ts+5) * -1));
		else
			front.add(Calendar.YEAR, ((ts+1) * -1));
		
		//	get same stock with additional data
		Stock s = YahooFinance.get(ticker, front, back, interval);
		
		//	size of stock before extracting additional data
		int oldStockSize = st.getHistory().size();
		
		//	size of closing prices + extra period data
		int segmentSize = oldStockSize + period;
		
		//	array to store retrieved data
		double[] temp = new double[segmentSize];
		
		//	gets data in reverse
		for (int i = oldStockSize + period - 1; i >= 0; --i) {
			temp[i] = s.getHistory().get(i).getAdjClose().doubleValue(); 
		}
		
		//	reverse the list
		for (int i = 0; i < temp.length / 2; ++i) {
			double t = temp[i];
			temp[i] = temp[temp.length - i - 1];
			temp[temp.length - i - 1] = t;
		}
		
		//	return array of data
		return temp;	
	}
	
	/**
	 * Displays live time using JavaFX timeline. Help from Blindworks && Community 
	 * http://stackoverflow.com/questions/13227809/displaying-changing-values-in-javafx-label
	 */
	private void time() {
		Timeline timeline = new Timeline(
			    new KeyFrame(Duration.seconds(0),
			      new EventHandler<ActionEvent>() {
			        @Override public void handle(ActionEvent actionEvent) {
			          Calendar time = Calendar.getInstance();
			          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
			          date.setText(simpleDateFormat.format(time.getTime()));
			        }
			      }
			    ),
			    new KeyFrame(Duration.seconds(1))
			  );
			  timeline.setCycleCount(Animation.INDEFINITE);
			  timeline.play();
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

    /**
     * Determines if the intersection point is a buy or sell points
     * @param s1 smaller data series
     * @param s2 larger data series
     * @param period period of moving average
     * @return array lost containing intersection points
     */
    private ArrayList<Flag> buyOrSell(XYChart.Series<Number,Number> s1, XYChart.Series<Number,Number> s2, int period1, int period2) {
			ArrayList<Point2D> intersectPointsMa20 = calculator.checkIntersection(s1,s2, period1, period2);
			ArrayList<Flag> listOfFlags = new ArrayList<Flag>();
			
			for(int i = 0; i < intersectPointsMa20.size() ; i++){
				//	get indices around intersection point
				int prev = (int) Math.floor(intersectPointsMa20.get(i).getX());
				int next = (int) Math.ceil(intersectPointsMa20.get(i).getX());
				
				//Getting the previous and next closing prices
				double prevY20 = s1.getData().get(prev).getYValue().doubleValue();
				double nextY20 = s1.getData().get(next).getYValue().doubleValue();
				
				double y = Math.round(intersectPointsMa20.get(i).getY() * 100.0) / 100.0;
				
				//If the previous closing price is bigger than the next then flag will be a buy
				if((prevY20 > nextY20)){
					listOfFlags.add(new Flag(y, dates[prev], Status.BUY, prev));					
				//If the previous closing price is smaller than the next one, then flag will be sell	
				}else if((prevY20 < nextY20)){
					listOfFlags.add(new Flag(y, dates[prev], Status.SELL, prev));
				}
			
			}
			//	return list
			return listOfFlags;
	}
    
    /**
     * Creates and displays a horizontal list view containing the buy and sell signals
     * @param s1 smaller data series
     * @param s2 larger data series
     * @param period period of ma calculation
     */
    private void showFlags(XYChart.Series<Number,Number> s1, XYChart.Series<Number,Number> s2, int period1, int period2) {
    	// 	flags
		   
		//	create vertical box pane for central display
		VBox middle = new VBox();
		   
		//	add graph
		middle.getChildren().add(show);
		   
		//	set preferred height
		middle.prefHeightProperty().bind(bp.heightProperty());
		
		//	list of flags
		ObservableList<String> fs = FXCollections.observableArrayList ();
		ArrayList<Flag> f = buyOrSell(s1, s2, period1, period2);
		fs.clear();
		for (int i = 0; i < f.size(); ++i)
			fs.add(f.get(i).displayInfo());
		        
		//	set menu items to list view
		flags = new ListView<String>();
		flags.setItems(fs);
		flags.setOrientation(Orientation.HORIZONTAL);
		   
		//	add signals to cells
		flags.setCellFactory(param -> new ListCell<String>() {
		   ImageView v = new ImageView();
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
				   if (name.charAt(0) == 'b')
	    				v.setImage(new Image("file:src/imgs/greenArrow.png", 15.0, 15.0, true, true));
				   else if (name.charAt(0) == 's')
	     				v.setImage(new Image("file:src/imgs/redArrow.png", 15.0, 15.0, true, true));
	     			
	     			setText(name.substring(1));
	     			setGraphic(v);
				   }
			   }
		   });
		   
		//	set sizing
		show.prefHeightProperty().bind(middle.prefHeightProperty());
		flags.prefHeightProperty().bind(stockList.widthProperty());
		   
		//	add custom css class
	    middle.getStyleClass().add("right-style");
	    flags.getStyleClass().add("list-view-style");
		   
		//	add to central display
		middle.getChildren().add(flags);
		   
		//	display central display
		bp.setCenter(middle);
    }
}