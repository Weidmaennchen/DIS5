package de.dis.blatt5.dbms;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class PersistenceManager {

    private static final String TA_ID_FILE_NAME = "taid";
    private static final String LSN_FILE_NAME = "lsn";

    private static PersistenceManager instance;
    private static final Set<Transaction> buffer = ConcurrentHashMap.newKeySet();

    static {
        instance = new PersistenceManager();
    }

    private PersistenceManager() {
        // TODO recovery after system crash
        new File("userdata").mkdirs();
        new File("logdata").mkdirs();
        failureRecovery();
    }

    public static PersistenceManager getInstance() {
        return instance;
    }

    public int beginTransaction() throws IOException {
        int transactionId = getNewTransactionId();
        Transaction trans = new Transaction(transactionId);
        buffer.add(trans);
        System.out.println("created transaction " + transactionId);
        return transactionId;
    }

    public void commit(int transactionId) throws Exception {
        Transaction trans = getTransaction(transactionId);
        if (trans != null) {
            System.out.println("transaction " + transactionId + " committed");
            trans.commit();
        } else {
            throw new Exception("Bad Transaction ID " + transactionId);
        }
    }

    /**
     * Throws exception when the lsn file cannot be read.
     */
    public void write(int transactionId, int pageid, String data) throws Exception {
        Transaction trans = getTransaction(transactionId);
        if (trans != null) {
            int newLsn = getNewLSN();
            UserData newData = new UserData(pageid, data, newLsn);
            LogData logdata = new LogData(pageid, data, newLsn, transactionId);

            //The ordering of following 3 statements is fundamentally important!
            persistData(logdata);
            trans.getDatasets().add(newData);
            persistUserDataIfNeeded();
        } else {
            throw new Exception("Bad Transaction ID " + transactionId);
        }
    }

    private static Transaction getTransaction(int transactionId) {
        for (Transaction t : buffer) {
            if (t.getId() == transactionId)
                return t;
        }
        return null;
    }

    /**
     * Here the buffer should also be cleared after persisting.
     * It is done even though the task does not mention it. See comments in the method for more information.
     *
     * @throws IOException
     */
    private void persistUserDataIfNeeded() throws IOException {
        int amountdatasets = 0;
        for (Transaction t : buffer) {
            amountdatasets += t.getDatasets().size();
        }
        //more than 5 datasets in write buffer --> write committed transactions
        if (amountdatasets >= 5) {
            Set<Transaction> todelete = new HashSet<>();

            //only one thread at a time should enter this block
            synchronized (this) {
//                System.out.println("thread " + Thread.currentThread().getId() + " entered persistUserDataIfNeeded sync block");
                for (Transaction t : buffer) {
                    if (t.isCommitted()) {
                        for (UserData data : t.getDatasets()) {
                            persistData(data);
                        }
                        todelete.add(t);
                    }
                }

                //here the deletion happens. Comment out if Abnahme is n�rgeling... :)
                for (Transaction t : todelete) {
                    System.out.println("removing transaction " + t.getId() + " from buffer");
                    buffer.remove(t);
                }
//                System.out.println("thread " + Thread.currentThread().getId() + " left persistUserDataIfNeeded sync block");
            }
        }
    }

    /**
     * Read the LSN from all UserData files and compare it with the last LSN from the logs
     */
    private void failureRecovery() {
        File transactionDir = new File("userdata");
        File[] files = transactionDir.listFiles();
        //maps PageId to userdata object
        HashMap<Integer, UserData> map = new HashMap<>();

        for (File file : files) {

        }
    }

    /**
     * Basically goes for a cheap guid by counting up an integer in a file.
     * Should have done this with long data type, but meh.
     */
    private synchronized int getNewLSN() throws IOException {
        File file = new File(LSN_FILE_NAME);
        int lsn = 0;

        if (file.exists()) {
            FileReader freader = new FileReader(file);
            BufferedReader breader = new BufferedReader(freader);

            String stringlsn = breader.readLine();
            lsn = Integer.parseInt(stringlsn);

            breader.close();
        } else {
            file.createNewFile();
        }

        FileWriter writer = new FileWriter(LSN_FILE_NAME);
        writer.write(Integer.toString(lsn + 1));
        writer.close();

        return lsn;
    }

    /**
     * Basically goes for a cheap guid by counting up an integer in a file.
     * Should have done this with long data type, but meh.
     */
    private synchronized int getNewTransactionId() throws IOException {
        File file = new File(TA_ID_FILE_NAME);
        int transactionId = 0;

        if (file.exists()) {
            FileReader freader = new FileReader(file);
            BufferedReader breader = new BufferedReader(freader);

            String stringtaid = breader.readLine();
            transactionId = Integer.parseInt(stringtaid);

            breader.close();
        } else {
            file.createNewFile();
        }

        FileWriter writer = new FileWriter(TA_ID_FILE_NAME);
        writer.write(Integer.toString(transactionId + 1));
        writer.close();

        return transactionId;
    }

    private synchronized void persistData(LogData data) throws IOException {
        File file = new File("logdata/" + data.getLSN());
        if (!file.exists()) {
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

    private synchronized void persistData(UserData data) throws IOException {
        System.out.println("persisting page " + data.getPageID());
        File file = new File("userdata/" + data.getPageID());
        if (!file.exists()) {
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
