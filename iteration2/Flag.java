package iteration2;

/**
 * Object representing a buy or sell flag. Used in ListView cell display
 * @author Stephen Prizio, Marco Dagostino, Ming Tsai, Maximilien Le Clei, Chris McArthur, Himmet Arican, Athanasios Babouras
 * @version 1.0
 */
public class Flag {
	/**
	 * Enum state of whether the stock should be bought or sold
	 */
	public enum Status {BUY, SELL};
	
	/**
	 * Field to store this flag's enum status
	 */
	private Status status;
	
	/**
	 * Price of the stock
	 */
	private double price;
	
	/**
	 * Date of the stock
	 */
	private DateObject date;
	
	/**
	 * Index in data series of stock
	 */
	private int index;
	
	/**
	 * Constructor that takes price, date and enum status
	 * @param p price of stock
	 * @param d date of stock
	 * @param s BUY or SELL
	 */
	public Flag(double p, DateObject d, Status s, int i) {
		price = p;
		date = d;
		status = s;
		index = i;
	}
	
	/**
	 * Returns price of stock
	 * @return return price
	 */
	public double getPrice() {
		return price;
	}
	
	/**
	 * Returns date of stock
	 * @return return date
	 */
	public DateObject getDate() {
		return date;
	}
	
	/**
	 * Return status of flag
	 * @return return buy or sell
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * Returns index of stock in series
	 * @return index of stock
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Returns a string for display in a listcell
	 * @return flag information
	 */
	public String displayInfo() {
		if (status == Status.BUY) 
			return ("b$" + price + " - Buy\n" + date.toString());
		else
			return ("s$" + price + " - Sell\n" + date.toString());
	}
}