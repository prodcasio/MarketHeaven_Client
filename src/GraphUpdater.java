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
            while(true){
                if(Main.lir.assets.isEmpty()) continue;
                for(int k=0; k<Main.lir.assets.length(); k++) {
                    assetObject = Main.lir.assets.getJSONObject(k);
                    String name = assetObject.getString("name");
                    float value = assetObject.getFloat("value");
                    if (name.equals(Main.assetChosen)) {
                        int maximum = 50;
                        if (Main.series.getItemCount() > maximum) {
                            Main.series.delete(0, Main.series.getItemCount() - (maximum + 1));
                        }
                        LocalDateTime ldt = LocalDateTime.now();
                        Day day = new Day();
                        Hour hour = new Hour(ldt.getHour(), day);
                        Minute minute = new Minute(ldt.getMinute(), hour);
                        Second second = new Second(ldt.getSecond(), minute);
                        Main.series.add(second, value);
                        float maxYValue = 0;
                        float minYValue = 999999;
                        for (int i = 0; i < Main.series.getItemCount(); i++) {
                            if (Main.series.getValue(i).floatValue() > maxYValue)
                                maxYValue = Main.series.getValue(i).floatValue();
                            if (Main.series.getValue(i).floatValue() < minYValue)
                                minYValue = Main.series.getValue(i).floatValue();
                        }
                        Main.grafico.getXYPlot().getRangeAxis().setUpperBound(maxYValue + maxYValue * 0.001);
                        Main.grafico.getXYPlot().getRangeAxis().setLowerBound(minYValue - minYValue * 0.001);
                        while (true) {
                            LocalDateTime ldt1 = LocalDateTime.now();
                            if (ldt1.getSecond() > ldt.getSecond() || ldt1.getMinute() > ldt.getMinute()) break;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                    }
                }
            }
        } catch(Exception e){
            System.out.println("Errore generale durante l'aggiornamento del grafico: " + e);
        }

    }
}
