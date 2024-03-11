import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class JRoundTextField  extends JTextField {

    private int borderRadius = 10;

    public JRoundTextField() {
        super();
        setOpaque(false); // Rendi il background trasparente per visualizzare lo sfondo personalizzato
        setBorder(new EmptyBorder(5, 10, 5, 10)); // Aggiungi un margine interno per separare il testo dai bordi
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Disegna il background arrotondato
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius));

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Non disegnare il bordo predefinito
    }

    @Override
    public Dimension getPreferredSize() {
        // Aggiungi un padding per garantire che il testo non sia troppo vicino ai bordi arrotondati
        return new Dimension(super.getPreferredSize().width + 20, super.getPreferredSize().height);
    }

    @Override
    public Insets getInsets() {
        // Aggiungi un margine interno per compensare l'arrotondamento dei bordi
        return new Insets(5, 10, 5, 10);
    }
}