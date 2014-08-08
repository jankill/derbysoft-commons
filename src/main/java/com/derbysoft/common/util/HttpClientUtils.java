package com.derbysoft.common.util;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-5-25
 */
@Deprecated
public abstract class HttpClientUtils {

    public static String getResult(String url, String request) {
        return com.derbysoft.common.http.HttpClientUtils.getResult(url, request);
    }

}
