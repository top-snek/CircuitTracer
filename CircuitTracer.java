import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail
 */
public class CircuitTracer {
	private CircuitBoard board;
	private Storage<TraceState> stateStore;
	private ArrayList<TraceState> bestPaths;
	private int shortestPathLength;
	/** launch the program
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			printUsage();
			System.exit(1);
		}
		try {
			new CircuitTracer(args); //create this with args
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private static void printUsage() {
		//TODO: print out clear usage instructions when there are problems with
		// any command line args
		
		System.out.println("Usage:\n" + " java CircuitTracer -<q|s> -<c|g> <FILENAME>\n"
				+ " -q or -s chooses whether to use a queue or stack respectively\n"
				+ " -c or -g chooses whether to use a cli or gui interface");
	}
	
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	private CircuitTracer(String[] args) {
		//TODO: parse command line args
		//TODO: initialize the Storage to use either a stack or queue
		//TODO: read in the CircuitBoard from the given file
		//TODO: run the search for best paths
		//TODO: output results to console or GUI, according to specified choice
		
		switch (args[0]) {
		case "-q":
			stateStore = new Storage<TraceState>(Storage.DataStructure.queue);
			break;
		case "-s":
			stateStore = new Storage<TraceState>(Storage.DataStructure.stack);
			break;
		default:
			printUsage();
			System.exit(1);
		}

		switch (args[1]) {
		case "-c":
		
			break;
		case "-g":
			System.out.println("GUI is not implemented, defaulting to cli.");
			break;
		default:
			printUsage();
			System.exit(1);
		}

		try {
			board = new CircuitBoard(args[2]);
		} catch (FileNotFoundException e) {
			System.out.println("File was not found: " + args[2]);
			printUsage();
			System.exit(1);
		}
		
		// ABOVE
		if (board.isOpen(board.getStartingPoint().x - 1, board.getStartingPoint().y)) {
			stateStore.store(new TraceState(board, board.getStartingPoint().x - 1, board.getStartingPoint().y));
		}
		// BELOW
		if (board.isOpen(board.getStartingPoint().x + 1, board.getStartingPoint().y)) {
			stateStore.store(new TraceState(board, board.getStartingPoint().x + 1, board.getStartingPoint().y));
		}
		// LEFT
		if (board.isOpen(board.getStartingPoint().x, board.getStartingPoint().y - 1)) {
			stateStore.store(new TraceState(board, board.getStartingPoint().x, board.getStartingPoint().y - 1));
		}
		// RIGHT
		if (board.isOpen(board.getStartingPoint().x, board.getStartingPoint().y + 1)) {
			stateStore.store(new TraceState(board, board.getStartingPoint().x, board.getStartingPoint().y + 1));
		}
		
		shortestPathLength = board.numCols() * board.numRows();
		while (!stateStore.isEmpty()) {
			TraceState currentTrace = stateStore.retrieve();
			if (currentTrace.isComplete()) {
				if (currentTrace.pathLength() < shortestPathLength){
					shortestPathLength = currentTrace.pathLength();
					bestPaths = new ArrayList<TraceState>();
					bestPaths.add(currentTrace);
				} else if (currentTrace.pathLength() == shortestPathLength){
					bestPaths.add(currentTrace);
				} 
			} else {

				// ABOVE
				if (currentTrace.isOpen(currentTrace.getRow() - 1, currentTrace.getCol())) {
					stateStore.store(new TraceState(currentTrace, currentTrace.getRow() - 1, currentTrace.getCol()));
				}
				// BELOW
				if (currentTrace.isOpen(currentTrace.getRow() + 1, currentTrace.getCol())) {
					stateStore.store(new TraceState(currentTrace, currentTrace.getRow() + 1, currentTrace.getCol()));
				}
				// LEFT
				if (currentTrace.isOpen(currentTrace.getRow(), currentTrace.getCol() - 1)) {
					stateStore.store(new TraceState(currentTrace, currentTrace.getRow(), currentTrace.getCol() - 1));
				}
				// RIGHT
				if (currentTrace.isOpen(currentTrace.getRow(), currentTrace.getCol() + 1)) {
					stateStore.store(new TraceState(currentTrace, currentTrace.getRow(), currentTrace.getCol() + 1));
				}
			}
		}

		for (TraceState s : bestPaths){
			System.out.println(s.toString());
		}
	}
	
} // class CircuitTracer
