package search.hillclimbing;

import gui.AlgorithmFrame;

import java.util.List;

import misc.Decision;
import misc.Period;
import misc.TaxFormula;
import search.Search;

public class Hillclimbing extends Search {
	
	private AlgorithmFrame frame;
	private List<Period> periods;
	private double interesstRate;
	
	private double stepSize;

	public Hillclimbing(List<Period> periods, double interesstRate) {
		this.periods = periods;
		this.interesstRate = interesstRate;
		frame = new AlgorithmFrame(periods);
		frame.setTitle("Hillclimbing");
	}

	@Override
	public void run() {
		
	}

	public void setDecisions() {
		for (int i = 0; i < periods.size(); i++) {
			if (periods.get(i).getIncome() < 0) {
				periods.get(i).setDecision(Decision.SHARED);
				TaxFormula.calculatePeriod(null, periods.get(i), periods.get(i + 1));
			} else {
				
			}
		}
	}

}
