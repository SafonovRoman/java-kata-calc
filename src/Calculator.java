import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.*;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String input;
        while (true) {
            System.out.print("Введите корректное выражение или пробел для выхода из программы\n");
            input = in.nextLine();
            if (Objects.equals(input, " ")) {
                System.exit(0);
            }
            try {
                System.out.println(calc(input));
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }
    }
    public static String calc (String input) throws IOException {
        int firstNumber;
        int secondNumber;
        String operand;
        Map<String,String> numbers = new HashMap<>();
        numbers.put("I", "1");
        numbers.put("II", "2");
        numbers.put("III", "3");
        numbers.put("IV", "4");
        numbers.put("V", "5");
        numbers.put("VI", "6");
        numbers.put("VII", "7");
        numbers.put("VIII", "8");
        numbers.put("IX", "9");
        numbers.put("X", "10");
        String[] expression = input.split(" ");
        if (expression.length != 3) {
            throw new IOException("Неверный формат ввода данных. Должно быть два числа и операнд, все разделенные пробелами.");
        }
        String regexArabic = "^[\\d, 10] [+\\-*/] [\\d, 10]$";
        Pattern patternArabic = Pattern.compile(regexArabic);
        Matcher matcherArabic = patternArabic.matcher(input);
        String regexRoman = "^(X|IX|IV|V?I{0,3}) [+\\-*/] (X|IX|IV|V?I{0,3})$";
        Pattern patternRoman = Pattern.compile(regexRoman);
        Matcher matcherRoman = patternRoman.matcher(input);
        operand = expression[1];
        if (matcherRoman.matches()) {
            firstNumber = Integer.parseInt(numbers.get(expression[0]));
            secondNumber = Integer.parseInt(numbers.get(expression[2]));
            return arabic_to_roman(do_calculation(firstNumber, secondNumber, operand));
        }
        if (matcherArabic.matches()) {
            firstNumber = Integer.parseInt(expression[0]);
            secondNumber = Integer.parseInt(expression[2]);
            return String.valueOf(do_calculation (firstNumber, secondNumber, operand));
        }
        throw new IOException("Некорректный ввод. Пример: '4 * 7' или 'IV / II'.");
    }
    public static Integer do_calculation(Integer first, Integer second, String operand) {
        return switch (operand) {
            case "+" -> first + second;
            case "-" -> first - second;
            case "*" -> first * second;
            default -> first / second;
        };
    }
    public static String arabic_to_roman (Integer arabic) throws IOException { // Можно было просто взять класс из википедии *facepalm*
        if (arabic < 1) {
            throw new IOException("В римской арифметике вычитать можно только из большего меньшее и делить большее на меньшее");
        }
        int [] convertationAnchors = {100, 50, 10, 5, 1};
        StringBuilder result = new StringBuilder();
        Character last = ' ';
        Map<Integer, Character> numbers = new HashMap<>();
        numbers.put(1, 'I');
        numbers.put(5, 'V');
        numbers.put(10, 'X');
        numbers.put(50, 'L');
        numbers.put(100, 'C');
        for (int anchor: convertationAnchors) {
            Character current = numbers.get(anchor);
            while (arabic >= anchor) {
                arabic = arabic - anchor;
                if (result.length() > 2 &&
                        result.charAt(result.length() - 1) == current &&
                        result.charAt(result.length()-2) == current &&
                        result.charAt(result.length()-3) == current)
                {
                    result.setLength(result.length()-2);
                    result.append(last);
                } else {
                    result.append(current);
                }
            }
            last = current;
        }
        return result.toString();
    }
}
