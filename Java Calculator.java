import java.util.Scanner;

/**
 * This is a calculator for simple base-10 arithmetic. It only runs once.
 * In other words, it only performs calculations on two numbers and then
 * terminates.
 */
public class JavaCalculator {
    // Assume that the calculator will work only with base 10.
    public static final int RADIX = 10;

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        String num1Str;
        String num2Str;
        int num1;
        int num2;
        String operatorChoice;

        System.out.println("Welcome to Java calculator!");
        System.out.println("Enter a number: ");
        num1Str = console.nextLine().trim();
        while(!isInteger(num1Str, RADIX)) {
            System.out.println("Type in a number's digits, like this: \"135\". Please try again.");
            num1Str = console.nextLine().trim();
        }
        num1 = Integer.parseInt(num1Str);

        System.out.println("Please type in another number: ");
        num2Str = console.nextLine().trim();
        while(!isInteger(num2Str, RADIX)) {
            System.out.println("Type in a number's digits, like this: \"135\". Please try again.");
            num2Str = console.nextLine().trim();
        }
        num2 = Integer.parseInt(num2Str);

        System.out.println("What would you like to do with these numbers?");
        System.out.println("\ta. Add the numbers");
        System.out.println("\tb. Subtract the numbers");
        System.out.println("\tc. Multiple the numbers");
        System.out.println("\td. Divide the numbers");

        operatorChoice = console.nextLine().trim();
        while(!(operatorChoice.equalsIgnoreCase("a") ||
                operatorChoice.equalsIgnoreCase("b") ||
                operatorChoice.equalsIgnoreCase("c") ||
                operatorChoice.equalsIgnoreCase("d"))) {
            System.out.println("Enter just the letter, like \"b\". Please try again.");
            operatorChoice = console.nextLine().trim();
        }

        // The choice was already checked to ensure it can only be one of these. Thus, no default.
        switch(operatorChoice) {
            case "a":
                System.out.println("The result is: " + (num1 + num2));
                break;
            case "b":
                System.out.println("The result is: " + (num1 - num2));
                break;
            case "c":
                System.out.println("The result is: " + (num1 * num2));
                break;
            case "d":
                System.out.println("The result is: " + (num1 / num2));
                break;
        }
    }

    /**
     * Checks if a string is an integer.
     * @param str a String which is the string to check
     * @param radix an int which is the base of the string
     * @return if the String is an integer
     */
    public static boolean isInteger(String str, int radix) {
        if(str.isEmpty()) {
            return false;
        }

        for(int i = 0; i < str.length(); i++) {
            // If the number appears to be negative, but has no values, return false.
            if(i == 0 && str.charAt(i) == '-') {
                if(str.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }
            char ch = str.charAt(i);
            if(Character.digit(ch, radix) < 0) {
                return false;
            }
        }
        return true;
    }
}