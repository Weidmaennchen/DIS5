package de.dis.blatt5;

public class Client extends Thread {

    private int id;
    private int startpage;
    private int amountpages;

    public Client(int id, int startpage, int amountpages) {
        this.id = id;
        this.startpage = startpage;
        this.amountpages = amountpages;

    }

    public void run() {
        System.out.println("Client " + id + " initialized!");
        PersistenceManager manager = PersistenceManager.getInstance();
        try {
            int taid = manager.beginTransaction();
            sleep(500);
            for (int i = startpage; i < startpage + amountpages; i++) {
                manager.write(taid, i, "TESTDATENXYZ");
                sleep(300 + i);
            }
            manager.commit(taid);
            System.out.println("Client " + id + " finished!");
        } catch (Exception e) {
            System.out.println("Client " + id + " witnessed an error:");
            e.printStackTrace();
        }

    }
}
