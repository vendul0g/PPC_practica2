package messages;

import serializacion.JSONParser;
import serializacion.XMLSetModeMessageParser;
import serializacion.XMLSetRefreshParser;

public class SetModeMessage extends ControlMessage{
	//Atributos
	int mode;
	
	//Constructor
	public SetModeMessage(ControlMessageType t, String m) {
		super(t);
		setMode(m);
	}
	
	public SetModeMessage(ControlMessageType t, int idServer, int m) {
		super(t, idServer);
	}
	
	public SetModeMessage(ControlMessageType t, int idServer, String m) {
		super(t, idServer);
		setMode(m);
	}
	
	//Getters & Setters
	public int getMode() {
		return mode;
	}
	
	//Funcionalidad
	private void setMode(String m) {
		switch(m) {
		case "xml":
			this.mode = Message.MODE_XML;
			break;
		case "json":
			this.mode = Message.MODE_JSON;
			break;
		case "celsius":
			this.mode = Message.MODE_CELSIUS;
			break;
		case "farenheit":
			this.mode = Message.MODE_FARENHEIT;
			break;		
		default:
			this.mode = -1;
		}
	}
	
	public byte[] serialize(int mode){
		return mode == Message.MODE_JSON 
				? JSONParser.serialize(this).getBytes()
				: new XMLSetModeMessageParser(this).serialize().getBytes();
	}
	
}
