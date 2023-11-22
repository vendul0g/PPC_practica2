package messages;

public enum ControlMessageType {
	//Enumerado
	SET_TIME_REFRESH, GET_STATISTICS, INVALID, ACK, HELP, VERBOSE, NOT_VERBOSE,
	CONTROL_MODE, BROADCAST_MODE, CHANGE_UNIT, DISABLE, ENABLE;

	//Funcionalidad
	public static ControlMessage getType(String message) {
		String line[] = message.split(" ");
		ControlMessage m; 
		int p, v;
		String mode;
		
		switch(line[0]) {
			case "setrefresh":
				if (line.length != 3)
					return new ControlMessage(INVALID);
				
				try {
					p = Integer.valueOf(line[1]);
					v = Integer.valueOf(line[2]);
					
					m = new SetRefreshMessage(SET_TIME_REFRESH, p, v);
					return m;

				}catch(NumberFormatException e) {
					return new ControlMessage(INVALID);
				}
			
			case "help":
				return new ControlMessage(HELP);
			
			case "verbose":
				return new ControlMessage(VERBOSE);
				
			case "notverbose":
				return new ControlMessage(NOT_VERBOSE);
				
			case "controlmode":
				if(line.length != 2)
					return new ControlMessage(INVALID);
				mode = line[1];
				if(!mode.equals("xml") && !mode.equals("json"))
					return new ControlMessage(INVALID);
				return new SetModeMessage(CONTROL_MODE, mode);
			
			case "broadcastmode":
				if(line.length != 3)
					return new ControlMessage(INVALID);
				p = Integer.valueOf(line[1]);
				mode = line[2];
				if(!mode.equals("xml") && !mode.equals("json"))
					return new ControlMessage(INVALID);
				return new SetModeMessage(BROADCAST_MODE, p, mode);
				
			case "changeunit":
				if(line.length != 3)
					return new ControlMessage(INVALID);
				p = Integer.valueOf(line[1]);
				mode = line[2];
				if(!mode.equals("celsius") && !mode.equals("farenheit"))
					return new ControlMessage(INVALID);
				return new SetModeMessage(CHANGE_UNIT, p, mode);
			
			case "disable":
				if(line.length != 2)
					return new ControlMessage(INVALID);
				p = Integer.valueOf(line[1]);
				return new ControlMessage(DISABLE, p);
			
			case "enable":
				if(line.length != 2)
					return new ControlMessage(INVALID);
				p = Integer.valueOf(line[1]);
				return new ControlMessage(ENABLE, p);
				
			default:
				return new ControlMessage(INVALID);
		}
	}
}
