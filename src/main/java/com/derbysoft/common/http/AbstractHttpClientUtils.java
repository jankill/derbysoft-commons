package com.derbysoft.common.http;

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
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author zhupan
 * @version 1.8
 */
public abstract class AbstractHttpClientUtils {

    private static final int TIMEOUT = 30 * 1000;

    private static final Charset UTF_8 = Consts.UTF_8;

    protected static Log logger = LogFactory.getLog(AbstractHttpClientUtils.class);

    private static final int ERROR_CODE = 500;

    protected static int getStatusCode(String url, String request, CloseableHttpClient httpclient) {
        try {
            HttpPost httpPost = getHttpPost(url, request);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            return httpResponse.getStatusLine().getStatusCode();
        } catch (Exception e) {
            logger.error("HttpClientUtils getResult error", e);
            return ERROR_CODE;
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error("httpclient.close() error", e);
            }
        }
    }

    protected static String getResult(String url, String request, CloseableHttpClient httpclient) {
        try {
            HttpPost httpPost = getHttpPost(url, request);
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

    protected static byte[] getResult(String url, byte[] request, CloseableHttpClient httpclient) {
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


    private static HttpPost getHttpPost(String url, String request) {
        return createHttpPost(url, new StringEntity(request, ContentType.create("text/xml", UTF_8)));
    }

    protected static HttpPost createHttpPost(String url, HttpEntity httpEntity) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(httpEntity);
        httpPost.setConfig(requestConfig());
        return httpPost;
    }

    private static RequestConfig requestConfig() {
        return RequestConfig.custom().setSocketTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).setConnectionRequestTimeout(TIMEOUT).build();
    }

}