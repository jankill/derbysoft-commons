package com.derbysoft.common.util.date;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DateValidator {

    private static final String EL = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))";

    public static boolean validate(String date) {
        if (date == null) {
            return false;
        }
        Pattern p = Pattern.compile(EL);
        Matcher m = p.matcher(date);
        return m.matches();
    }

}
