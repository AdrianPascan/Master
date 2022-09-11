import java.util.Random;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {
	// How many nodes and how many edges to create.
	private static final int GRAPH_NODES = 100;
	private static final int GRAPH_EDGES = 2000;

	// How many searches to perform
	private static final int SEARCHES = 50;
	
	// 3 neighbor distances for computing distance transitives
	private static final int[] N_DISTANCES = new int[]{4, 99, 1999};

	// Server host
	private static final String HOST = "localhost";
	// private static final String HOST = "u-pl0.ms.mff.cuni.cz:1099";

	private static Node[] nodes;
	private static Node[] remoteNodes;

	private static Random random = new Random(5999999);
	private static Searcher searcher = new SearcherImpl();
	private static Searcher remoteSearcher;
	private static NodeFactory remoteNodeFactory;
	
	/**
	 * Creates nodes of a graph.
	 * 
	 * @param howMany number of nodes
	 */
	public static void createNodes(int howMany) throws RemoteException, NotBoundException, MalformedURLException {
		nodes = new Node[howMany];
		remoteNodes = new Node[howMany];

		for (int i = 0; i < howMany; i++) {
			nodes[i] = new NodeImpl();

			remoteNodeFactory.createNode();
			remoteNodes[i] = (Node) Naming.lookup("//" + HOST + "/Node" + String.valueOf(i));
		}
	}

	/**
	 * Creates a fully connected graph.
	 */
	public static void connectAllNodes() throws RemoteException {
		for (int idxFrom = 0; idxFrom < nodes.length; idxFrom++) {
			for (int idxTo = idxFrom + 1; idxTo < nodes.length; idxTo++) {
				nodes[idxFrom].addNeighbor(nodes[idxTo]);
				nodes[idxTo].addNeighbor(nodes[idxFrom]);
				remoteNodes[idxFrom].addNeighbor(remoteNodes[idxTo]);
				remoteNodes[idxTo].addNeighbor(remoteNodes[idxFrom]);
			}
		}
	}

	/**
	 * Creates a randomly connected graph.
	 * 
	 * @param howMany number of edges
	 */
	public static void connectSomeNodes(int howMany) throws RemoteException {
		for (int i = 0; i < howMany; i++) {
			final int idxFrom = random.nextInt(nodes.length);
			final int idxTo = random.nextInt(nodes.length);

			nodes[idxFrom].addNeighbor(nodes[idxTo]);
			remoteNodes[idxFrom].addNeighbor(remoteNodes[idxTo]);
		}
	}

	/**
	 * Runs a quick measurement on the graph.
	 * 
	 * @param howMany number of measurements
	 */
	public static void searchBenchmark(int howMany) throws RemoteException {
		// Display measurement header.
		System.out.printf("%7s %6s %8s %13s %13s%n", "Attempt", "Config", "Distance", "Time", "TTime");
		for (int i = 0; i < howMany; i++) {
			// Select two random nodes.
			final int idxFrom = random.nextInt(nodes.length);
			final int idxTo = random.nextInt(nodes.length);

			// Calculate distance on local graph with local searcher, measure operation time
			final long startTimeNsLGLS = System.nanoTime();
			final int distanceLGLS = searcher.getDistance(nodes[idxFrom], nodes[idxTo]);
			final long durationNsLGLS = System.nanoTime() - startTimeNsLGLS;

			// Calculate transitive distance on local graph with local searcher, measure operation time
			final long startTimeTransitiveNsLGLS = System.nanoTime();
			final int distanceTransitiveLGLS = searcher.getDistanceTransitive(99, nodes[idxFrom], nodes[idxTo]);
			final long durationTransitiveNsLGLS = System.nanoTime() - startTimeTransitiveNsLGLS;
			
			// Calculate distance on local graph with remote searcher, measure operation time
			final long startTimeNsLGRS = System.nanoTime();
			final int distanceLGRS = remoteSearcher.getDistance(nodes[idxFrom], nodes[idxTo]);
			final long durationNsLGRS = System.nanoTime() - startTimeNsLGRS;
			
			// Calculate transitive distance on local graph with remote searcher, measure operation time
			final long startTimeTransitiveNsLGRS = System.nanoTime();
			final int distanceTransitiveLGRS = remoteSearcher.getDistanceTransitive(99, nodes[idxFrom], nodes[idxTo]);
			final long durationTransitiveNsLGRS = System.nanoTime() - startTimeTransitiveNsLGRS;

			// Calculate distance on remote graph with local searcher, measure operation time
			final long startTimeNsRGLS = System.nanoTime();
			final int distanceRGLS = searcher.getDistance(remoteNodes[idxFrom], remoteNodes[idxTo]);
			final long durationNsRGLS = System.nanoTime() - startTimeNsRGLS;

			// Calculate transitive distance on remote graph with local searcher, measure operation time
			final long startTimeTransitiveNsRGLS = System.nanoTime();
			final int distanceTransitiveRGLS = searcher.getDistanceTransitive(99, remoteNodes[idxFrom], remoteNodes[idxTo]);
			final long durationTransitiveNsRGLS = System.nanoTime() - startTimeTransitiveNsRGLS;
			
			// Calculate distance on remote graph with remote searcher, measure operation time
			final long startTimeNsRGRS = System.nanoTime();
			final int distanceRGRS = remoteSearcher.getDistance(remoteNodes[idxFrom], remoteNodes[idxTo]);
			final long durationNsRGRS = System.nanoTime() - startTimeNsRGRS;
			
			// Calculate transitive distance on remote graph with remote searcher, measure operation time
			final long startTimeTransitiveNsRGRS = System.nanoTime();
			final int distanceTransitiveRGRS = remoteSearcher.getDistanceTransitive(99, remoteNodes[idxFrom], remoteNodes[idxTo]);
			final long durationTransitiveNsRGRS = System.nanoTime() - startTimeTransitiveNsRGRS;

			if (distanceLGLS != distanceTransitiveLGLS) {
				System.out.printf("LGLS: Standard and transitive algorithms inconsistent (%d != %d)%n", distanceLGLS,
						distanceTransitiveLGLS);
			} else {
				// Print the measurement result.
				System.out.printf("%7d %6s %8d %13d %13d%n", i, "LGLS", distanceLGLS, durationNsLGLS / 1000, durationTransitiveNsLGLS / 1000);
			}

			if (distanceLGRS != distanceTransitiveLGRS) {
				System.out.printf("LGRS: Standard and transitive algorithms inconsistent (%d != %d)%n", distanceLGRS,
						distanceTransitiveLGRS);
			} else {
				// Print the measurement result.
				System.out.printf("%7d %6s %8d %13d %13d%n", i, "LGRS", distanceLGRS, durationNsLGRS / 1000, durationTransitiveNsLGRS / 1000);
			}

			if (distanceRGLS != distanceTransitiveRGLS) {
				System.out.printf("RGLS: Standard and transitive algorithms inconsistent (%d != %d)%n", distanceRGLS,
					distanceTransitiveRGLS);
			} else {
				// Print the measurement result.
				System.out.printf("%7d %6s %8d %13d %13d%n", i, "RGLS", distanceRGLS, durationNsRGLS / 1000, durationTransitiveNsRGLS / 1000);
			}

			if (distanceRGRS != distanceTransitiveRGRS) {
				System.out.printf("RGRS: Standard and transitive algorithms inconsistent (%d != %d)%n", distanceRGRS,
					distanceTransitiveRGRS);
			} else {
				// Print the measurement result.
				System.out.printf("%7d %6s %8d %13d %13d%n", i, "RGRS", distanceRGRS, durationNsRGRS / 1000, durationTransitiveNsRGRS / 1000);
			}
		}
	}

	/**
	 * Runs a quick measurement on the graph with multiple neighbor distances (N_DISTANCES) for transitive distance.
	 * 
	 * @param howMany number of measurements
	 */
	public static void searchBenchmarkWithNDistances(int howMany) throws RemoteException {
		// Display measurement header.
		System.out.printf("%7s %6s %8s %13s %13s %13s %13s%n", "Attempt", "Config", "Distance", "Time", 
			String.format("TTime%d", N_DISTANCES[0]), String.format("TTime%d", N_DISTANCES[1]),  String.format("TTime%d", N_DISTANCES[2]));
		for (int i = 0; i < howMany; i++) {
			// Select two random nodes.
			final int idxFrom = random.nextInt(nodes.length);
			final int idxTo = random.nextInt(nodes.length);

			// LGLS = Local Graph & Local Searcher
			// Calculate distance on local graph with local searcher, measure operation time
			final long startTimeNsLGLS = System.nanoTime();
			final int distanceLGLS = searcher.getDistance(nodes[idxFrom], nodes[idxTo]);
			final long durationNsLGLS = System.nanoTime() - startTimeNsLGLS;

			// Calculate transitive distance with N_DISTANCES[0] on local graph with local searcher, measure operation time
			final long startTimeTransitiveNsLGLS0 = System.nanoTime();
			final int distanceTransitiveLGLS0 = searcher.getDistanceTransitive(N_DISTANCES[0], nodes[idxFrom], nodes[idxTo]);
			final long durationTransitiveNsLGLS0 = System.nanoTime() - startTimeTransitiveNsLGLS0;

			// Calculate transitive distance with N_DISTANCES[1] on local graph with local searcher, measure operation time
			final long startTimeTransitiveNsLGLS1 = System.nanoTime();
			final int distanceTransitiveLGLS1 = searcher.getDistanceTransitive(N_DISTANCES[1], nodes[idxFrom], nodes[idxTo]);
			final long durationTransitiveNsLGLS1 = System.nanoTime() - startTimeTransitiveNsLGLS1;

			// Calculate transitive distance with N_DISTANCES[2] on local graph with local searcher, measure operation time
			final long startTimeTransitiveNsLGLS2 = System.nanoTime();
			final int distanceTransitiveLGLS2 = searcher.getDistanceTransitive(N_DISTANCES[2], nodes[idxFrom], nodes[idxTo]);
			final long durationTransitiveNsLGLS2 = System.nanoTime() - startTimeTransitiveNsLGLS2;
			
			// LGRS = Local Graph & Remote Searcher
			// Calculate distance on local graph with remote searcher, measure operation time
			final long startTimeNsLGRS = System.nanoTime();
			final int distanceLGRS = remoteSearcher.getDistance(nodes[idxFrom], nodes[idxTo]);
			final long durationNsLGRS = System.nanoTime() - startTimeNsLGRS;
			
			// Calculate transitive distance with N_DISTANCES[0] on local graph with remote searcher, measure operation time
			final long startTimeTransitiveNsLGRS0 = System.nanoTime();
			final int distanceTransitiveLGRS0 = remoteSearcher.getDistanceTransitive(N_DISTANCES[0], nodes[idxFrom], nodes[idxTo]);
			final long durationTransitiveNsLGRS0 = System.nanoTime() - startTimeTransitiveNsLGRS0;

			// Calculate transitive distance with N_DISTANCES[1] on local graph with remote searcher, measure operation time
			final long startTimeTransitiveNsLGRS1 = System.nanoTime();
			final int distanceTransitiveLGRS1 = remoteSearcher.getDistanceTransitive(N_DISTANCES[1], nodes[idxFrom], nodes[idxTo]);
			final long durationTransitiveNsLGRS1 = System.nanoTime() - startTimeTransitiveNsLGRS1;

			// Calculate transitive distance with N_DISTANCES[2] on local graph with remote searcher, measure operation time
			final long startTimeTransitiveNsLGRS2 = System.nanoTime();
			final int distanceTransitiveLGRS2 = remoteSearcher.getDistanceTransitive(N_DISTANCES[2], nodes[idxFrom], nodes[idxTo]);
			final long durationTransitiveNsLGRS2 = System.nanoTime() - startTimeTransitiveNsLGRS2;

			// RGLS = Remote Graph & Local Searcher
			// Calculate distance on remote graph with local searcher, measure operation time
			final long startTimeNsRGLS = System.nanoTime();
			final int distanceRGLS = searcher.getDistance(remoteNodes[idxFrom], remoteNodes[idxTo]);
			final long durationNsRGLS = System.nanoTime() - startTimeNsRGLS;

			// Calculate transitive distance with N_DISTANCES[0] on remote graph with local searcher, measure operation time
			final long startTimeTransitiveNsRGLS0 = System.nanoTime();
			final int distanceTransitiveRGLS0 = searcher.getDistanceTransitive(N_DISTANCES[0], remoteNodes[idxFrom], remoteNodes[idxTo]);
			final long durationTransitiveNsRGLS0 = System.nanoTime() - startTimeTransitiveNsRGLS0;

			// Calculate transitive distance with N_DISTANCES[0] on remote graph with local searcher, measure operation time
			final long startTimeTransitiveNsRGLS1 = System.nanoTime();
			final int distanceTransitiveRGLS1 = searcher.getDistanceTransitive(N_DISTANCES[1], remoteNodes[idxFrom], remoteNodes[idxTo]);
			final long durationTransitiveNsRGLS1 = System.nanoTime() - startTimeTransitiveNsRGLS1;

			// Calculate transitive distance with N_DISTANCES[0] on remote graph with local searcher, measure operation time
			final long startTimeTransitiveNsRGLS2 = System.nanoTime();
			final int distanceTransitiveRGLS2 = searcher.getDistanceTransitive(N_DISTANCES[2], remoteNodes[idxFrom], remoteNodes[idxTo]);
			final long durationTransitiveNsRGLS2 = System.nanoTime() - startTimeTransitiveNsRGLS2;
			
			// RGRS = Remote Graph & Remote Searcher
			// Calculate distance on remote graph with remote searcher, measure operation time
			final long startTimeNsRGRS = System.nanoTime();
			final int distanceRGRS = remoteSearcher.getDistance(remoteNodes[idxFrom], remoteNodes[idxTo]);
			final long durationNsRGRS = System.nanoTime() - startTimeNsRGRS;
			
			// Calculate transitive distance with N_DISTANCES[0] on remote graph with remote searcher, measure operation time
			final long startTimeTransitiveNsRGRS0 = System.nanoTime();
			final int distanceTransitiveRGRS0 = remoteSearcher.getDistanceTransitive(N_DISTANCES[0], remoteNodes[idxFrom], remoteNodes[idxTo]);
			final long durationTransitiveNsRGRS0 = System.nanoTime() - startTimeTransitiveNsRGRS0;

			// Calculate transitive distance with N_DISTANCES[1] on remote graph with remote searcher, measure operation time
			final long startTimeTransitiveNsRGRS1 = System.nanoTime();
			final int distanceTransitiveRGRS1 = remoteSearcher.getDistanceTransitive(N_DISTANCES[1], remoteNodes[idxFrom], remoteNodes[idxTo]);
			final long durationTransitiveNsRGRS1 = System.nanoTime() - startTimeTransitiveNsRGRS1;

			// Calculate transitive distance with N_DISTANCES[0] on remote graph with remote searcher, measure operation time
			final long startTimeTransitiveNsRGRS2 = System.nanoTime();
			final int distanceTransitiveRGRS2 = remoteSearcher.getDistanceTransitive(N_DISTANCES[2], remoteNodes[idxFrom], remoteNodes[idxTo]);
			final long durationTransitiveNsRGRS2 = System.nanoTime() - startTimeTransitiveNsRGRS2;

			if (distanceLGLS != distanceTransitiveLGLS0) {
				System.out.printf("LGLS: Standard and transitive algorithms inconsistent (%d != %d)%n", distanceLGLS,
						distanceTransitiveLGLS0);
			} else {
				// Print the measurement result.
				System.out.printf("%7d %6s %8d %13d %13d %13d %13d%n", i, "LGLS", distanceLGLS, durationNsLGLS / 1000, 
					durationTransitiveNsLGLS0 / 1000, durationTransitiveNsLGLS1 / 1000, durationTransitiveNsLGLS2 / 1000);
			}

			if (distanceLGRS != distanceTransitiveLGRS0) {
				System.out.printf("LGRS: Standard and transitive algorithms inconsistent (%d != %d)%n", distanceLGRS,
						distanceTransitiveLGRS0);
			} else {
				// Print the measurement result.
				System.out.printf("%7d %6s %8d %13d %13d %13d %13d%n", i, "LGRS", distanceLGRS, durationNsLGRS / 1000, 
					durationTransitiveNsLGRS0 / 1000, durationTransitiveNsLGRS1 / 1000, durationTransitiveNsLGRS2 / 1000);
			}

			if (distanceRGLS != distanceTransitiveRGLS0) {
				System.out.printf("RGLS: Standard and transitive algorithms inconsistent (%d != %d)%n", distanceRGLS,
					distanceTransitiveRGLS0);
			} else {
				// Print the measurement result.
				System.out.printf("%7d %6s %8d %13d %13d %13d %13d%n", i, "RGLS", distanceRGLS, durationNsRGLS / 1000, 
					durationTransitiveNsRGLS0 / 1000, durationTransitiveNsRGLS1 / 1000, durationTransitiveNsRGLS2 / 1000);
			}

			if (distanceRGRS != distanceTransitiveRGRS0) {
				System.out.printf("RGRS: Standard and transitive algorithms inconsistent (%d != %d)%n", distanceRGRS,
					distanceTransitiveRGRS0);
			} else {
				// Print the measurement result.
				System.out.printf("%7d %6s %8d %13d %13d %13d %13d%n", i, "RGRS", distanceRGRS, durationNsRGRS / 1000,
					durationTransitiveNsRGRS0 / 1000, durationTransitiveNsRGRS1 / 1000, durationTransitiveNsRGRS2 / 1000);
			}
		}
	}

	public static void main(String[] args) {
		try {
			remoteSearcher = (Searcher) Naming.lookup("//" + HOST + "/SearcherServer");
			remoteNodeFactory = (NodeFactory) Naming.lookup("//" + HOST + "/NodeFactoryServer");

			// Create a randomly connected graph and do a quick measurement.
			// Consider replacing connectSomeNodes with connectAllNodes to verify that all distances are equal to one.
			createNodes(GRAPH_NODES);
			connectSomeNodes(GRAPH_EDGES);
			// connectAllNodes();
			// searchBenchmark(SEARCHES);
			searchBenchmarkWithNDistances(SEARCHES);
		} catch (Exception e) {
			System.out.println("Client Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
