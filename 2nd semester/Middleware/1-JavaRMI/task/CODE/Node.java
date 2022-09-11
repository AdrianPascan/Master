import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

public interface Node extends Remote, Serializable {
	/** Gets the set of nodes connected to this node by an edge. */
	Set<Node> getNeighbors() throws RemoteException;
	/** Gets the set of nodes connected to this node by a path up to the specified distance. */
	Map<Node, Integer> getTransitiveNeighbors(int distance) throws RemoteException;
	/** Connects this node to another by an edge. */
	void addNeighbor(Node neighbor) throws RemoteException;
}
