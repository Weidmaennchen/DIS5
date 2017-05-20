package de.dis.blatt5;

public class Startup {
	public static void main(String[] args) {
		System.out.println("Welcome to the Persistence Manager Test!");
		System.out.println("Starting Clients..\n");
		new Client(1,1,5).start();
		new Client(2,10,4).start();
		new Client(3,20,2).start();
		new Client(4,30,8).start();
	}
}
