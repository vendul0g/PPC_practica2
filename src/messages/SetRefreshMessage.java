package messages;

import serializacion.JSONParser;
import serializacion.XMLSetRefreshParser;

public class SetRefreshMessage extends ControlMessage{
	//Atributos
	private int time;
	
	//Constructor
	public SetRefreshMessage(ControlMessageType c, int port, int time) {
		super(c, port);
		this.time = time;
	}
	
	public SetRefreshMessage(ControlMessageType c, int time) {
		super(c);
		this.time = time;
	}
	
	//Getters & Setters
	public int getTime() {
		return time;
	}
	
	//Funcionalidad
	public String toString() {
		return getCommandString()+" "+getIdServer()+" "+time;
	}
	
	public byte[] serialize(int mode) {
		return mode == Message.MODE_JSON 
				? JSONParser.serialize(this).getBytes() 
				: new XMLSetRefreshParser(this).serialize().getBytes(); 
	}
}