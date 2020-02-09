package client.communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPConnection {
	private static TCPConnection instance;
	private int puerto;
	private String serverIP;
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private TCPConnection() {

	}
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public static synchronized TCPConnection getInstance() {
		if(instance == null) {
			instance = new TCPConnection();
		}
		return instance;
	}
	public void requestConnection() {
		try {
			this.socket = new Socket(serverIP, puerto);
			System.out.println("Conexión establecida");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void initReceiver() {
		try {
			InputStream is = this.socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void initSender() {
		try {
			OutputStream os = this.socket.getOutputStream();
			writer = new BufferedWriter(new OutputStreamWriter(os));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void receiveMessage() {
		try {
			String line = reader.readLine();
			observer.onMessageReceive(line);//usando patrón observer
		} catch (IOException e) {
			System.out.println("...");
		}
	}
	public void sendMessage(String msg) {
		try {
			writer.write(msg+"\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//patrón observer
	private OnMessageListener observer;//observador--> está nulo
	
	public void setObserver(OnMessageListener observer) {//instanciar el observador
		this.observer = observer;
	}
	
	public interface OnMessageListener{//Interfaz para el patrón observer
		void onMessageReceive(String msg);
		
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
