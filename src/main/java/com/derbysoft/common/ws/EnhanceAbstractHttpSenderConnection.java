package com.derbysoft.common.ws;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.ws.transport.AbstractSenderConnection;
import org.springframework.ws.transport.FaultAwareWebServiceConnection;
import org.springframework.ws.transport.http.HttpTransportConstants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

public abstract class EnhanceAbstractHttpSenderConnection extends AbstractSenderConnection implements FaultAwareWebServiceConnection {

    /**
     * Buffer used for reading the response, when the content length is invalid.
     */
    private byte[] responseBuffer;

    public final boolean hasError() throws IOException {
        return getResponseCode() / 100 != 2;
    }

    public final String getErrorMessage() throws IOException {
        StringBuilder builder = new StringBuilder();
        String responseMessage = getResponseMessage();
        if (StringUtils.hasLength(responseMessage)) {
            builder.append(responseMessage);
        }
        builder.append(" [");
        builder.append(getResponseCode());
        builder.append(']');
        return builder.toString();
    }

    /*
     * Receiving response
     */
    @Override
    protected final boolean hasResponse() throws IOException {
        int responseCode = getResponseCode();
        if (HttpTransportConstants.STATUS_ACCEPTED == responseCode ||
                HttpTransportConstants.STATUS_NO_CONTENT == responseCode) {
            return false;
        }
        long contentLength = getResponseContentLength();
        if (contentLength < 0) {
            if (responseBuffer == null) {
                responseBuffer = FileCopyUtils.copyToByteArray(getRawResponseInputStream());
            }
            contentLength = responseBuffer.length;
        }
        return contentLength > 0;
    }

    @Override
    protected final InputStream getResponseInputStream() throws IOException {
        InputStream inputStream;
        if (responseBuffer == null) {
            responseBuffer = FileCopyUtils.copyToByteArray(getRawResponseInputStream());
        }
        inputStream = new ByteArrayInputStream(responseBuffer);
        return isGzipResponse() ? new GZIPInputStream(inputStream) : inputStream;
    }

    /**
     * Determine whether the response is a GZIP response.
     */
    private boolean isGzipResponse() throws IOException {
        Iterator<String> iterator = getResponseHeaders(HttpTransportConstants.HEADER_CONTENT_ENCODING);
        if (iterator.hasNext()) {
            String encodingHeader = iterator.next();
            return encodingHeader.toLowerCase().indexOf(HttpTransportConstants.CONTENT_ENCODING_GZIP) != -1;
        }
        return false;
    }

    /**
     * Returns the HTTP status code of the response.
     */
    protected abstract int getResponseCode() throws IOException;

    /**
     * Returns the HTTP status message of the response.
     */
    protected abstract String getResponseMessage() throws IOException;

    /**
     * Returns the length of the response.
     */
    protected abstract long getResponseContentLength() throws IOException;

    /**
     * Returns the raw, possibly compressed input stream to read the response from.
     */
    protected abstract InputStream getRawResponseInputStream() throws IOException;

    /*
     * Faults
     */

    public final boolean hasFault() throws IOException {
        return HttpTransportConstants.STATUS_INTERNAL_SERVER_ERROR == getResponseCode() && isXmlResponse();
    }

    /**
     * Determine whether the response is a XML message.
     */
    private boolean isXmlResponse() throws IOException {
        Iterator<String> iterator = getResponseHeaders(HttpTransportConstants.HEADER_CONTENT_TYPE);
        if (iterator.hasNext()) {
            String contentType = iterator.next().toLowerCase();
            return contentType.indexOf("xml") != -1;
        }
        return false;
    }

    public final void setFault(boolean fault) {
    }

}
