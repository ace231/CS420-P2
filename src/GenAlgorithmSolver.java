import java.util.*;

public class GenAlgorithmSolver {

    private static final int MAX_NUM_GENERATIONS = 7500;
    private static List<Board> solutions;  // If solutions are found, save them here.
    private int currGenerations;
    private PriorityQueue<Board> population;
    private int populationSize;
    private int nodesGenerated;
    private double mutationProb;



    public GenAlgorithmSolver(Board[] initPopulation, double mutationProb) {
        population = new PriorityQueue<>();
        population.addAll(Arrays.asList(initPopulation)); // Take in board array, turn into a list for queue
        solutions = new ArrayList<>();
        currGenerations = 1;
        populationSize = initPopulation.length;
        nodesGenerated = 0;
        this.mutationProb = mutationProb;
    }


    public List<Board> getSolutions() {
        return solutions;
    }

    public void solve() {
        Iterator<Board> b;

        // Loop through the process of selection, breeding, and mutation until the limit set is reached or
        // a solution is found
        do {
            // Check if the best candidate is the solution, since this is a priority queue, check the top.
            // If it is, add it to the solutions list and break the loop, no need for extra work
            if(population.peek().getFitness() == 0) {
                solutions.add(population.peek());
                System.out.print("Solution found!!");
                break;
            }

            // This will become the new population. Since the population size has been set, the new generation will be
            // of the same size and will consist of the children generated from this run through and some randomly
            // selected candidates of the top 25%
            PriorityQueue<Board> newGeneration = new PriorityQueue<>();

            for(int i = 0; i < populationSize; i++) {
                // For my implementation 2 random parents will be selected from the top 25% of candidates rather than
                // consistently drawing the top 2
                Board parent1 = getRandBestParent(40, null);
                Board parent2 = getRandBestParent(40, parent1);
                //Add them back into the population
                newGeneration.add(parent1);
                newGeneration.add(parent2);

                // Breed the two parents to generate 2 children, this method also handles mutations
                Board[] children = breed(parent1, parent2); // consists of 2 children
                newGeneration.add(children[0]); // Add children to population
                newGeneration.add(children[1]);

            }

            this.population = newGeneration; // Set population to new population
            currGenerations++; // Increment generations
        } while(currGenerations < MAX_NUM_GENERATIONS && population.peek().getFitness() != 0);

    }


    // Randomly selects a parent from top given percentage of the population.
    // A board object simulating a parent is passed as a parameter to make sure that the same parent is not
    // picked twice. I did that just to appeal to my paranoia.
    private Board getRandBestParent(int percentile, Board parent1) {
        Random rand = new Random();
        int limit = (int)(populationSize * (percentile / 100.0));  // Find limiting index
        int randIndex = rand.nextInt(limit);  // Pick a random index between 0 and limit
        Iterator<Board> itr = population.iterator();
        Board newParent;

        // Go through randIndex amount of elements to select a parent
        int i = 0;
        do {
            newParent = itr.next();

            if(newParent != parent1) {
                i++;
            }

        } while (i < randIndex);

        return newParent;
    }


    private Board[] breed(Board parent1, Board parent2) {
        Board[] children = new Board[2];
        Random rand = new Random();

        // The mutation probability is a percentage, so we'll multiply it by 100 and generate a number between 0 and 100.
        // If the number is between 0 and the mutation * 100, we'll throw a mutation into that child.
        int chanceMutation = (int)(mutationProb * 100.0);
        int chanceMutateC1 = rand.nextInt(chanceMutation);
        int chanceMutateC2 = rand.nextInt(chanceMutation);

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
        int[] p2Half2 = parent1.getPieceOfBoard(crossOverIndex, parent1.getSizeOfBoard());

        int[] boardChild1 = new int[parent1.getSizeOfBoard()]; // Should be p1Half1 + p2Half2
        int[] boardChild2 = new int[parent1.getSizeOfBoard()]; // Should be p2Half1 + p1Half2

        int i = 0, j = 0;
        while(i < p1Half1.length) {
            boardChild1[i] = p1Half1[i];
            boardChild2[i] = p2Half1[i];
            i++;
        }

        while(j < p1Half2.length) {
            boardChild1[i] = p1Half2[j];
            boardChild2[i] = p2Half2[j];
            i++;
            j++;
        }

        children[0] = new Board(boardChild1);
        children[1] = new Board(boardChild2);

        // Here we can mutate the children if need be
        if(chanceMutateC1 < chanceMutation) {
            children[0].mutateBoard();
        }
        if(chanceMutateC2 < chanceMutation) {
            children[1].mutateBoard();
        }


        return children;
    }


    public void printInfo() {
        System.out.println("Size of population: " + population.size() + "\nCurrent generation: " + currGenerations +
                            "\npopulationSize: " + populationSize);
    }
}
