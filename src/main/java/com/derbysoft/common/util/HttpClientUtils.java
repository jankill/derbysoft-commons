package com.derbysoft.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-5-25
 */
public abstract class HttpClientUtils {

    private static Log logger = LogFactory.getLog(HttpClientUtils.class);

    public static String getResult(String url, String request) {
        HttpClient httpclient = new DefaultHttpClient();
        String response = "";
        try {
            HttpPost httpPost = createHttpPost(url, request);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            response = EntityUtils.toString(httpResponse.getEntity(), Consts.UTF_8);
        } catch (Exception e) {
            logger.error("HttpClientUtils getResult error", e);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return response;
    }

    private static HttpPost createHttpPost(String url, String request) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(createRequest(request));
        httpPost.setHeader("Accept-Encoding", "gzip");
        return httpPost;
    }

    private static StringEntity createRequest(String request) {
        return new StringEntity(request, ContentType.create("text/xml", Consts.UTF_8));
    }

}
