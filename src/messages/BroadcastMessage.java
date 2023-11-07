package messages;

public class BroadcastMessage {
	//Constantes
	public static final String ID_SEPARATOR = ":";
	public static final String VALUE_SEPARATOR = ",";

	//Atributos
	private String id;
	private String param1; 
	private String param2;
	private String param3;
	
	//Constructor
	public BroadcastMessage() {

	}
	
	public BroadcastMessage(String id, String p1, String p2, String p3) {
		this.id = id;
		this.param1 = p1;
		this.param2 = p2;
		this.param3 = p3;
	}
	
	//Setters
	public void setId(String id) {
		this.id = id;
	}
	
	public void setParam1(String p1) {
		this.param1 = p1;
	}
	
	public void setParam2(String p2) {
		this.param2 = p2;
	}
	
	public void setParam3(String p3) {
		this.param3 = p3;
	}
	
	public String getMessage() {
		return id+ID_SEPARATOR
				+param1+VALUE_SEPARATOR
				+param2+VALUE_SEPARATOR
				+param3;
	}
}
