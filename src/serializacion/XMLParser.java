package serializacion;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import messages.BroadcastMessage;
import servidores.ServidorCalidadAire;
import servidores.ServidorClima;
import servidores.ServidorMeteorologia;

import java.io.*;

public class XMLParser {
	
	public XMLParser() {
		
	}
	
	public String serializeBroadcastMessage(String id, String np1, String p1, String np2, String p2, String np3, String p3) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.err.println("Error inicializando la serializacion XML");
			return null;
		}
		Document doc = dBuilder.newDocument();
		
		//Elemento raíz
		Element rootElement = doc.createElement("BroadcastMessage");
		doc.appendChild(rootElement);
		
		// Declaramos XSI
		rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		
		//Añadimos atributos: XSD
		Attr attr = doc.createAttribute("xsi:noNamespaceSchemaLocation");
		attr.setValue("BroadcastMessage.xsd");
		rootElement.setAttributeNode(attr);
		
		// Elemento id
		Element idE = doc.createElement("ID");
		idE.appendChild(doc.createTextNode(id));
		rootElement.appendChild(idE);
		
		//Parámetro 1
		Element param1 = doc.createElement(np1);
		param1.appendChild(doc.createTextNode(p1));
		rootElement.appendChild(param1);
	
		//Parámetro 2
		Element param2 = doc.createElement(np2);
		param2.appendChild(doc.createTextNode(p2));
		rootElement.appendChild(param2);		
	
		//Parámetro 3
		Element param3 = doc.createElement(np3);
		param3.appendChild(doc.createTextNode(p3));
		rootElement.appendChild(param3);

		// Convert to string
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			return writer.getBuffer().toString();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error serializando XML");
			return null;
		}
	
	}
	
//	public Message deserializeBroadcastMessage TODO general para XML y JSON
	
	public BroadcastMessage deserializeBroadcastMessage(String xml) {
		// Leemos el documento XML
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.err.println("Error extrayendo XML");
			return null;
		}
		
		Document doc;
		try {
			doc = dBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error parseando XML");
			return null;
		}
		doc.getDocumentElement().normalize();

		// Extract root
		Element root = doc.getDocumentElement();
		
		//Comprobamos el tipo de datos que estamos recibiendo y deserializamos en consecuencia
		if(root.getElementsByTagName(ServidorClima.PRES_ATMOS).item(0) != null) {
			return deserializeServidorClima(xml, root);
		}else if(root.getElementsByTagName(ServidorMeteorologia.LLOVIENDO).item(0) != null) {
			return deserializeServidorMeteorologia(xml, root);
		}else {
			return deserializeServidorCalidadAire(xml, root);
		}
	}
	
	private BroadcastMessage deserializeServidorClima(String xml, Element root) {
		//Validamos el documento XML
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        Schema schema = factory.newSchema(new StreamSource(new File("src/serializacion/BroadcastMessageServidorClima.xsd"))); // Load your XSD file
	        Validator validator = schema.newValidator();
	        validator.validate(new StreamSource(new StringReader(xml)));
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("El documento XML no cumple el formato del XSD");
			return null;
		}
		// El documento es válido
		
		int id = Integer.valueOf(root.getElementsByTagName("ID").item(0).getTextContent());
		String presAtmos = root.getElementsByTagName(ServidorClima.PRES_ATMOS).item(0).getTextContent();
		String temp = root.getElementsByTagName(ServidorClima.TEMP).item(0).getTextContent();
		String hum = root.getElementsByTagName(ServidorClima.HUMEDAD).item(0).getTextContent();
		return new BroadcastMessage(id, ServidorClima.PRES_ATMOS, presAtmos, ServidorClima.TEMP, temp, ServidorClima.HUMEDAD, hum);
	}
	
	private BroadcastMessage deserializeServidorMeteorologia(String xml, Element root) {
		//Validamos el documento XML
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        Schema schema = factory.newSchema(new StreamSource(new File("src/serializacion/BroadcastMessageServidorClima.xsd"))); // Load your XSD file
	        Validator validator = schema.newValidator();
	        validator.validate(new StreamSource(new StringReader(xml)));
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("El documento XML no cumple el formato del XSD");
			return null;
		}
		int id = Integer.valueOf(root.getElementsByTagName("ID").item(0).getTextContent());
		String lloviendo = root.getElementsByTagName(ServidorMeteorologia.LLOVIENDO).item(0).getTextContent();
		String velViento = root.getElementsByTagName(ServidorMeteorologia.VEL_VIENTO).item(0).getTextContent();
		String radiacion = root.getElementsByTagName(ServidorMeteorologia.RADIACION).item(0).getTextContent();
		return new BroadcastMessage(id, ServidorMeteorologia.LLOVIENDO, lloviendo, ServidorMeteorologia.VEL_VIENTO, velViento, ServidorMeteorologia.RADIACION, radiacion);
	}
	
	private BroadcastMessage deserializeServidorCalidadAire(String xml, Element root) {
		//Validamos el documento XML
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        Schema schema = factory.newSchema(new StreamSource(new File("src/serializacion/BroadcastMessageServidorClima.xsd"))); // Load your XSD file
	        Validator validator = schema.newValidator();
	        validator.validate(new StreamSource(new StringReader(xml)));
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("El documento XML no cumple el formato del XSD");
			return null;
		}
		
		int id = Integer.valueOf(root.getElementsByTagName("ID").item(0).getTextContent());
		String conCO2 = root.getElementsByTagName(ServidorCalidadAire.CON_CO2).item(0).getTextContent();
		String conO3 = root.getElementsByTagName(ServidorCalidadAire.CON_O3).item(0).getTextContent();
		String conPartSusp = root.getElementsByTagName(ServidorCalidadAire.CON_PART_SUSP).item(0).getTextContent();
		return new BroadcastMessage(id, ServidorCalidadAire.CON_CO2, conCO2, ServidorCalidadAire.CON_O3, conO3, ServidorCalidadAire.CON_PART_SUSP, conPartSusp);
	}
}
