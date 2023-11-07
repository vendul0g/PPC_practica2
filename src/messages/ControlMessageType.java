package messages;

public enum ControlMessageType {
	//Enumerado
	SET_TIME_REFRESH, GET_STATISTICS, INVALID, ACK;

	//Constantes
	public static final String COMMAND_SET_TIME_REFRESH = "setrefresh";
	
	//Funcionalidad
	public static ControlMessage getType(String message) {
		//Ejemplo de mensaje:
		//command [arg]...
		String line[] = message.split(" ");
		ControlMessage m; 
		
		switch(line[0]) {
			case "setrefresh":
				if (line.length != 3)
					return new ControlMessage(INVALID);
				
				try {
					int v = Integer.valueOf(line[1]);
					int p = Integer.valueOf(line[2]);
					
					m = new SetTimeRefreshMessage(SET_TIME_REFRESH, v, p);
					return m;

				}catch(NumberFormatException e) {
					return new ControlMessage(INVALID);
				}
			
			default:
				return new ControlMessage(INVALID);
		}
	}
}
