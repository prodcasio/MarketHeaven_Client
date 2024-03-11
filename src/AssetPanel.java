
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
        setLayout(new MigLayout("w 100%", "[50%][20%][15%][15%]"));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Main.graficoPanel.setVisible(true);
                Main.assetChosen = name;
                Main.grafico.setTitle(name);
                if(Main.series.getItemCount()>0)
                    Main.series.delete(0, Main.series.getItemCount());
            }
        });
        setBorder(BorderFactory.createLineBorder(new Color(84, 84, 84)));

        JLabel nomeAsset = new JLabel(name);
        nomeAsset.setForeground(javax.swing.UIManager.getDefaults().getColor("SystemColor.text"));
        add(nomeAsset);

        Font fontLeggero = nomeAsset.getFont();
        fontLeggero = fontLeggero.deriveFont(Collections.singletonMap(TextAttribute.WEIGHT, TextAttribute.WEIGHT_SEMIBOLD));

        Color textColor = new Color(72, 143, 255);

        if (change < 0) textColor = new Color(255, 86, 86);
        JLabel prezzoAsset = new JLabel(String.format("%.6f", value));
        prezzoAsset.setForeground(textColor);
        add(prezzoAsset);
        prezzoAsset.setFont(fontLeggero);

        JLabel spreadAsset = new JLabel(String.valueOf(spread));
        spreadAsset.setForeground(textColor);
        add(spreadAsset, "center");
        spreadAsset.setFont(fontLeggero);

        JLabel variazioneAsset = new JLabel(String.format("%.2f", change) + "%");
        variazioneAsset.setForeground(textColor);
        add(variazioneAsset);
        variazioneAsset.setFont(fontLeggero);
    }
}
