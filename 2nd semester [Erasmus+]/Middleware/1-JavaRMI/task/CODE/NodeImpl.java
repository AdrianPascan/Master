import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NodeImpl implements Node {
	private final Set<Node> nodes = new HashSet<Node>();

	@Override
	public Set<Node> getNeighbors() {
		return nodes;
	}

	@Override
	public Map<Node, Integer> getTransitiveNeighbors(int distance) throws RemoteException {
		if (distance <= 0)
			throw new IllegalArgumentException("Argument distance must be positive");

		Map<Node, Integer> nodeToDistance = new HashMap<Node, Integer>();
		Set<Node> currentLayer = new HashSet<Node>();

		// Initial node at zero-distance
		currentLayer.add(this);

		// Closure  for each level of i-distant nodes
		for (int i = 0; i < distance; ++i) {
			Set<Node> nextLayer = new HashSet<Node>();

			// Use nodes which are in the current level 
			for (Node node : currentLayer) {
				if (!nodeToDistance.containsKey(node)) {
					nodeToDistance.put(node, i);
					nextLayer.addAll(node.getNeighbors());
				}
			}

			// Move to the next layer
			currentLayer = nextLayer;
		}

		// Handle the last layer
		for (Node node : currentLayer) {
			if (!nodeToDistance.containsKey(node))
				nodeToDistance.put(node, distance);
		}

		return nodeToDistance;
	}

	@Override
	public void addNeighbor(Node neighbor) {
		nodes.add(neighbor);
	}
}
