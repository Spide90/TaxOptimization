package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import de.erichseifert.gral.data.DataSource;
import misc.Period;

public class AlgorithmFrameParticleSwarm extends AlgorithmFrame {
	private static final long serialVersionUID = 5228056101519377917L;

	public AlgorithmFrameParticleSwarm(List<Period> periods) {
		super(periods, "Particle Swarm");
	}
	
	public void updatePlots(DataSource... plotsData) {
		plotsPanel.setLayout(new GridLayout(2,1));
		plotsPanel.setPreferredSize(new Dimension(preferredPlotSize.width, preferredPlotSize.height*2));
		plotsPanel.setMinimumSize(new Dimension(preferredPlotSize.width, preferredPlotSize.height*2));
		super.updatePlots();	                
		plotsPanel.add(createLinePlot(plotsData[0], "Best outcome per iteration", "Iteration", "Best outcome", Color.green));
		pack();
	}
}
