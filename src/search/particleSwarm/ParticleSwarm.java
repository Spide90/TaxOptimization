package search.particleSwarm;

import gui.AlgorithmFrame;
import gui.AlgorithmFrame.PlotType;

import java.util.ArrayList;
import java.util.List;

import misc.Period;
import misc.TaxFormula;
import search.Search;

import java.util.Random;

import de.erichseifert.gral.data.DataTable;

public class ParticleSwarm extends Search{
	static final float weightBestParticlePosition = 0.3f; // phi_p in the wikipedia algorithm
	static final float weightGlobalBestPosition = 0.5f; // phi_g in the wikipedia algorithm
	static final float weightCurrentVelocity = 0.85f; // omega in the wikipedia algorithm
	
	static final Random random = new Random();
	
	private List<Period> periods;
	private float interesstRate;
	private int numberOfParticles;
	private int numberOfIterations;
	private AlgorithmFrame gui;
	
	private List<Particle> particles;
	private ParticlePosition globalBestPosition;
	
	// for the plots
	private boolean drawPlots;
	private DataTable plotBestOutcomesPerIteration;
	
	@SuppressWarnings("unchecked")
	public ParticleSwarm(List<Period> periods, float interesstRate, int numberOfParticles, int numberOfIterations, boolean drawPlots) {
		this.periods =  periods;
		this.interesstRate = interesstRate;
		this.numberOfParticles = numberOfParticles;
		this.numberOfIterations = numberOfIterations;
		this.drawPlots = drawPlots;
		if (drawPlots) {
			plotBestOutcomesPerIteration = new DataTable(Integer.class, Integer.class);
		}
		gui = new AlgorithmFrame(periods, "Particle Swarm");
	}
	
	@Override
	public void run() {
		initialization();
		if (drawPlots)
			plotBestOutcomesPerIteration.add(0, globalBestPosition.getOutcome());
		//System.out.println("Best outcome after initialization: "+globalBestPosition.getOutcome());
		gui.printDebugMessage("Best outcome after initialization: "+globalBestPosition.getOutcome());
		for (int i=0; i<numberOfIterations; ++i) {
			iterationStep();
			//System.out.println("Best outcome after iteration "+(i+1)+": "+globalBestPosition.getOutcome());
			gui.appendDebugMessage("Best outcome after iteration "+(i+1)+": "+globalBestPosition.getOutcome());
			if (drawPlots)
				plotBestOutcomesPerIteration.add(i+1, globalBestPosition.getOutcome());
		}
		updateGui();
		if (drawPlots) {
			gui.setTitle("Particle Swarm - drawing plots...");
			gui.updatePlots(plotBestOutcomesPerIteration, PlotType.LinePlot,  "Best outcome per iteration", "Iteration", "Best outcome");
		}
		gui.setTitle("Particle Swarm - done.");
	}
	
	
	/**
	 * initialize the particles
	 */
	private void initialization() {
		particles = new ArrayList<Particle>();
		Particle firstParticle = new Particle(periods, interesstRate); // generate random particle
		particles.add(firstParticle);
		globalBestPosition = new ParticlePosition(firstParticle.getBestParticlePosition());
		for (int i=1; i<numberOfParticles; ++i) {
			Particle newParticle = new Particle(periods, interesstRate); // generate random particle
			particles.add(newParticle);
			if (newParticle.getBestParticlePosition().getOutcome() > globalBestPosition.getOutcome())
				globalBestPosition.copy(newParticle.getBestParticlePosition());
		}
		
	}
	
	
	/**
	 * perform one single iteration step
	 */
	private void iterationStep() {
		for (Particle particle : particles) {
			if (particle.updateVelocitiesAndPositions(globalBestPosition)) // if true, best particle position has changed
				if (particle.getBestParticlePosition().getOutcome() > globalBestPosition.getOutcome())
					globalBestPosition.copy(particle.getBestParticlePosition());
		}
	}

	
	private void updateGui() {
		// apply the best values to the periods
		for (int i=1; i<periods.size(); ++i) {
			periods.get(i).setDecision(globalBestPosition.getDecision(i));
			periods.get(i).setLossCarryback(Math.round(globalBestPosition.getCarryback(i)));
		}
		TaxFormula.updatePeriods(periods, interesstRate);
		gui.updatePeriodTable(periods);
	}
}
