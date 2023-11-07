package pruebas;

import java.net.*;
import java.io.*;

public class ClienteEco {
	private DatagramSocket socket;
	private InetAddress address;
	private byte[] buf;
	
	public ClienteEco() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			address = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String sendEcho(String msg) {
		buf = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
		try {
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		packet = new DatagramPacket(buf, buf.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String received = new String(packet.getData(), 0, packet.getLength());
		return received;
	}
	
	public void close() {
		socket.close();
	}
	
	public static void main(String args[]) {
		ClienteEco c = new ClienteEco();
		String resp = c.sendEcho("hola");
		System.out.println("C: hola");
		System.out.println("S: "+resp);
	}
}
