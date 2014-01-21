package misc;

import java.util.List;

/**
 * Tax formulas taken from excel template
 * 
 * @author chris
 * 
 */
public class TaxFormula {

	/**
	 * calculate the maximum loss carryback according to excel template
	 * 
	 * @param predesseccor
	 *            the previous period, if there is no previous period (null) a
	 *            neutral period will be created
	 * @param current
	 *            the current period
	 * @return the maximum loss carryback
	 */
	public static int calculateMaximumLosscarryback(Period predesseccor,
			Period current) {
		if (predesseccor == null) {
			predesseccor = new Period(current.getTime() - 1, 0, 0,
					Decision.SHARED, 0);
			predesseccor.setPeriodMoney(current.getPeriodMoney());
		}
		int currentmin = Math.min(0, (1 - current.getDecision().getValue())
				* current.getIncomeAndInteresst()
				+ current.getDecision().getValue() * current.getIncome());
		int premax = Math.max(0, predesseccor.getTaxableProfit());
		int lossLimit = Math.max(-511500,
				Math.max(currentmin, -premax));
		return lossLimit;
	}

	/**
	 * calculate the taxable profit according to excel template
	 * 
	 * @param predesseccor
	 *            the previous period, if there is no previous period (null) a
	 *            neutral period will be created
	 * @param current
	 *            the current period
	 * @param successor
	 *            the next period, if there is no next period (null) a neutral
	 *            period will be created. From the successor only the loss carryback is used.
	 * @return the taxable profit
	 */
	public static int calculateTaxableProfit(Period predesseccor,
			Period current, Period successor) {
		if (predesseccor == null) {
			predesseccor = new Period(current.getTime() - 1, 0, 0,
					Decision.SHARED, 0);
			predesseccor.setPeriodMoney(current.getPeriodMoney());
		}
		if (successor == null) {
			successor = new Period(current.getTime() + 1, 0, 0,
					Decision.SHARED, 0);
		}
		int taxableProfit = 0;
		if (current.getDecision().equals(Decision.SHARED)) {

			int preMin = (int) Math.min(-1000000 - 0.6 * (predesseccor.getIncomeAndInteresst() - 1000000), 0);
			int currentMax = Math.max(predesseccor.getNotUsedLossCarryforward(), preMin);
			
			taxableProfit = Math.max(0, current.getIncomeAndInteresst() + currentMax);
		} else {
			int currentMin = (int) Math.min(-1000000 - 0.6 * (current.getIncome() - 1000000), 0);
			int currentMax = Math.max(predesseccor.getNotUsedLossCarryforward(), currentMin);
			
			taxableProfit = Math.max(0, current.getIncome() + currentMax);
		}

		return taxableProfit;
	}
	
	public static int calculateTaxesAfterLossCarryback(Period current, Period successor) {
		if (successor == null) {
			return Math.max(current.getTaxableProfit(), 0);
		} else {
			return Math.max(current.getTaxableProfit() + successor.getLossCarryback(), 0);
		}
	}

	/**
	 * calculate the TaxA according to excel template
	 * 
	 * @param current
	 *            the current period
	 * @return the TaxA
	 */
	public static double calculateTaxA(Period current) {
		double taxA = ((current.getTaxableProfit() - 8004) / 10000.0);
		return taxA;
	}

	/**
	 * calculate the TaxB according to excel template
	 * 
	 * @param current
	 *            the current period
	 * @return the TaxB
	 */
	public static double calculateTaxB(Period current) {
		double taxB = (current.getTaxableProfit() - 13469) / 10000.0;
		return taxB;
	}

	/**
	 * calculate the taxes according to excel template
	 * 
	 * @param current
	 *            the current period
	 * @return the taxes
	 */
	public static int calculateTaxes(Period current) {
		int taxes = 0;
		if (current.getDecision().equals(Decision.DIVIDED)) {
			taxes = (int) (0.25 * current.getInteresst());
		}

		if (current.getTaxableProfit() >= 0
				&& current.getTaxableProfit() <= 8354) {
			taxes += 0;
		}
		if (current.getTaxableProfit() >= 8355
				&& current.getTaxableProfit() <= 13469) {
			taxes += (int) ((974.58 * current.getTaxA() + 1.400) * current
					.getTaxA());
		}
		if (current.getTaxableProfit() >= 13470
				&& current.getTaxableProfit() <= 52881) {
			taxes += (int) ((228.74 * current.getTaxB() + 2397)
					* current.getTaxB() + 971);
		}
		if (current.getTaxableProfit() >= 52882
				&& current.getTaxableProfit() <= 250730) {
			taxes += (int) (0.42 * current.getTaxableProfit() - 8239);
		}
		if (current.getTaxableProfit() >= 250731) {
			taxes += (int) (0.45 * current.getTaxableProfit() - 15761);
		}
		return taxes;
	}

	
	public static int calculateTaxRecalculation(Period predessesor, Period current) {
		int taxes = 0;
		if (current.getDecision().equals(Decision.DIVIDED)) {
			taxes = (int) (0.25 * current.getInteresst());
		}

		if (current.getTaxableProfitAfterLossCarryback() >= 0
				&& current.getTaxableProfitAfterLossCarryback() <= 8354) {
			taxes += 0;
		}
		if (current.getTaxableProfitAfterLossCarryback() >= 8355
				&& current.getTaxableProfitAfterLossCarryback() <= 13469) {
			taxes += (int) ((974.58 * current.getTaxA() + 1.400) * current
					.getTaxA());
		}
		if (current.getTaxableProfitAfterLossCarryback() >= 13470
				&& current.getTaxableProfitAfterLossCarryback() <= 52881) {
			taxes += (int) ((228.74 * current.getTaxB() + 2397)
					* current.getTaxB() + 971);
		}
		if (current.getTaxableProfitAfterLossCarryback() >= 52882
				&& current.getTaxableProfitAfterLossCarryback() <= 250730) {
			taxes += (int) (0.42 * current.getTaxableProfitAfterLossCarryback() - 8239);
		}
		if (current.getTaxableProfitAfterLossCarryback() >= 250731) {
			taxes += (int) (0.45 * current.getTaxableProfitAfterLossCarryback() - 15761);
		}
		return taxes;
	}

