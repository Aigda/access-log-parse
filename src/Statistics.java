import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Statistics {

    LocalDateTime maxTime  = LocalDateTime.of(1020, 9, 9, 19, 46, 45);
    LocalDateTime  minTime  = LocalDateTime.of(3020, 9, 9, 16, 46, 45);

    public LocalDateTime getMaxTime() {
        return maxTime;
    }

    public LocalDateTime getMinTime() {
        return minTime;
    }

    public Long getLongTraffic() {
        return longTraffic;
    }

    Long longTraffic = 0L;

    public  void addEntry(LogEntry logEntry){
        longTraffic = longTraffic + Long.parseLong(logEntry.getStrBytesSent());


        //LocalDateTime date = LocalDateTime.parse( logEntry.getStrDateTime() , DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z").withLocale(Locale.US);;

            String datetime = logEntry.getStrDateTime();
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(datetime, formatter);

        // System.out.println(zonedDateTime.toString());

        Duration durationMin = Duration.between( zonedDateTime.toLocalDateTime() , minTime );
        Duration durationMax = Duration.between( zonedDateTime.toLocalDateTime() , maxTime );

        Long longDiffMin = durationMin.getSeconds();
        Long longDiffMax = durationMax.getSeconds();

        if (longDiffMin > 0)
        {
            minTime = zonedDateTime.toLocalDateTime();
        }

        if (longDiffMax < 0)
        {
            maxTime = zonedDateTime.toLocalDateTime();
        }

    }

    public Long getTrafficRate() {
        Duration duration = Duration.between(minTime, maxTime);

        long seconds = duration.getSeconds();

        long hours = seconds / 60;
        if (hours == 0) return 0L;

        return longTraffic/hours;
    }
}
