package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;

import misc.Period;

/**
 * @author chris
 *
 */
public class AlgorithmFrame extends JFrame{

	private static final long serialVersionUID = -8287674932793764458L;
	
	private List<Period> periods;
	
	private JTable periodTable;
	private PeriodTableModell periodTableModell;
	private JTextPane console;
	private JButton buttonShowConsole;

	public AlgorithmFrame(List<Period> periods) {
		this.periods = periods;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(800, 300));
		init();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	//TODO
	//scrollpane for periods
	//check input fields
	
	private void init() {
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.insets = new Insets(3, 3, 3, 3);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		periodTable = new JTable();
		periodTableModell = new PeriodTableModell(periods);
		periodTable.setModel(periodTableModell);
		periodTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		periodTable.setTableHeader(null);
		JScrollPane scrollPane = new JScrollPane(periodTable);
		layout.setConstraints(scrollPane, gbc);
		add(scrollPane);
		
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		gbc.weighty = 0;
		buttonShowConsole = new JButton("Console");
		buttonShowConsole.addActionListener(actionShowConsole());
		layout.setConstraints(buttonShowConsole, gbc);
		add(buttonShowConsole);
		
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		console = new JTextPane();
		console.setVisible(false);
		layout.setConstraints(console, gbc);
		add(console);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		JPanel panel = new JPanel();
		panel.setBackground(Color.PINK);
		panel.setPreferredSize(new Dimension(300, 500));
		layout.setConstraints(panel, gbc);
		add(panel);
		
		pack();
		setVisible(true);
	}

	private ActionListener actionShowConsole() {
		ActionListener actionShowConsole = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				console.setVisible(!console.isVisible());
			}
		};
		return actionShowConsole;
	}
	
	/**
	 * updates the period view
	 * 
	 * @param periods the new periods
	 */
	public void updatePeriodTable(List<Period> periods) {
		periodTableModell.updatePeriodTable(periods);
	}
	
	/**
	 * print a debug message to the console
	 * 
	 * @param message the message to be printed
	 */
	public void printDebugMessage(String message) {
		console.setText(message);
	}
	
	/**
	 * append a debug message to the console
	 * 
	 * @param message the message to be printed
	 */
	public void appendDebugMessage(String message) {
		console.setText(console.getText()+"\n\n"+message);
	}
}
