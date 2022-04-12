package ws;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Insurance {
	
	private String id;
	private boolean active;
	private int allowance;
	
	public Insurance() {
		super();
	}

	public Insurance(String id, boolean active, int allowance) {
		super();
		this.id = id;
		this.active = active;
		this.allowance = allowance;
	}

	@XmlElement(name="id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement(name="active")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@XmlElement(name="allowance")
	public int getAllowance() {
		return allowance;
	}

	public void setAllowance(int allowance) {
		this.allowance = allowance;
	}

	@Override
	public String toString() {
		return "Insurance [id=" + id + ", active=" + active + ", allowance=" + allowance + "]";
	}	
}
