import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;

// Estendi JFormattedTextField per creare un campo di testo formattato con bordi arrotondati
public class JRoundFormattedTextField extends JFormattedTextField {

    private int borderRadius = 10; // Raggio per l'arrotondamento dei bordi

    // Costruttore che accetta un formato per il campo di testo formattato
    public JRoundFormattedTextField(DecimalFormat format) {
        super(format);
        setOpaque(false); // Rendi il background trasparente
        setBorder(new EmptyBorder(5, 10, 5, 10)); // Aggiungi margine interno per separare il testo dai bordi
    }

    // Sovrascrivi il metodo paintComponent per disegnare il background arrotondato
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

    // Sovrascrivi il metodo paintBorder per evitare di disegnare il bordo predefinito
    @Override
    protected void paintBorder(Graphics g) {
        // Non disegnare il bordo predefinito
    }

    // Sovrascrivi il metodo getPreferredSize per aggiungere un padding
    @Override
    public Dimension getPreferredSize() {
        // Aggiungi padding per il testo
        return new Dimension(super.getPreferredSize().width + 20, super.getPreferredSize().height);
    }

    // Sovrascrivi il metodo getInsets per aggiungere un margine interno
    @Override
    public Insets getInsets() {
        // Aggiungi margine interno per compensare l'arrotondamento dei bordi
        return new Insets(5, 10, 5, 10);
    }

    // Metodo main per eseguire l'applicazione di esempio
    public static void main(String[] args) {
        JFrame frame = new JFrame("RoundedFormattedTextField Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        DecimalFormat format = new DecimalFormat("#.##");
        JFormattedTextField formattedTextField = new JRoundFormattedTextField(format);
        formattedTextField.setValue(new Float(3.35)); // Imposta il valore iniziale
        formattedTextField.setPreferredSize(new Dimension(200, 30)); // Imposta le dimensioni desiderate
        frame.add(formattedTextField);

        frame.pack();
        frame.setVisible(true);
    }
}
