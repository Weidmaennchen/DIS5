package de.dis.blatt5.dbms;

class UserData {

    private int pageID;

    private String content;

    private int LSN;

    int getPageID() {
        return pageID;
    }

    String getContent() {
        return content;
    }

    int getLSN() {
        return LSN;
    }

    UserData(int pageID, String content, int LSN) {
        this.pageID = pageID;
        this.content = content;
        this.LSN = LSN;
    }
}
