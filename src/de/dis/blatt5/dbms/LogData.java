package de.dis.blatt5.dbms;

class LogData {

    private int pageID;

    private String content;

    private int LSN;

    private int transactionID;

    int getPageID() {
        return pageID;
    }

    String getContent() {
        return content;
    }

    int getLSN() {
        return LSN;
    }

    int getTransactionID() {
        return transactionID;
    }

    LogData(int pageID, String content, int LSN, int transactionID) {
        this.pageID = pageID;
        this.content = content;
        this.LSN = LSN;
        this.transactionID = transactionID;
    }
}

