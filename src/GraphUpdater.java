import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Second;
import org.json.JSONObject;
import java.time.LocalDateTime;

public class GraphUpdater extends Thread{
    JSONObject assetObject;
    GraphUpdater(){}

    @Override
    public void run(){
        try{
            // Ciclo principale per l'aggiornamento del grafico
            while(true){
                // Verifica se l'elenco degli asset è vuoto, in tal caso continua con il prossimo ciclo
                if(Main.lir.assets.isEmpty()) continue;
                // Iterazione su tutti gli asset
                for(int k=0; k<Main.lir.assets.length(); k++) {
                    assetObject = Main.lir.assets.getJSONObject(k);
                    String name = assetObject.getString("name");
                    float value = assetObject.getFloat("value");
                    // Verifica se l'asset corrente è quello scelto
                    if (name.equals(Main.assetChosen)) {
                        int maximum = 50;
                        // Se il numero di punti nel grafico supera la soglia massima, rimuovi i punti più vecchi
                        if (Main.series.getItemCount() > maximum) {
                            Main.series.delete(0, Main.series.getItemCount() - (maximum + 1));
                        }
                        // Ottieni la data e l'ora attuali
                        LocalDateTime ldt = LocalDateTime.now();
                        // Crea istanze di Day, Hour, Minute e Second per rappresentare la data e l'ora attuali
                        Day day = new Day();
                        Hour hour = new Hour(ldt.getHour(), day);
                        Minute minute = new Minute(ldt.getMinute(), hour);
                        Second second = new Second(ldt.getSecond(), minute);
                        // Aggiungi il nuovo valore al grafico
                        Main.series.add(second, value);
                        // Calcola i valori massimi e minimi sull'asse Y per aggiornare la scala del grafico
                        float maxYValue = 0;
                        float minYValue = 999999;
                        for (int i = 0; i < Main.series.getItemCount(); i++) {
                            if (Main.series.getValue(i).floatValue() > maxYValue)
                                maxYValue = Main.series.getValue(i).floatValue();
                            if (Main.series.getValue(i).floatValue() < minYValue)
                                minYValue = Main.series.getValue(i).floatValue();
                        }
                        // Aggiorna la scala dell'asse Y del grafico
                        Main.grafico.getXYPlot().getRangeAxis().setUpperBound(maxYValue + maxYValue * 0.001);
                        Main.grafico.getXYPlot().getRangeAxis().setLowerBound(minYValue - minYValue * 0.001);
                        // Attendi fino al cambio di secondo o minuto prima di aggiornare nuovamente il grafico
                        while (true) {
                            LocalDateTime ldt1 = LocalDateTime.now();
                            if (ldt1.getSecond() > ldt.getSecond() || ldt1.getMinute() > ldt.getMinute()) break;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        // Esci dal ciclo dopo aver aggiornato il grafico per l'asset corrente
                        break;
                    }
                }
            }
        } catch(Exception e){
            // Gestione degli errori durante l'aggiornamento del grafico
            System.out.println("Errore generale durante l'aggiornamento del grafico: " + e);
        }

    }
}
