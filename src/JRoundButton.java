import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

// Estende JButton per creare un pulsante rotondo personalizzato
public class JRoundButton extends JButton {
    // Costanti per l'ombra del pulsante
    private static final int SHADOW_SIZE = 5;
    private static final int SHADOW_ALPHA = 30;

    // Colori del pulsante e del testo
    private Color buttonColor;
    private Color textColor;

    // Colori di destinazione per la transizione fluida
    private Color targetColorBackground;
    private Color targetColorText;

    // Timer per gestire la transizione dei colori
    private Timer timer;

    // Costruttore
    public JRoundButton(String text, Color buttonColor, Color textColor) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        this.textColor = textColor;
        this.targetColorText = textColor;
        this.buttonColor = buttonColor;
        this.targetColorBackground = buttonColor;

        // Imposta il margine per il pulsante
        setMargin(new Insets(10, 30, 10, 30)); // Regola i valori degli insetti secondo necessità
    }

    // Metodo per cambiare i colori in modo fluido
    public void changeColorsSmooth(Color targetBackgroundColor, Color targetTextColor) {
        this.targetColorBackground = targetBackgroundColor;
        this.targetColorText = targetTextColor;

        // Interrompe il timer se è già in esecuzione
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        // Crea e avvia il timer per la transizione dei colori
        timer = new Timer(10, new ActionListener() {
            float increment = 0.20f;

            @Override
            public void actionPerformed(ActionEvent e) {
                buttonColor = calculateNextColor(buttonColor, targetColorBackground, increment);
                textColor = calculateNextColor(textColor, targetColorText, increment);
                repaint();

                // Interrompe il timer quando i colori raggiungono la destinazione
                if (buttonColor.equals(targetBackgroundColor) && textColor.equals(targetTextColor)) {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    // Metodo privato per calcolare il prossimo colore nella transizione
    private Color calculateNextColor(Color currentColor, Color targetColor, float increment) {
        int r = (int) (currentColor.getRed() + (targetColor.getRed() - currentColor.getRed()) * increment);
        int g = (int) (currentColor.getGreen() + (targetColor.getGreen() - currentColor.getGreen()) * increment);
        int b = (int) (currentColor.getBlue() + (targetColor.getBlue() - currentColor.getBlue()) * increment);
        return new Color(r, g, b);
    }

    // Sovrascrive il metodo paintComponent per disegnare il pulsante personalizzato
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Disegna l'ombra sotto il pulsante
        for (int i = 0; i < SHADOW_SIZE; i++) {
            int alpha = SHADOW_ALPHA / (SHADOW_SIZE - i);
            Color shadowColor = new Color(buttonColor.getRed(), buttonColor.getGreen(), buttonColor.getBlue(), alpha);
            g2.setColor(shadowColor);
            Shape shadowShape = new RoundRectangle2D.Float(i, i, getWidth() - i * 2, getHeight() - i * 2, 10, 10);
            g2.fill(shadowShape);
        }

        // Disegna il pulsante
        Shape shape = new RoundRectangle2D.Float(SHADOW_SIZE, SHADOW_SIZE, getWidth() - SHADOW_SIZE * 2, getHeight() - SHADOW_SIZE * 2, 10, 10);
        g2.setColor(buttonColor);
        g2.fill(shape);
        g2.setColor(textColor);

        // Centra il testo nel pulsante
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(getText())) / 2;
        int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(getText(), textX, textY);
        g2.dispose();
    }

    // Sovrascrive il metodo paintBorder per non disegnare il bordo del pulsante
    @Override
    protected void paintBorder(Graphics g) {
        // Non disegna il bordo
    }
}
