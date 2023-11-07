package servidores;

import messages.BroadcastMessage;

public class ServidorCalidadAire extends Servidor{
	//Constantes
	public static final int CONTROL_PORT = 2002;
	public static final String ID = "ServidorCalidadAire";
	public static final String CON_CO2 = "ConcentracionCO2=";
	public static final String CON_O3 = "ConcentracionO3=";
	public static final String CON_PART_SUSP = "ConcentracionParticulasSuspension=";
	
	//Atributos
	private int conCO2; //[350,450] ppm
	private double conO3; //[0,1] ppm
	private double conPartSusp; //[0,40] microgramos/m3
	
	
	//Constructor
	public ServidorCalidadAire() {
		super(CONTROL_PORT);
		this.id = ID;
	}
	
	//Getters & Setters
	public int getConCO2() {
		this.conCO2 = this.r.nextInt(250,451);
		return conCO2;
	}

	public double getConO3() {
		this.conO3 = Math.round(this.r.nextDouble()* 100.0) / 100.0;
		return conO3;
	}

	public double getConPartSusp() {
		this.conPartSusp = Math.round(this.r.nextDouble(41)* 100.0) / 100.0;
		return conPartSusp;
	}
	
	public String getParameters() {
		BroadcastMessage msg = new BroadcastMessage();
		msg.setId(ID);
		msg.setParam1(CON_CO2+"="+getConCO2());
		msg.setParam2(CON_O3+"="+getConO3());
		msg.setParam3(CON_PART_SUSP+"="+getConPartSusp());
		
		return msg.getMessage();
	}
	
	public String getID() {
		return id;
	}
	
	//Funcionalidad
	
	//Main
	public static void main(String args[]) {
		//Creamos los servidores
		Servidor sAire = new ServidorCalidadAire();
		//Ejecutamos
		sAire.run();
	}
}
