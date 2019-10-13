package utils;

public class Link{
	
	private int node1;
	private int node2;
	private double cost;
	private int active;
	
	public Link(){
		
	}
	
	public Link(Link otherLink) {
		this.node1 = otherLink.getNode1();
		this.node2 = otherLink.getNode2();
		this.cost = otherLink.getCost();
		this.active = otherLink.getActive();
	}
	
	public void setNode1(int sNode1) {
		node1 = sNode1;
	}
	
	public void setNode2(int sNode2) {
		node2 = sNode2;
	}

	public void setCost(double sCost) {
		cost = sCost;
	}

	public void setActive(int sActive) {
		active = sActive;
	}
	
	public int getNode1() {
		return node1;
	}
	
	public int getNode2() {
		return node2;
	}
	
	public double getCost() {
		return cost;
	}

	public int getActive() {
		return active;
	}

}
