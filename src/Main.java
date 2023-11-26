import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

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

        }

        System.out.println("Браузер в последней строке " + strLastLineBrowser);
        System.out.println("OS в последней строке " + strLastLineOS);
        System.out.println("Минимальное время: " + statistics.getMinTime());
        System.out.println("Максимальное время: " + statistics.getMaxTime());
        System.out.println("Всего байт: " + statistics.getLongTraffic());
        System.out.println("Байт/час: " + statistics.getTrafficRate());

    }
}
