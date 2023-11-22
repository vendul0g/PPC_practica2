package cliente;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import estadistico.Estadistico;
import messages.Message;
import servidores.Servidor;
import threadsCliente.ControlThreadClient;
import threadsCliente.ListenerThread;

public class Cliente {	
	//Atributos
	private DatagramSocket socketListener;
	private Map<Integer, InetAddress> portServerMapper;
	private boolean verbose;
	
	//Constructor
	public Cliente() {
		//Inicializamos valores
		crearSocketListener();
		this.portServerMapper = new TreeMap<Integer, InetAddress>(); 
		this.verbose = false;
	}
	
	//Getters & Setters
	public void addServer(int port, InetAddress addr) {
		if(portServerMapper.get(port) != null)
			return;
		portServerMapper.put(port, addr);
	}
	
	public InetAddress getAddress(int port) {
	    return this.portServerMapper.get(port);
	}

	
	public String getServersRunning() {
	    if(portServerMapper.isEmpty()) 
	    	return null;
	    String s = "";
	    for(int i : portServerMapper.keySet()) {
	    	s += "\n - [id]="+i+" - "+portServerMapper.get(i);
	    }
	    return s;
	}
	
	public void setVerbose(boolean v) {
		this.verbose = v;
	}

	public boolean isVerbose() {
		return this.verbose;
	}
	
	//Funcionalidad
	public void run() {
		//Creamos los hilos
		ListenerThread l = new ListenerThread(this, socketListener);
		ControlThreadClient c = new ControlThreadClient(this);
		
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
	
	public void closeSockets() {
		this.socketListener.close();
	}
	
	//Main
	public static void main(String args[]) {
		Cliente c = new Cliente();
		c.run();
	}
}
