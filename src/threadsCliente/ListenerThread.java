package threadsCliente;

import java.net.*;

import estadistico.Estadistico;

import java.io.*;

public class ListenerThread extends Thread{
	//Atributos
	private DatagramSocket s;
	private Estadistico e;
	
	//Constructor
	public ListenerThread(DatagramSocket s, Estadistico e) {
		this.s = s;
		this.e = e;
	}
	
	//Funcionalidad
	public void run() {
		byte[] buf = new byte[512];
		DatagramPacket packet;
		String msg;
		
		while(true) { //Vamos escuchando los mensajes del servidor
			packet= new DatagramPacket(buf, buf.length);
			
			//Recibimos el mensaje
			msg = recieveMessage(packet, buf);
			
			//Mandamos a procesar el mensaje TODO al estadístico
			e.addEntradas(msg);
		}
	}
	
	public String recieveMessage(DatagramPacket packet, byte[] buf) {
		String msg;
		try {
			//Recibimos el mensaje
			s.receive(packet);
			msg = new String(packet.getData(), 0, packet.getLength());
			return msg;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Cliente: Error en el procesamiento del mensaje del servidor");
			return "";
		}
	}
	
	public void close() {
		s.close();
	}
}
