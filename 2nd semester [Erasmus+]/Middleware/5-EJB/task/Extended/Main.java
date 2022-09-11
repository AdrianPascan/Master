import java.util.Random;
import java.util.Properties;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import mwy.Searcher;
import mwy.SearcherImpl;

public class Main {
	// How many nodes and how many edges to create.
	private static final int GRAPH_NODES = 1000;
	private static final int GRAPH_EDGES = 2000;

	// How many searches to perform
	private static final int SEARCHES = 50;
	
	private static Random random = new Random();
	private static Searcher searcher;
	
	private static int[] nodeIds;
	
	/**
	 * Creates nodes of a graph.
	 * @param nodeCount number of nodes to be created
	 */
	public static void createNodes(int nodeCount) {
		nodeIds = new int[nodeCount];
		
		for (int i = 0; i < nodeCount; ++i) {
			int id = searcher.addNode();
			nodeIds[i] = id;
		}
	}
	
	/**
	 * Creates a randomly connected graph.
	 * @param edgeCount number of edges to be added
	 */
	public static void connectSomeNodes(int edgeCount) {
		for (int i = 0; i < edgeCount; ++i) {
			int nodeFromId = nodeIds[random.nextInt(nodeIds.length)];
			int nodeToId = nodeIds[random.nextInt(nodeIds.length)];

			searcher.connectNodes(nodeFromId, nodeToId);
		}
	}
	
	/**
	 * Runs a quick measurement on the graph.
	 * @param attemptCount number of searches
	 */
	public static void searchBenchmark(int attemptCount) {
		// Display measurement header.
		System.out.printf("%7s %8s %13s%n", "Attempt", "Distance", "Time");
		for (int i = 0; i < attemptCount; ++i) {
			// Select two random nodes.
			int nodeFromId = nodeIds[random.nextInt(nodeIds.length)];
			int nodeToId = nodeIds[random.nextInt(nodeIds.length)];

			// Calculate distance, timing the operation.
			long time = System.nanoTime();
			int distance = searcher.getDistance(nodeFromId, nodeToId);
			time = System.nanoTime() - time;
			
			// Print the measurement result.
			System.out.printf("%7d %8d %13d%n", i, distance, time / 1000);
		}        
	}

	private static void run() throws IOException {
		LineNumberReader in = new LineNumberReader(new InputStreamReader(System.in));

		loop:
		while (true) {
			System.out.println("\nAvailable commands (type and press enter):");
			System.out.println(" s - search benchmark");
			System.out.println(" q - quit");
			// read first character
			int c = in.read();
			// throw away rest of the buffered line
			while (in.ready())
				in.read();
			switch (c) {
				case 'q': // Quit the application
					break loop;
				case 's': // Search benchmark
					searchBenchmark(SEARCHES);
					break;
				case '\n':
				default:
					break;
			}
		}
	}

	public static void main(String[] args) throws NamingException, IOException {
		if (args.length != 1) {
			System.err.println("Usage: ./main <clientId>");
			return;
		}

		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.RemoteInitialContextFactory");
		props.put(Context.PROVIDER_URL, "ejbd://127.0.0.1:4201");
		Context ctx = new InitialContext(props);

		// Create a randomly connected graph and do a quick measurement.
		// Consider replacing connectSomeNodes with connectAllNodes to
		// verify that all distances are equal to one.
		searcher = (Searcher) ctx.lookup("SearcherImplRemote");
		searcher.setClient(args[0]);

		createNodes(GRAPH_NODES);
		connectSomeNodes(GRAPH_EDGES);

		// Run console menu
		run();
	}
}
