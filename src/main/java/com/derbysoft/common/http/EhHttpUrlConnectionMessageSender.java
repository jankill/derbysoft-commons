package com.derbysoft.common.http;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class EhHttpUrlConnectionMessageSender extends HttpUrlConnectionMessageSender implements InitializingBean {

    private static final String SSL = "SSL";

    private int readTimeout = 120000;

    private int connectionTimeout = 30000;

    private boolean trustAllHttpsCertificates = true;

    @Override
    protected void prepareConnection(HttpURLConnection connection) throws IOException {
        connection.setReadTimeout(readTimeout);
        connection.setConnectTimeout(connectionTimeout);
        super.prepareConnection(connection);
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setTrustAllHttpsCertificates(boolean trustAllHttpsCertificates) {
        this.trustAllHttpsCertificates = trustAllHttpsCertificates;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (trustAllHttpsCertificates) {
            SSLContext context = SSLContext.getInstance(SSL);
            context.init(null, new TrustManager[]{new IgnorantTrustManager()}, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            });
        }
    }

    private static class IgnorantTrustManager implements TrustManager, X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}

