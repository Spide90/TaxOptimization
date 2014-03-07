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
		// give the user a hint if one loss carryback was not allowed
		boolean showHint = false;
		int index =1;
		do {
			// compute the table
			TaxFormula.updatePeriodsStartingAt(periods, interesstRate, index-1); // -1 because the predecessor also needs to be
			// recomputed because its tax recomputation depends on the fixed loss carryback. The handling for index 0 (dummy period) is correctly handled.
			index = fixLossCarryBacks();
			showHint |= (index!=-1); // needed to fix some loss carryback
		} while(index != -1);
		
	/*	for (int i=1; i<periods.size(); ++i) {
			Period current = periods.get(i);
			Period predecessor = periods.get(i-1);
			TaxFormula.updateInterest(current, predecessor, interesstRate);
			current.setMaximumLoss(TaxFormula.calculateMaximumLosscarryback(predecessor, current));
			if (current.getLossCarryback() < current.getMaximumLossCarryback()) { // loss carrybacks are negative
				current.setLossCarryback(current.getMaximumLossCarryback());
				showHint=true;
			}
			TaxFormula.recalculatePeriodMoney(current, predecessor, false, interesstRate, false);
		}*/
		
		gui.updatePeriodTable(periods);
		if (drawPlots) {
			gui.updatePlots(null, null, null, null, null);
		}
		
		if (showHint)
			JOptionPane.showMessageDialog(gui, "You supplied a loss carryback smaller than the allowed maximum loss caryyback. The value has been adapted.",
					"Warning!", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * The period should be calculated before calling this method.
	 * 
	 * Check wheather the loss carrybacks are in the allowed range.
	 * The first occurence of a not allowed loss carryback is fixed.
	 * We fix only the first occurence due to the dependency of the following periods from the fixed period and because
	 * the max loss carrybacks of the successor period also depends on the fixed period. The max loss carryback however does
	 * not depend on any following period, thus it is correct to fix the periods one after another.
	 * 
	 * The method returns the position, at which a carryback was fixed or -1 if nothing was fixed.
	 * If any value has been fixed you need to update your periods and call this method again.
	 */
	protected int fixLossCarryBacks() {
		for (int i=1; i<periods.size(); ++i) {
			Period current = periods.get(i);
			if (current.getLossCarryback() < current.getMaximumLossCarryback()) {
				current.setLossCarryback(current.getMaximumLossCarryback());
				return i;
			}
		}
		return -1;
	}
}
