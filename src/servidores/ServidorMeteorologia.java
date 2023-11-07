package servidores;

import messages.BroadcastMessage;

public class ServidorMeteorologia extends Servidor {
	//Constantes
	public static final int CONTROL_PORT = 2003;
	public static final String ID = "ServidorMeteorologia";
	public static final String LLOVIENDO = "Lloviendo=";
	public static final String VEL_VIENTO = "VelocidadViento=";
	public static final String RADIACION = "Radiacion=";
	
	//Atributos
	private boolean lluvia; //Si o No
	private double velViento; //[0,50] m/s
	private int radiacion; //[800,1200] W/m2
	
	//Constructor
	public ServidorMeteorologia() {
		super(CONTROL_PORT);
		this.id = ID;
	}
	
	//Getters & Setters
	public boolean isLluvia() {
		this.lluvia = this.r.nextBoolean();
		return lluvia;
	}

	public double getVelViento() {
		this.velViento = Math.round(this.r.nextDouble(51)* 100.0) / 100.0;
		return velViento;
	}

	public int getRadiacion() {
		this.radiacion = this.r.nextInt();
		return radiacion;
	}

	public String getParameters() {
		BroadcastMessage msg = new BroadcastMessage();
		msg.setId(ID);
		msg.setParam1(LLOVIENDO+"="+isLluvia());
		msg.setParam2(VEL_VIENTO+"="+getVelViento());
		msg.setParam3(RADIACION+"="+getRadiacion());
		
		return msg.getMessage();
	}
	
	public String getID() {
		return id;
	}
	
	//Funcionalidad
	
	//Main
	public static void main(String args[]) {
		//Creamos los servidores
		Servidor sMeteo = new ServidorMeteorologia();
		//Ejecutamos
		sMeteo.run();
	}
}
