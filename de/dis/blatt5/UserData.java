package de.dis.blatt5;

public class UserData {
	
	private int pageID;
	
	private String content;
	
	private int LSN;
	
	public int getPageID() {
		return pageID;
	}
	public String getContent() {
		return content;
	}
	public int getLSN() {
		return LSN;
	}
	
	public UserData(int pageID, String content, int LSN)
	{
		this.pageID = pageID;
		this.content = content;
		this.LSN = LSN;
	}
}
