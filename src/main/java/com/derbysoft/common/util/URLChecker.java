package com.derbysoft.common.util;

import java.util.regex.Pattern;

public abstract class URLChecker {

    private static final String REGEX = "((http|ftp|https)://)(([a-zA-Z0-9._-]+.[a-zA-Z]{2,6})|([0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9&%_./-~-]*)?";

    public static boolean check(String url) {
        return url != null && !url.trim().equals("") && Pattern.matches(REGEX, url);
    }

}
