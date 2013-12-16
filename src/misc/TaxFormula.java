package misc;

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
		int currentmin = (1 - current.getDecision().getValue())
				* current.getIncomeAndInteresst()
				+ current.getDecision().getValue() * current.getIncome();
		int premax = (1 - predesseccor.getDecision().getValue())
				* predesseccor.getIncomeAndInteresst()
				+ predesseccor.getDecision().getValue()
				* predesseccor.getIncome();
		int lossLimit = Math.max(-511500,
				Math.max(Math.min(0, currentmin), -Math.max(0, premax)));
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
	 *            period will be created
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
			successor = new Period(current.getTime() - 1, 0, 0,
					Decision.SHARED, 0);
		}
		int taxableProfit = 0;
		if (current.getDecision().equals(Decision.SHARED)) {
			int preMin = Math.min(0, predesseccor.getIncomeAndInteresst()
					- predesseccor.getLossCarryback());
			int preMin2 = Math.min(0,
					predesseccor.getIncome() - predesseccor.getLossCarryback());
			taxableProfit = Math.max(0, current.getIncomeAndInteresst()
					+ successor.getLossCarryback()
					+ (1 - predesseccor.getDecision().getValue()) * preMin
					+ predesseccor.getDecision().getValue() * preMin2);
		} else {
			taxableProfit = Math
					.max(0,
							current.getIncome()
									+ successor.getLossCarryback()
									+ ((1 - predesseccor.getDecision()
											.getValue())
											* Math.min(
													0,
													(predesseccor
															.getIncomeAndInteresst() - predesseccor
															.getLossCarryback())) + predesseccor
											.getDecision().getValue()
											* Math.min(0, (predesseccor
													.getIncome() - predesseccor
													.getLossCarryback()))));
		}

		return taxableProfit;
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
				&& current.getTaxableProfit() <= 8004) {
			taxes += 0;
		}
		if (current.getTaxableProfit() >= 8005
				&& current.getTaxableProfit() <= 13469) {
			taxes += (int) ((912.17 * current.getTaxA() + 1.400) * current
					.getTaxA());
		}
		if (current.getTaxableProfit() >= 13470
				&& current.getTaxableProfit() <= 52881) {
			taxes += (int) ((228.74 * current.getTaxB() + 2397)
					* current.getTaxB() + 1038);
		}
		if (current.getTaxableProfit() >= 52882
				&& current.getTaxableProfit() <= 250730) {
			taxes += (int) (0.42 * taxes - 8172);
		}
		if (current.getTaxableProfit() >= 250731) {
			taxes += (int) (0.45 * current.getTaxableProfit() - 15694);
		}
		return taxes;
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
		current.setPeriodMoney(predesseccor.getPeriodMoney()
				+ current.getIncomeAndInteresst() - current.getTaxes());
	}

//formula tests 
	
//	public static void main(String[] args) {
//		Period p0 = new Period(0, 0, 0, Decision.SHARED, 0);
//		p0.setPeriodMoney(600000);
//		Period p1 = new Period(1, 0, (int) (p0.getPeriodMoney() * 0.08),
//				Decision.SHARED, 0);
//		Period p2 = new Period(2, -150000, 0, Decision.DIVIDED, -18550);
//		Period p3 = new Period(3, 50000, 0, Decision.SHARED, 0);
//		Period p4 = new Period(4, 0, 0, Decision.SHARED, 0);
//		Period p5 = new Period(5, -150000, 0, Decision.SHARED, -21456);
//		Period p6 = new Period(6, 50000, 0, Decision.DIVIDED, 0);
//
//		calculatePeriod(p0, p1, p2);
//		System.out.println("period 1: " + p1.getPeriodMoney());
//	
//		p2.setInteresst((int) (p1.getPeriodMoney() * 0.08));
//		calculatePeriod(p1, p2, p3);
//		System.out.println("period 2: " + p2.getPeriodMoney());
//		
//		p3.setInteresst((int) (p2.getPeriodMoney() * 0.08));
//		calculatePeriod(p2, p3, p4);
//		System.out.println("period 3: " + p3.getPeriodMoney());
//		
//		p4.setInteresst((int) (p3.getPeriodMoney() * 0.08));
//		calculatePeriod(p3, p4, p5);
//		System.out.println("period 4: " + p4.getPeriodMoney());
//		
//		p5.setInteresst((int) (p4.getPeriodMoney() * 0.08));
//		calculatePeriod(p4, p5, p6);
//		System.out.println("period 5: " + p5.getPeriodMoney());
//		
//		p6.setInteresst((int) (p5.getPeriodMoney() * 0.08));		
//		calculatePeriod(p5, p6, null);
//		System.out.println("period 6: " + p6.getPeriodMoney());
//	}
}
