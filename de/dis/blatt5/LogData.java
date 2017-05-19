package de.dis.blatt5;

public class LogData {

	private int pageID;
	
	private String content;
	
	private int LSN;
	
	private int transactionID;
	
	public int getPageID() {
		return pageID;
	}
	public String getContent() {
		return content;
	}
	public int getLSN() {
		return LSN;
	}
	public int getTransactionID() {
		return transactionID;
	}
	
	public LogData(int pageID, String content, int LSN, int transactionID)
	{
		this.pageID = pageID;
		this.content = content;
		this.LSN = LSN;
		this.transactionID = transactionID;
	}
}

