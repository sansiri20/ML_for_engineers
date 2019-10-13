package main;


import ga.GeneticAlgorithm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import utils.FileHelper;
import utils.Link;

public class RunGA {

	public static void main(String args[]) throws FileNotFoundException {
		Vector<Link> network = new Vector<Link>();
		
		// read file
		FileHelper filehelper = new FileHelper();
		String fileName = "network.txt";
		try {
			network = filehelper.readFile(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Set of initial parameters. Add more values in these sets.
		int[] populationSize = {50,100,250,500};
		double[] crossoverProb = {0.30, 0.50, 0.70};
		double[] mutationProb = {0.01, 0.001};
		for(int pop : populationSize)
			for(double cross: crossoverProb)
				for(double mutate: mutationProb){
					GeneticAlgorithm ga = new GeneticAlgorithm(network, pop, cross, mutate); 
					ga.Operate();
				}
		
		/*
		 * [READ THIS]
		 * I've run 2 sets of experiments-> 1) 1000-iteration, 2) 5000-iteration. Check out the directory iteration_1000 and iteration_5000
		 * For each set, I tried all combinations of parameters (set above)
		 * The file convention: output_populationSize_crossoverProb_mutationProb. 
		 * For example, output_50_0.3_0.001 --> output for experiment of population=50, crossover prob.=0.3, mutation prob.=0.001
		 * It looks like, more iterations are needed to get best/avg. fitness close to 1.0
		 * The trend shows that fitness generally converges really slow. But it does converge.
		 * 
		 * If you want to try more values, add new values to the set above.
		 */
	}
}
