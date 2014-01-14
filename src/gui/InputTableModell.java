package gui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class InputTableModell extends AbstractTableModel {

	private static final long serialVersionUID = 5580172268854002274L;

	private List<Integer> periods;
	private List<Integer> incomes;

	public InputTableModell() {
		periods = new LinkedList<Integer>();
		incomes = new LinkedList<Integer>();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) {
			return String.class;
		} else {
			return Integer.class;
		}
	}

	@Override
	public int getColumnCount() {
		return periods.size() + 1;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0) {
			return "labels";
		} else {
			return String.valueOf(periods.get(columnIndex - 1));
		}
	}

	@Override
	public int getRowCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			if (rowIndex == 0) {
				return "Periode";
			} else {
				return "Zahlungsüberschuss";
			}
		} else {
			if (rowIndex == 0) {
				return periods.get(columnIndex - 1);
			} else {
				return incomes.get(columnIndex - 1);
			}
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (rowIndex > 0) && (columnIndex != 0);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (rowIndex == 0) {
			periods.set(columnIndex-1, (int) aValue);
		} else {
			incomes.set(columnIndex-1, (int) aValue);
		}
		fireTableStructureChanged();
	}

	public void updatePeriodNumber(int periodNumber) {
		if (periods.size() < periodNumber) {
			for (int i = periods.size(); i < periodNumber; i++) {
				periods.add(i + 1);
				incomes.add(0);
			}
		} else {
			for (int i = periods.size(); i > periodNumber; i--) {
				periods.remove(i - 1);
				incomes.remove(i - 1);
			}
		}
		fireTableStructureChanged();
	}
}
