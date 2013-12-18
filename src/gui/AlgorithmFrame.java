package gui;

import java.util.List;

import misc.Period;

/**
 * implement this in all 3 algorithmn frames to ensure same base of all frames
 * 
 * @author chris
 *
 */
public interface AlgorithmFrame {

	/**
	 * updates the period view
	 * 
	 * @param periods the new periods
	 */
	public void updatePeriodTable(List<Period> periods);
	
	/**
	 * print a debug message to the console
	 * 
	 * @param message the message to be printed
	 */
	public void printDebugMessage(String message);
	
}
