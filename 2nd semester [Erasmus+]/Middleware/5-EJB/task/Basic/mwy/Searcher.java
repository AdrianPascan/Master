package mwy;

import javax.ejb.Remote;

@Remote
public interface Searcher {
	/**
	 * Value returned from getDistance when there is no path between the two nodes.
	 */
	public static final int DISTANCE_INFINITE = -1;
	/**
	 * Computes the distance between nodes from and to.
	 * Returns DISTANCE_INFINITE if there is no path between them.
	 */
	public int getDistance(int nodeFromId, int nodeToId);
	/**
	 * Adds a new node into the graph. Returns the id of the node.
	 */
	public int addNode();
	/**
	 * Adds a new edge into the graph.
	 */
	public void connectNodes(int nodeFromId, int nodeToId);
}
