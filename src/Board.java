import java.util.Arrays;
import java.util.Random;

// Rather than create an extra class, Node, I got lazy and figured states could be well represented in this
// one class
public class Board implements Comparable<Board> {

    private int[] board;  // The board filled with queens. A queens column is its index in the array, and its rows is the
                          // value at that index

    private int cost;  // The value of the evaluation function on this node
    private int sizeOfBoard;
    
    public Board() {
        int defaultSize = 21;   // Default to 21
        this.board = genBoard(defaultSize);
        this.cost = calcCost();
        this.sizeOfBoard = defaultSize;
    }


    public Board(int size) {
        this.board = genBoard(size);
        this.cost = calcCost();
        this.sizeOfBoard = size;
    }


    public Board(int[] arr) {
        this.board = arr;
        this.cost = calcCost();
        this.sizeOfBoard = arr.length;
    }


    // Setter methods for Board class
    public void setBoard(int[] board) {
        this.board = board;
    }


    public void sethCost(int hCost) {
        this.cost = hCost;
    }


    // Getter methods for Board class
    public int[] getBoard() {
        return board;
    }


    public int getFitness() {
        return cost;
    }


    public int getSizeOfBoard() {
        return sizeOfBoard;
    }

    // Generates a random valid board for the Board class
    public int[] genBoard(int size) {
        Random rand = new Random();
        int[] board = new int[size];

        for(int i = 0; i < size; i++) {
            board[i] = rand.nextInt(size);
        }

        return board;
    }  // End of genBoard


    // Counts number of attacking queens, this is the fitness function in this case
    public int calcCost() {
        int attackQueens = 0;

        for (int i = 0; i < board.length; i++){
            for (int j = i + 1; j < board.length; j++) {

                // If 2 queens are on the same rows, increment attackQueens
                // If they attack diagonally, increment attackQueens
                if(board[i] == board[j] || Math.abs(i - j) == Math.abs(board[i] - board[j])) {
                    attackQueens++;
                }

            }
        }

        return attackQueens;
    }


    // Returns an int array of a piece of the board between the given indices
    public int[] getPieceOfBoard(int i, int j) {
        return Arrays.copyOfRange(this.board, i, j);
    }


    // This method allows for a board to be mutated by moving up to 3 queens.
    // This number was a design choice as it would not make sense in general for a mutation to change a child's
    // appearance or characteristics so much as to render them drastically different than their parents
    public void mutateBoard() {
        Random rand = new Random();
        int numQueensToMove = rand.nextInt(3);

        if(numQueensToMove != 0) {

            for(int i = 0; i < numQueensToMove; i++) {
                int randQueen = rand.nextInt(this.sizeOfBoard); // Pick a random queen
                this.board[randQueen] = rand.nextInt(this.sizeOfBoard); // Move to a random place on the board
            }

        }

    }

    // The fitness function in this case is the number of attacking queens given a certain board configuration.
    // The lower the value from that fitness function, the better. So although it seems confusing and counter intuitive,
    // a higher value is less desirable. So higher values should be "lesser" and lower values "greater"
    @Override
    public int compareTo(Board b) {
        if(this.cost == b.cost) {
            return 0;
        } else if (this.cost < b.cost) {
            return -1;
        } else {
            return 1;
        }
    }

    
    @Override
    public String toString() {
        return Arrays.toString(board);
    }

} // End of Board class
