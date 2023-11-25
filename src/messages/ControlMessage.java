package messages;

import serializacion.JSONParser;
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
	
	public int getIdServer() {
		return idServer;
	}
	
	//Funcionalidad
	public byte[] serialize(int mode){
		return mode == Message.MODE_JSON 
				? (JSON_HEADER +JSONParser.serialize(this)).getBytes()
				: (XML_HEADER + new XMLControlMessageParser(this).serialize()).getBytes();
	}
}
