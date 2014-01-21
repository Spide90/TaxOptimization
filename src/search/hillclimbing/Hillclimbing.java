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
		frame = new AlgorithmFrame(periods, "Hillclimbing");
	}

	@Override
	public void run() {
		setDecisions();
	}

	public void setDecisions() {
		for (int i = 1; i < periods.size(); i++) {
			Period predesseccor = i - 1 < 0 ? null : periods.get(i - 1);
			Period successor = i + 1 == periods.size() ? null : periods.get(i + 1);
			if (predesseccor != null) {
				periods.get(i).setInteresst((int) (predesseccor.getPeriodMoney() *  interesstRate));
			}
			TaxFormula.calculatePeriod(predesseccor, periods.get(i), successor);
		}
		frame.updatePeriodTable(periods);
	}

}
