import java.util.List;

public class Main {
    // Create new population every run for genetic alg, population size can vary
    // Run > 400 tests
    // Check for solution set at beginning of loop for simulated annealing
    public static void main(String[] args) {
        Board[] initPopulation = new Board[250];
        GenAlgorithmSolver genetic = null;

        //for(int iterations = 0; iterations < 500; iterations++) {
            for (int i = 0; i < 250; i++) {
                initPopulation[i] = new Board(); // Default constructor makes a random board of size 21
            }

            // Initial population of size 200 and a mutation probably of 0.1, or 10%
            genetic = new GenAlgorithmSolver(initPopulation, 0.01);
            genetic.solve();
       // }

        List<Board> sols = genetic.getSolutions();
        for(Board b : sols) {
            System.out.println(b.toString());
        }
    }

}
