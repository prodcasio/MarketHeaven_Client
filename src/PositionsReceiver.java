import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PositionsReceiver {

    public static void receivePositions() {
        DatagramSocket socket = null;
        try {
            // Crea un DatagramSocket sulla porta 7788
            socket = new DatagramSocket();

            // Indirizzo IP e porta del server
            InetAddress address = InetAddress.getLocalHost();
            int port = 7788;

            // Invia una richiesta al server
            sendRequest(socket, address, port);

            // Ricevi la risposta dal server
            String jsonData = receiveData(socket);

            // Converte il JSON ricevuto in XML
            String xmlData = convertJSONtoXML(jsonData);

            // Salva i dati XML nel file positions.xml
            saveXMLToFile(xmlData, "positions.xml");

            Popup.createPopup("Dati ricevuti e salvati correttamente nel file positions.xml.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    private static void sendRequest(DatagramSocket socket, InetAddress address, int port) throws Exception {
        // Crea il messaggio da inviare al server
        String message = "a";
        byte[] sendData = message.getBytes();

        // Crea un pacchetto Datagram da inviare
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);

        // Invia il pacchetto al server
        socket.send(sendPacket);
    }

    private static String receiveData(DatagramSocket socket) throws Exception {
        // Prepara il buffer per ricevere i dati
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        // Ricevi la risposta dal server
        socket.receive(receivePacket);

        // Estrai i dati ricevuti e convertili in una stringa
        return new String(receivePacket.getData(), 0, receivePacket.getLength());
    }

    private static String convertJSONtoXML(String jsonData) {
        JSONObject json = new JSONObject(jsonData);
        return XML.toString(json);
    }

    private static void saveXMLToFile(String xmlData, String fileName) throws Exception {
        // Converti la stringa XML in un oggetto Document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlData)));

        // Crea un oggetto Transformer per formattare l'output
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        // Scrivi il Document nel file
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fileName));
        transformer.transform(source, result);
    }
}
