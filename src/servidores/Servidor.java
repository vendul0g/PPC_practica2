package servidores;

import java.io.IOException;
import java.net.*;
import java.util.Random;

import addressCalculator.AddressCalculator;
import threadsServidor.*;

public abstract class Servidor {
	//Constantes
	public static final int BROADCAST_PORT = 2048;
	private static final int DEFAULT_REFRESH = 1*1000; //Miliegundos
	
	//Atributos
	protected String id;
	private int timeRefresh;
	private DatagramSocket s;
	protected Random r;
	private SenderThread sender;
	private ControlThreadServer controller;
	private InetAddress broadcastAddr;
	
	//Constructor
	public Servidor(int port) {
		createSocket(port);
		this.sender = new SenderThread(this, getSocket());
		this.controller = new ControlThreadServer(this, getSocket());
		
		this.timeRefresh = DEFAULT_REFRESH;
		this.r = new Random();
		
		broadcastAddr = AddressCalculator.getWloAddress();
	}
	
	//Getters & Setters
	protected DatagramSocket getSocket() {
		return this.s;
	}
	
	protected Random getRandom() {
		return this.r;
	}
	
	public String getID() {
		return id;
	}
	
	public int getTimeRefresh() {
		return timeRefresh;
	}
	
	public void setTimeRefresh(int n) {
		this.timeRefresh = n;
		System.out.println(timeRefresh);
	}
	
	public abstract String getParameters();
	
	//Funcionalidad
	public void run() {
		sender.start();
		controller.start();
	}
	
	public void createSocket(int port) {
		try {
			s = new DatagramSocket(port);
		}catch(IOException e) {
			e.printStackTrace();
			System.err.println("["+id+"]: Error con binding de servidor");
		}
	}
}
