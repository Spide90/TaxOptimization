package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.areas.AreaRenderer;
import de.erichseifert.gral.plots.areas.DefaultAreaRenderer2D;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.Insets2D;


/**
 * @author chris
 *
 */
public class AlgorithmFrame extends JFrame{

	private static final long serialVersionUID = -8287674932793764458L;
	
	private List<Period> periods;
	private String algorithmName;
	
	private JTable periodTable;
	private PeriodTableModell periodTableModell;
	private JTextPane console;
	private JButton buttonShowConsole;
	private JPanel plotsPanel;
	
	private final Dimension preferredPlotSize = new Dimension(300,300);

	public AlgorithmFrame(List<Period> periods, String algorithmName) {
		this.periods = periods;
		this.algorithmName = algorithmName;
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
		plotsPanel = new JPanel();
		plotsPanel.setBackground(Color.PINK);
		layout.setConstraints(plotsPanel, gbc);
		add(plotsPanel);
		
		pack();
		setVisible(true);
		setTitle(algorithmName + " - rechnet...");
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
	
	
	public void updatePlots(DataSource... plotsData) {
		plotsPanel.removeAll();
		
		// generate a plot for the outcome per period
		DataTable periodsData = new DataTable(Integer.class, Integer.class);
		for (int i=1; i<periods.size(); ++i)
			periodsData.add(i,periods.get(i).getPeriodMoney());
		plotsPanel.add(createLinePlot(periodsData, "Outcome per period", "Period", "Outcome", Color.blue));
			                
		if (algorithmName.equals("Particle Swarm")) {
			plotsPanel.setPreferredSize(new Dimension(preferredPlotSize.height*3, preferredPlotSize.width));
			plotsPanel.setLayout(new GridLayout(3,1));
			plotsPanel.add(createLinePlot(plotsData[0], "Best outcome per iteration", "Iteration", "Best outcome", Color.green));
		}
		pack();
	}

	
	private JPanel createLinePlot(DataSource data, String title, String xAxisLabel, String yAxisLabel, Color color) {
		XYPlot plot = new XYPlot(data); // plot for the best outcomes per iteration
		plot.getTitle().setText(title);
		// set the labels of the axes
		plot.getAxisRenderer(XYPlot.AXIS_X).setLabel(xAxisLabel);
		plot.getAxisRenderer(XYPlot.AXIS_X).setLabel(yAxisLabel);
		// make a margin for the axes and labels
		plot.setInsets(new Insets2D.Double(20.0, 100.0, 60.0, 40.0));
		LineRenderer lines = new DefaultLineRenderer2D();
        plot.setLineRenderer(data, lines);
        plot.setPointRenderer(data, null);
        plot.getLineRenderer(data).setColor(color);
       // draw the axes outside of the plot
        plot.getAxisRenderer(XYPlot.AXIS_X).setIntersection(-Double.MAX_VALUE);
        plot.getAxisRenderer(XYPlot.AXIS_Y).setIntersection(-Double.MAX_VALUE);
        // set the drawn area if the value never changed (all values are the same). Otherwise it would render an empty range
        if (data.get(1, 0).equals(data.get(1, data.getRowCount()-1))) {
        	int value = Integer.valueOf(data.get(1, 0).toString());
        	if (value>=0)
        		plot.getAxis(XYPlot.AXIS_Y).setRange(0, 2.1*(value+1));
        	else
        		plot.getAxis(XYPlot.AXIS_Y).setRange(2.1*(value+1), 0);
        }
        // draw an area between the graph and the x axis
        AreaRenderer area = new DefaultAreaRenderer2D();
		area.setColor(GraphicsUtils.deriveWithAlpha(color, 64));
		plot.setAreaRenderer(data, area);
		InteractivePanel plotPanel = new InteractivePanel(plot);
		plotPanel.setPreferredSize(preferredPlotSize);
		return plotPanel;
	}
}
