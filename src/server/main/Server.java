package server.main;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;

import server.communication.*;
import server.communication.TCPConnection.OnMessageListener;
public class Server implements OnMessageListener{
	public static void main(String[] args) {
		Server ser = new Server();
	}
	TCPConnection server;
	public Server() {
		server = TCPConnection.getInstance();
		server.setPuerto(5000);
		server.setObserver(this);
		server.waitForConnection();
		server.initReceiver();
		server.initSender();
		while(server.receiveMessage()) {
		}
		
	}
	public void onMessageReceive(String msg) {
		if(msg.equals("Dis")) {
			server.close();
			return;
		}else if(msg.equals("remoteIpconfig")) {
			try {
				InetAddress myAddress = InetAddress.getLocalHost();
				server.sendMessage(myAddress.getHostAddress());
				System.out.println(msg);
				return;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(msg.equals("interface")) {
			try {
				Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
				InetAddress thisAddress = InetAddress.getLoopbackAddress();
				String hostAddress = thisAddress.getHostAddress();
				server.sendMessage(hostAddress);
				System.out.println(msg);
				return;
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}else if(msg.equals("whatTimeIsIt")) {
			Calendar calendario = new GregorianCalendar();
			int hora, minutos, segundos;
			hora =calendario.get(Calendar.HOUR_OF_DAY);
			minutos = calendario.get(Calendar.MINUTE);
			segundos = calendario.get(Calendar.SECOND);
			server.sendMessage(hora + ":" + minutos + ":" + segundos);
			System.out.println(msg);
			return;
		}
		server.sendMessage(msg);
		System.out.println(msg);
	}
}
