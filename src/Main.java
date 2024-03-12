import com.formdev.flatlaf.FlatDarkLaf;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Main {
    public static JPanel listaAssetPanel;
    public static JScrollPane listaAssetScrollPane;
    public static JFrame frame;
    public static TimeSeries series;
    public static JFreeChart grafico;
    public static String assetChosen;
    public static JPanel graficoPanel;
    public static volatile ListInformationReceiver lir;
    public static JRoundTextField quantitaInput;

    public static void main(String[] args) throws IOException, FontFormatException {

        File font_file = new File("Montserrat.ttf");
        Font defaultFont = Font.createFont(Font.TRUETYPE_FONT, font_file);
        defaultFont = defaultFont.deriveFont(14f);
        setUIFont(new FontUIResource(defaultFont));

        FlatDarkLaf.setup();
        frame = new JFrame("MarketHeaven - 1.0.0");
        frame.setSize(1400, 800);
        JPanel mainPanel = new JPanel(new MigLayout("", "[25%][75%]", "[10%][90%]"));

        JPanel apriOperazionePanel = new JPanel(new MigLayout("filly", "[40%][20%][40%]"));

        Color vendiButtonColore = new Color(148, 57, 57);
        JRoundButton vendiButton = new JRoundButton("VENDI", vendiButtonColore, new Color(145, 145, 145));
        vendiButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        vendiButton.addActionListener(e -> PositionSender.sendPosition(false));
        vendiButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                vendiButton.changeColorsSmooth(new Color(208, 72, 72), new Color(231, 231, 231));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                vendiButton.changeColorsSmooth(vendiButtonColore, new Color(145, 145, 145));
            }
        });

        quantitaInput = new JRoundTextField();

        Color compraButtonColore = new Color(41, 83, 138);
        JRoundButton compraButton = new JRoundButton("COMPRA", compraButtonColore, new Color(145, 145, 145));
        compraButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        compraButton.addActionListener(e -> PositionSender.sendPosition(true));
        compraButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                compraButton.changeColorsSmooth(new Color(57, 119, 191), new Color(231, 231, 231));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                compraButton.changeColorsSmooth(compraButtonColore, new Color(145, 145, 145));
            }
        });

        Color scaricaOperazioniButtonColore = new Color(21, 21, 21);
        JRoundButton scaricaOperazioniButton = new JRoundButton("SCARICA POSIZIONI", scaricaOperazioniButtonColore, new Color(145, 145, 145));
        scaricaOperazioniButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        scaricaOperazioniButton.addActionListener(e -> PositionsReceiver.receivePositions());
        scaricaOperazioniButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                scaricaOperazioniButton.changeColorsSmooth(new Color(10, 10, 10), new Color(145, 145, 145));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                scaricaOperazioniButton.changeColorsSmooth(scaricaOperazioniButtonColore, new Color(145, 145, 145));
            }
        });

        apriOperazionePanel.add(vendiButton, "w 100%");
        apriOperazionePanel.add(quantitaInput, "w 100%");
        apriOperazionePanel.add(compraButton, "w 100%, wrap");
        apriOperazionePanel.add(scaricaOperazioniButton, "w 100%, span 3");


        // PANNELLO PER IL GRAFICO

        series = new TimeSeries("Prezzi");
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        grafico = ChartFactory.createTimeSeriesChart("", "Tempo", "Prezzo", dataset);

        XYPlot plot = grafico.getXYPlot();
        DateAxis xAxis = (DateAxis) plot.getDomainAxis();
        xAxis.setDateFormatOverride(new SimpleDateFormat("hh:mm:ss"));
        ChartPanel graficoInnerPanel = new ChartPanel(grafico);

        XYSplineRenderer lineRenderer = new XYSplineRenderer(100);
        lineRenderer.setSeriesShapesVisible(0, false);
        lineRenderer.setSeriesShape(0, null);
        lineRenderer.setSeriesStroke(0, new BasicStroke(2.0f));

        grafico.getTitle().setPaint(Color.white);
        grafico.getLegend().setVisible(false);

        plot.setRenderer(lineRenderer);
        grafico.setBackgroundPaint(UIManager.getDefaults().getColor("SystemColor.background"));
        plot.getRangeAxis().setTickLabelPaint(UIManager.getDefaults().getColor("TextField.foreground"));
        plot.getDomainAxis().setTickLabelPaint(UIManager.getDefaults().getColor("TextField.foreground"));
        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        ((NumberAxis) plot.getRangeAxis()).setNumberFormatOverride(new DecimalFormat("0.000000"));
        plot.setBackgroundPaint(UIManager.getDefaults().getColor("SystemColor.background"));

        graficoPanel = new JPanel(new MigLayout(""));
        graficoPanel.setVisible(false);
        graficoPanel.add(graficoInnerPanel, "w 100%, h 100%");


        // PANNELLO PER LA LISTA DI ASSET

        listaAssetPanel = new JPanel(new MigLayout("w 100%"));

        listaAssetScrollPane = new JScrollPane(listaAssetPanel);

        lir = new ListInformationReceiver();
        lir.start();

        mainPanel.add(apriOperazionePanel, "w 100%, h 10%");
        mainPanel.add(graficoPanel, "w 100%, h 100%, wrap, span 1 2");
        mainPanel.add(listaAssetScrollPane, "w 100%, h 90%");

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void setUIFont(FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                FontUIResource orig = (FontUIResource) value;
                Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
                UIManager.put(key, new FontUIResource(font));
            }
        }
    }

}