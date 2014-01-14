package search.particleSwarm;


/**
 * A class representing the position of a particle
 */
public class ParticlePosition {
	public float[] decisions; // will get rounded to 0 or 1 and mapped to a Decision
	public float[] carrybacks;
	public int outcome; // the resulting outcome for those decisions and carrybacks
	
	
	public ParticlePosition(float[] decisions, float[] carrybacks, int outcome) {
		this.decisions = decisions;
		this.carrybacks = carrybacks;
		this.outcome = outcome;
	}
	
	/**
	 * copy constructor
	 * @param toCopy the particle position to copy
	 */
	public ParticlePosition(ParticlePosition toCopy) {
		copy(toCopy);
	}
	
	
	/**
	 * Copies a particle position into this object
	 * @param toCopy The position to copy
	 */
	public void copy(ParticlePosition toCopy) {
		if (decisions==null || toCopy.decisions.length != decisions.length) {
			decisions = new float[toCopy.decisions.length];
			carrybacks = new float[toCopy.decisions.length];
		}
		for (int i=0; i<toCopy.decisions.length; ++i) {
			decisions[i] = toCopy.decisions[i];
			carrybacks[i] = toCopy.carrybacks[i];
		}
		outcome = toCopy.outcome;
	}
	
	
}
