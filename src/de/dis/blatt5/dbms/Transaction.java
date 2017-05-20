package de.dis.blatt5.dbms;

import java.util.HashSet;

public class Transaction {

    private boolean active;
    private HashSet<UserData> datasets;
    private int id;

    int getId() {
        return id;
    }

    boolean isActive() {
        return active;
    }

    void setActive(boolean active) {
        this.active = active;
    }

    HashSet<UserData> getDatasets() {
        return datasets;
    }

    Transaction(int id) {
        active = true;
        datasets = new HashSet<UserData>();
        this.id = id;
    }
}
