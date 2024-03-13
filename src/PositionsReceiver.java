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
import java.io.DataInputStream;
import java.io.File;
import java.io.StringReader;
import java.net.Socket;

public class PositionsReceiver {

    public static void receivePositions() {
        try {
            // Crea un DatagramSocket sulla porta 7788
            Socket socket = new Socket("127.0.0.1", 7788);

            // Estrai i dati dal pacchetto
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String jsonData = dis.readUTF();

            // Converte il JSON ricevuto in XML
            String xmlData = convertJSONtoXML(jsonData);

            // Salva i dati XML nel file positions.xml
            saveXMLToFile(xmlData, "positions.xml");

            Popup.createPopup("Dati ricevuti e salvati correttamente nel file positions.xml.");

            dis.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
