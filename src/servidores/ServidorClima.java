package servidores;

import messages.BroadcastMessage;

public class ServidorClima extends Servidor{
	//Constantes
	public static final int CONTROL_PORT = 2001;
	public static final int ID = CONTROL_PORT;
	public static final String PRES_ATMOS = "PresionAtmosferica";
	public static final String TEMP = "Temperatura";
	public static final String HUMEDAD = "Humedad";
	
	//Atributos
	private double presionAtmosferica; //[950, 1020] hPa
	private int temperatura; // [-20, 40] ºC
	private int humedad; // [0-100] %
	
	//Constructor
	public ServidorClima() {
		super(ID, CONTROL_PORT);
	}

	//Getters & Setters
	public double getPresionAtmosferica() {
		//generamos un double con 2 decimales
		this.presionAtmosferica = Math.round(this.r.nextDouble(950,1021)* 100.0) / 100.0;
		return presionAtmosferica;
	}

	public int getTemperatura() {
		this.temperatura = this.r.nextInt(-20,40);
		return temperatura;
	}

	public int getHumedad() {
		this.humedad = this.r.nextInt(101);
		return humedad;
	}
	
	public String getParameters() {
		return new BroadcastMessage(ID, 
				PRES_ATMOS, String.valueOf(getPresionAtmosferica()), 
				TEMP, String.valueOf(getTemperatura()), 
				HUMEDAD, String.valueOf(getHumedad())).serialize();
	}
	
	public String getID() {
		return id;
	}

	//Funcionalidad
	
	//Main
	public static void main(String args[]) {
		//Creamos los servidores
		Servidor sClima = new ServidorClima();
		//Ejecutamos
		sClima.run();
	}
}
