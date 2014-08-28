package com.derbysoft.common.http;

import com.derbysoft.common.exception.SystemException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author zhupan
 * @version 1.8
 */
public abstract class HttpsClientUtils extends AbstractHttpClientUtils {

    private static SSLConnectionSocketFactory sslConnectionSocketFactory = getSslConnectionSocketFactory();

    public static int getStatusCode(String url, String request) {
        if (isNotHttpsProtocol(url)) {
            return HttpClientUtils.getStatusCode(url, request);
        }
        return getStatusCode(url, request, getHttpClient());
    }

    public static String getResult(String url, String request) {
        if (isNotHttpsProtocol(url)) {
            return HttpClientUtils.getResult(url, request);
        }
        return getResult(url, request, getHttpClient());
    }

    public static byte[] getResult(String url, byte[] request) {
        if (isNotHttpsProtocol(url)) {
            return HttpClientUtils.getResult(url, request);
        }
        return getResult(url, request, getHttpClient());
    }

    private static boolean isNotHttpsProtocol(String url) {
        return !url.startsWith("https");
    }

    private static CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
    }

    private static SSLConnectionSocketFactory getSslConnectionSocketFactory() {
        try {
            SSLContext sslcontext = buildSSLContext();
            return new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    private static SSLContext buildSSLContext() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        return SSLContexts.custom().setSecureRandom(new SecureRandom()).loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
    }
}