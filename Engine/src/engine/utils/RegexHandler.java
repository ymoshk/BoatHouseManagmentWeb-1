package engine.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHandler {

    //private static final Pattern EMAIL_PATTERN = Pattern.compile("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_PATTERN = Pattern.compile("05\\d-\\d{7}");
    private static final Pattern PHONE_WITHOUT_PATTERN = Pattern.compile("05\\d{8}");

    private static boolean verifyPattern(Pattern pattern, String string) {
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    public static boolean isEmailAddressValid(String email) {
        return verifyPattern(EMAIL_PATTERN, email);
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        return verifyPattern(PHONE_WITHOUT_PATTERN, phoneNumber) || verifyPattern(PHONE_PATTERN, phoneNumber);
    }
}
