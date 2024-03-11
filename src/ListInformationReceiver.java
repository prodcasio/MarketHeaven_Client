import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ListInformationReceiver extends Thread{
    JSONArray assets;
    ListInformationReceiver(){
        assets = new JSONArray();
    }
    @Override
    public void run(){
        try {
            GraphUpdater gu = new GraphUpdater();
            gu.start();

            DatagramSocket socket = new DatagramSocket(5566); // Crea un socket Datagram in ascolto sulla porta specificata
            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket); // Riceve il pacchetto dal client

                String asset = new String(receivePacket.getData(), 0, receivePacket.getLength()); // Estrae l'asset dal pacchetto ricevuto

                // Creare un oggetto JSON per l'asset e aggiungerlo al JSONArray
                JSONObject assetObject = new JSONObject(asset);
                boolean existing = false;
                int indexExisting = 0;
                for(int i=0; i<assets.length(); i++){
                    if(assets.getJSONObject(i).getString("name").equals(assetObject.getString("name"))){
                        existing = true;
                        indexExisting = i;
                        break;
                    }
                }
                String name = assetObject.getString("name");
                float change = assetObject.getFloat("change");
                float value = assetObject.getFloat("value");
                float spread = assetObject.getFloat("spread");
                if(existing){
                    assets.getJSONObject(indexExisting).put("change", change);
                    assets.getJSONObject(indexExisting).put("value", value);
                    assets.getJSONObject(indexExisting).put("spread", spread);
                    JPanel oldAssetPanel = (JPanel) Main.listaAssetPanel.getComponent(indexExisting);

                    Color textColor = new Color(72, 143, 255);
                    if (change < 0) textColor = new Color(255, 86, 86);

                    JLabel oldAssetValue = (JLabel) oldAssetPanel.getComponent(1);
                    oldAssetValue.setText(String.valueOf(value));
                    oldAssetValue.setForeground(textColor);

                    JLabel oldAssetSpread = (JLabel) oldAssetPanel.getComponent(2);
                    oldAssetSpread.setText(String.valueOf(spread));
                    oldAssetSpread.setForeground(textColor);

                    JLabel oldAssetChange = (JLabel) oldAssetPanel.getComponent(3);
                    oldAssetChange.setText(change + "%");
                    oldAssetChange.setForeground(textColor);
                } else{
                    Main.listaAssetPanel.add(new AssetPanel(name, value, change, spread), "wrap, w 100%");
                    assets.put(assetObject);
                    Main.frame.revalidate();
                    Main.frame.repaint();
                    Main.frame.setVisible(true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
