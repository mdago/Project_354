package iteration2;

/**
 * Small class representing the dat of a stock
 * @author s_prizio
 * @version 1.0
 */
public class DateObject {
	/**
	 * Day of the month
	 */
	private int day;
	
	/**
	 * Integer representation of the month of the year. 0 is January, 11 is December
	 */
	private int month;
	
	/**
	 * String representation of the month of the year
	 */
	private String monthString;
	
	/**
	 * Integer value of the year
	 */
	private int year;
	
	/**
	 * Default constructor, instantiates everything to empty
	 */
	public DateObject() {
		day = 0;
		month = 0;
		monthString = "";
		year = 0;
	}
	
	/**
	 * Takes the day, month and year of a given date
	 * @param d day of the month
	 * @param m month of the year
	 * @param y year out of the date
	 */
	public DateObject(int d, int m, int y) {
		day = d;
		month = m;
		monthString = getMonthAsString();
		year = y;
	}
	
	/**
	 * Return the day of the month
	 * @return day of the month
	 */
	public int getDay() {
		return day;
	}
	
	/**
	 * Return month of the year as an integer
	 * @return month as an integer
	 */
	public int getMonth() {
		return month;
	}
	
	/**
	 * Return year
	 * @return year
	 */
	public int getYear() {
		return year;
	}
	
	/**
	 * Returns month of the year as a string
	 * @return String representation of the month
	 */
	public String getMonthAsString() {
		switch (month) {
		case 0:
			monthString = "January";
			break;
		case 1:
			monthString = "February";
			break;
		case 2:
			monthString = "March";
			break;
		case 3:
			monthString = "April";
			break;
		case 4:
			monthString = "May";
			break;
		case 5:
			monthString = "June";
			break;
		case 6:
			monthString = "July";
			break;
		case 7:
			monthString = "August";
			break;
		case 8:
			monthString = "September";
			break;
		case 9:
			monthString = "October";
			break;
		case 10:
			monthString = "November";
			break;
		case 11:
			monthString = "December";
			break;
		}
		return monthString;
	}
	
	/**
	 * Override object toString()
	 * @return String representation of entire date
	 */
	@Override
	public String toString() {
		return (monthString + " " + day + ", " + year);
	}
}
