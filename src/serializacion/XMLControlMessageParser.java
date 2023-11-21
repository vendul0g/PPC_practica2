package serializacion;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import messages.ControlMessage;

public abstract class XMLControlMessageParser {
	//Atributos
	protected ControlMessage cm;
	protected Document doc;
	protected Element rootElement;
	
	//Constructor
	public XMLControlMessageParser() {
	
	}
	
	public XMLControlMessageParser(ControlMessage cm) {
		this.cm = cm;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.err.println("Error inicializando la serializacion XML");
			return;
		}
		this.doc = dBuilder.newDocument();
		
		//Elemento raíz
		this.rootElement = doc.createElement("ControlMessage");
		this.doc.appendChild(rootElement);
		
		//Añadimos el comando
		Element command = doc.createElement("Command");
		command.appendChild(doc.createTextNode(cm.getCommand().toString()));
		this.rootElement.appendChild(command);
		
		//Añadimos el id del servidor
		Element dstPort = doc.createElement("serverID");
		dstPort.appendChild(doc.createTextNode(String.valueOf(cm.getIdServer())));
		this.rootElement.appendChild(dstPort);
	}
	
	//Funcionalidad
	public abstract String serialize();
	
	public abstract ControlMessage deserialize(String xml);
}
