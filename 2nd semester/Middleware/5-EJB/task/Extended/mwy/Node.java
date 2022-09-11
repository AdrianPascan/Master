package mwy;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Node {

	/* Id of the node */
	private int id;
	
	/* The set of nodes connected to this node by an edge. */
	private Collection<Node> neighbors;

	/* Id of the client owning the node */
	private String clientId;

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Node() {}

	public Node(String clientId) {
		this.clientId = clientId;
	}
	
	/** Gets the collection of nodes connected to this node by an edge. */
	@OneToMany public Collection<Node> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(Collection<Node> neighbors) {
		this.neighbors = neighbors;
	}
	
	public void addNeighbor(Node neighbor) {
		getNeighbors().add(neighbor);
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
