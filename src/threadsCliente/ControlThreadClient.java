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
	private DatagramSocket socket;
	private Cliente creator;
	private Scanner scanner;
	
	//Constructor
	public ControlThreadClient(Cliente c) {
		this.creator = c;
		scanner = new Scanner(System.in);
		this.socket = createSocket();
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
			
			//Controlamos si se quiere cambiar el modo verbose
			if(c.getCommand() == ControlMessageType.VERBOSE) {
				this.creator.setVerbose(true);
				continue;
			}
			
			//Controlamos si se quiere cambiar el modo verbose
			if(c.getCommand() == ControlMessageType.NOT_VERBOSE) {
				this.creator.setVerbose(false);
				continue;
			}
			
			//Caso de set refresh
			//Comprobamos servidor existente
			if(creator.getAddress(c.getIdServer()) == null) {
				System.out.println("Servidor inexistente");
				printServersRunning();
				continue;
			}
			
			//Mandamos la peticion
			if(c instanceof SetTimeRefreshMessage) {
				byte[] buf = ((SetTimeRefreshMessage)c).serialize();
				packet = new DatagramPacket(buf, buf.length, creator.getAddress(c.getIdServer()), c.getIdServer());
				System.out.println("addr="+creator.getAddress(c.getIdServer())+", port="+c.getIdServer());
				sendMessage(socket, packet);
			}
			
			
			//Recibimos respuesta
//			packet = new DatagramPacket(buf, buf.length);
//			String resp = receiveMessage(packet);
//			System.out.println(resp);
		}
		//Cerramos el socket
//		closeSocket(socket);
	}
	
	public void printPrompt() {
		System.out.print("\nCliente:$ ");
	}
	
	public void printHelp() {
		System.out.println("USAGE: <command> [id_server] [options] \n"
				+ " setrefresh <id_server> <time> : Establece el tiempo de refresco de un servidor\n"
				+ " statistic : Muestra las estadísticas de valores ofrecidas por los servidores\n"
				+ " verbose: Muestra los mensajes que van enviando los servidores\n"
				+ " notverbose: Deja de mostrar los mensajes que van enviando los servidores\n"
				+ " help : Muestra este mensaje");
		printServersRunning();
	}
	
	public void printServersRunning() { 
		System.out.println("Servers running: "+creator.getServersRunning());
	}
	
	public void sendMessage(DatagramSocket socket, DatagramPacket packet) {
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Cliente: Error en el envío de mensaje mensaje de control");
		}
	}
	
	public String receiveMessage(DatagramPacket packet) {
		try {
			this.socket.receive(packet);
			return new String(packet.getData(), 0, packet.getLength());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Cliente: Error en la lectura de mensaje de control");
			return "";
		}
	}
	
	public DatagramSocket createSocket() {
		try {
			DatagramSocket socket = new DatagramSocket();
			return socket;
		} catch (SocketException e) {
			e.printStackTrace();
			System.err.println("Error creando el socket del hilo control del cliente");
			return null;
		}
	}
	
	public void closeSocket() {
		this.socket.close();
	}
}
