import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PositionSender {

    public static void sendPosition(boolean isBuy) {
        if (Main.assetChosen == null || Main.assetChosen.isEmpty()) {
            Popup.createPopup("Scegliere un asset.");
            return;
        }

        JSONObject json = new JSONObject();
        json.put("asset", Main.assetChosen);

        if(Main.quantitaInput.getText().isEmpty()){
            Popup.createPopup("Inserire la quantità di lotti.");
            return;
        }

        double quantity = Double.parseDouble(Main.quantitaInput.getText());
        json.put("lots", quantity);
        json.put("operation", isBuy ? "buy" : "sell");

        try {
            // Indirizzo IP e porta del destinatario
            InetAddress address = InetAddress.getLocalHost();
            int port = 6677;

            // Crea un DatagramSocket
            DatagramSocket socket = new DatagramSocket();

            // Converti la stringa JSON in un array di byte
            byte[] data = json.toString().getBytes();

            // Crea un pacchetto Datagram da inviare
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);

            // Invia il pacchetto
            socket.send(packet);

            Popup.createPopup("Posizione aperta correttamente.");

            // Chiudi il socket
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
