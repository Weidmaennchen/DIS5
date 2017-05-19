package de.dis.blatt5;

import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;

public class PersistenceManager {
	
	private static PersistenceManager instance;
	
	private PersistenceManager(){}
	
	public static PersistenceManager getInstance()
	{
		if(PersistenceManager.instance == null)
		{
			PersistenceManager.instance = new PersistenceManager();
		}
		return PersistenceManager.instance;
	}
	
	public int beginTransaction()
	{
		return 0;
	}
	
	public void commit(int taid)
	{
		
	}
	
	public void write(int taid, int pageid, String data)
	{
		
	}
	
	private void persistData(LogData data) throws IOException
	{
		FileWriter writer = new FileWriter(Integer.toString(data.getLSN()));		
		
		StringBuilder builder = new StringBuilder();
		builder.append(data.getTransactionID());
		builder.append(',');
		builder.append(data.getPageID());
		builder.append(',');
		builder.append(data.getContent());
		
		String topersist = builder.toString();		
		writer.write(topersist);
	}
	
	private void persistData(UserData data) throws IOException
	{
		FileWriter writer = new FileWriter(Integer.toString(data.getPageID()));
		StringBuilder builder = new StringBuilder();
		builder.append(data.getLSN());
		builder.append(',');
		builder.append(data.getContent());
		
		String topersist = builder.toString();		
		writer.write(topersist);
	}
}
