# Práctica 2 PPC
Consiste en montar una serie de servidores que tienen instalados unos sensores. Estos sensores estarán mandando contínuamente mensajes broadcast a la red.

Por otro lado tenemos clientes, que reciben los mensajes broadcast, y además implementan una funcionalidad para controlar de manera remota a los servidores; como para cambiar sus parámetros y eso.

---
# Arquitectura
Vamos a tener una arquitectura con clientes y servidores

	.  ·-----·
	.  | C1  |---·
	.  ·-----·   |    ·----·
	.  ·-----·   |----| S1 |
	.  | C2  |---|    ·----·
	.  ·-----·   |    ·----·
	.            |----| S2 |
	.            |    ·----·
	.            |    ·----·
	.            ·----| S3 |
	.                 ·----·


Tendremos servidores que registran información temporal cada cierto tiempo
# Métodos de comunicación
- Multicast (no es recomendable)
- Broadcast: mediante paquetes UDP

# Funcionamiento
## Servidor
Cada servidor genera unos valores aleatorios y los va mandando. Por ejemplo, si los genera cada segundo, va a mandar un broadcast cada segundo.

Estos valores aleatorios tienen que representar distintos valores como temperatura, radiación, velocidad del viento, etc.
Cada servidor tendrá unos valores distintos

<span style="color: lime;">Para la implementación</span>, tendremos 2 hilos funcionando
- El primer hilo estará generando los valores y mandándolos por broadcast
- El otro escuchará comunicaciones por el puerto para el cambio de configuración
## Cliente
Va a estar escuchando en un puerto concreto las comunicaciones broadcast.
<span style="color: lime;">OJO</span>. Que los clientes estarán escuchando en el mismo puerto ==> habrá que <span style="color: aqua;">usar el socket reuse address</span>.

El cliente también puede mandar <span style="color: aqua;">mensajes de control</span>, para controlar la configuración de cada servidor. Para ello <span style="color: orange;">usaremos mensajes unicast</span> a un determinado servidor, indicando el parámetro a modificar y su valor.
<span style="color: lime;">Este mensaje unicast se manda a un puerto concreto</span>, como el 9001 (para el servidor 1) ==> <span style="color: aqua;">todos los servidores estarán escuchando en un puerto distinto para recibir instrucciones de configuración</span>: mapa

|  nombre servidor | puerto donde escucha mensajes de control | 
|---|---|
| servidor 1 | 9001 |
| servidor 2 | 9002 |
| servidor 3 | 9003 | 
| ... | ... |

# Formatos de las tramas
Puesto que todos los servidores mandan al mismo puerto broadcast, solo puede identificar servidores por el mensaje

El cliente tendrá una tabla (Map) donde asociará <span style="color: lime;">nombre de servidor con su respectiva IP y puerto</span>. OJO que <span style="color: aqua;">cada servidor tiene su identificador (int)</span>

Formato del mensaje:

| idServidor | temperatura | humedad relativa | radiación |
|---|---|---|---|

Este mensje:
1. <span style="color: aqua;">Se codificará con espacios para entenderlo</span>
2. <span style="color: yellow;">Implementaremos el envío de estos mensajes con XML o JSON</span>.

# Opcional
- Opcional hacer 2 clientes <-- solo es obligatorio hacer 1
- Implementar los ACKs de los mensajes de configuración <-- obligatoriamente no hay que hacer nada de ACKs
	- Habrá que controlar también reenvíos, timeout, esperas, etc
	- Habrá que abrir también otro puerto abierto en el cliente. Mandar y recibir mensajes de control por el mismo puerto ==> para cada cliente un puerto distinto

# Serialización
Ahora mismo tenemos la serialización en Strings, pero <span style="color: lime;">esto no es eficiente</span>.
Vamos a necesitar un <span style="color: yellow;">parser</span> para serializar o deserializar en XML o JSON

Podemos implementar ambas cosas dentro del propio servidor o fuera.
<span style="color: yellow;">Tenemos que implementar ambos métodos de des/serializacion</span>.
## Mensajes
Ahora los mensajes (broadcast que llegan al cliente) serán como

| nombreServidor | puertoControl | X/J | parámetro | valor | 
|---|---|---|---|---|
- X/J: Si es X será que tenemos que deserializar XML y si es J tenemos que deserializar JSON

Para XML tenemos que definir un DTD --> y con <span style="color: yellow;">este DTD tenemos que validar el formato</span>. <span style="color: lime;">( en la deserialización)</span>.
==> En el cliente, <span style="color: aqua;">cada mensaje que nos llegue lo meteremos en un archivo</span>. <span style="color: yellow;">Tenemos que almacenar para todos los roles, TODOS LOS MENSAJES QUE RECIBIMOS</span> guardando en un fichero.

