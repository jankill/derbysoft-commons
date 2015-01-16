package com.derbysoft.common.http;

import com.derbysoft.common.exception.SystemInternalException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhupan
 * @version 1.8
 */
public abstract class AbstractHttpClientUtils {

    private static final int TIMEOUT = 120 * 1000;

    private static final Charset UTF_8 = Consts.UTF_8;

    protected static Log logger = LogFactory.getLog(AbstractHttpClientUtils.class);

    private static final int ERROR_CODE = 500;

    protected static int getStatusCode(String url, Map<String, String[]> parameters, CloseableHttpClient httpclient) {
        return getStatusCode(httpclient, httpPost(url, parameters));
    }

    protected static int getStatusCode(String url, String request, CloseableHttpClient httpclient) {
        return getStatusCode(httpclient, httpPost(url, request));
    }

    protected static String getResult(String url, Map<String, String[]> parameters, CloseableHttpClient httpclient) {
        return getContent(httpclient, httpPost(url, parameters));
    }

    protected static String getResult(String url, String request, CloseableHttpClient httpclient) {
        return getContent(httpclient, httpPost(url, request));
    }

    protected static byte[] getResult(String url, byte[] request, CloseableHttpClient httpclient) {
        try {
            HttpPost httpPost = httpPost(url, new ByteArrayEntity(request));
            httpPost.setHeader("Content-type", "application/octet-stream");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            return EntityUtils.toByteArray(httpResponse.getEntity());
        } catch (Exception e) {
            logger.error("HttpClientUtils getResult error", e);
            throw new SystemInternalException(e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error("httpclient.close() error", e);
            }
        }
    }

    private static String getContent(CloseableHttpClient httpclient, HttpPost httpPost) {
        try {
            HttpResponse httpResponse = httpclient.execute(httpPost);
            return EntityUtils.toString(httpResponse.getEntity(), UTF_8);
        } catch (Exception e) {
            logger.error("HttpClientUtils getResult error", e);
            throw new SystemInternalException(e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error("httpclient.close() error", e);
            }
        }
    }


    private static int getStatusCode(CloseableHttpClient httpclient, HttpPost httpPost) {
        try {
            HttpResponse httpResponse = httpclient.execute(httpPost);
            return httpResponse.getStatusLine().getStatusCode();
        } catch (Exception e) {
            logger.error("HttpClientUtils getStatusCode error", e);
            return ERROR_CODE;
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error("httpclient.close() error", e);
            }
        }
    }


    private static HttpPost httpPost(String url, String request) {
        return httpPost(url, new StringEntity(request, contentType()));
    }

    private static ContentType contentType() {
        return ContentType.create("text/xml", UTF_8);
    }

    private static HttpPost httpPost(String url, Map<String, String[]> parameters) {
        return httpPost(url, createParams(parameters));
    }

    private static HttpEntity createParams(Map<String, String[]> parameterMap) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            parameters.add(new BasicNameValuePair(entry.getKey(), ArrayUtils.isEmpty(entry.getValue()) ? "" : entry.getValue()[0]));
        }
        return new UrlEncodedFormEntity(parameters, UTF_8);
    }


    protected static HttpPost httpPost(String url, HttpEntity httpEntity) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(httpEntity);
        httpPost.setConfig(requestConfig());
        return httpPost;
    }

    private static RequestConfig requestConfig() {
        return RequestConfig.custom().setSocketTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).setConnectionRequestTimeout(TIMEOUT).build();
    }

}