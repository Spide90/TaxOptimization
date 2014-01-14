package search.particleSwarm;

/**
 * A class representing the velocity of a particle
 */
public class ParticleVelocity {
	public float[] decisionsVelocity;
	public float[] carrybacksVelocity;
	
	public ParticleVelocity(float[] decisionsVelocity, float[] carrybacksVelocity) {
		this.decisionsVelocity = decisionsVelocity;
		this.carrybacksVelocity = carrybacksVelocity;
	}
}
