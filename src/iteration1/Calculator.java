package iteration1;

/**
 * Class implementation of a moving average calculator
 * @author Stephen Prizio, Marco Dagostino, Ming Tsai, Maximilien Le Clei. Chris McArthur, Himmet Arican, Athanasios Babouras
 * @version 2.0
 */
public class Calculator {	
	/**
	 * Constructor for calculating MA for array of Doubles
	 */
	public Calculator() { }
	
	/**
	 * Method that calculates moving averages from the parameter 'list' using a certain period, taken from param period
	 * @param list List of doubles (representing prices)
	 * @param period to which to be calculated
	 * @return Array of moving averages (Type: Double). Returns null if period is not 20, 50, 100 or 200
	 */
	public double[] calculateMovingAverage(double[] list, int period) {	
		//	size of avgs will be smaller since the period will consume some of the data
		double[] avgs = new double[list.length - period + 1];
		
		
		for (int i = period; i < list.length + 1; ++i) {
			double sum = 0;
			for (int j = i - 1; j >= i - period; --j) {
				sum += list[j];
			}
			avgs[i - period] = sum / period;
		}
		
		return avgs;
	}
	
	/**
	 * Helper method to calculate average of an array of doubles
	 * @param a Array of doubles
	 * @return Double value representing average of passed array
	 */
	public double average(double[] a) {
		//	store sum of all array values
		double sum = 0;
		
		for (int l = 0; l < a.length; ++l)
			sum += a[l];
		
		return (sum / a.length);
	}
	
	/**
	 * Method that returns the max value in an array
	 */
	public double getMax(double[] a) {
		//	store max value
		double max = a[0];
		
		for (int i = 1; i < a.length; ++i) {
			if (max <= a[i])
				max = a[i];
		}
		
		//	return max
		return max;
	}
	
	/**
	 * Method that returns the min value in an array
	 */
	public double getMin(double[] a) {
		//	store max value
		double min = a[0];
		
		for (int i = 1; i < a.length; ++i) {
			if (min >= a[i])
				min = a[i];
		}
		
		//	return max
		return min;
	}
}
