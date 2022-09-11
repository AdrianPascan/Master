package ws2;

public class Insurance {
	
	private String id;
	private boolean active;
	private int allowance;
	
	public Insurance(String id, boolean active, int allowance) {
		this.id = id;
		this.active = active;
		this.allowance = allowance;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getAllowance() {
		return allowance;
	}

	public void setAllowance(int allowance) {
		this.allowance = allowance;
	}

}
