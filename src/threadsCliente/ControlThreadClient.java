package threadsCliente;

import java.net.*;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import cliente.Cliente;
import messages.*;
import servidores.ServidorCalidadAire;
import servidores.ServidorClima;
import servidores.ServidorMeteorologia;

import java.io.*;

public class ControlThreadClient extends Thread{
	//Atributos
	private Cliente creator;
	private DatagramSocket socket;
	private InetAddress dst;
	private Scanner scanner;
	private Map<Integer, Integer> serverPorts;
	
	//Constructor
	public ControlThreadClient(Cliente c, DatagramSocket s, InetAddress dst) {
		this.creator = c;
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
				printHelp();
				continue;
			}
			
			//Controlamos si es un comando de ayuda
			if(c.getCommand() == ControlMessageType.HELP) {
				printHelp();
				continue;
			}
			
			//Comprobamos si el puerto de destino es correcto
			if(!creator.isServerPort(c.getDstPort())) {
				System.out.println("Servidor inexistente");
				printServersRunning();
				continue;
			}
			
			//Mandamos la peticion
			byte[] buf = c.getBytes(); // TODO serializar
			packet = new DatagramPacket(buf, buf.length, dst, c.getDstPort());
			sendMessage(packet);
			
			//Recibimos respuesta
//			packet = new DatagramPacket(buf, buf.length);
//			String resp = receiveMessage(packet);
//			System.out.println(resp);
		}
	}
	
	public void printPrompt() {
		System.out.print("\nCliente:$ ");
	}
	
	public void printHelp() {
		System.out.println("USAGE: <command> [id_server] [options] \n"
				+ " sentrefresh <id_server> <time> : Establece el tiempo de refresco de un servidor\n"
				+ " statistic : get statistics of paremeters sent by the servers\n"
				+ " help : output this message");
		printServersRunning();
	}
	
	public void printServersRunning() { 
		System.out.println("Servers running: "+creator.getServersRunning());
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
