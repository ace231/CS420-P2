import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    // Create new population every run for genetic alg, population size can vary
    // Run > 400 tests
    // Check for solution set at beginning of loop for simulated annealing
    public static void main(String[] args) {
        // Generate initial population of size 100
        Board[] initPopulation = new Board[1000];
        for(int i = 0; i < 1000; i++) {
            initPopulation[i] = new Board(); // Default constructor makes a random board of size 21
        }

        System.out.println(initPopulation[999]);

        GenAlgorithmSolver genetic = new GenAlgorithmSolver(initPopulation, 0.1);
        genetic.solve();
        ArrayList<Board> sols = (ArrayList) genetic.getSolutions();
        for(Board b: sols) {
            System.out.println(b.toString());
        }
    }

}
