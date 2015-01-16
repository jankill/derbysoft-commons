package com.derbysoft.common.util;

import com.derbysoft.common.util.collect.Lists;

import java.util.List;

/**
 * @author zhupan
 * @version 2.1
 * @since 2012-5-25
 */
public abstract class Strings {

    private static final String SEPARATOR = ",";
    private static final String REGEX = "[^0-9]";

    public static List<String> getNumbers(String content) {
        List<String> result = Lists.newArrayList();
        for (String number : content.replaceAll(REGEX, SEPARATOR).split(SEPARATOR)) {
            if (number.length() > 0)
                result.add(number);
        }
        return result;
    }

    public static Integer firstNumber(String content) {
        List<String> numbers = getNumbers(content);
        if (Lists.isEmpty(numbers)) {
            return null;
        }
        return Integer.valueOf(numbers.get(0));
    }

}
