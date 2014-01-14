package search.particleSwarm;

import java.util.List;

import misc.Period;
import misc.TaxFormula;

/**
 * This class represents a particle for the particle swarm algorithm.
 */
public class Particle {
	private ParticlePosition currentPosition;
	private ParticlePosition bestPosition;
	private ParticleVelocity particleVelocity;
	
	private List<Period> periods;
	private float interestRate;
	
	/**
	 * Generates a new Particle with random start position and random velocities
	 * @param periods Periods of Particle swarm. Warning: the periods get modified, which although is not a problem.
	 */
	public Particle(List<Period> periods, float interestRate) {
		this.periods = periods;
		this.interestRate = interestRate;
		// periods[0] does only contain the input money and does not need to be optimized, so values at the zero position are not used.
		// however we include them into the arrays to have less of -1 / +1 computations with the period indices
		float randomDecisions[] = new float[periods.size()];
		float randomCarrybacks[] = new float[periods.size()];
		float randomDecisionVelocities[] = new float[periods.size()];
		float randomCarrybackVelocities[] = new float[periods.size()];
		//set the decisions first, because the carrybacks are dependent on max loss carryback which depend on the decisions
		for (int i=1; i<periods.size(); ++i) {
			randomDecisions[i] = ParticleSwarm.random.nextFloat(); // between 0 and 1
			periods.get(i).setDecision(randomDecisions[i]);
			randomDecisionVelocities[i] =  ParticleSwarm.random.nextFloat()*2.0f-1.0f; // between -1 and 1
		}
		// set the carrybacks
		for (int i=1; i<periods.size(); ++i) {
			int maxLossCarryback = TaxFormula.calculateMaximumLosscarryback(periods.get(i-1), periods.get(i));
			randomCarrybacks[i] = ParticleSwarm.random.nextFloat() * maxLossCarryback; // between 0 and max loss carryback
			periods.get(i).setLossCarryback(Math.round(randomCarrybacks[i]));
			randomCarrybackVelocities[i] = ParticleSwarm.random.nextFloat() * 2.0f * maxLossCarryback - maxLossCarryback; // between -maxlosscaryyback and +maxlosscarryback
		}
		// compute the outcome with those setting
		TaxFormula.updatePeriods(periods, interestRate);
		int outcome = periods.get(periods.size()-1).getPeriodMoney();
		
		// set the attributes of the particle
		currentPosition = new ParticlePosition(randomDecisions, randomCarrybacks, outcome);
		bestPosition = new ParticlePosition(currentPosition);
		particleVelocity = new ParticleVelocity(randomDecisionVelocities, randomCarrybackVelocities);
	}
	
	
	/**
	 * Update the velocities and position of this particle. This constitutes one iteration step.
	 * @param globalBestPosition the global best position of the particle swarm
	 * @return true if the best position of the particle has changed.
	 */
	public boolean updateVelocitiesAndPositions(ParticlePosition globalBestPosition) {
		updateVelocities(globalBestPosition);
		return updatePosition();
	}
	
	
	/**
	 * Returns the best position of this particle
	 */
	public ParticlePosition getBestParticlePosition() {
		return bestPosition;
	}
	
	
	/**
	 * Applies the velocities to the current position and updates the best position
	 * @return true if the best position has changed
	 */
	private boolean updatePosition() {
		// update the decisions first because max carryback depends on the decisions
		for (int i=1; i<periods.size(); ++i) {
			currentPosition.decisions[i]+=particleVelocity.decisionsVelocity[i];
			if (currentPosition.decisions[i] > 1.0f)
				currentPosition.decisions[i] = 1.0f;
			else if (currentPosition.decisions[i] < 0.0f)
				currentPosition.decisions[i] = 0.0f;
			periods.get(i).setDecision(currentPosition.decisions[i]);
		}
		// update the carrybacks
		for (int i=1; i<periods.size(); ++i) {
			currentPosition.carrybacks[i]+=particleVelocity.carrybacksVelocity[i];
			if (currentPosition.carrybacks[i] > 0)
				currentPosition.carrybacks[i] = 0;
			else {
				int maxLossCarryback = TaxFormula.calculateMaximumLosscarryback(periods.get(i-1), periods.get(i));
				if (currentPosition.carrybacks[i] < maxLossCarryback) // maxLossCarryback is <= 0
					currentPosition.carrybacks[i] = maxLossCarryback;
			}
			periods.get(i).setLossCarryback(Math.round(currentPosition.carrybacks[i]));
		}
		// recompute outcome
		TaxFormula.updatePeriods(periods, interestRate);
		currentPosition.outcome = periods.get(periods.size()-1).getPeriodMoney();
		if (currentPosition.outcome > bestPosition.outcome) {
			bestPosition.copy(currentPosition);
			return true;
		} else
			return false;
	}
	
	
	/**
	 * Update the velocities of this particle. No bounds need to be enforced.
	 * @param globalBestPosition The global best position of the particle swarm
	 */
	private void updateVelocities(ParticlePosition globalBestPosition) {
		float randomWeightBestPosition = ParticleSwarm.random.nextFloat();
		float randomWeightGlobalBestPosition = ParticleSwarm.random.nextFloat();
		for (int i=1; i<periods.size(); ++i) {
			particleVelocity.decisionsVelocity[i] = ParticleSwarm.weightCurrentVelocity * particleVelocity.decisionsVelocity[i]
					+ ParticleSwarm.weightBestParticlePosition * randomWeightBestPosition * (bestPosition.decisions[i]-currentPosition.decisions[i])
					+ ParticleSwarm.weightGlobalBestPosition * randomWeightGlobalBestPosition * (globalBestPosition.decisions[i]-currentPosition.decisions[i]);
			particleVelocity.carrybacksVelocity[i] = ParticleSwarm.weightCurrentVelocity * particleVelocity.carrybacksVelocity[i]
					+ ParticleSwarm.weightBestParticlePosition * randomWeightBestPosition * (bestPosition.carrybacks[i]-currentPosition.carrybacks[i])
					+ ParticleSwarm.weightGlobalBestPosition * randomWeightGlobalBestPosition * (globalBestPosition.carrybacks[i]-currentPosition.carrybacks[i]);
			// we don't need to enforce any bounds for the velocities
		}
	}
}
