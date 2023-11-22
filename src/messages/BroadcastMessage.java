package messages;

import serializacion.JSONParser;
import serializacion.XMLBroadcastMessageParser;

public class BroadcastMessage extends Message{
	//Atributos
	private int id;
	private String nameParam1;
	private String param1; 
	private String nameParam2;
	private String param2;
	private String nameParam3;
	private String param3;
	
	//Constructor
	public BroadcastMessage() {
	}
	
	public BroadcastMessage(int id, String np1, String p1, String np2, String p2, String np3, String p3) {
		this.id = id;
		this.nameParam1 = np1;
		this.param1 = p1;
		this.nameParam2 = np2;
		this.param2 = p2;
		this.nameParam3 = np3;
		this.param3 = p3;
	}
	
	//Getters & Setters
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public String getNameParam1() {
		return nameParam1;
	}

	public String getParam1() {
		return param1;
	}

	public String getNameParam2() {
		return nameParam2;
	}

	public String getParam2() {
		return param2;
	}

	public String getNameParam3() {
		return nameParam3;
	}

	public String getParam3() {
		return param3;
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
	public BroadcastMessage deserialize(String msg) {
		if(msg.startsWith("{")) return deserializeJSON(msg);
		if(msg.startsWith("<")) return deserializeXML(msg);
		return null;
	}
	
	public String serializeXML() {
		return new XMLBroadcastMessageParser().serialize(this);
	}
	
	private BroadcastMessage deserializeXML(String xml) {
		return new XMLBroadcastMessageParser().deserialize(xml);
	}
	
	public String serializeJSON() {
		return JSONParser.serialize(this);
	}
	
	private BroadcastMessage deserializeJSON(String json) {
		return JSONParser.deserialize(json, BroadcastMessage.class);
	}
	
	public String toString() {
		return "{"+nameParam1+"="+param1+", "
				+ nameParam2+"="+param2+", "
				+ nameParam3+"="+param3+"}";
	}	
}
