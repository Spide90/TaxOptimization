package search.simpleTableComputation;

import gui.AlgorithmFrame;

import java.util.List;

import javax.swing.JOptionPane;

import misc.Period;
import misc.TaxFormula;
import search.Search;

/**
 * This algorithm simple computes the table with the user specified values
 */
public class SimpleTableComputation extends Search {

	private List<Period> periods;
	private float interesstRate;
	private AlgorithmFrame gui;
	
	// for the plots
	private boolean drawPlots;
	
	public SimpleTableComputation(List<Period> periods, float interesstRate, boolean drawPlots) {
		this.periods =  periods;
		this.interesstRate = interesstRate;
		this.drawPlots = drawPlots;
		gui = new AlgorithmFrame(periods, "Table for user specified values");
	}
	
	@Override
	public void run() {
		boolean showHint = false;
		// check the user supplied loss carrybacks
		for (int i=1; i<periods.size(); ++i) {
			int maxLossCarryback = TaxFormula.calculateMaximumLosscarryback(periods.get(i-1), periods.get(i));
			System.out.println(maxLossCarryback);
			if (periods.get(i).getLossCarryback() < maxLossCarryback) {
				showHint = true;
				periods.get(i).setLossCarryback(maxLossCarryback);
			}
		}
		
		TaxFormula.updatePeriods(periods, interesstRate);
		gui.updatePeriodTable(periods);
		if (drawPlots) {
			gui.updatePlots(null, null, null, null, null);
		}
		
		if (showHint)
			JOptionPane.showMessageDialog(gui, "You supplied a loss carryback smaller than the allowed maximum loss caryyback. The value has been adapted.",
					"Warning!", JOptionPane.INFORMATION_MESSAGE);
	}
}
