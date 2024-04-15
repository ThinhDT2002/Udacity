package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Utils {

    private static final Scanner SCANNER = new Scanner(System.in);

    private Utils() {
    }

    public static boolean isStringEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static Date promptDate(String message) {
        try {
            System.out.println(message);
            String date = SCANNER.nextLine();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
            Date convertedDate = simpleDateFormat.parse(date);
            if(!simpleDateFormat.format(convertedDate).equals(date)) {
                System.out.println("Please provide valid date format!");
                return promptDate(message);
            } else {
                return convertedDate;
            }
        } catch (Exception ex) {
            return promptDate(message);
        }
    }

    public static String promtpString(String message) {
        try {
            System.out.println(message);
            return SCANNER.nextLine();
        } catch (Exception ex) {
            return promtpString(message);
        }
    }

    public static Double promptDouble(String message) {
        try {
            System.out.println(message);
            double input = SCANNER.nextDouble();
            SCANNER.nextLine();
            return input;
        } catch (Exception ex) {
            SCANNER.nextLine();
            return promptDouble(message);
        }
    }

    public static Integer promptInteger(String message) {
        try {
            System.out.println(message);
            int input = SCANNER.nextInt();
            SCANNER.nextLine();
            return input;
        } catch (Exception ex) {
            SCANNER.nextLine();
            return promptInteger(message);
        }
    }

    public static int getCustomerChoiceForYesNoOption(String message) {
        try {
            Integer customerChoice;
            do {
                customerChoice = promptInteger(message);
            } while (customerChoice != 1 && customerChoice != 2);
            return customerChoice;
        } catch (Exception ex) {
            System.out.println("You must choose 1 or 2 options");
            return getCustomerChoiceForYesNoOption(message);
        }
    }

    public static int getCustomerChoice(String message, int numberOfOptions) {
        try {
            Integer customerChoice;
            do {
                customerChoice = promptInteger(message);
            } while (customerChoice < 1 || customerChoice > numberOfOptions);
            return customerChoice;
        } catch (Exception ex) {
            System.out.println("You must choose 1 of " + numberOfOptions + " options");
            return getCustomerChoice(message, numberOfOptions);
        }
    }
}
