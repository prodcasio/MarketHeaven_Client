import javax.swing.JOptionPane;

public class Popup {
    public static void createPopup(String content) {
        // Mostra un popup con il contenuto
        JOptionPane.showMessageDialog(null, content, "", JOptionPane.INFORMATION_MESSAGE);
    }
}