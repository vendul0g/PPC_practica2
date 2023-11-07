package threadsCliente;

import java.net.*;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import messages.*;
import servidores.ServidorCalidadAire;
import servidores.ServidorClima;
import servidores.ServidorMeteorologia;

import java.io.*;

public class ControlThreadClient extends Thread{
	//Atributos
	private DatagramSocket socket;
	private InetAddress dst;
	private Scanner scanner;
	private Map<Integer, Integer> serverPorts;
	
	//Constructor
	public ControlThreadClient(DatagramSocket s, InetAddress dst) {
		this.socket = s;
		this.dst = dst;
		scanner = new Scanner(System.in);
		
		//Inicializamos el mapa con los puertos de los servidores
		this.serverPorts = new TreeMap<>();
		this.serverPorts.put(0, ServidorClima.CONTROL_PORT);
		this.serverPorts.put(1, ServidorCalidadAire.CONTROL_PORT);
		this.serverPorts.put(2, ServidorMeteorologia.CONTROL_PORT);
	}
	
	//Funcionalidad
	public void run() {
		String inst;
		DatagramPacket packet;
		
		while(true) {
			//Preparamos la interfaz
			printPrompt();
			//Leemos la entrada del usuario
			inst = scanner.nextLine();
			
			//Procesamos lo que quiere el usuario
			ControlMessage c = ControlMessageType.getType(inst);
			
			//Comprobamos si el comando introducido es inválido
			if(c.getCommand() == ControlMessageType.INVALID) {
				System.out.println("Comando incorrecto");
				continue;
			}
			
			//Mandamos la peticion
			byte[] buf = c.getBytes();
			packet = new DatagramPacket(buf, buf.length, dst, this.serverPorts.get(c.getDstPort()));
			sendMessage(packet);
			
			//Recibimos respuesta
			packet = new DatagramPacket(buf, buf.length);
			String resp = receiveMessage(packet);
			System.out.println(resp);
		}
	}
	
	public void printPrompt() {
		System.out.print("\nCliente:$ ");
		//TODO Añadir ayuda de comandos
	}
	
	public void sendMessage(DatagramPacket packet) {
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Cliente: Error en el envío de mensaje mensaje de control");
		}
	}
	
	public String receiveMessage(DatagramPacket packet) {
		try {
			socket.receive(packet);
			return new String(packet.getData(), 0, packet.getLength());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Cliente: Error en la lectura de mensaje de control");
			return "";
		}
	}
}