# Objetivo semana1
- Arquitectura de clientes y servidores montada
- Planteada la parte de los hilos
- Envío de las tramas broadcast
- (Si funciona el broadcast) Plantear tramas de control --> Mandar el mensaje de control sin procesamiento

# Objetivo semana 2
Para la semana que viene vamos a serializar, así que tendremos que tener hecho ya el protocolo de transporte
- Difusión (broadcast)
- Mensajes de control

# Objetivo semana 3
- Serializar y deserializar tanto XML como JSON

# Objetivo Semana 4
Serialización con JSON. Copiando el ejemplo que nos dio --> Utilizando GSON.
```
Stirng serializarJSON(){}
	GSon gon = new GSon()
	String json = gson.toJson(this)
	return json
}
```

Deserializar JSON.
```
void deserializar(String json){
	GSon gson = new GSon()
	Servidor s = gson.fromJson(json, Servidor.class)
	temp = s.temp
}
```

# Notas
- Hacer un procesador de informacion: ProcesarDatos, Estadísticas o algo así
  Este señor va a mostrar por pantalla la media de los valores que se están anunciando cada 10 segundos o algo así


Vamos a ver. Yo tengo un cliente Que va a usar 2 tipos de hilos, manejar mensajes de control y escuchar broadcast
Eso como tengo pensado implementarlo es: un LanzadorCliente va a crear el cliente, ejecuta su funcuión run() y entonces es cuando el cliente crea 2 hilos, el de control y el listener, y cada uno que tenga que hacer sus cosas
Vale. Otra idea que tengo es que, el cliente cuando introduzca los parámetros de control también deberá haber un tipo de mensaje para ver la estadística de los valores recibidos de los servidores.

También, para la imiplementación de los mensajes de control, estaría bien almacenar el id del cliente y los logs de modificación.

# Serialización y Deserialización
## Broadcast Message Serialization
```java
// Creating the XML for BroadcastMessage 
Document doc = dBuilder.newDocument(); 
Element rootElement = doc.createElement("BroadcastMessage"); doc.appendChild(rootElement);  
Element id = doc.createElement("ID"); id.appendChild(doc.createTextNode(String.valueOf(msg.getId()))); rootElement.appendChild(id);  
Element param1 = doc.createElement("Param1"); param1.appendChild(doc.createTextNode(msg.getParam1())); rootElement.appendChild(param1);  
// ... similarly for Param2 and Param3

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
```

## Broadcast Message Deserialization
```java
//Validamos el documento XML
try {
	SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	Schema schema = factory.newSchema(new StreamSource(new File("src/serializacion/BroadcastMessage.xsd"))); // Load your XSD file
	Validator validator = schema.newValidator();
	validator.validate(new StreamSource(new StringReader(xml)));
}catch (Exception e) {
	e.printStackTrace();
	System.err.println("El documento XML no cumple el formato del XSD");
	return null;
}
// El documento es válido

// Parsing the XML for BroadcastMessage 
Document doc = dBuilder.parse(new ByteArrayInputStream(xmlString.getBytes())); doc.getDocumentElement().normalize();  
Element root = doc.getDocumentElement(); 
String idValue = root.getElementsByTagName("ID").item(0).getTextContent(); String param1Value = root.getElementsByTagName("Param1").item(0).getTextContent();
// ... similarly for Param2 and Param3
```

## Control Message Serialization

```java
// Assuming ControlMessage has methods getCommandString(), getDstPort(), and getTime() 
Document doc = dBuilder.newDocument(); 
Element rootElement = doc.createElement("ControlMessage"); doc.appendChild(rootElement);  
Element command = doc.createElement("Command"); command.appendChild(doc.createTextNode(controlMessage.getCommandString())); rootElement.appendChild(command);  
// ... similarly for DstPort and Time

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
```

### Control Message Deserialization
```java
//Validamos el documento XML
try {
	SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	Schema schema = factory.newSchema(new StreamSource(new File("src/serializacion/BroadcastMessage.xsd"))); // Load your XSD file
	Validator validator = schema.newValidator();
	validator.validate(new StreamSource(new StringReader(xml)));
}catch (Exception e) {
	e.printStackTrace();
	System.err.println("El documento XML no cumple el formato del XSD");
	return null;
}
// El documento es válido

// Parsing the XML for ControlMessage 
Document doc = dBuilder.parse(new ByteArrayInputStream(xmlString.getBytes())); doc.getDocumentElement().normalize();  
Element root = doc.getDocumentElement(); 
String commandValue = root.getElementsByTagName("Command").item(0).getTextContent(); 
// ... similarly for DstPort and Time
```
