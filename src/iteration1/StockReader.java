package iteration1;

import java.io.*;
import java.lang.Math;

/**
 * Class implementation of a file reader that will read the file 'sampledata.csv' and create subsequent arrays to store the stocks and their closing values
 * @author Stephen Prizio, Marco Dagostino, Ming Tsai, Maximilien Le Clei. Chris McArthur, Himmet Arican, Athanasios Babouras
 * @version 1.0
 *
 */
public class StockReader {
	/**
	 * File name representing the file to be read. This would normally store the relative path, ex: 'C:\Users\file name', however in this case the program will be stored in a folder where the executable
	 * is in the same location as sampledata.csv therefore there is no need to specify directories. (Allows for easy use on any machine).
	 */
	private static String file = "sampledata.csv";
	
	/**
	 * Array of Stock objects taken from the file
	 */
	private static Stock[] stocks;
	
	/**
	 * Array of doubles to store closing prices of stocks
	 */
	private static double[] stockClosingPrices;
	
	/**
	 * Array of doubles to store adjusted closing prices of stocks
	 */
	private static double[] adjustedClosingPrices;
	
	/**
	 * Array of doubles for a time spanned period
	 */
	private static double[] timeSpannedPrices;
	
	/**
	 * Default Constructor that reads file and initializes all fields. This will be called whenever a new StockReader object is called, therefore when the object is created the file is automotaically read.
	 * There isn't any need to call a reading function 
	 */
	public StockReader() {	
		try {
			//	read the file by invoking read
			read();
		}
		catch(FileNotFoundException e) {
			System.out.println("Error, file was not found. Exiting now...");
			System.exit(1);
		}
		catch(IOException e) {
			System.out.println("Corrupted File. Exiting Now...");
			System.exit(1);
		}
	}
	
	/**
	 * Helper method that is used to determine the size of the file. Necessary to initialize sizes of arrays later on
	 * @param fileName Name of file to be read
	 * @return Size of file represented by its number of lines
	 * @throws IOException Exception if corrupted file or File not Found
	 */
	private static int fileSize(String fileName) throws IOException
	{
		BufferedReader textReader  = new BufferedReader(new FileReader(fileName));	//	create new reader
		@SuppressWarnings("unused")
		String lineTracker = null;	//	string that will store the entire line, used to determine when the file stops
		int lineCounter = 0;		//	this counter will determine the number of lines which will be used as the array size
		
		try {
			while ((lineTracker = textReader.readLine()) != null)	//	increment line counter if it is not empty
				++lineCounter;
		}
		catch (FileNotFoundException e) {
			System.out.println("Error, file not found.");
		}
		finally {
			if (textReader != null)		//	close the opened reader
				textReader.close();
		}
		return lineCounter;		//	return number of lines
		
	}
	
	/**
	 * Method that reads the file and stores each line as a stock, with each cell representing a field from the 'Stock' class
	 * @param reader BufferedReader to read the file
	 * @throws IOException Throws exception if corrupted file or File not Found
	 */
	private static void reader(BufferedReader reader) throws IOException {
		stocks = new Stock[fileSize(file)];	//	create array of stocks
		stockClosingPrices = new double[fileSize(file)];
		adjustedClosingPrices = new double[fileSize(file)];
		String line = null;	//	track each line in file
		String str = ",";	//	establish delimiter for file as comma. The file sampledata.csv has extension 'csv' which stands for comma separated file therefore each cell is techincally separated by a ','
		int counter = 0;	//	count each line
		
		while (((line = reader.readLine()) != null)) {		//	check if current line is null
			String[] data = line.split(str);		//	array storing the data of each line in cells where each arary entry is determined by extracting each cell separated by a comma
			
			//	parse cell values to numbers after rounding each number to 2 decimal places
			double open = Math.round((Double.parseDouble(data[1])) * 100.0) / 100.0;
			double high = Math.round((Double.parseDouble(data[2])) * 100.0) / 100.0;
			double low = Math.round((Double.parseDouble(data[3])) * 100.0) / 100.0;
			double close = Math.round((Double.parseDouble(data[4])) * 100.0) / 100.0;
			int volume = Math.round((Integer.parseInt(data[5])) * 100) / 100;
			double adjClose = Math.round((Double.parseDouble(data[6])) * 100.0) / 100.0;
			
			//	create new array entries 
			stocks[counter] = new Stock(data[0], open, high, low, close, volume, adjClose);
			stockClosingPrices[counter] = close;
			adjustedClosingPrices[counter] = adjClose;
			
			//	increment line counter
			++counter;
		}
	}
	
	/**
	 * Method that will execute when constructor is invoked, reads the file by invoking reader()
	 * @throws IOException Throws exception if corrupted file or File not Found
	 */
	public static void read() throws IOException {
		//	create new file reader
		BufferedReader r = null;
		
		try {
			//	initialize reader to file that needs to be read
			r = new BufferedReader(new FileReader(file));
			
			//	read the file
			reader(r);
		}
		catch(FileNotFoundException e) {	//	catch exceptions
			System.out.println("Error, file not found.");
			System.exit(1);
		}
		catch(IOException e) {	//	catch exceptions
			System.out.println("Error encountered.");
			System.exit(1);
		}
		finally {	//	close file once execution completes
			if (r != null)
				r.close();
		}
	}

	/**
	 * Gets all stocks in form of Array
	 * @return Array of Stock objects
	 */
	public Stock[] getStocks() {
		return stocks;
	}
	
	/**
	 * Gets array of closing prices
	 * @return Array of doubles of closing prices
	 */
	public double[] getClosingPrices() {
		return stockClosingPrices;
	}
	
	/**
	 * Gets array of adjusted closing prices
	 * @return Array of doubles of adjusted closing prices
	 */
	public double[] getAdjustedClosingPrices() {
		return adjustedClosingPrices;
	}
	
	/**
	 * Gets array of closing prices within a specified time span
	 * @param timeSpan Length of time in years
	 * @return Array with values spanning the time span
	 */
	public double[] getClosingPrices(int timeSpan) {
		//	specify the year where the time span will end
		int stopYear = stocks[0].getYear() - timeSpan;
		
		//	create a counter that will determine size of array
		int i = 0;
		
		//	loop until end of span reached
		while (stocks[i].getYear() != stopYear) {
			++i;
		}
		
		//	initialize new array
		timeSpannedPrices = new double[i];
		
		//	fill array with correct values
		for (int j = 0; j < i; ++j)
			timeSpannedPrices[j] = stockClosingPrices[j];
		
		return timeSpannedPrices;
	}
}