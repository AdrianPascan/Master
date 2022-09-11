import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class SearcherImpl implements Searcher {

	/**
	 * A trivial distance measurement algorithm.
	 * 
	 * Starting from the source node, then a set of visited nodes
	 * is always extended by immediate neighbors of all visited nodes,
	 * until the target node is visited or no node is left.
	 */
	@Override
	public int getDistance(Node from, Node to) throws RemoteException {
		// visited keeps the nodes visited in past steps.
		Set<Node> visited = new HashSet<Node>();
		// boundary keeps the nodes visited in current step.
		Set<Node> boundary = new HashSet<Node>();

		int distance = 0;

		// We start from the source node.
		boundary.add(from);

		// Traverse the graph until finding the target node.
		while (!boundary.contains(to)) {
			// Not having anything to visit means the target node cannot be reached.
			if (boundary.isEmpty())
				return Searcher.DISTANCE_INFINITE;

			Set<Node> traversing = new HashSet<Node>();

			// Nodes visited in current step become nodes visited in past steps.
			visited.addAll(boundary);

			// Collect a set of immediate neighbors of nodes visited in current step.
			for (Node node : boundary)
				traversing.addAll(node.getNeighbors());

			// Out of immediate neighbors, consider only those not yet visited.
			for (Iterator<Node> node = traversing.iterator(); node.hasNext();) {
				if (visited.contains(node.next()))
					node.remove();
			}

			// Make these nodes the new nodes to be visited in current step.
			boundary = traversing;

			distance++;
		}

		return distance;
	}

	/**
	 * A transitive distance measurement algorithm.
	 * 
	 * Starting from the source node, a set of visited nodes
	 * is always extended by transitive neighbors of all visited
	 * nodes, until the target node is visited or no node is left.
	 */
	@Override
	public int getDistanceTransitive(int neighborDistance, Node from, Node to) throws RemoteException {
		// visited keeps the nodes visited in past steps.
		Set<Node> visited = new HashSet<Node>();
		// boundary keeps the nodes visited in current step.
		Map<Node, Integer> boundary = new HashMap<Node, Integer>();

		// We start from the source node.
		boundary.put(from, 0);

		// Traverse the graph until finding the target node or having an empty boundary.
		while (!boundary.isEmpty()) {
			
			Map<Node, Integer> traversing = new HashMap<Node, Integer>();

			// Collect transitive neighbors of nodes visited in current step
			for (Entry<Node, Integer> currentTuple : boundary.entrySet()) {
				final Node currentNode = currentTuple.getKey();
				final int currentDistance = currentTuple.getValue();
				if (visited.contains(currentNode)) {
					continue;
				}

				Map<Node, Integer> partialGraph = currentNode.getTransitiveNeighbors(neighborDistance);

				// Store the distance of each transitive neighbor
				for (Entry<Node, Integer> searchedTuple : partialGraph.entrySet()) {
					final Node searchedNode = searchedTuple.getKey();
					final int newDistance = currentDistance + searchedTuple.getValue();

					Integer oldDistance = traversing.get(searchedNode); 
					if (oldDistance == null || newDistance < oldDistance)
						traversing.put(searchedNode, newDistance);
				}

				// Nodes visited in current step become nodes visited in past steps
				visited.add(currentNode);
			}

			// Check if the distance to the destination has been computed
			Integer distance = traversing.get(to);
			if (distance != null)
				return distance;

			boundary = traversing;
		}
		// Not having anything to visit means the target node cannot be reached.
		return Searcher.DISTANCE_INFINITE;
	}
}
