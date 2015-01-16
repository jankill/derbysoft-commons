package com.derbysoft.common.util;

import org.apache.commons.lang3.StringUtils;

public class SplitUtils {

    public static final String[] SEARCH_LIST = new String[]{",", ";", "\r","\n"};
    public static final String[] REPLACEMENT_LIST = new String[]{" ", " "," ", " "};

    public static String[] split(String values) {
        return StringUtils.split(StringUtils.replaceEach(values, SEARCH_LIST, REPLACEMENT_LIST));
    }
}
