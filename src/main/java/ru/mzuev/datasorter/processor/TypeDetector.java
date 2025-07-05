package ru.mzuev.datasorter.processor;

import java.math.BigDecimal;

public class TypeDetector {

    // Кэшируем чтобы каждый раз не компилировать регулярку
    private static final java.util.regex.Pattern INTEGER_PATTERN =
            java.util.regex.Pattern.compile("^[-+]?\\d+$");

    public static boolean isInteger(String s) {
        if (s == null || s.isEmpty()) return false;
        return INTEGER_PATTERN.matcher(s).matches();
    }

    public static boolean isDecimal(String s) {
        if (s == null || s.isEmpty()) return false;

        try {
            new BigDecimal(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}