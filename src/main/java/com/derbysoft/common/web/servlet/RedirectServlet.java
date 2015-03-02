package com.derbysoft.common.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.*;

public class RedirectServlet extends HttpServlet {
    private static final Log log = LogFactory.getLog(RedirectServlet.class);

    public static final String REDIRECT_URL_KEY = "Redirect-URL";
    public static final String REDIRECT_METHOD = "Redirect-Method";
    public static final String REDIRECT_HEADERS = "Redirect-Headers";

    private int connectionTimeout = 60000;
    private int readTimeout = 60000;

    protected Set<String> getSkipHeaders() {
        Set<String> headers = new HashSet<String>();
        headers.add(REDIRECT_URL_KEY);
        return headers;
    }

    @Override
    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String redirectURL = getRedirectURL(httpServletRequest);
        String method = getRedirectMethod(httpServletRequest);
        Map<String, Object> headers = getHeaders(httpServletRequest);

        HttpURLConnection connection = null;
        HttpsURLConnection.setDefaultHostnameVerifier(new AlwayTrustHostnameVerifier());
        HttpsURLConnection.setDefaultSSLSocketFactory(new AlwaysTrustSSLSocketFactory());
        try {
            URL url = new URL(redirectURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(connectionTimeout);
            connection.setReadTimeout(readTimeout);
            for (String headerName : headers.keySet()) {
                if (getSkipHeaders().contains(headerName)) {
                    continue;
                }
                if ("host".equalsIgnoreCase(headerName)) {
                    continue;
                }
                if (headers.get(headerName) instanceof String) {
                    connection.setRequestProperty(headerName, (String) headers.get(headerName));
                    continue;
                }
                List<String> headerValues = (List<String>) headers.get(headerName);
                for (String headerValue : headerValues) {
                    connection.setRequestProperty(headerName, headerValue);
                }
            }
            if ("POST".equals(httpServletRequest.getMethod()) || "PUT".equals(httpServletRequest.getMethod())) {
                InputStream inputStream = httpServletRequest.getInputStream();
                IOUtils.copy(inputStream, connection.getOutputStream());
                connection.getOutputStream().flush();
                connection.getOutputStream().close();
            }

            Map<String, List<String>> headerFields = connection.getHeaderFields();
            for (String headerName : headerFields.keySet()) {
                List<String> headerValue = headerFields.get(headerName);

                if (headerName == null || "Transfer-Encoding".equals(headerName)) {
                    continue;
                }
                for (String value : headerValue) {
                    if (value != null && !"".equals(value)) {
                        httpServletResponse.setHeader(headerName, value);
                    }
                }
            }
            int code = connection.getResponseCode();

            if (code != HttpServletResponse.SC_OK) {
                httpServletResponse.sendError(connection.getResponseCode());
                if (log.isErrorEnabled()) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    InputStream inputStream = connection.getErrorStream();
                    if (inputStream == null) {
                        inputStream = connection.getInputStream();
                    }
                    if (inputStream != null) {
                        IOUtils.copy(inputStream, outputStream);
                    }
                    log.error("Redirect to [" + redirectURL + "] with response code [" + code + "],Reason:[" + outputStream.toString()
                            + "]");
                }
                return;
            }
            httpServletResponse.setStatus(connection.getResponseCode());
            IOUtils.copy(connection.getInputStream(), httpServletResponse.getOutputStream());
        } catch (Exception e) {
            log.error("redirect [" + redirectURL + "] fail", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    protected String getRedirectURL(HttpServletRequest httpServletRequest) {
        String redirectUrl = httpServletRequest.getHeader(REDIRECT_URL_KEY);
        if (redirectUrl == null) {
            redirectUrl = httpServletRequest.getParameter(REDIRECT_URL_KEY);
        }
        return redirectUrl;
    }

    private String getRedirectMethod(HttpServletRequest httpServletRequest) {
        String method = httpServletRequest.getHeader(REDIRECT_METHOD);
        if (method == null) {
            method = httpServletRequest.getParameter(REDIRECT_METHOD);
        }
        if (method == null) {
            method = httpServletRequest.getMethod();
        }
        return method.toUpperCase();
    }

    private Map<String, Object> getHeaders(HttpServletRequest httpServletRequest) {
        Map<String, Object> headers;
        String headerValueString = httpServletRequest.getHeader(REDIRECT_HEADERS);
        if (headerValueString == null) {
            headerValueString = httpServletRequest.getParameter(REDIRECT_HEADERS);
        }
        if (headerValueString != null) {
            try {
                JSONObject jsonObject = JSON.parseObject(headerValueString);
                headers = new HashMap<String, Object>();
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    headers.put(entry.getKey(), entry.getValue());
                }
                return headers;
            } catch (Exception e) {
                throw new RuntimeException("Illegal headers format", e);
            }
        }
        headers = new HashMap<String, Object>();
        Enumeration<?> headerNameEnumeration = httpServletRequest.getHeaderNames();
        for (; headerNameEnumeration.hasMoreElements(); ) {
            String headerName = (String) headerNameEnumeration.nextElement();
            if (getSkipHeaders().contains(headerName)) {
                continue;
            }
            if ("host".equalsIgnoreCase(headerName)) {
                continue;
            }
            headers.put(headerName, new ArrayList<String>());
            Enumeration headerValueEnumeration = httpServletRequest.getHeaders(headerName);
            for (; headerValueEnumeration.hasMoreElements(); ) {
                String value = (String) headerValueEnumeration.nextElement();
                ((ArrayList) headers.get(headerName)).add(value);
            }
        }
        return headers;
    }

    final class AlwayTrustHostnameVerifier implements HostnameVerifier {
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }

    final class AlwaysTrustSSLSocketFactory extends SSLSocketFactory {
        private SSLSocketFactory sslSocketFactory;

        public AlwaysTrustSSLSocketFactory() {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new AlwaysX509TrustManager()}, new java.security.SecureRandom());
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (KeyManagementException e) {
                throw new RuntimeException(e);
            }
        }

        public String[] getDefaultCipherSuites() {
            return sslSocketFactory.getDefaultCipherSuites();
        }

        public String[] getSupportedCipherSuites() {
            return sslSocketFactory.getSupportedCipherSuites();
        }

        public Socket createSocket(Socket socket, String s, int i, boolean b) throws IOException {
            return sslSocketFactory.createSocket(socket, s, i, b);
        }

        public Socket createSocket(String s, int i) throws IOException {
            return sslSocketFactory.createSocket(s, i);
        }

        public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws IOException {
            return sslSocketFactory.createSocket(s, i, inetAddress, i1);
        }

        public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
            return sslSocketFactory.createSocket(inetAddress, i);
        }

        public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) throws IOException {
            return sslSocketFactory.createSocket(inetAddress, i, inetAddress1, i1);
        }

        public Socket createSocket() throws IOException {
            return sslSocketFactory.createSocket();
        }
    }

    final class AlwaysX509TrustManager implements X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {
        }
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
