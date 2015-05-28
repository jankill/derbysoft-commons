package com.derbysoft.common.ws;

import com.derbysoft.common.util.IOUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.ws.WebServiceMessage;

import javax.xml.namespace.QName;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class EnhanceHttpUrlConnection extends EnhanceAbstractHttpSenderConnection {

    private final HttpURLConnection connection;
    private final WebServiceMessageHolder webServiceMessageHolder;

    protected EnhanceHttpUrlConnection(HttpURLConnection connection, WebServiceMessageHolder webServiceMessageHolder) {
        this.webServiceMessageHolder = webServiceMessageHolder;
        Assert.notNull(connection, "connection must not be null");
        this.connection = connection;
    }

    public HttpURLConnection getConnection() {
        return connection;
    }

    @Override
    public void onClose() {
        connection.disconnect();
    }

    public URI getUri() throws URISyntaxException {
        return new URI(StringUtils.replace(connection.getURL().toString(), " ", "%20"));
    }

    @Override
    protected void addRequestHeader(String name, String value) throws IOException {
        connection.addRequestProperty(name, value);
    }

    @Override
    protected OutputStream getRequestOutputStream() throws IOException {
        return connection.getOutputStream();
    }

    @Override
    protected void onSendAfterWrite(WebServiceMessage message) throws IOException {
        connection.connect();
    }

    @Override
    protected void onSendBeforeWrite(WebServiceMessage message) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        message.writeTo(os);
        os.flush();

        if (webServiceMessageHolder != null) {
            webServiceMessageHolder.holdRequest(new String(os.toByteArray(), "UTF-8"));
        }

        super.onSendBeforeWrite(message);
    }

    @Override
    protected void onReceiveBeforeRead() throws IOException {
        if (webServiceMessageHolder != null) {
            webServiceMessageHolder.holdResponse(new String(IOUtils.readAsBytes(getResponseInputStream()), "UTF-8"));
        }
        super.onReceiveBeforeRead();
    }

    @Override
    protected long getResponseContentLength() throws IOException {
        return connection.getContentLength();
    }

    @Override
    protected Iterator<String> getResponseHeaderNames() throws IOException {
        List<String> headerNames = new ArrayList<String>();
        // Header field 0 is the status line, so we start at 1
        int i = 1;
        while (true) {
            String headerName = connection.getHeaderFieldKey(i);
            if (!StringUtils.hasLength(headerName)) {
                break;
            }
            headerNames.add(headerName);
            i++;
        }
        return headerNames.iterator();
    }

    @Override
    protected Iterator<String> getResponseHeaders(String name) throws IOException {
        String headerField = connection.getHeaderField(name);
        if (headerField == null) {
            return Collections.<String>emptyList().iterator();
        } else {
            Set<String> tokens = StringUtils.commaDelimitedListToSet(headerField);
            return tokens.iterator();
        }
    }

    @Override
    protected int getResponseCode() throws IOException {
        return connection.getResponseCode();
    }

    @Override
    protected String getResponseMessage() throws IOException {
        return connection.getResponseMessage();
    }

    @Override
    protected InputStream getRawResponseInputStream() throws IOException {
        if (connection.getResponseCode() / 100 != 2) {
            return connection.getErrorStream();
        } else {
            return connection.getInputStream();
        }
    }

    @Override
    public void setFaultCode(QName qName) throws IOException {

    }
}