	public static int calculateTaxRefund(Period predesessor, Period current) {
		return predesessor.getTaxes() - predesessor.getTaxRecalculation();
	}
	
	/**
	 * combined method to calculate (and update) a whole period
	 * 
	 * @param predesseccor
	 *            the previous period
	 * @param current
	 *            the current period
	 * @param successor
	 *            the next period
	 */
	public static void calculatePeriod(Period predesseccor, Period current,
			Period successor) {
		current.setMaximumLoss(calculateMaximumLosscarryback(predesseccor,
				current));
		current.setTaxableProfit(calculateTaxableProfit(predesseccor, current,
				successor));
		current.setTaxA(calculateTaxA(current));
		current.setTaxB(calculateTaxB(current));
		current.setTaxes(calculateTaxes(current));
		current.setTaxableProfitAfterLossCarryback(TaxFormula.calculateTaxesAfterLossCarryback(current, successor));
		current.setTaxRecalculation(calculateTaxRecalculation(predesseccor, current));
		current.setTaxRefund(TaxFormula.calculateTaxRefund(predesseccor, current));
		current.setPeriodMoney(predesseccor.getPeriodMoney() + current.getIncomeAndInteresst() - current.getTaxes() + current.getTaxRefund());
	}
	
	
	/**
	 *  updates all values of the periods, that are dynamically computed.
	 */
	public static void updatePeriods(List<Period> periods, float interesstRate) {
		// period 0 only contains the start money in periodMoney
		for (int i=1; i<periods.size(); ++i) {
			periods.get(i).setInteresst((int)(interesstRate * periods.get(i-1).getPeriodMoney()));
			TaxFormula.calculatePeriod(periods.get(i-1), periods.get(i), i+1>=periods.size()? null : periods.get(i+1));
		}
	}
	

//formula tests 
	
	public static void main(String[] args) {
		Period p0 = new Period(0, 0, 0, Decision.SHARED, 0);
		p0.setPeriodMoney(600000);
		Period p1 = new Period(1, 100000, (int) (p0.getPeriodMoney() * 0.1),
				Decision.SHARED, 0);
		Period p2 = new Period(2, -50000, 0, Decision.DIVIDED, -100000);
		Period p3 = new Period(3, 100000, 0, Decision.DIVIDED, 0);
		Period p4 = new Period(4, 100000, 0, Decision.DIVIDED, 0);
		Period p5 = new Period(5, -100000, 0, Decision.DIVIDED, -100000);
		Period p6 = new Period(6, 100000, 0, Decision.DIVIDED, 0);

		calculatePeriod(p0, p1, p2);
		System.out.println("period 1: " + p1.getPeriodMoney());
	
		p2.setInteresst((int) (p1.getPeriodMoney() * 0.1));
		calculatePeriod(p1, p2, p3);
		System.out.println("period 2: " + p2.getPeriodMoney());
		
		p3.setInteresst((int) (p2.getPeriodMoney() * 0.1));
		calculatePeriod(p2, p3, p4);
		System.out.println("period 3: " + p3.getPeriodMoney());
		
		p4.setInteresst((int) (p3.getPeriodMoney() * 0.1));
		calculatePeriod(p3, p4, p5);
		System.out.println("period 4: " + p4.getPeriodMoney());
		
		p5.setInteresst((int) (p4.getPeriodMoney() * 0.1));
		calculatePeriod(p4, p5, p6);
		System.out.println("period 5: " + p5.getPeriodMoney());
		
		p6.setInteresst((int) (p5.getPeriodMoney() * 0.1));		
		calculatePeriod(p5, p6, null);
		System.out.println("period 6: " + p6.getPeriodMoney());
	}
}
