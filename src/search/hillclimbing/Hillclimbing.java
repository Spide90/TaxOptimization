package search.hillclimbing;

import gui.AlgorithmFrame;

import java.util.List;
import java.util.Random;

import misc.Decision;
import misc.Period;
import misc.TaxFormula;
import search.Search;

public class Hillclimbing extends Search {
	
	private enum Direction { UP, DOWN };
	
	private AlgorithmFrame frame;
	private List<Period> periods;
	private double interesstRate;
	
	private double stepSize = 1.005;
	private int tries = 10;

	public Hillclimbing(List<Period> periods, double interesstRate) {
		this.periods = periods;
		this.interesstRate = interesstRate;
		frame = new AlgorithmFrame(periods, "Hillclimbing");
	}

	@Override
	public void run() {
		setDecisions();
		for (int i = 0; i < periods.size(); i++) {
			if (periods.get(i).getIncome() < 0) {
				Period predeseccor;
				Period successor;
				if ((i - 1) < 0) {
					predeseccor = new Period(0, 0, Decision.SHARED, 0);
				} else {
					predeseccor = periods.get(i - 1);
				}
				if ((i + 1) == periods.size()) {
					successor = new Period(0, 0, Decision.SHARED, 0);
				} else {
					successor = periods.get(i + 1);
				}
				optimizeLoss(predeseccor, periods.get(i), successor);
			}
		}
	}

	private void optimizeLoss(Period predeseccor, Period current,
			Period successor) {
		if (current.getMaximumLossCarryback() == 0) return;
		Random random = new Random();
		for (int i = 0; i < tries; i++) {
			int position = -1 * random.nextInt(-1 * current.getMaximumLossCarryback());
			Direction direction = random.nextBoolean() ? Direction.UP : Direction.DOWN;
			current.setLossCarryback(position);
			TaxFormula.calculatePeriod(predeseccor, current, successor);
			int bestValue = current.getPeriodMoney();
			boolean finished = false;
			while (!finished) {
				if (direction.equals(Direction.UP)) {
					position += position * stepSize;
				} else {
					position -= position * stepSize;
				}
				if (position < current.getMaximumLossCarryback() || position >= 0) return;
				current.setLossCarryback(position);
				TaxFormula.calculatePeriod(predeseccor, current, successor);
				if (current.getPeriodMoney() < bestValue) {
					finished = true;
				} else {
					bestValue = current.getPeriodMoney();
				}
			}
		}
	}

	public void setDecisions() {
		for (int i = 1; i < periods.size(); i++) {
			Period predesseccor = i - 1 < 0 ? null : periods.get(i - 1);
			Period successor = i + 1 == periods.size() ? null : periods.get(i + 1);
			if (predesseccor != null) {
				periods.get(i).setInteresst((int) (predesseccor.getPeriodMoney() *  interesstRate));
			}
			TaxFormula.calculatePeriod(predesseccor, periods.get(i), successor);
			if (periods.get(i).getIncome() < 0 && periods.get(i).getMaximumLossCarryback() == 0) {
				periods.get(i).setDecision(Decision.DIVIDED);
				TaxFormula.calculatePeriod(predesseccor, periods.get(i), successor);
				continue;
			}
			int sharedDecision = periods.get(i).getPeriodMoney();
			periods.get(i).setDecision(Decision.DIVIDED);
			TaxFormula.calculatePeriod(predesseccor, periods.get(i), successor);
			if (sharedDecision > periods.get(i).getPeriodMoney()) {
				periods.get(i).setDecision(Decision.SHARED);
				TaxFormula.calculatePeriod(predesseccor, periods.get(i), successor);
			}
		}
		frame.updatePeriodTable(periods);
	}

}
