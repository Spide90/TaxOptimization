package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

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
		setPreferredSize(new Dimension(640, 480));
		init();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	//TODO set size for textfields
	//TODO set size of table
	
	private void init() {
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.insets = new Insets(3, 3, 3, 3);

		gbc.gridy = 0;
		
		gbc.gridx = 0;
		fieldInterestRate = new JTextField();
		layout.setConstraints(fieldInterestRate, gbc);
		add(fieldInterestRate);
		
		gbc.gridx = 1;
		fieldStartMoney = new JTextField();
		layout.setConstraints(fieldStartMoney, gbc);
		add(fieldStartMoney);
		
		gbc.gridx = 2;
		fieldPeriods = new JSpinner();
		fieldPeriods.setModel(new SpinnerNumberModel(3, 0, 25, 1));
		layout.setConstraints(fieldPeriods, gbc);
		add(fieldPeriods);
		
		gbc.gridx = 0;
		
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 3;
		gbc.gridy = 1;
		inputTable = new JTable();
		inputTableModell = new InputTableModell();
		inputTable.setModel(inputTableModell);
		layout.setConstraints(inputTable, gbc);
		add(inputTable);
		
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
		
		gbc.gridy = 2;
		
		buttonImport = new JButton("Import");
		buttonImport.addActionListener(ImportAction());
		layout.setConstraints(buttonImport, gbc);
		add(buttonImport);
		
		gbc.gridx = 1;
		buttonSettings = new JButton("Settings");
		buttonSettings.addActionListener(settingsAction());
		layout.setConstraints(buttonSettings, gbc);
		add(buttonSettings);
	}
	
	private ActionListener runAction() {
		ActionListener runActionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		return runActionListener;
	}
	
	private ActionListener ImportAction() {
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
	
}
