package com.derbysoft.common.http;

import org.apache.http.impl.client.HttpClients;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-5-25
 */
public abstract class HttpClientUtils extends AbstractHttpClientUtils {

    public static int getStatusCode(String url, String request) {
        return getStatusCode(url, request, HttpClients.createDefault());
    }

    public static String getResult(String url, String request) {
        return getResult(url, request, HttpClients.createDefault());
    }

    public static byte[] getResult(String url, byte[] request) {
        return getResult(url, request, HttpClients.createDefault());
    }

}
