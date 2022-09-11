import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Searcher extends Remote {
	/**
	 * Value returned from getDistance when there is no path between the two nodes.
	 */
	public static final int DISTANCE_INFINITE = -1;
	/**
	 * Computes the distance between nodes from and to.
	 * Returns DISTANCE_INFINITE if there is no path between them.
	 */
	public int getDistance(Node from, Node to) throws RemoteException;
	/**
	 * Computes the distance between nodes from and to, using an algorithm that retrieves nodes transitively up to neighborDistance.
	 * Returns DISTANCE_INFINITE if there is no path between them.
	 */
	public int getDistanceTransitive(int neighborDistance, Node from, Node to) throws RemoteException;
}
