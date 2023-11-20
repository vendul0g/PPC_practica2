package messages;

import serializacion.XMLParser;

public class BroadcastMessage extends Message{
	//Atributos
	private XMLParser xmlParser;
	private int id;
	private String nameParam1;
	private String param1; 
	private String nameParam2;
	private String param2;
	private String nameParam3;
	private String param3;
	
	//Constructor
	public BroadcastMessage() {
		this.xmlParser = new XMLParser();
	}
	
	public BroadcastMessage(int id, String np1, String p1, String np2, String p2, String np3, String p3) {
		this.xmlParser = new XMLParser();
		this.id = id;
		this.nameParam1 = np1;
		this.param1 = p1;
		this.nameParam2 = np2;
		this.param2 = p2;
		this.nameParam3 = np3;
		this.param3 = p3;
	}
	
	//Setters
	public void setId(int id) {
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
	
	//Funcionalidad
	public String serialize() {
		return this.xmlParser.serializeBroadcastMessage(String.valueOf(id), 
				nameParam1, String.valueOf(param1), 
				nameParam2, String.valueOf(param2), 
				nameParam3, String.valueOf(param3));
	}
	
	public BroadcastMessage deserialize(String xml) {
		return this.xmlParser.deserializeBroadcastMessage(xml);
	}
	
	public String toString() {
		return "{"+nameParam1+"="+param1+", "
				+ nameParam2+"="+param2+", "
				+ nameParam3+"="+param3+"}";
	}
}
