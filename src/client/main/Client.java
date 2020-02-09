package client.main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import client.communication.TCPConnection;

public class Client implements TCPConnection.OnMessageListener{
	public static void main(String[]args) {
		Client c = new Client();
	}
	TCPConnection client;
	long inicio;
	String men;
	String a;
	public Client() {
	    client = TCPConnection.getInstance();
		client.setPuerto(5000);
		client.setServerIP("127.0.0.1");
		client.setObserver(this);
		client.requestConnection();
		client.initSender();
		client.initReceiver();
		Scanner s = new Scanner(System.in);
		String msg;
		while(true) {
			msg = s.nextLine();
			if(msg.equals("Dis")) {
				client.sendMessage(msg);
				client.receiveMessage();
				client.close();
				break;
			}else if(msg.contentEquals("RTT")) {
				byte[] buff = new byte[1024];
				for(int i = 0; i < buff.length; i++) {
					buff[i] = 65;
				}
				men = new String(buff);
				client.sendMessage(men);
				inicio = System.currentTimeMillis();
				client.receiveMessage();
			}else if(msg.equals("speed")) {
				byte[] buff = new byte[8192];
				for(int i = 0; i < buff.length; i++) {
					buff[i] = 65;
				}
				a = new String(buff);
				client.sendMessage(a);
				inicio = System.currentTimeMillis();
				client.receiveMessage();
		    }else {
				client.sendMessage(msg);
				client.receiveMessage();
			}
			
		}
		
	}
	@Override
	public void onMessageReceive(String msg) {
		if(msg.equals(men)) {
			long fin = System.currentTimeMillis();
	        double tiempo = (double) ((fin - inicio));
	        System.out.println("Tiempo = " + tiempo +" milisegundos");
		    System.out.println(msg);
		}else if(msg.equals(a)) {
			long fin = System.currentTimeMillis();
	        double tiempo = (double) ((fin - inicio));
	        double velocidad = 8.192/tiempo;
	        System.out.println("Velocidad = " + velocidad +"KB/ms");
		    System.out.println(msg);
		}else {
			System.out.println(msg);
		}
	}
}
