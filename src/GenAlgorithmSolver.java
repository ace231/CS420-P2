import java.lang.management.MemoryUsage;
import java.util.*;

public class GenAlgorithmSolver {

    private static final int MAX_NUM_GENERATIONS = 10000;
    private static List<Board> solutions;  // If solutions are found, save them here.
    private int currGenerations;
    private PriorityQueue<Board> population;
    private int populationSize;
    private int nodesGenerated;
    private double mutationProb;



    public GenAlgorithmSolver(Board[] initPopulation, double mutationProb) {
        this.population = new PriorityQueue<>(Arrays.asList(initPopulation)); // Take in board array, turn into a list for queue
        this.solutions = new ArrayList<>();
        this.currGenerations = 1;
        this.populationSize = initPopulation.length;
        this.nodesGenerated = 0;
        this.mutationProb = mutationProb;
        printInfo();
    }


    public List<Board> getSolutions() {
        return solutions;
    }

    public void solve() {
        // Loop through the processes of selection, breeding, and mutation until the limit set is reached or
        // a solution is found
        do {
            // Check if the best candidate is the solution, since this is a priority queue, check the top.
            // If it is, add it to the solutions list and break the loop, no need for extra work
            if(population.peek().getFitness() == 0) {
                solutions.add(population.peek());
                System.out.print("Solution found!!");
                break;
            }

            // This will become the new population
            PriorityQueue<Board> newGeneration = new PriorityQueue<>();

            for(int i = 0; i < populationSize / 2; i++) {
                // Pick top 2 parents
                Board parent1 = population.poll();
                Board parent2 = population.poll();

               // System.out.println("Population size: " + population.size() + " i: " + i);//delete

                //Add them back into the population
                newGeneration.add(parent1);
                newGeneration.add(parent2);

                // Breed the two parents to generate 2 children, this method also handles mutations
                Board[] children = breed(parent1, parent2); // consists of 2 children

                newGeneration.add(children[0]); // Add children to population
                newGeneration.add(children[1]);
            }

            this.population = newGeneration; // Set population to new population/generation
            currGenerations++; // Increment generations
        } while(currGenerations < MAX_NUM_GENERATIONS && population.peek().getFitness() != 0);

        System.out.println("Max generations reached");
    }


    // Handles making 2 children from 2 parents and any mutations
    private Board[] breed(Board parent1, Board parent2) {
        Board[] children = new Board[2];
        Random rand = new Random();

        // Calculate the probability of either child being mutated
        double chanceMutateC1 = rand.nextDouble();
        double chanceMutateC2 = rand.nextDouble();

        // We don't want the very first or very last indices as the cross over point
        int crossOverIndex = rand.nextInt(parent1.getSizeOfBoard() - 1);
        if(crossOverIndex == 0) {
            crossOverIndex++;
        }

        // To generate the children, we break each parents "genes", in this case board states, at the crossover point
        // and swap the halves. So child 1 gets half of parent 1's genes and the other half from parent 2. Same for
        // child 2
        int[] p1Half1 = parent1.getPieceOfBoard(0, crossOverIndex);
        int[] p1Half2 = parent1.getPieceOfBoard(crossOverIndex, parent1.getSizeOfBoard());

        int[] p2Half1 = parent2.getPieceOfBoard(0, crossOverIndex);
        int[] p2Half2 = parent2.getPieceOfBoard(crossOverIndex, parent2.getSizeOfBoard());

        int[] boardChild1 = new int[parent1.getSizeOfBoard()]; // Should be p1Half1 + p2Half2
        int[] boardChild2 = new int[parent1.getSizeOfBoard()]; // Should be p2Half1 + p1Half2

        int i = 0, j = 0;
        while(i < p1Half1.length) {
            boardChild1[i] = p1Half1[i];
            boardChild2[i] = p2Half1[i];
            i++;
        }

        while(j < p1Half2.length) {
            boardChild1[i] = p2Half2[j];
            boardChild2[i] = p1Half2[j];
            i++;
            j++;
        }

        children[0] = new Board(boardChild1);
        children[1] = new Board(boardChild2);

        // Here we can mutate the children if need be
        if(chanceMutateC1 < mutationProb) {
            children[0].mutateBoard();
        }
        if(chanceMutateC2 < mutationProb) {
            children[1].mutateBoard();
        }


        return children;
    }


    public void printInfo() {
        System.out.println("Size of population: " + population.size() + "\nCurrent generation: " + currGenerations +
                            "\npopulationSize: " + populationSize);
    }

    public void printPopulation() {
        Board[] temp2 = new Board[population.size()];
        temp2 = population.toArray(temp2);
        for(int i = 0; i < 200; i++) {
            Board temp = population.poll();
            System.out.println(i+ ": " + temp.toString() + " cost " + temp.getFitness());
        }


        System.out.println("As array");

        for(int i = 0; i < temp2.length; i++) {
            System.out.println(i + ": " + temp2[i].toString() + " cost " + temp2[i].getFitness());
        }

    }
}
