package server.communication;

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
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	
	public static synchronized TCPConnection getInstance() {
		if(instance == null) {
			instance = new TCPConnection();
		}
		return instance;
	}
	private TCPConnection() {
		
	}
	
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	
	public void waitForConnection() {
		try {
			ServerSocket server = new ServerSocket(this.puerto);
			System.out.println("Esperando....");
			this.socket = server.accept();
			System.out.println("Conexi�n establecida");
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
	public boolean receiveMessage() {
		try {
			String line = reader.readLine();
			observer.onMessageReceive(line);//usando patr�n observer
			return true;
		} catch (IOException e) {
			System.out.println("Culpa del compa�ero");
			return false;
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
	//patr�n observer
	private OnMessageListener observer;//observador--> est� nulo
	
	public void setObserver(OnMessageListener observer) {//instanciar el observador
		this.observer = observer;
	}
	
	public interface OnMessageListener{//Interfaz para el patr�n observer
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
