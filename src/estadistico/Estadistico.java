package estadistico;

public class Estadistico {
	//Atributos
	private int tempMedia;
	private int humedadMedia;
	private double presAtmosMedia;
	private int conCO2Media;
	private double conO3Media;
	private double conPartSuspMedia;
	private double lluviaMedia;
	private double velVientoMedia;
	private int radiacionMedia;
	
	//Constructor
	public Estadistico() {
		this.tempMedia = 0;
		this.humedadMedia = 0;
		this.presAtmosMedia = 0;
		this.conCO2Media = 0;
		this.conO3Media = 0;
		this.conPartSuspMedia = 0;
		this.lluviaMedia = 0;
		this.velVientoMedia = 0;
		this.radiacionMedia = 0;
	}
	
	//Funcionalidad
	public void addEntradas(String msg) {
		//Formato de mensaje Ejemplo:
		//ServidorCalidadAire:ConcentracionCO2=294,concentracionO3=0.81,concentracionParticulasSuspension=22.8
//		System.out.println(msg);
		//TODO procesar Mensaje
	}
	
	//TODO get Media
}
