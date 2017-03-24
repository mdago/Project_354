package iteration1;

import java.lang.Math;

/**
 * Class implementation of a moving average calculator
 * @author Stephen Prizio, Marco Dagostino, Ming Tsai, Maximilien Le Clei. Chris McArthur, Himmet Arican, Athanasios Babouras
 * @version 1.0
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
		//	check if period that is passed not one of the 4 acceptable values
		if ((period != 20) && (period != 50) && (period != 100) && (period!= 200)) {
			System.out.println("FAILED");
			return null;	//	null is returned if passed period is not one of the 4 values
		}
		
		//	array that will store ma's
		double[] averages = new double[list.length - period + 1];
		
		//	loops through list of given numbers
		for (int i = 0; i < (list.length - (period - 1)); ++i) {
			//	loops as many times as the length of the period
			for (int j = i; j < (i + period); ++j) {
				//	stores the sum of the total period value of numbers in the helper array
				averages[i] += list[j];
			}
		}
		
		//	obtain moving average by dividing each sum by the period
		for (int k = 0; k < averages.length; ++k)
			averages[k] = Math.round((averages[k] / period) * 100.0) / 100.0;
		
		// return averages;
		return averages;
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
}
