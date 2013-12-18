package gui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class InputTableModell implements TableModel{

	private List<TableModelListener> listeners;
	
	private List<Integer> periods;
	private List<Integer> incomes;
	
	public InputTableModell() {
		listeners = new LinkedList<TableModelListener>();
		periods = new LinkedList<Integer>();
		incomes = new LinkedList<Integer>();
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
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
		return periods.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0) {
			return "labels";
		} else {
			return String.valueOf(periods.get(columnIndex));
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
				return periods.get(rowIndex - 1);
			} else {
				return incomes.get(rowIndex - 1);
			}
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (rowIndex > 0) && (columnIndex != 0);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (rowIndex == 0) {
			periods.set(columnIndex - 1, (int) aValue);
		} else {
			incomes.set(columnIndex - 1, (int) aValue);
		}
	}

}
