package iteration1;

/**
 * Class implementation of a Stock to be used concurrently with the given file containing stocks, 'sampledata.csv'
 * @author Stephen Prizio, Marco Dagostino, Ming Tsai, Maximilien Le Clei. Chris McArthur, Himmet Arican, Athanasios Babouras
 * @version 1.0
 */
public class Stock {
	/**
	 * String variable representing the date when the data for the stock was recorded
	 */
	private String date;
	
	/**
	 * Integer variable representing year of stock
	 */
	private int year;
	
	/**
	 * Double variable storing the opening price of the stock on a certain date
	 */
	private double open;
	
	/**
	 * Double variable storing the highest attained price of the stock on a certain date
	 */
	private double high;
	
	/**
	 * Double variable storing the lowest attained price of the stock on a certain date
	 */
	private double low;
	
	/**
	 * Double variable storing the closing price of the stock on a certain date
	 */
	private double close;
	
	/**
	 * Integer variable storing the volume of available stock as of a certain date
	 */
	private int volume;
	
	/**
	 * Double variable storing the adjusted closing price of the stock on a certain date
	 */
	private double adjustedClose;
	
	/**
	 * Default Constructor. Initializes all values to zero when no stock information is available
	 */
	public Stock() {
		date = "";
		open = 0;
		high = 0;
		low = 0;
		close = 0;
		volume = 0;
		adjustedClose = 0;
	}
	
	/**
	 * Regular constructor representing the given values per each date in the given stock file
	 * @param someDate Recorded date for the stock information
	 * @param someOpen Opening price of stock
	 * @param someHigh Highest price of stock during recorded date
	 * @param someLow Lowest price of stock during recorded date
	 * @param someClose Closing price of stock
	 * @param someVolume Available volume of stock as of specific date
	 * @param someAdjusted Adjusted closing price of stock
	 */
	public Stock(String someDate, double someOpen, double someHigh, double someLow, double someClose, int someVolume, double someAdjusted) {
		date = someDate;
		open = someOpen;
		high = someHigh;
		low = someLow;
		close = someClose;
		volume = someVolume;
		adjustedClose = someAdjusted;
	}
	
	/**
	 * Gets the date of the stock
	 * @return String representing date of stock
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * Gets opening price of stock on date
	 * @return Double representing opening price of stock
	 */
	public double getOpen() {
		return open;
	}
	
	/**
	 * Gets highest price of stock on date
	 * @return Double representing highest price of stock for specific date
	 */
	public double getHigh() {
		return high;
	}

	/**
	 * Gets lowest price of stock on date
	 * @return Double representing lowest price of stock for specific date
	 */
	public double getLow() {
		return low;
	}

	/**
	 * Gets closing price of stock on date
	 * @return Double representing closing price of stock for specific date
	 */
	public double getClose() {
		return close;
	}
	
	/**
	 * Gets available volume for stock as of specific date
	 * @return Integer representing available volume of stock as of specific date
	 */
	public int getVolume() {
		return volume;
	}
	
	/**
	 * Gets adjusted closing price of stock for specific date
	 * @return Double representing adjusted closing price of stock
	 */
	public double getAdjustedClose() {
		return adjustedClose;
	}

	/**
	 * Gets year of stock by parsing last two characters of date to integers. Assumes no years will exceed 2017
	 * @return Integer value representing year of stock information
	 */
	public int getYear() {		
		char beforeLastChar = date.charAt(date.length() - 2);
		char lastChar = date.charAt(date.length() - 1);
		
		year = Integer.parseInt(new StringBuilder().append(beforeLastChar).append(lastChar).toString());
		
		year += 2000;
		
		if (year > 2016)
			year -= 100;
		
		return year;
	}
	
	@Override
	/**
	 * Overrides Object's toString()
	 * @return String displaying each value of the stock on one line
	 */
	public String toString() {
		return ("Date: " + date + " Open: " + open + " High: " + high + " Low: " + low + " Close: " + close + " Volume: " + volume + " Adj Close: " + adjustedClose);
	}

}