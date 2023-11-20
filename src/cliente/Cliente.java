package cliente;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
	private List<Integer> portServerMapper;
	
	//Constructor
	public Cliente() {
		//Inicializamos valores
		setAddress();
		crearSocketListener();
		crearSocketControl();
		e = new Estadistico();
		this.portServerMapper = new LinkedList<Integer>(); 
	}
	
	//Getters & Setters
	public void addServer(int port) {
		if(portServerMapper.contains(port))
			return;
		portServerMapper.add(port);
	}
	
	public boolean isServerPort(int port) {
	    return this.portServerMapper.contains(Integer.valueOf(port));
	}

	
	public String getServersRunning() {
	    if(portServerMapper.isEmpty()) 
	    	return null;
	    String s = portServerMapper.get(0).toString();
	    for(int i = 1; i < portServerMapper.size(); i++) {
	    	s += ", "+portServerMapper.get(i).toString();
	    }
	    return s;
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
		ListenerThread l = new ListenerThread(this, socketListener, e);
		ControlThreadClient c = new ControlThreadClient(this, socketControl, address);
		
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
