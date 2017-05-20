package de.dis.blatt5.dbms;

class CommitLog extends LogData {
    CommitLog(int LSN, int transactionID) {
        super(0, "commit", LSN, transactionID);
    }
}
