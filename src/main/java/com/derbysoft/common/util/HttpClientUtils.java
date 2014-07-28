package com.derbysoft.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-5-25
 */
public abstract class HttpClientUtils {

    private static final int TIMEOUT = 10 * 1000;

    private static final Charset UTF_8 = Consts.UTF_8;

    private static Log logger = LogFactory.getLog(HttpClientUtils.class);

    public static String getResult(String url, String request) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = createHttpPost(url, new StringEntity(request, ContentType.create("text/xml", UTF_8)));
            HttpResponse httpResponse = httpclient.execute(httpPost);
            return EntityUtils.toString(httpResponse.getEntity(), UTF_8);
        } catch (Exception e) {
            logger.error("HttpClientUtils getResult error", e);
            return null;
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error("httpclient.close() error", e);
            }
        }
    }

    public static byte[] getResult(String url, byte[] request) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = createHttpPost(url, new ByteArrayEntity(request));
            httpPost.setHeader("Content-type", "application/octet-stream");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            return EntityUtils.toByteArray(httpResponse.getEntity());
        } catch (Exception e) {
            logger.error("HttpClientUtils getResult error", e);
            return null;
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error("httpclient.close() error", e);
            }
        }
    }


    private static HttpPost createHttpPost(String url, HttpEntity httpEntity) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(httpEntity);
        httpPost.setConfig(requestConfig());
        return httpPost;
    }

    private static RequestConfig requestConfig() {
        return RequestConfig.custom().setSocketTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).setConnectionRequestTimeout(TIMEOUT).build();
    }

}
