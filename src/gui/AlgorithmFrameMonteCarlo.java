package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import misc.Period;
import de.erichseifert.gral.data.DataSource;

public class AlgorithmFrameMonteCarlo extends AlgorithmFrame {
	private static final long serialVersionUID = 9016982909514310379L;

	public AlgorithmFrameMonteCarlo(List<Period> periods) {
		super(periods, "Monte Carlo");
	}
	
	public void updatePlots(DataSource... plotsData) {
		plotsPanel.setLayout(new GridLayout(2,1));
		plotsPanel.setPreferredSize(new Dimension(preferredPlotSize.width, preferredPlotSize.height*2));
		plotsPanel.setMinimumSize(new Dimension(preferredPlotSize.width, preferredPlotSize.height*2));
		super.updatePlots();	                
		plotsPanel.add(createBarPlot(plotsData[0], "Outcome distribution", "Outcome", "Occurence", Color.green));
		pack();
	}
}
