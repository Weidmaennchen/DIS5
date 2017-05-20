package de.dis.blatt5.dbms;

import java.util.HashSet;

public class Transaction {

    //true if committed, false otherwise
    private boolean committed;
    private HashSet<UserData> datasets;
    private int id;

    int getId() {
        return id;
    }

    /**
     * @return true if committed, false otherwise
     */
    boolean isCommitted() {
        return committed;
    }

    void commit() {
        this.committed = true;
    }

    HashSet<UserData> getDatasets() {
        return datasets;
    }

    Transaction(int id) {
        committed = true;
        datasets = new HashSet<UserData>();
        this.id = id;
    }
}
