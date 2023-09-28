import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

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
    }
}
