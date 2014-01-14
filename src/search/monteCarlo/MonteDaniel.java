package search.monteCarlo;

import java.util.List;

import misc.Period;
import misc.TaxFormula;
import gui.AlgorithmFrame;
import search.Search;
import search.particleSwarm.Particle;
import search.particleSwarm.ParticlePosition;

/**
 * This is an implementation of the monte carlo algorithm. It is implemented in the same way as the initialization of the Particle swarm algorithm
 * because this is equivalent to Monte Carlo
 */
public class MonteDaniel extends Search {

	private List<Period> periods;
	private float interesstRate;
	private ParticlePosition bestResult;
	private AlgorithmFrame gui;
	private int numberOfIterations;
	
	
	public MonteDaniel(List<Period> periods, float interesstRate, int numberOfIterations) {
		this.periods =  periods;
		this.interesstRate = interesstRate;
		this.numberOfIterations = numberOfIterations;
		gui = new AlgorithmFrame(periods);
		gui.setTitle("Monte Daniel von Zott");
	}
	
	@Override
	public void run() {
		Particle firstParticle = new Particle(periods, interesstRate); // generate random particle
		bestResult = new ParticlePosition(firstParticle.getBestParticlePosition());
		for (int i=0; i<numberOfIterations; ++i) {
			Particle newParticle = new Particle(periods, interesstRate); // generate random particle
			if (newParticle.getBestParticlePosition().outcome > bestResult.outcome)
				bestResult.copy(newParticle.getBestParticlePosition());
		}
		updateGui();
		gui.setTitle(gui.getTitle()+ " - fertig");
	}
	
	
	private void updateGui() {
		// apply the best values to the periods
		for (int i=1; i<periods.size(); ++i) {
			periods.get(i).setDecision(bestResult.decisions[i]);
			periods.get(i).setLossCarryback(Math.round(bestResult.carrybacks[i]));
		}
		TaxFormula.updatePeriods(periods, interesstRate);
		gui.updatePeriodTable(periods);
	}

}
