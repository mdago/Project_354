package iteration2;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javafx.scene.chart.XYChart;

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
	
	/**
	 * Checks and returns intersection points between 2 lines
	 * @param s1 data series 1 (should be closing prices if closing prices are being used to check)
	 * @param s2 data series 2
	 * @param period period of moving average (if both series are moving averages, put the period as 1)
	 * @return ArrayList of intersection points
	 */
	public ArrayList<Point2D> checkIntersection(XYChart.Series<Number,Number> s1, XYChart.Series<Number,Number> s2, int period1, int period2) {
		//	array that will be returned
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		
		//	starting point for loop
		int bound = 0;
		int size = 0;
		int diff = period2 - period1;
		
		//	if the series are the same size, start at 1, else start at the period (special case of all time timespan)
		if (s1.getData().size() <= s2.getData().size()) {
			bound = 1; 
			size = s1.getData().size();
			//	loop through entire data series
			for (int i = bound; i < size; ++i) {
				//	if there's an intersection, calculate intersection point and add it to the array
				if (Line2D.linesIntersect(s1.getData().get(i-1).getXValue().doubleValue(), s1.getData().get(i-1).getYValue().doubleValue(), 
						s1.getData().get(i).getXValue().doubleValue(), s1.getData().get(i).getYValue().doubleValue(), 
						s2.getData().get(i-1).getXValue().doubleValue(), s2.getData().get(i-1).getYValue().doubleValue(), 
						s2.getData().get(i).getXValue().doubleValue(), s2.getData().get(i).getYValue().doubleValue())) 
				{
					points.add(getIntersectionPoint(s1.getData().get(i-1).getXValue().doubleValue(), s1.getData().get(i-1).getYValue().doubleValue(), 
						s1.getData().get(i).getXValue().doubleValue(), s1.getData().get(i).getYValue().doubleValue(), 
						s2.getData().get(i-1).getXValue().doubleValue(), s2.getData().get(i-1).getYValue().doubleValue(), 
						s2.getData().get(i).getXValue().doubleValue(), s2.getData().get(i).getYValue().doubleValue()));
				}
			}
		}
		else if (s1.getData().size() >= s2.getData().size()) {
			bound = 1; 
			size = s2.getData().size();
			//	loop through entire data series
			for (int i = bound; i < size; ++i) {
				//	if there's an intersection, calculate intersection point and add it to the array
				if (Line2D.linesIntersect(s1.getData().get(i-1+diff).getXValue().doubleValue(), s1.getData().get(i-1+diff).getYValue().doubleValue(), 
						s1.getData().get(i+diff).getXValue().doubleValue(), s1.getData().get(i+diff).getYValue().doubleValue(), 
						s2.getData().get(i-1).getXValue().doubleValue(), s2.getData().get(i-1).getYValue().doubleValue(), 
						s2.getData().get(i).getXValue().doubleValue(), s2.getData().get(i).getYValue().doubleValue())) 
				{
					points.add(getIntersectionPoint(s1.getData().get(i-1).getXValue().doubleValue(), s1.getData().get(i-1).getYValue().doubleValue(), 
						s1.getData().get(i).getXValue().doubleValue(), s1.getData().get(i).getYValue().doubleValue(), 
						s2.getData().get(i-1).getXValue().doubleValue(), s2.getData().get(i-1).getYValue().doubleValue(), 
						s2.getData().get(i).getXValue().doubleValue(), s2.getData().get(i).getYValue().doubleValue()));
				}
			}
		}
		else {
			bound = period1;
			//	loop through entire data series
			for (int i = bound; i < size; ++i) {
				//	if there's an intersection, calculate intersection point and add it to the array
				if (Line2D.linesIntersect(s1.getData().get(i-1).getXValue().doubleValue(), s1.getData().get(i-1).getYValue().doubleValue(), 
						s1.getData().get(i).getXValue().doubleValue(), s1.getData().get(i).getYValue().doubleValue(), 
						s2.getData().get(i-1).getXValue().doubleValue(), s2.getData().get(i-1).getYValue().doubleValue(), 
						s2.getData().get(i).getXValue().doubleValue(), s2.getData().get(i).getYValue().doubleValue())) 
				{
					points.add(getIntersectionPoint(s1.getData().get(i-1).getXValue().doubleValue(), s1.getData().get(i-1).getYValue().doubleValue(), 
						s1.getData().get(i).getXValue().doubleValue(), s1.getData().get(i).getYValue().doubleValue(), 
						s2.getData().get(i-1).getXValue().doubleValue(), s2.getData().get(i-1).getYValue().doubleValue(), 
						s2.getData().get(i).getXValue().doubleValue(), s2.getData().get(i).getYValue().doubleValue()));
				}
			}
		}
		
		//	return array of points
		return points;
	}
	
	/**
	 * Calculates the intersection point using the determinant method based on simplified Linear Algebraic matrices
	 * @param x1 x-coordinate of the first point
	 * @param y1 y-coordinate of the first point
	 * @param x2 x-coordinate of the second point
	 * @param y2 y-coordinate of the second point
	 * @param x3 x-coordinate of the third point
	 * @param y3 y-coordinate of the third point
	 * @param x4 x-coordinate of the fourth point
	 * @param y4 y-coordinate of the fourth point
	 * @return The intersection point, x and y coordinates as a Point2D.Double
	 */
	public Point2D.Double getIntersectionPoint(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		//	solved using determinants, based on inspiration from CommanderKeith and lbackstrom and references from 
		//	https://www.topcoder.com/community/data-science/data-science-tutorials/geometry-concepts-line-intersection-and-its-applications/
		
		//	determinant for line 1
	    double det1And2 = det(x1, y1, x2, y2);
	    
	    //	determinant for line 2
	    double det3And4 = det(x3, y3, x4, y4);
	    
	    //	algebraic multiplications
	    double x1X2 = x1 - x2;
	    double y1Y2 = y1 - y2;
	    double x3X4 = x3 - x4;
	    double y3Y4 = y3 - y4;
	    
	    //	determinant of algebraic multiplications
	    double det1Less2And3Less4 = det(x1X2, y1Y2, x3X4, y3Y4);
	    
	    //	using the above algebraic simplifications, the coordinate points can be solved
	    double x = (det(det1And2, x1X2, det3And4, x3X4) / det1Less2And3Less4);
	    double y = (det(det1And2, y1Y2, det3And4, y3Y4) / det1Less2And3Less4);
	    
	    //	return the point
	    return new Point2D.Double(x, y);
	}
	
	/**
	 * Calculates determinant from the equivalent of a 2d matrix
	 * @param a row 1, column 1
	 * @param b row 1, column 2
	 * @param c row 2, column 1
	 * @param d row 2, column 2
	 * @return determinant
	 */
	public static double det(double a, double b, double c, double d) {
	      return a * d - b * c;
	}
	
	
}
