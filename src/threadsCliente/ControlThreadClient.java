package threadsCliente;

import java.net.*;
import java.util.Scanner;
import cliente.Cliente;
import messages.*;
import java.io.*;

public class ControlThreadClient extends Thread{
	//Atributos
	private DatagramSocket socket;
	private Cliente creator;
	private Scanner scanner;
	private int serializationMode;
	
	//Constructor
	public ControlThreadClient(Cliente c) {
		this.creator = c;
		scanner = new Scanner(System.in);
		this.socket = createSocket();
		
		this.serializationMode = Message.MODE_XML; //Por defecto en JSON
	}
	
	//Getters & Setters
	public int getSerializationMode() {
		return this.serializationMode;
	}
	
	public void setSerializationMode(int m) {
		if(m == Message.MODE_XML || m == Message.MODE_JSON) {
			this.serializationMode = m;
		}
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
			
			//Comprobamos si se quiere cambiar el modo de serialización de los mensajes de control
			if(c.getCommand() == ControlMessageType.CONTROL_MODE) {
				this.serializationMode = ((SetModeMessage) c).getMode();	
				continue;
			}
			
			//Caso de mensajes con envío 
			
			//Se quiere cambiar el tiempo de refresco
			if(c.getCommand() == ControlMessageType.SET_TIME_REFRESH && checkAddress(c)) {
				byte[] buf = ((SetRefreshMessage)c).serialize(getSerializationMode());
				packet = new DatagramPacket(buf, buf.length, creator.getAddress(c.getIdServer()), c.getIdServer());
				sendMessage(packet);
			}
			
			//Comprobamos si se quiere cambiar el modo de serialización de los mensajes broadcast
			//o un cambio de temperatura
			if( (c.getCommand() == ControlMessageType.BROADCAST_MODE 
					|| c.getCommand() == ControlMessageType.CHANGE_UNIT)
					&& checkAddress(c)) {
				byte[] buf = ((SetModeMessage) c).serialize(getSerializationMode());		
				packet = new DatagramPacket(buf, buf.length, creator.getAddress(c.getIdServer()), c.getIdServer());
				sendMessage(packet);
			}
			
			//Comprobamos si se quiere habilitar/deshabilitar el envio de datos de un servidor
			if( (c.getCommand() == ControlMessageType.DISABLE 
					|| c.getCommand() == ControlMessageType.ENABLE)
					&& checkAddress(c)) {
				byte[] buf = c.serialize(getSerializationMode());
				packet = new DatagramPacket(buf, buf.length, creator.getAddress(c.getIdServer()), c.getIdServer());
				sendMessage(packet);
			}
			
			//Recibimos respuesta
//			packet = new DatagramPacket(buf, buf.length);
//			String resp = receiveMessage(packet);
//			System.out.println(resp);
		}
		//Cerramos el socket
//		closeSocket(socket);
	}
	
	private boolean checkAddress(ControlMessage cm) {
		if(creator.getAddress(cm.getIdServer()) == null) {
			System.out.println("Servidor inexistente");
			printServersRunning();
			return false;
		}
		return true;
	}
	
	public void printPrompt() {
		System.out.print("\nCliente:$ ");
	}
	
	public void printHelp() {
		System.out.println("USAGE: <command> [id_server] [options] \n"
				+ " setrefresh <id_server> <time> : Establece el tiempo de refresco de un servidor\n"
				+ " controlmode <xml/json> : Establece el tipo de serialización para los mensajes de control\n"
				+ " broadcastmode <id_server> <xml/json> : Establece el tipo de serialización para los mensajes broadcast\n"
				+ " changeunit <id_server> <celsius/farenheit> : Cambia la unidad de temperatura\n"
				+ " disable <id_server> : Deshabilita el envío de datos del servidor con id_server\n"
				+ " enable <id_server> : Habilita el envío de datos del servidor con id_server\n"
				+ " statistic : Muestra las estadísticas de valores ofrecidas por los servidores\n"
				+ " verbose : Muestra los mensajes que van enviando los servidores\n"
				+ " notverbose : Deja de mostrar los mensajes que van enviando los servidores\n"
				+ " help : Muestra este mensaje");
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
