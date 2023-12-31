import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Statistics {

    LocalDateTime maxTime  = LocalDateTime.of(1020, 9, 9, 19, 46, 45);
    LocalDateTime  minTime  = LocalDateTime.of(3020, 9, 9, 16, 46, 45);

    public Statistics() {

    }

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

    ArrayList<String> listOfAllExistingPages = new ArrayList<>();
    ArrayList<String> listOfNotExistingPages = new ArrayList<>();
    ArrayList<String> listOfIP = new ArrayList<>();

    public int getIntErrorRequestCount() {
        return intErrorRequestCount;
    }

    ArrayList<String> listErrorRequest = new ArrayList<>();
    int intErrorRequestCount = 0;

    HashSet<String> pages = new HashSet<String>();
    HashSet<String> noPages = new HashSet<String>();



    HashSet<String> listIP = new HashSet<String>();



    HashMap<String, Integer> operationStats = new HashMap<String, Integer> ();
    HashMap<String, Integer> browserStats = new HashMap<String, Integer> ();

    public ArrayList<UserAgent> getUserAgentList() {
        return userAgentList;
    }

    ArrayList<UserAgent> userAgentList = new ArrayList<>();


    public  void addEntry(LogEntry logEntry){

        if (!logEntry.getStrIPAddress().equals("104.124.156.139"))
        {
           // return;
        }
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

       // добавляем в список все существующие страницы
       // System.out.println(logEntry.getStrResponse());
        if (Objects.equals(logEntry.getStrResponse(), "200")) {
            pages.add(logEntry.getStrRequest());
        }

        // добавляем в список все несуществующие страницы
        // System.out.println(logEntry.getStrResponse());
        if (Objects.equals(logEntry.getStrResponse(), "404")) {
            noPages.add(logEntry.getStrRequest());
        }

        listOfIP.add(logEntry.getStrIPAddress());

        if (logEntry.getStrIPAddress().equals("104.124.156.139"))
        {
            //System.out.println(listOfIP.size());
        }

        // Найдем все ошибки
        // Получим цифровое значение кода
        if (logEntry.getStrResponse().length()==3) {
            int intCodeRequest = Integer.parseUnsignedInt(logEntry.getStrResponse());
            if (intCodeRequest >= 400 && intCodeRequest <=599) {
                listErrorRequest.add(logEntry.getStrRequest());
                intErrorRequestCount = intErrorRequestCount + 1;
            }
        }



        // добавляем статистику операционных систем
        //String oper = logEntry.getStrUserAgent();

        UserAgent userAgent = new UserAgent(logEntry.getStrUserAgent());

        userAgentList.add(userAgent);

        //System.out.println(" " + userAgent);

        String strBrowser = userAgent.getBrowserName();
        String strOS = userAgent.getBrowserOperatingSystem();

        if (strOS != null) {
            if (operationStats.containsKey(strOS)) {
                int countOperSys = operationStats.get(strOS) + 1;
                operationStats.put(strOS, countOperSys);
            } else {
                operationStats.put(strOS, 1);
            }
        }

        if (strBrowser != null) {
            if (browserStats.containsKey(strBrowser)) {
                int countBrowser = browserStats.get(strBrowser) + 1;
                browserStats.put(strBrowser, countBrowser);
            } else {
                browserStats.put(strBrowser, 1);
            }
        }



    }

    public Long getTrafficRate() {
        Duration duration = Duration.between(minTime, maxTime);

        long seconds = duration.getSeconds();

        long hours = seconds / 60;
        if (hours == 0) return 0L;

        return longTraffic/hours;
    }

    public Long getUsersRate() {
        Duration duration = Duration.between(minTime, maxTime);

        long seconds = duration.getSeconds();

        long hours = seconds / 3660;
        if (hours == 0) return 0L;

        return this.getUserAgentList().size()/hours;
    }

    public ArrayList<String> getListOfAllExistingPages()
    {
        listOfAllExistingPages.addAll(pages);
        return listOfAllExistingPages;
    }

    public ArrayList<String> getListOfNotExistingPages()
    {
        listOfNotExistingPages.addAll(noPages);
        return listOfNotExistingPages;
    }

    public ArrayList<String> getListIP()
    {
        //listOfIP.addAll(listIP);
        return listOfIP;
    }
    public HashMap<String, Double> getListOfOperationSystem()
    {
        HashMap<String, Double> ratio = new HashMap<String, Double>();

        // пройдем по всему списку
        int countOperAll = 0;
        for(Map.Entry<String, Integer> entry : operationStats.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            countOperAll = countOperAll + value;
        }

        for(Map.Entry<String, Integer> entry : operationStats.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            ratio.put(key,(double)value/countOperAll);
        }

        return ratio;
    }

    public HashMap<String, Double> getListOfBrowsers()
    {
        HashMap<String, Double> ratio = new HashMap<String, Double>();

        // пройдем по всему списку
        int countBrowsersAll = 0;
        for(Map.Entry<String, Integer> entry : browserStats.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            countBrowsersAll = countBrowsersAll + value;
        }

        for(Map.Entry<String, Integer> entry : browserStats.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            ratio.put(key,(double)value/countBrowsersAll);
        }

        return ratio;
    }



    public ArrayList<String> getListErrorRequest()
    {
        //listErrorRequest.addAll(listErrorRequest);
        return listErrorRequest;
    }

    public double getErrorRequestRate() {
        Duration duration = Duration.between(minTime, maxTime);

        long seconds = duration.getSeconds();

        long hours = seconds / 3660;
        if (hours == 0) return 0L;

        return (double)this.intErrorRequestCount/hours;
    }


}
