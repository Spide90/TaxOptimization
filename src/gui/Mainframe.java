package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import misc.Decision;
import misc.Period;
import search.Search;
import search.hillclimbing.Hillclimbing;
import search.monteCarlo.MonteCarlo;
import search.monteCarlo.MonteDaniel;
import search.particleSwarm.ParticleSwarm;

public class Mainframe extends JFrame{

	private static final long serialVersionUID = -7274316265207846835L;

	private JTextField fieldInterestRate;
	private JTextField fieldStartMoney;
	private JSpinner fieldPeriods;
	private JComboBox<String> comboBoxAlgorithm;
	
	private JButton buttonRun;
	private JButton buttonImport;
	private JButton buttonSettings;
	
	private JTable inputTable;
	private InputTableModell inputTableModell;
	
	public Mainframe() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(500, 200));
		setTitle("Super Rückträger");
		init();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void init() {
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.insets = new Insets(3, 3, 3, 3);

		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridx = 0;
		fieldInterestRate = new JTextField();
		fieldInterestRate.setPreferredSize(new Dimension(75, 25));
		fieldInterestRate.setText("Interest Rate");
		layout.setConstraints(fieldInterestRate, gbc);
		add(fieldInterestRate);
		
		gbc.gridx = 1;
		fieldStartMoney = new JTextField();
		fieldStartMoney.setPreferredSize(new Dimension(75, 25));
		fieldStartMoney.setText("Start Money");
		layout.setConstraints(fieldStartMoney, gbc);
		add(fieldStartMoney);
		
		gbc.gridx = 2;
		fieldPeriods = new JSpinner();
		fieldPeriods.addChangeListener(periodChange());
		fieldPeriods.setPreferredSize(new Dimension(100, 25));
		fieldPeriods.setModel(new SpinnerNumberModel(3, 3, 25, 1));
		layout.setConstraints(fieldPeriods, gbc);
		add(fieldPeriods);
		
		gbc.gridx = 0;
		
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 3;
		gbc.gridy = 1;
		inputTable = new JTable();
		inputTableModell = new InputTableModell();
		inputTableModell.updatePeriodNumber((int) fieldPeriods.getValue());
		inputTable.setModel(inputTableModell);
		inputTable.setTableHeader(null);
		inputTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane scrollPane = new JScrollPane(inputTable);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		layout.setConstraints(scrollPane, gbc);
		add(scrollPane);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.weighty = 0;
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbc.gridy = 2;
		
		comboBoxAlgorithm = new JComboBox<>();
		comboBoxAlgorithm.addItem("Hillclimbing");
		comboBoxAlgorithm.addItem("Monte Carlo");
		comboBoxAlgorithm.addItem("Particle Swarm");
		layout.setConstraints(comboBoxAlgorithm, gbc);
		add(comboBoxAlgorithm);
		
		gbc.gridx = 1;
		buttonRun = new JButton("run");
		buttonRun.addActionListener(runAction());
		layout.setConstraints(buttonRun, gbc);
		add(buttonRun);
		
		gbc.gridy = 3;
		
		gbc.gridx = 0;
		buttonImport = new JButton("Import");
		buttonImport.addActionListener(importAction());
		layout.setConstraints(buttonImport, gbc);
		add(buttonImport);
		
		gbc.gridx = 1;
		buttonSettings = new JButton("Settings");
		buttonSettings.addActionListener(settingsAction());
		layout.setConstraints(buttonSettings, gbc);
		add(buttonSettings);
	}
	
	private ChangeListener periodChange() {
		ChangeListener periodChangeListener = new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if (e.getSource() instanceof JSpinner) {
					JSpinner spinner = (JSpinner) e.getSource();
					inputTableModell.updatePeriodNumber((int) spinner.getValue());
					repaint();
				}
			}
		}; 
		return periodChangeListener;
	}
	
	private ActionListener runAction() {
		ActionListener runActionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Integer.parseInt(fieldStartMoney.getText());
					Double.parseDouble(fieldInterestRate.getText());
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(Mainframe.this, "Error in field start money or interesst rate", "ERROR!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				List<Period> periods = new ArrayList<>();
				Period period = new Period(0, 0, 0, Decision.SHARED, 0);
				period.setPeriodMoney(Integer.valueOf(fieldStartMoney.getText()));
				periods.add(period);
				for (int i = 1; i < inputTableModell.getColumnCount(); i++) {
					period = new Period(Integer.valueOf(inputTableModell.getValueAt(0, i).toString()), Integer.valueOf(inputTableModell.getValueAt(1, i).toString()), Decision.SHARED, 0);
					periods.add(period);
				}
				Search search = null;
				switch (comboBoxAlgorithm.getSelectedItem().toString()) {
				case "Hillclimbing":
					search = new Hillclimbing(periods, Double.valueOf(fieldInterestRate.getText()));
					break;
				case "Monte Carlo":
					search = new MonteDaniel(periods, Float.valueOf(fieldInterestRate.getText()), 100000);
					break;
				case "Particle Swarm":
					search = new ParticleSwarm(periods, Float.valueOf(fieldInterestRate.getText()), 100, 100);
					break;
				default:
					break;
				}
				search.start();
			}
		};
		return runActionListener;
	}
	
	private ActionListener importAction() {
		ActionListener importActionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		return importActionListener;
	}
	
	private ActionListener settingsAction() {
		ActionListener settingsActionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		return settingsActionListener;
	}
	
	public static void main(String[] args) {
		new Mainframe();
	}
}
