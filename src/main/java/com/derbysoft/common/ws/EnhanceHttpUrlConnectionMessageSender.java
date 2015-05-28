package com.derbysoft.common.ws;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.http.HttpTransportException;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class EnhanceHttpUrlConnectionMessageSender extends HttpUrlConnectionMessageSender implements InitializingBean {
    private int readTimeout;
    private int connectionTimeout;
    private WebServiceMessageHolder webServiceMessageHolder;
    private boolean trustAllHttpsCertificates;

    @Override
    public WebServiceConnection createConnection(URI uri) throws IOException {
        if (webServiceMessageHolder == null) {
            return super.createConnection(uri);
        }
        URL url = uri.toURL();
        URLConnection connection = url.openConnection();
        if (!(connection instanceof HttpURLConnection)) {
            throw new HttpTransportException("URI [" + uri + "] is not an HTTP URL");
        } else {
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            prepareConnection(httpURLConnection);
            webServiceMessageHolder.reset();
            return new EnhanceHttpUrlConnection(httpURLConnection, webServiceMessageHolder);
        }
    }

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

    private static final HostnameVerifier VERIFIER = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHttpsCertificates() throws Exception {
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(null, new TrustManager[]{new IgnorantTrustManager()}, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (trustAllHttpsCertificates) {
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(VERIFIER);
        }
    }

    static class IgnorantTrustManager implements TrustManager, X509TrustManager {

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

    public void setTrustAllHttpsCertificates(boolean trustAllHttpsCertificates) {
        this.trustAllHttpsCertificates = trustAllHttpsCertificates;
    }

    public void setWebServiceMessageHolder(WebServiceMessageHolder webServiceMessageHolder) {
        this.webServiceMessageHolder = webServiceMessageHolder;
    }
}

