package search.particleSwarm;

import gui.AlgorithmFrame;

import java.util.ArrayList;
import java.util.List;

import misc.Decision;
import misc.Period;
import misc.TaxFormula;
import search.Search;

import java.util.Random;

public class ParticleSwarm extends Search{
	static final float weightBestParticlePosition = 0.2f; // phi_p in the wikipedia algorithm
	static final float weightGlobalBestPosition = 0.4f; // phi_g in the wikipedia algorithm
	static final float weightCurrentVelocity = 0.8f; // omega in the wikipedia algorithm
	
	
	static final Random random = new Random();
	
	
	private List<Period> periods;
	private float interesstRate;
	private int numberOfParticles;
	private int numberOfIterations;
	private AlgorithmFrame gui;
	
	private List<Particle> particles;
	private ParticlePosition globalBestPosition;
	
	public ParticleSwarm(List<Period> periods, float interesstRate, int numberOfParticles, int numberOfIterations) {
		this.periods =  periods;
		this.interesstRate = interesstRate;
		this.numberOfParticles = numberOfParticles;
		this.numberOfIterations = numberOfIterations;
		gui = new AlgorithmFrame(periods);
		gui.setTitle("Particle Swarm - rechnet...");
	}
	
	@Override
	public void run() {
		initialization();
		//gui.printDebugMessage("Best outcome after initialization: "+globalBestPosition.outcome);
		for (int i=0; i<numberOfIterations; ++i) {
			iterationStep();
			//System.out.println("Best outcome after iteration "+(i+1)+": "+globalBestPosition.outcome);
			//gui.appendDebugMessage("Best outcome after iteration "+(i+1)+": "+globalBestPosition.outcome);
			// 699595 seems to be the best possible outcome (tested with 10000 particles and 10000 iterations)
		}
		updateGui();
		gui.setTitle("Particle Swarm - fertig");
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
			if (newParticle.getBestParticlePosition().outcome > globalBestPosition.outcome)
				globalBestPosition.copy(newParticle.getBestParticlePosition());
		}
		
	}
	
	
	/**
	 * perform one single iteration step
	 */
	private void iterationStep() {
		for (Particle particle : particles) {
			if (particle.updateVelocitiesAndPositions(globalBestPosition)) // if true, best particle position has changed
				if (particle.getBestParticlePosition().outcome > globalBestPosition.outcome)
					globalBestPosition.copy(particle.getBestParticlePosition());
		}
	}

	
	private void updateGui() {
		// apply the best values to the periods
		for (int i=1; i<periods.size(); ++i) {
			periods.get(i).setDecision(globalBestPosition.decisions[i]);
			periods.get(i).setLossCarryback(Math.round(globalBestPosition.carrybacks[i]));
		}
		TaxFormula.updatePeriods(periods, interesstRate);
		gui.updatePeriodTable(periods);
	}
	
	
	// just for testing of the table. Apply this to 6 periods as defined in TaxFormula.main(). So insert following values into the GUI:
	// Start money: 600000
	// Interest rate: 0.08
	// Periode                  1       2       3       4       5       6
	// Zahlungsueberschuesse    0 -150000   50000       0 -150000   50000
	// The correct result for PeriodMoney of period 6 should be 656114 (+/-1).
	private void formulaTest() {
		// set the variables (those are not computed in the excel sheet but set by the user)
		periods.get(1).setDecision(Decision.SHARED);
		periods.get(2).setDecision(Decision.DIVIDED);
		periods.get(3).setDecision(Decision.SHARED);
		periods.get(4).setDecision(Decision.SHARED);
		periods.get(5).setDecision(Decision.SHARED);
		periods.get(6).setDecision(Decision.DIVIDED);
		periods.get(1).setLossCarryback(0);
		periods.get(2).setLossCarryback(-18550);
		periods.get(3).setLossCarryback(0);
		periods.get(4).setLossCarryback(0);
		periods.get(5).setLossCarryback(-21456);
		periods.get(6).setLossCarryback(0);
		TaxFormula.updatePeriods(periods, interesstRate);
	}
}
