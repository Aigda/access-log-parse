import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws MyRuntimeException {

        String path = "C:\\Users\\asabanin\\Downloads\\access.log";
        FileReader fileReader = null;

        int iGooglebotCount = 0;
        int iGoogleBotCount = 0;
        int iYandexCount = 0;
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

            Pattern p = Pattern.compile(regex);
            Matcher matcher = p.matcher(line);

            if (matcher.find()) {

                // Нашли User-Agent
                String strUserAgent = matcher.group(9);

                //Нашли все вхождения по скобкам
                Matcher m = Pattern.compile("\\((.*?)\\)").matcher(strUserAgent);

                while (m.find()) {
                    // Нашли первые скобки
                    String firstBrackets = m.group(0);
                    String[] parts = firstBrackets.split(";");

                    if (parts.length >= 2) {
                        String fragment = parts[1];

                        //Очистим от пробелов
                        String replacedString = fragment.replace(" ", "");

                        // найдем до слэша
                        String[] nosleshString = replacedString.split("/");

                        // Нашли бота
                        String strBot = nosleshString[0];

                        // Равно GoogleBot (заглавная b)
                        if (strBot.equals("GoogleBot")) {
                            iGoogleBotCount++;
                        }

                        //  Равно Googlebot (строчная b)
                        if (strBot.equals("Googlebot")) {
                            iGooglebotCount++;
                        }

                        if (strBot.equals("YandexBot")) {
                            iYandexCount++;
                        }
                        // Второй фрагмент со скобками не ищем
                        break;
                    }
                }

            }
            iCount++;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.###");

        System.out.println("Число линий: " + iCount);
        System.out.println("Число YandexBot: " + iYandexCount);
        System.out.println("Число GoogleBot: " + iGoogleBotCount);
        System.out.println("Число Googlebot: " + iGooglebotCount);

        String s1 = String.format("%1$,.2f", (100*(double)iYandexCount/iCount));
        String s2= String.format("%1$,.2f", (100*(double)iGoogleBotCount/iCount));
        String s3= String.format("%1$,.2f", (100*(double)iGooglebotCount/iCount));

        System.out.println("Доля YandexBot: " + s1 + "%");
        System.out.println("Доля GoogleBot: " + s2 + "%");
        System.out.println("Доля Googlebot: " + s3 + "%");
    }
}
