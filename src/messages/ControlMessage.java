package messages;

import serializacion.XMLControlMessageParser;
import serializacion.XMLSetRefreshParser;

public class ControlMessage extends Message{
	//Atributos
	private ControlMessageType command;
	private int idServer;
	
	//Constructor
	public ControlMessage(ControlMessageType c) {
		this.command = c;
	}
	
	public ControlMessage(ControlMessageType c, int port) {
		this.command = c;
		this.idServer = port;
	}
	
	//Getters & Setters
	public ControlMessageType getCommand() {
		return this.command;
	}
	
	protected String getCommandString() {
		return command.toString();
	}
	
//	public byte[] getBytes() {
////		return toString().getBytes();
//		XMLControlMessageParser cm = new XMLSetRefreshParser();
//	}
	
	public int getIdServer() {
		return idServer;
	}
	
	//Funcionalidad
//	public String toString() {
//		return command.toString();
//	}
}
