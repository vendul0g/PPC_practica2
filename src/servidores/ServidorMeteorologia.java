package servidores;

import messages.BroadcastMessage;
import messages.Message;
import serializacion.XMLBroadcastMessageParser;

public class ServidorMeteorologia extends Servidor {
	//Constantes
	public static final int CONTROL_PORT = 2003;
	public static final int ID = CONTROL_PORT;
	public static final String LLOVIENDO = "Lloviendo";
	public static final String VEL_VIENTO = "VelocidadViento";
	public static final String RADIACION = "Radiacion";
	
	//Atributos
	private boolean lluvia; //Si o No
	private double velViento; //[0,50] m/s
	private int radiacion; //[800,1200] W/m2
	
	//Constructor
	public ServidorMeteorologia() {
		super(ID, CONTROL_PORT);
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
		BroadcastMessage bm = new BroadcastMessage(ID,
				LLOVIENDO, String.valueOf(isLluvia()), 
				VEL_VIENTO, String.valueOf(getVelViento()), 
				RADIACION, String.valueOf(getRadiacion()));
		return getMode() == Message.MODE_XML ? bm.serializeXML() : bm.serializeJSON();
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
