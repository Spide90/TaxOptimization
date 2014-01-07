package search.hillclimbing;

import gui.AlgorithmFrame;

import java.util.List;

import search.Search;
import misc.Period;

public class Hillclimbing extends Search {
	
	private AlgorithmFrame frame;
	private List<Period> periods;
	private double interesstRate;

	public Hillclimbing(List<Period> periods, double interesstRate) {
		this.periods = periods;
		this.interesstRate = interesstRate;
		frame = new AlgorithmFrame(periods);
		frame.setTitle("Hillclimbing");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}


}
