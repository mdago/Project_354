package iteration2;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import javafx.scene.chart.AreaChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;

public class Tutorial {
	/**
	 * Stock Popover message box
	 */
	private static PopOver stockOver;
	
	/**
	 * Graph popover
	 */
	private static PopOver graphOver;
	
	/**
	 * Radio button popover
	 */
	private static PopOver radioOver;
	
	/**
	 * MA button popover
	 */
	private static PopOver checkOver;
	
	/**
	 * Log popover
	 */
	private static PopOver logOver;
	
	/**
	 * Controls popover
	 */
	private static PopOver menuOver;
	
	/**
	 * Displays popup for stock list help
	 */
	public static void displayStockTutorial(ListView<?> list) {
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
	public static void displayGraphTutorial(AreaChart<Number, Number> chart) {
		if (graphOver == null) {
			graphOver = new PopOver();
			graphOver.setArrowLocation(ArrowLocation.TOP_CENTER);
			graphOver.setContentNode(new Label("The daily closings prices will be displayed here \nonce a stock is selected.\nUse the mouse wheel to zoom. "
					+ "Hold down Right click \nand move the mouse to pan. The view can be reset at any time\nby selecting 'reset' from the 'Chart' tab"));
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
	public static void displayTimeSpanTutorial(RadioButton r) {
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
	public static void displayMATutorial(CheckBox cb) {
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
	public static void displayLogTutorial(TableView<?> t) {
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
	 * Display popup for menu bar
	 */
	public static void displayMenuBarTutorial(MenuBar mb) {
		if (menuOver == null) {
			menuOver = new PopOver();
			menuOver.setArrowLocation(ArrowLocation.TOP_CENTER);
			menuOver.setContentNode(new Label("Controls for logging out, exiting, accessing settings, finding help\n and adjusting the chart can be found in these menus"));
			menuOver.setAutoFix(true);
			menuOver.setAutoHide(true);
			menuOver.setHideOnEscape(true);
			menuOver.setDetachable(false);
		}
		
		menuOver.show(mb);
	}
	
	/**
	 * Displays helper messages to user
	 */
	public static void tutorial(MenuBar mb, ListView<?> list, AreaChart<Number, Number> chart, RadioButton r, CheckBox cb, TableView<?> t) {
		displayMenuBarTutorial(mb);
		displayStockTutorial(list);
		displayGraphTutorial(chart);
		displayTimeSpanTutorial(r);
		displayMATutorial(cb);
		displayLogTutorial(t);
	}
}
