package com.derbysoft.common.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public abstract class ServletFilter implements javax.servlet.Filter {

    public static final String NO_FILTER = "NO_FILTER";

    protected FilterConfig filterConfig;

    protected String exceptionsToLogDifferently;

    protected boolean suppressStackTraces;

    private static final Logger LOG = LoggerFactory.getLogger(ServletFilter.class);

    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            if (filterNotDisabled(httpRequest)) {
                doFilter(httpRequest, httpResponse, chain);
            } else {
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            logThrowable(e, httpRequest);
        }
    }

    protected boolean filterNotDisabled(HttpServletRequest httpRequest) {
        return httpRequest.getAttribute(NO_FILTER) == null;
    }

    private void logThrowable(Throwable throwable, HttpServletRequest httpRequest) throws ServletException, IOException {
        StringBuffer messageBuffer = new StringBuffer("Throwable thrown during doFilter on request with URI: ")
                .append(httpRequest.getRequestURI())
                .append(" and Query: ")
                .append(httpRequest.getQueryString());
        String message = messageBuffer.toString();
        boolean matchFound = matches(throwable);
        if (matchFound) {
            try {
                if (suppressStackTraces) {
                    LOG.error(throwable.getMessage());
                } else {
                    LOG.error(throwable.getMessage(), throwable);
                }
            } catch (Exception e) {
                LOG.error("Could not invoke Log method", e);
            }
            if (throwable instanceof IOException) {
                throw (IOException) throwable;
            } else {
                throw new ServletException(message, throwable);
            }
        } else {
            if (suppressStackTraces) {
                LOG.warn(messageBuffer.append(throwable.getMessage()).append("\nTop StackTraceElement: ")
                        .append(throwable.getStackTrace()[0].toString()).toString());
            } else {
                LOG.warn(messageBuffer.append(throwable.getMessage()).toString(), throwable);
            }
            if (throwable instanceof IOException) {
                throw (IOException) throwable;
            }
            throw new ServletException(throwable);
        }
    }

    private boolean matches(Throwable throwable) {
        if (exceptionsToLogDifferently == null) {
            return false;
        }
        if (exceptionsToLogDifferently.indexOf(throwable.getClass().getName()) != -1) {
            return true;
        }
        if (throwable instanceof ServletException) {
            Throwable rootCause = (((ServletException) throwable).getRootCause());
            if (exceptionsToLogDifferently.indexOf(rootCause.getClass().getName()) != -1) {
                return true;
            }
        }
        if (throwable.getCause() != null) {
            Throwable cause = throwable.getCause();
            if (exceptionsToLogDifferently.indexOf(cause.getClass().getName()) != -1) {
                return true;
            }
        }
        return false;
    }

    public final void init(final FilterConfig filterConfig) throws ServletException {
        try {

            this.filterConfig = filterConfig;
            processInitParams(filterConfig);

            doInit(filterConfig);
        } catch (Exception e) {
            LOG.error("Could not initialise servlet filter.", e);
            throw new ServletException("Could not initialise servlet filter.", e);
        }
    }

    protected void processInitParams(FilterConfig config) throws ServletException {
        String exceptions = config.getInitParameter("exceptionsToLogDifferently");
        String level = config.getInitParameter("exceptionsToLogDifferentlyLevel");
        String suppressStackTracesString = config.getInitParameter("suppressStackTraces");
        suppressStackTraces = Boolean.valueOf(suppressStackTracesString).booleanValue();
        LOG.debug("Suppression of stack traces enabled for " + this.getClass().getName());

        if (exceptions != null) {
            validateMandatoryParameters(exceptions, level);
            exceptionsToLogDifferently = exceptions;
            LOG.debug("Different logging levels configured for " + this.getClass().getName());
        }
    }

    private void validateMandatoryParameters(String exceptions, String level) throws ServletException {
        if ((exceptions != null && level == null) || (level != null && exceptions == null)) {
            throw new ServletException("Invalid init-params. Both exceptionsToLogDifferently"
                    + " and exceptionsToLogDifferentlyLevelvalue should be specified if one is"
                    + " specified.");
        }
    }

    public final void destroy() {
        this.filterConfig = null;
        doDestroy();
    }

    protected boolean acceptsEncoding(HttpServletRequest request, String name) {
        return headerContains(request, "Accept-Encoding", name);
    }

    private boolean headerContains(HttpServletRequest request, String header, String value) {
        logRequestHeaders(request);
        final Enumeration accepted = request.getHeaders(header);
        while (accepted.hasMoreElements()) {
            final String headerValue = (String) accepted.nextElement();
            if (headerValue.indexOf(value) != -1) {
                return true;
            }
        }
        return false;
    }

    protected void logRequestHeaders(HttpServletRequest request) {
        if (LOG.isDebugEnabled()) {
            Map headers = new HashMap();
            Enumeration enumeration = request.getHeaderNames();
            StringBuffer logLine = new StringBuffer();
            logLine.append("Request Headers");
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                String headerValue = request.getHeader(name);
                headers.put(name, headerValue);
                logLine.append(": ").append(name).append(" -> ").append(headerValue);
            }
            LOG.debug(logLine.toString());
        }
    }

    protected abstract void doDestroy();

    protected abstract void doFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain chain) throws IOException, ServletException;

    protected abstract void doInit(FilterConfig filterConfig);

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    protected boolean acceptsGzipEncoding(HttpServletRequest request) {
        return acceptsEncoding(request, "gzip");
    }

}

