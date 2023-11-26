import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws MyRuntimeException {

        String path = "C:\\Users\\asabanin\\Downloads\\access.log";
        FileReader fileReader = null;
        int iMinLength = 0;
        int iMaxLength = 0;
        boolean bFirstInit = true;
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

            if (bFirstInit) {
                bFirstInit = false;
                iMaxLength = length;
                iMinLength = length;
            }

            if (length > iMaxLength) iMaxLength = length;
            if (length < iMinLength) iMinLength = length;

            iCount++;
        }

        System.out.println("Число линий: " + iCount);
        System.out.println("Длина самой длинной линии: " + iMaxLength);
        System.out.println("Длина самой короткой линии: " + iMinLength);

/*
        System.out.println("Введите первое число: ");
        int firstNumber = new Scanner(System.in).nextInt();

        System.out.println("Введите второе число: ");
        int secondNumber = new Scanner(System.in).nextInt();

        //int firstNumber = 5;
        //int secondNumber = 10;

        int sumNumbers = firstNumber + secondNumber;
        int subNumbers = firstNumber - secondNumber;
        int multNumbers = firstNumber * secondNumber;
        double quotientNumbers  = (double) firstNumber / secondNumber;

        System.out.println("Были введены следующие числа : " + firstNumber + " и " + secondNumber);
        System.out.println("Сумма чисел: " + sumNumbers);
        System.out.println("Разность чисел: " + subNumbers);
        System.out.println("Умножение чисел: " + multNumbers);
        System.out.println("Частное чисел: " + quotientNumbers);


 */
    }
}
