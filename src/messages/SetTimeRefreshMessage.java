package messages;

import serializacion.XMLSetRefreshParser;

public class SetTimeRefreshMessage extends ControlMessage{
	//Atributos
	private int time;
	
	//Constructor
	public SetTimeRefreshMessage(ControlMessageType c, int port, int time) {
		super(c, port);
		this.time = time;
	}
	
	public SetTimeRefreshMessage(ControlMessageType c, int time) {
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
	
	public byte[] serialize() {
		return new XMLSetRefreshParser(this).serialize().getBytes();
	}
}
