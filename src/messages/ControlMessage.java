package messages;

public class ControlMessage extends Message{
	//Atributos
	private ControlMessageType command;
	private int dstPort;
	
	//Constructor
	public ControlMessage(ControlMessageType c) {
		this.command = c;
	}
	
	public ControlMessage(ControlMessageType c, int port) {
		this.command = c;
		this.dstPort = port;
	}
	
	//Getters & Setters
	public ControlMessageType getCommand() {
		return this.command;
	}
	
	protected String getCommandString() {
		return command.toString();
	}
	
	public byte[] getBytes() {
		return toString().getBytes();
	}
	
	public int getDstPort() {
		return dstPort;
	}
	
	//Funcionalidad
	public String toString() {
		return command.toString();
	}
}
