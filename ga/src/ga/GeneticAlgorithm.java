package ga;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*; 

import javafx.util.Pair;
import utils.Fitness;
import utils.Link;

public class GeneticAlgorithm {
	private Vector<Link> network;
	private int populationSize;
	private double crossoverProb;
	private double mutationProb;
	
	public GeneticAlgorithm(Vector<Link> _network, int _populationSize, double _crossoverProb, double _mutationProb) {
		network = _network;
		populationSize = _populationSize;
		crossoverProb = _crossoverProb;
		mutationProb = _mutationProb;
	}
	
	public void printChrome(List<Vector<Link>> chrome){
		for(int i=0;i<chrome.size();i++){
			for(int j=0;j<chrome.get(i).size();j++)
				System.out.print(chrome.get(i).elementAt(j).getActive()+" ");
			System.out.println();
		}
	}
	
	public void Operate() throws FileNotFoundException{
		int maxIterations = 5000; //vary the number iterations here.
		int count=0, best_count=0;
		double[] best = new double[maxIterations];
		double[] avg = new double[maxIterations];
		
		//initialize population
		List<Vector<Link>> pop = initPopulation();
		do{
			System.out.println("\n--- Iteration: "+count+" ---");
			//printChrome(pop);
			
			//Calculate fitness of each individual chromosome
			double[] fitness = calcFitness(pop);
			
			System.out.println("Best fitness: "+findBest(fitness));
			best[count] = findBest(fitness);
			
			System.out.println("Avg. fitness: "+findAvg(fitness));
			avg[count] = findAvg(fitness);
			
			pop = Reproduction(pop, fitness);
			
			if(best[count]==1.0)
				best_count++;
			count++;
			
			System.out.println();
			
		}while(count<maxIterations); //best_count<3 && 
		
		//write output to file
		writeOutputFile(best, avg);
		System.out.println("\nFinish!");
	}
	
	public List<Vector<Link>> Reproduction(List<Vector<Link>> all, double[] fitness){

		List<Vector<Link>> new_gen = new ArrayList<Vector<Link>>();
		List<Vector<Link>> selected, crossed;
		List<Vector<Link>> mutated = new ArrayList<Vector<Link>>();
		
		while(new_gen.size()<all.size()){
			//Selection
			selected = tournamentSelection(all); 
			
			//Crossover
			double rand_cross = getRandom(100)/100.0;
			if(rand_cross<=crossoverProb){
				crossed = crossover(selected);
				
				//Mutation
				for(Vector<Link> chrome : crossed){
					double rand_mutate = getRandom(100)/1000.0;
					if(rand_mutate<=mutationProb){
						mutated.add(mutation(chrome));
					}
					else{
						mutated.add(chrome);
					}
				}			
				new_gen.add(mutated.get(0));
				new_gen.add(mutated.get(1));
			}
			else{
				new_gen.add(selected.get(0));
				new_gen.add(selected.get(1));
			}
		}
		return new_gen;
	}
	
	public void writeOutputFile(double[] best, double[] avg) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(new File(String.format("output\\output_%d_%s_%s.csv",populationSize, String.valueOf(crossoverProb), String.valueOf(mutationProb))));
        StringBuilder sb = new StringBuilder();
        sb.append("Iteration");
        sb.append(',');
        sb.append("Best");
        sb.append(',');
        sb.append("Average");
        sb.append('\n');

        for(int i=0;i<best.length;i++){
        	sb.append(i);
            sb.append(',');
            sb.append(best[i]);
            sb.append(',');
            sb.append(avg[i]);
            sb.append('\n');
        }

