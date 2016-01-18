package com.derbysoft.common.util.date;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DateValidator {

    private static final String REGEX = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-9])))";

    private static final String SEPARATOR = "-";

    public static boolean validate(String date) {
        if (date == null) {
            return false;
        }
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(date);
        boolean matched = m.matches();
        if (!matched) {
            return matched;
        }
        String[] values = date.split(SEPARATOR);
        if (Integer.valueOf(values[1]) == 2) {
            Integer day = Integer.valueOf(values[2]);
            if (isLeapYear(Integer.valueOf(values[0]))) {
                return matched && day <= 29;
            }
            return matched && day <= 28;
        }

        return matched;
    }

    private static boolean isLeapYear(int year) {
        return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
    }

}
