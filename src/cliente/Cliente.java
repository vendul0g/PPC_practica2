package cliente;

import java.io.*;
import java.net.*;

import estadistico.Estadistico;
import servidores.Servidor;
import threadsCliente.ControlThreadClient;
import threadsCliente.ListenerThread;

public class Cliente {	
	//Atributos
	private DatagramSocket socketListener;
	private DatagramSocket socketControl;
	private InetAddress address;
	private Estadistico e;
	
	//Constructor
	public Cliente() {
		//Inicializamos valores
		setAddress();
		crearSocketListener();
		crearSocketControl();
		e = new Estadistico();
	}
	
	//Funcionalidad
	public void setAddress() {
		try {
			this.address = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Error en la dirección de escucha");
		}
	}
	
	public void run() {
		//Creamos los hilos
		ListenerThread l = new ListenerThread(socketListener, e);
		ControlThreadClient c = new ControlThreadClient(socketControl, address);
		
		//Invocamos el hilo de recepción de mensajes broadcast
		l.start();
		
		//Invocamos al hilo de mensajes de control
		c.start();
		
//		closeSockets(); No se cierran 
	}
	
	public void crearSocketListener() {
		try {
			this.socketListener = new DatagramSocket(Servidor.BROADCAST_PORT);
		} catch (SocketException e) {
			e.printStackTrace();
			System.err.println("Error creando el socket de escucha del cliente");
		}
	}
	
	public void crearSocketControl() {
		try {
			this.socketControl = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			System.err.println("Error creando el socket de control del cliente");
		}
	}

	
	public void closeSockets() {
		this.socketListener.close();
		this.socketControl.close();
	}
	
	//Main
	public static void main(String args[]) {
		Cliente c = new Cliente();
		c.run();
	}
}
