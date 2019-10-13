package utils;

import java.util.Vector;

public class Fitness {
	
	private double fitness;
	private double cost;
	private double costCompleteNetwork;
	
	public double calculateFitnessOfOverlayNetwork(Vector<Link> network) {
			fitness = 0.0;
			cost = 0.0;
			costCompleteNetwork = 0.0;
			for (int i=0; i< network.size(); i++) {
				costCompleteNetwork += ((Link)network.elementAt(i)).getCost();
				if (((Link)network.elementAt(i)).getActive() == 1) {
					cost += ((Link)network.elementAt(i)).getCost();
				}
			}
			fitness = 1 - (cost/costCompleteNetwork); 
			return fitness; 
	}
}
