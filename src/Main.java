import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

enum METHOD
{
    GET,POST
}
public class Main {
    public static void main(String[] args) throws MyRuntimeException {

        String path = "C:\\Users\\asabanin\\Downloads\\access.log";
        FileReader fileReader = null;

        String strLastLineBrowser ="";
        String strLastLineOS ="";

        try {
            fileReader = new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new MyRuntimeException("Понятное сообщение об ошибке: " + e.getMessage());
        }
        BufferedReader reader =
                new BufferedReader(fileReader);
        String line;
        int length = 0;
        int iCount = 0;

        // Для разбора лога по фрагментам
        String regex = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"(.+?)\"";

        Statistics statistics = new Statistics();

        Statistics statisticsNotBot = new Statistics();

        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                throw new MyRuntimeException("Понятное сообщение об ошибке: " + e.getMessage());
            }
            length = line.length();

            if (length > 1024) {
                throw new MyRuntimeException("В файле встречается строка длинее 1024 символов");
            }

            LogEntry logEntry = new LogEntry(line);

            //if (logEntry.getEnumMethod().equals(METHOD.POST)) {
            //    System.out.println(" " + logEntry);
            //}

            UserAgent userAgent = new UserAgent(logEntry.getStrUserAgent());

            //System.out.println(" " + userAgent);

            statistics.addEntry(logEntry);


             strLastLineBrowser = userAgent.getBrowserName();
             strLastLineOS = userAgent.getBrowserOperatingSystem();

             if (!userAgent.isBot()) {
                statisticsNotBot.addEntry(logEntry);
                 if (logEntry.getStrIPAddress().equals("104.124.156.139"))
                 {
                    // System.out.println(">>" + logEntry.getStrIPAddress());
                 }
             }

        }

        // Новая статистика для не ботов
        System.out.println("Минимальное время не ботов: " + statisticsNotBot.getMinTime());
        System.out.println("Максимальное время не ботов: " + statisticsNotBot.getMaxTime());
        System.out.println("Пользователей (не ботов)/час: " + statisticsNotBot.getUsersRate());
        System.out.println("Число ошибочных случаев: " + statisticsNotBot.getListErrorRequest().size());
        System.out.println("Число ошибок: " + statisticsNotBot.getIntErrorRequestCount());
        System.out.println("Ошибочных запросов/час: " + statisticsNotBot.getErrorRequestRate());

        System.out.println("Пользователей всего: " + statisticsNotBot.getUserAgentList().size());


        Stream<String> streamIP = statisticsNotBot.getListIP().stream();
        Stream<String> streamUniqueIP = statisticsNotBot.getListIP().stream();


        System.out.println("Всего IP: " + statisticsNotBot.getListIP().size());
        System.out.println("Всего IP через поток: " + + streamIP.count());
        long lUniqueIp = streamUniqueIP.distinct().count();
        System.out.println("Уникальных IP: " + lUniqueIp);
        System.out.println("Средняя посещаемость пользователем: " + (double)statisticsNotBot.getUserAgentList().size()/lUniqueIp);
        //проверим уникальность с помощью сортировки
        /*
        streamIP.distinct().sorted()
                .forEach(System.out::println);
         */

        //в связи с завершением курса только основные моменты. Остальные уже были рассмотрены ранее

        // Метод расчёта максимальной посещаемости одним пользователем.

        Stream<String> streamMaxIP = statisticsNotBot.getListIP().stream();
        /*
        streamMaxIP.sorted()
                .forEach(System.out::println);
        */
        Map<String, Long> map = streamMaxIP
                .collect(Collectors.toMap(Function.identity(), x -> 1L, Long::sum));
        //теперь отберем максимальное значение
        Stream<Map.Entry<String, Long>> streamMaxOne = map.entrySet().stream();

        System.out.println("Vаксимальная посещаемость одним пользователем");
        streamMaxOne.sorted(Map.Entry.<String, Long>comparingByValue().reversed()).limit(1)
                .forEach(x -> System.out.println(x.getKey() + " " + x.getValue()));


/*
        for (Map.Entry<String, Long> entry : map.entrySet())
        {
            String key = entry.getKey();
            Long value = entry.getValue();

            System.out.println(key + "   " + value );
        }
*/





       /* System.out.println("Браузер в последней строке " + strLastLineBrowser);
        System.out.println("OS в последней строке " + strLastLineOS);
        System.out.println("Минимальное время: " + statistics.getMinTime());
        System.out.println("Максимальное время: " + statistics.getMaxTime());
        System.out.println("Всего байт: " + statistics.getLongTraffic());
        System.out.println("Байт/час: " + statistics.getTrafficRate());


        System.out.println(statisticsNotBot.getListOfAllExistingPages().size());
        System.out.println(statistics.getListOfAllExistingPages().size());

        System.out.println(statistics.getListOfOperationSystem());

        Double sumDouble = 0.0;
        for (Map.Entry<String, Double> entry : statistics.getListOfOperationSystem().entrySet())
        {
            String key = entry.getKey();
            Double value = entry.getValue();

            sumDouble = sumDouble + value;

            System.out.println(key + "   " + value );
        }
        System.out.println(" Сумма всех долей " + sumDouble);


        // Метода возвращает список не существующих странц сайта
        System.out.println(statistics.getListOfNotExistingPages().size());

        // Метод возвращает список браузеров
        System.out.println(statistics.getListOfBrowsers());

        // Метод по возвращение статистики браузеров
        Double sumDoubleBreowser = 0.0;
        for (Map.Entry<String, Double> entry : statistics.getListOfBrowsers().entrySet())
        {
            String key = entry.getKey();
            Double value = entry.getValue();

            sumDoubleBreowser = sumDoubleBreowser + value;

            System.out.println(key + "   " + value );
        }
        System.out.println(" Сумма всех долей " + sumDoubleBreowser);


        //проверим уникальность с помощью сортировки
        Stream<Map.Entry<String, Double>> stream = statistics.getListOfBrowsers().entrySet().stream();

        System.out.println(" сортируем для проверки уникальности: " );
        stream.sorted(Map.Entry.<String, Double>comparingByKey())
                .forEach(x -> System.out.println(x.getKey() + " " + x.getValue()));

        // Метода возвращает список не существующих странц сайта
        System.out.println(statistics.getListOfNotExistingPages());


         */
    }


}
