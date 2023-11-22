package servidores;

import messages.BroadcastMessage;
import messages.Message;

public class ServidorClima extends Servidor{
	//Constantes
	public static final int CONTROL_PORT = 2001;
	public static final int ID = CONTROL_PORT;
	public static final String PRES_ATMOS = "PresionAtmosferica";
	public static final String TEMP = "Temperatura";
	public static final String HUMEDAD = "Humedad";
	
	//Atributos
	private double presionAtmosferica; //[950, 1020] hPa
	private int temperatura; // [-20, 40] ºC or F
	private int humedad; // [0-100] %
	private boolean farenheit;
	
	//Constructor
	public ServidorClima() {
		super(ID, CONTROL_PORT);
		farenheit = false;
	}

	//Getters & Setters
	public double getPresionAtmosferica() {
		//generamos un double con 2 decimales
		this.presionAtmosferica = Math.round(this.r.nextDouble(950,1021)* 100.0) / 100.0;
		return presionAtmosferica;
	}

	public int getTemperatura(boolean farenheit) {
		this.temperatura = this.r.nextInt(15,35);
		return farenheit ? (temperatura* 9/5) + 32 : temperatura;
	}

	public int getHumedad() {
		this.humedad = this.r.nextInt(101);
		return humedad;
	}
	
	public boolean getFarenheit() {
		return this.farenheit;
	}
	
	public void setFarenheit(boolean f) {
		this.farenheit = f;
	}
	
	public String getParameters() {
		BroadcastMessage bm = new BroadcastMessage(ID, 
				PRES_ATMOS, String.valueOf(getPresionAtmosferica()), 
				TEMP, String.valueOf(getTemperatura(getFarenheit())), 
				HUMEDAD, String.valueOf(getHumedad()));
		return getMode() == Message.MODE_XML ? bm.serializeXML() : bm.serializeJSON();
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
