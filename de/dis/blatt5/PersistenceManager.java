package de.dis.blatt5;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.util.HashSet;
import java.io.FileReader;


public class PersistenceManager {
	
	private static String taidfilename = "taid";
	private static String lsnfilename = "lsn";
	
	private static PersistenceManager instance;
	
	private static HashSet<Transaction> buffer;
	
	private PersistenceManager()
	{
		// TODO recovery after system crash
		buffer = new HashSet<Transaction>();
	}
	
	public static PersistenceManager getInstance()
	{
		if(PersistenceManager.instance == null)
		{
			PersistenceManager.instance = new PersistenceManager();
		}
		return PersistenceManager.instance;
	}
	
	public int beginTransaction() throws IOException
	{
		int taid = getNewTaid();
		Transaction trans = new Transaction(taid);
		buffer.add(trans);
		return taid;
	}
	
	public void commit(int taid) throws Exception
	{
		Transaction trans = getTransaction(taid);
		if(trans != null)
		{	
			
		}
		else
		{
			throw new Exception("Bad Transaction ID" + taid);
		}
	}
	
	/**
	 * Throws exception when the lsn file cannot be read.
	 */
	public void write(int taid, int pageid, String data) throws Exception
	{
		Transaction trans = getTransaction(taid);
		if(trans != null)
		{	
			int newlsn = getNewLSN();
			UserData newData = new UserData(pageid,data,newlsn);			
			LogData logdata = new LogData(pageid,data,newlsn,taid);
		
			//The ordering of following 3 statements is fundamentally important!
			persistData(logdata);
			trans.getDatasets().add(newData);
			persistUserDataIfNeeded();			
		}
		else
		{
			throw new Exception("Bad Transaction ID" + taid);
		}
	}
	
	private Transaction getTransaction(int taid)
	{		
		for(Transaction t : buffer)
		{
			if(t.getId()==taid)
				return t;
		}
		return null;
	}
	
	/** Here the buffer should also be cleared after persisting.
	 * It is done even though the task does not mention it. See comments in the method for more information.
	 * @throws IOException
	 */
	private void persistUserDataIfNeeded() throws IOException
	{
		int amountdatasets = 0;		
		for(Transaction t : buffer)
		{
			amountdatasets += t.getDatasets().size();
		}
		if (amountdatasets >= 5)
		{
			HashSet<Transaction> todelete = new HashSet<Transaction>();
			
			for(Transaction t : buffer)
			{
				if(!t.isActive())
				{
					for(UserData data : t.getDatasets())
					{
						persistData(data);
					}
					todelete.add(t);
				}								
			}
			
			//here the deletion happens. Comment out if Abnahme is nörgeling... :)
			for(Transaction t : todelete)
			{
				buffer.remove(t);
			}
		}
	}

	/**
	 * Basically goes for a cheap guid by counting up an integer in a file.
	 * Should have done this with long data type, but meh.
	 */
	private int getNewLSN() throws IOException
	{
		File file = new File(lsnfilename);
		int lsn = 0;
		
		if(file.exists())
		{
			FileReader freader = new FileReader(file);
			BufferedReader breader = new BufferedReader(freader);
			
			String stringlsn = breader.readLine();
			lsn = Integer.parseInt(stringlsn);
			
			breader.close();
		}
		else
		{
			file.createNewFile();			
		}		

		FileWriter writer = new FileWriter(lsnfilename);
		writer.write(Integer.toString(lsn + 1));
		writer.close();
		
		return lsn;
	}
	
	/**
	 * Basically goes for a cheap guid by counting up an integer in a file.
	 * Should have done this with long data type, but meh.
	 */
	private int getNewTaid() throws IOException
	{
		File file = new File(taidfilename);
		int taid = 0;
		
		if(file.exists())
		{
			FileReader freader = new FileReader(file);
			BufferedReader breader = new BufferedReader(freader);
			
			String stringtaid = breader.readLine();
			taid = Integer.parseInt(stringtaid);
			
			breader.close();
		}
		else
		{
			file.createNewFile();			
		}		

		FileWriter writer = new FileWriter(taidfilename);
		writer.write(Integer.toString(taid + 1));
		writer.close();
		
		return taid;
	}
	
	private void persistData(LogData data) throws IOException
	{
		File file = new File("logdata\\" + data.getLSN());
		if(!file.exists())
		{
			file.createNewFile();
		}
		FileWriter writer = new FileWriter(file);		
		
		StringBuilder builder = new StringBuilder();
		builder.append(data.getTransactionID());
		builder.append(',');
		builder.append(data.getPageID());
		builder.append(',');
		builder.append(data.getContent());
		
		String topersist = builder.toString();		
		writer.write(topersist);
		writer.close();
	}
	
	private void persistData(UserData data) throws IOException
	{
		File file = new File("userdata\\" + data.getPageID());
		if(!file.exists())
		{
			file.createNewFile();
		}
		FileWriter writer = new FileWriter(file);
		
		StringBuilder builder = new StringBuilder();
		builder.append(data.getLSN());
		builder.append(',');
		builder.append(data.getContent());
		
		String topersist = builder.toString();		
		writer.write(topersist);
		writer.close();
	}
}
