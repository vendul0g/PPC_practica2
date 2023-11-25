package cliente;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import estadistico.Estadistico;
import messages.BroadcastMessage;
import messages.Message;
import servidores.Servidor;
import threadsCliente.ControlThreadClient;
import threadsCliente.ListenerThread;

public class Cliente {	
	//Atributos
	private DatagramSocket socketListener;
	private Map<Integer, InetAddress> portServerMapper;
	private boolean verbose;
	private Estadistico e;
	
	//Constructor
	public Cliente() {
		//Inicializamos valores
		crearSocketListener();
		this.portServerMapper = new TreeMap<Integer, InetAddress>(); 
		this.verbose = false;
		this.e = new Estadistico();
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
	
	public String getStatistic() {
		return this.e.getStatistic();
	}
	
	public void addEntry(BroadcastMessage bm) {
		this.e.addEntry(bm);
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
	        this.socketListener = new DatagramSocket(null); 
	        this.socketListener.setReuseAddress(true); // Habilitamos SO_REUSEADDR
	        this.socketListener.bind(new InetSocketAddress(Servidor.BROADCAST_PORT)); // Bind the socket to the port
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
