package messages;

public class SetTimeRefreshMessage extends ControlMessage{
	//Atributos
	private int time;
	
	//Constructor
	public SetTimeRefreshMessage(ControlMessageType c, int time, int port) {
		super(c, port);
		this.time = time;
	}
	
	//Getters & Setters
	public int getTime() {
		return time;
	}
	
	//Funcionalidad
	public String toString() {
		return getCommandString()+"_"+time;
	}
}
