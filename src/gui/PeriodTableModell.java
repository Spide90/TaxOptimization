package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import misc.Period;

public class PeriodTableModell extends AbstractTableModel {

	private static final long serialVersionUID = 5692869003475173093L;

	private List<Period> periods;

	public PeriodTableModell(List<Period> periods) {
		this.periods = periods;
	}

	@Override
	public int getColumnCount() {
		return periods.size() + 1;
	}

	@Override
	public int getRowCount() {
		return 17;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		if (arg1 == 0) {
			switch (arg0) {
			case 0:
				return "Zeit";
			case 1:
				return "Zahlungsüberschuss";
			case 2:
				return "Zinseinnahmen";
			case 3:
				return "Zahlungsüberschuss und Zinseinnahmen";
			case 4:
				return "Besteuerung";
			case 5:
				return "Maximaler Verlustrücktrag";
			case 6:
				return "Zu versteuerndes Einkommen";
			case 7:
				return "";
			case 8:
				return "";
			case 9:
				return "Steuerzahlungen";
			case 10:
				return "Steuern nach Verlustrücktrag";
			case 11:
				return "Steuer Neuberechnung";
			case 12:
				return "Steuererstattung";
			case 13:
				return "Vermögen";
			case 14:
				return "Verlustrücktrag";
			case 15:
				return "Verlustvortag";
			case 16:
				return "Verlustvortrag übrig";
			default:
				throw new IndexOutOfBoundsException();
			}
		} else {
			switch (arg0) {
			case 0:
				return periods.get(arg1 - 1).getTime();
			case 1:
				return periods.get(arg1 - 1).getIncome();
			case 2:
				return periods.get(arg1 - 1).getInteresst();
			case 3:
				return periods.get(arg1 - 1).getIncomeAndInteresst();
			case 4:
				return periods.get(arg1 - 1).getDecision();
			case 5:
				return periods.get(arg1 - 1).getMaximumLossCarryback();
			case 6:
				return periods.get(arg1 - 1).getTaxableProfit();
			case 7:
				return periods.get(arg1 - 1).getTaxA();
			case 8:
				return periods.get(arg1 - 1).getTaxB();
			case 9:
				return periods.get(arg1 - 1).getTaxes();
			case 10:
				return periods.get(arg1 - 1).getTaxableProfitAfterLossCarryback();
			case 11:
				return periods.get(arg1 - 1).getTaxRecalculation();
			case 12:
				return periods.get(arg1 - 1).getTaxRefund();
			case 13:
				return periods.get(arg1 - 1).getPeriodMoney();
			case 14:
				return periods.get(arg1 - 1).getLossCarryback();
			case 15:
				return periods.get(arg1 - 1).getLossCarryForward();
			case 16:
				return periods.get(arg1 - 1).getNotUsedLossCarryforward();
			default:
				throw new IndexOutOfBoundsException();
			}
		}
	}

	public void updatePeriodTable(List<Period> periods) {
		this.periods = periods;
		fireTableStructureChanged();
	}

}
