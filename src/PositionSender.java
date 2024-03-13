import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
            // Crea un DatagramSocket
            Socket socket = new Socket("127.0.0.1", 6677);

            // Converti la stringa JSON in un array di byte
            String richiesta = json.toString();

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(richiesta);
            dos.flush();
            dos.close();

            socket.close();
            Popup.createPopup("Posizione aperta correttamente.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
