package threadsServidor;

import java.net.*;

import messages.ControlMessage;
import messages.ControlMessageType;
import messages.SetTimeRefreshMessage;
import servidores.Servidor;

import java.io.*;

public class ControlThreadServer extends Thread{
	//Atributos
	private Servidor creator;
	private DatagramSocket socket;
	
	//Constructor
	public ControlThreadServer(Servidor server, DatagramSocket s) {
		this.creator = server;
		this.socket = s;
	}
	
	//Funcionalidad
	public void run() {
		byte[] buf = new byte[256];
		DatagramPacket packet;
		InetAddress addr;
		int port;
		String msg;
		
		while(true) {
			//Recibimos mensajes de control
			packet = new DatagramPacket(buf, buf.length);
			msg = receiveMessage(packet);
			addr = packet.getAddress();
			port = packet.getPort();
			
			//Procesamos el mensaje
			proccesMesage(ControlMessageType.getType(msg));
			
			//Devolvemos un ACK
//			buf = new ControlMessage(ControlMessageType.ACK).getBytes();
//			packet = new DatagramPacket(buf, buf.length, addr, port);
//			sendMessage(packet);
		}
	}
	
	public String receiveMessage(DatagramPacket packet) {
		try {
			socket.receive(packet);
			return new String(packet.getData(), 0, packet.getLength());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Servidor: Error al leer mensaje de control");
			return "";
		}
	}
	
	public void sendMessage(DatagramPacket packet) {
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Servidor: error al responder mensaje de control");
		}
	}
	
	public void proccesMesage(ControlMessage c) {
		switch(c.getCommand()) {
		case SET_TIME_REFRESH:
			this.creator.setTimeRefresh( ((SetTimeRefreshMessage) c).getTime()*1000 );
			break;
			
		default:
			break;
		}
		//TODO implementar NACK por si la petición no es exitosa
	}
}
