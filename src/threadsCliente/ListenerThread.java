package threadsCliente;

import java.net.*;

import cliente.Cliente;
import estadistico.Estadistico;
import messages.BroadcastMessage;

import java.io.*;

public class ListenerThread extends Thread{
	//Atributos
	private Cliente creator;
	private DatagramSocket s;
	private Estadistico e;
	
	//Constructor
	public ListenerThread(Cliente c, DatagramSocket s) {
		this.creator = c;
		this.s = s;
		this.e = new Estadistico();
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
			
			//Anotamos el puerto de envío
			creator.addServer(packet.getPort(), packet.getAddress());
			
			//Mandamos a procesar el mensaje TODO al estadístico
			BroadcastMessage bm = new BroadcastMessage().deserialize(msg);
			if(bm != null) {
				if(creator.isVerbose()) System.out.println(bm.toString());
				e.addEntradas(msg);
			}
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
