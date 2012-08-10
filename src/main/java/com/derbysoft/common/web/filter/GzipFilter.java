package com.derbysoft.common.web.filter;

import com.derbysoft.common.web.filter.support.GenericResponseWrapper;
import com.derbysoft.common.web.filter.support.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

public class GzipFilter extends Filter {

    private static final Logger LOG = LoggerFactory.getLogger(GzipFilter.class);
    private static final String VARY_HEADER_PARAM = "varyHeader";
    private static final String RETURN_ON_NOT_OK_PARAM = "returnOnNonOK";

    private boolean setVaryHeader;
    private boolean returnOnNonOk = true;

    protected void doInit(FilterConfig filterConfig) throws Exception {
        String varyParam = filterConfig.getInitParameter(VARY_HEADER_PARAM);
        if (varyParam != null) {
            setVaryHeader = Boolean.valueOf(varyParam);
        }

        String returnOnNotOkParam = filterConfig.getInitParameter(RETURN_ON_NOT_OK_PARAM);
        if (returnOnNotOkParam != null) {
            returnOnNonOk = Boolean.valueOf(returnOnNotOkParam);
        }
    }

    protected void doDestroy() {
    }

    protected void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws Exception {
        if (!isIncluded(request) && acceptsEncoding(request, "gzip") && !response.isCommitted()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(request.getRequestURL() + ". Writing with gzip compression");
            }
            final ByteArrayOutputStream compressed = new ByteArrayOutputStream();
            final GZIPOutputStream gzout = new GZIPOutputStream(compressed);

            final GenericResponseWrapper wrapper = new GenericResponseWrapper(response, gzout);
            wrapper.setDisableFlushBuffer(true);
            chain.doFilter(request, wrapper);
            wrapper.flush();
            gzout.close();
            if (response.isCommitted()) {
                return;
            }
            switch (wrapper.getStatus()) {
                case HttpServletResponse.SC_NO_CONTENT:
                case HttpServletResponse.SC_RESET_CONTENT:
                case HttpServletResponse.SC_NOT_MODIFIED:
                    return;
                default:
            }
            byte[] compressedBytes = compressed.toByteArray();
            boolean shouldGzippedBodyBeZero = ResponseUtil.shouldGzippedBodyBeZero(compressedBytes, request);
            boolean shouldBodyBeZero = ResponseUtil.shouldBodyBeZero(request, wrapper.getStatus());
            if (shouldGzippedBodyBeZero || shouldBodyBeZero) {
                response.setContentLength(0);
                return;
            }
            ResponseUtil.addGzipHeader(response);
            if (setVaryHeader) {
                ResponseUtil.addVaryAcceptEncoding(wrapper);
            }
            response.setContentLength(compressedBytes.length);
            response.getOutputStream().write(compressedBytes);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug(request.getRequestURL() + ". Writing without gzip compression because the request does not accept gzip.");
            }
            chain.doFilter(request, response);
        }
    }

    private boolean isIncluded(final HttpServletRequest request) {
        String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
        boolean includeRequest = !(uri == null);
        if (includeRequest && LOG.isDebugEnabled()) {
            LOG.debug(request.getRequestURL() + " resulted in an include request. This is unusable, because the response will be assembled into the overrall response. Not gzipping.");
        }
        return includeRequest;
    }

    protected boolean acceptsGzipEncoding(HttpServletRequest request) {
        return acceptsEncoding(request, "gzip");
    }

}
