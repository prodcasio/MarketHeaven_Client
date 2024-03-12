import net.miginfocom.swing.MigLayout;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Locale;

public class AssetPanel extends JPanel{
    AssetPanel(String name, float value, float change, float spread){
        // Imposta il layout del pannello usando MigLayout
        setLayout(new MigLayout("w 100%", "[50%][20%][15%][15%]"));
        // Imposta il cursore del mouse come mano
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Aggiunge un listener per gestire il clic del mouse
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Quando viene cliccato, mostra il pannello del grafico e imposta il nome dell'asset scelto
                Main.graficoPanel.setVisible(true);
                Main.assetChosen = name;
                Main.grafico.setTitle(name);
                // Cancella eventuali dati precedenti nel grafico
                if(Main.series.getItemCount()>0)
                    Main.series.delete(0, Main.series.getItemCount());
            }
        });
        // Imposta il bordo del pannello
        setBorder(BorderFactory.createLineBorder(new Color(84, 84, 84)));

        // Crea un'etichetta per il nome dell'asset e imposta il colore del testo
        JLabel nomeAsset = new JLabel(name);
        nomeAsset.setForeground(javax.swing.UIManager.getDefaults().getColor("SystemColor.text"));
        add(nomeAsset);

        // Crea un font semibold per il nome dell'asset
        Font fontLeggero = nomeAsset.getFont();
        fontLeggero = fontLeggero.deriveFont(Collections.singletonMap(TextAttribute.WEIGHT, TextAttribute.WEIGHT_SEMIBOLD));

        // Imposta il colore del testo in base alla variazione del prezzo
        Color textColor = new Color(72, 143, 255);
        if (change < 0) textColor = new Color(255, 86, 86);

        // Crea un'etichetta per il prezzo dell'asset, imposta il colore del testo e il font
        JLabel prezzoAsset = new JLabel(String.format("%.6f", value));
        prezzoAsset.setForeground(textColor);
        add(prezzoAsset);
        prezzoAsset.setFont(fontLeggero);

        // Crea un'etichetta per lo spread dell'asset, imposta il colore del testo e il font
        JLabel spreadAsset = new JLabel(String.valueOf(spread));
        spreadAsset.setForeground(textColor);
        add(spreadAsset, "center");
        spreadAsset.setFont(fontLeggero);

        // Crea un'etichetta per la variazione percentuale dell'asset, imposta il colore del testo e il font
        JLabel variazioneAsset = new JLabel(String.format("%.2f", change) + "%");
        variazioneAsset.setForeground(textColor);
        add(variazioneAsset);
        variazioneAsset.setFont(fontLeggero);
    }
}