        pw.write(sb.toString());
        pw.close();
	}
	
	public double findBest(double[] fitness){
		Arrays.sort(fitness);
		return fitness[fitness.length-1];
	}
	
	public double findAvg(double[] fitness){
		double sum = 0.0;
		for(int i=0;i<fitness.length;i++)
			sum+=fitness[i];
		return sum/fitness.length;
	}
	
	public List<Vector<Link>> initPopulation(){
		System.out.println("initial population");
		int count=0;
		List<Vector<Link>> all = new ArrayList<Vector<Link>>();
		
		while(count<populationSize){
			//deep copy to clone a new network
			Vector<Link> clone = cloneNetwork(network);
			
			for(int i=0;i<clone.size();i++){
				((Link) clone.elementAt(i)).setActive(getRandom(50)%2);
			}
			all.add(clone);
			count++;
		}
		return all;
	}
	
	public Vector<Link> cloneNetwork(Vector<Link> otherVector){
        Vector<Link> copyNetwork = new Vector<Link>();
        Iterator <Link> iter = otherVector.iterator();
        while(iter.hasNext()){
            Link copy = new Link(iter.next());
            copyNetwork.add(copy);
        }
        return copyNetwork;
    }
	
	public double[] calcFitness(List<Vector<Link>> pop){
		double[] fit = new double[pop.size()];
		Fitness f;
		for(int i=0;i<pop.size();i++){
			f = new Fitness();
			double fitness = f.calculateFitnessOfOverlayNetwork(pop.get(i));
			fit[i] = fitness;
		}
		return fit;
	}
	
	public int getRandom(int upperBound){
		Random rand = new Random();
		return rand.nextInt(upperBound);
	}
	
	public List<Vector<Link>> tournamentSelection(List<Vector<Link>> all){
		List<Vector<Link>> selected = new ArrayList<Vector<Link>>();
		int K=2;
		Fitness f = new Fitness();
		
		//choose K=2 individuals from population at random
		for(int i=0;i<K;i++){
			int rand_chrome1 = getRandom(populationSize);
			int rand_chrome2 = getRandom(populationSize);
			
			//choose the best individual from the tournament
			int diff = Double.compare(f.calculateFitnessOfOverlayNetwork(all.get(rand_chrome1)),f.calculateFitnessOfOverlayNetwork(all.get(rand_chrome2)));
			if (diff<0)
				selected.add(all.get(rand_chrome2));
				
			else if(diff>0)
				selected.add(all.get(rand_chrome1));
			
			else
				selected.add(all.get(rand_chrome1));
		}
		return selected;
	}
	
	public List<Vector<Link>> crossover(List<Vector<Link>> selected){
		//Implement two-point crossover
		int start, end;
		List<Vector<Link>> crossed = new ArrayList<Vector<Link>>();
			
		//two crossover points're picked randomly from the parent chromosomes
		int point1 = getRandom(network.size());
		int point2 = getRandom(network.size());
		
		if (point1<point2){
			start = point1;
			end = point2;
		}
		else{
			start = point2;
			end = point1;
		}
		
		//The bits in between the 2 points are swapped between the parent organisms
		Vector<Link> parent1 = cloneNetwork(selected.get(0)); //original parent1
		Vector<Link> parent2 = cloneNetwork(selected.get(1)); //original parent2
		
		//Child1
		for(int i=start;i<=end;i++){
			int bit = ((Link)selected.get(1).elementAt(i)).getActive();
			((Link)parent1.elementAt(i)).setActive(bit); //change values at crossover points
		}
		crossed.add(parent1);

		//Child2
		for(int i=start;i<=end;i++){
			int bit = ((Link)selected.get(0).elementAt(i)).getActive();
			((Link)parent2.elementAt(i)).setActive(bit); //change values at crossover points
		}
		
		crossed.add(parent2);
		return crossed;
	}
	
	public Vector<Link> mutation(Vector<Link> chrome){
		//flips a randomly selected gene in a chromosome
		Vector<Link> mutated = cloneNetwork(chrome);
		int rand_gene = getRandom(chrome.size());
		int bit = ((Link)chrome.elementAt(rand_gene)).getActive(); //get old bit value
		((Link)mutated.elementAt(rand_gene)).setActive(Math.abs(bit-1)); //flip it
		return mutated;
	}
}
