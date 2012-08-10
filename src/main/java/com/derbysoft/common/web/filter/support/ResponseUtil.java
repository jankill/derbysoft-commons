package com.derbysoft.common.web.filter.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Collection;

public final class ResponseUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseUtil.class);

    private static final int EMPTY_GZIPPED_CONTENT_SIZE = 20;

    public static boolean shouldGzippedBodyBeZero(byte[] compressedBytes, HttpServletRequest request) {
        if (compressedBytes.length == EMPTY_GZIPPED_CONTENT_SIZE) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(request.getRequestURL() + " resulted in an empty response.");
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean shouldBodyBeZero(HttpServletRequest request, int responseStatus) {
        if (responseStatus == HttpServletResponse.SC_NO_CONTENT) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(request.getRequestURL() + " resulted in a " + HttpServletResponse.SC_NO_CONTENT + " response. Removing message body in accordance with RFC2616.");
            }
            return true;
        }

        if (responseStatus == HttpServletResponse.SC_NOT_MODIFIED) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(request.getRequestURL() + " resulted in a " + HttpServletResponse.SC_NOT_MODIFIED + " response. Removing message body in accordance with RFC2616.");
            }
            return true;
        }
        return false;
    }

    public static void addGzipHeader(final HttpServletResponse response) {
        response.setHeader("Content-Encoding", "gzip");
        boolean containsEncoding = response.containsHeader("Content-Encoding");
        if (!containsEncoding) {
            throw new RuntimeException("Failure when attempting to set " + "Content-Encoding: gzip");
        }
    }

    public static void addVaryAcceptEncoding(final GenericResponseWrapper wrapper) {
        Collection<Header<? extends Serializable>> headers = wrapper.getAllHeaders();
        Header<? extends Serializable> varyHeader = null;
        for (Header<? extends Serializable> header : headers) {
            if (header.getName().equals("Vary")) {
                varyHeader = header;
                break;
            }
        }
        if (varyHeader == null) {
            wrapper.setHeader("Vary", "Accept-Encoding");
        } else {
            String varyValue = varyHeader.getValue().toString();
            if (!varyValue.equals("*") && !varyValue.contains("Accept-Encoding")) {
                wrapper.setHeader("Vary", varyValue + ",Accept-Encoding");
            }
        }
    }

}
