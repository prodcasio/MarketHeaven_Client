// Import delle librerie necessarie
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

// Classe per la ricezione delle informazioni e l'aggiornamento della GUI
public class ListInformationReceiver extends Thread {
    // JSONArray per memorizzare gli asset
    JSONArray assets;

    // Costruttore della classe
    ListInformationReceiver() {
        assets = new JSONArray(); // Inizializza il JSONArray
    }

    // Metodo principale eseguito quando il thread viene avviato
    @Override
    public void run() {
        try {
            // Avvia il thread per l'aggiornamento del grafico
            GraphUpdater gu = new GraphUpdater();
            gu.start();

            // Crea un socket Datagram in ascolto sulla porta 5566
            DatagramSocket socket = new DatagramSocket(5566);

            // Loop infinito per ricevere pacchetti
            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket); // Riceve il pacchetto dal client

                // Estrae l'asset dal pacchetto ricevuto e lo converte in oggetto JSON
                String asset = new String(receivePacket.getData(), 0, receivePacket.getLength());
                JSONObject assetObject = new JSONObject(asset);

                // Controlla se l'asset esiste già nell'array
                boolean existing = false;
                int indexExisting = 0;
                for (int i = 0; i < assets.length(); i++) {
                    if (assets.getJSONObject(i).getString("name").equals(assetObject.getString("name"))) {
                        existing = true;
                        indexExisting = i;
                        break;
                    }
                }

                // Estrae i dati dell'asset
                String name = assetObject.getString("name");
                float change = assetObject.getFloat("change");
                float value = assetObject.getFloat("value");
                float spread = assetObject.getFloat("spread");

                // Se l'asset esiste già, aggiorna i dati e la GUI
                if (existing) {
                    assets.getJSONObject(indexExisting).put("change", change);
                    assets.getJSONObject(indexExisting).put("value", value);
                    assets.getJSONObject(indexExisting).put("spread", spread);

                    // Ottiene il pannello dell'asset esistente
                    JPanel oldAssetPanel = (JPanel) Main.listaAssetPanel.getComponent(indexExisting);

                    // Imposta il colore del testo in base alla variazione
                    Color textColor = new Color(72, 143, 255);
                    if (change < 0) textColor = new Color(255, 86, 86);

                    // Aggiorna i componenti del pannello dell'asset
                    JLabel oldAssetValue = (JLabel) oldAssetPanel.getComponent(1);
                    oldAssetValue.setText(String.valueOf(value));
                    oldAssetValue.setForeground(textColor);

                    JLabel oldAssetSpread = (JLabel) oldAssetPanel.getComponent(2);
                    oldAssetSpread.setText(String.valueOf(spread));
                    oldAssetSpread.setForeground(textColor);

                    JLabel oldAssetChange = (JLabel) oldAssetPanel.getComponent(3);
                    oldAssetChange.setText(change + "%");
                    oldAssetChange.setForeground(textColor);
                } else { // Se l'asset non esiste, aggiunge un nuovo pannello all'interfaccia
                    Main.listaAssetPanel.add(new AssetPanel(name, value, change, spread), "wrap, w 100%");
                    assets.put(assetObject); // Aggiunge l'asset al JSONArray
                    Main.frame.revalidate(); // Aggiorna il layout della finestra principale
                    Main.frame.repaint(); // Ridisegna la finestra principale
                    Main.frame.setVisible(true); // Rende la finestra principale visibile
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
