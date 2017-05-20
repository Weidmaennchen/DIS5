package de.dis.blatt5;

import java.util.HashSet;
import java.util.Set;

public class Transaction {
	
	private boolean active;
	private HashSet<UserData> datasets;
	int id;
	
	public int getId(){return id;}	
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public HashSet<UserData> getDatasets() {
		return datasets;
	}
	
	public Transaction(int id)
	{
		active = true;
		datasets = new HashSet<UserData>();
		this.id = id;
	}	
}
