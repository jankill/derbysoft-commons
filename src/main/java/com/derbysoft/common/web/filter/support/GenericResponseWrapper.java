package com.derbysoft.common.web.filter.support;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.*;

public class GenericResponseWrapper extends HttpServletResponseWrapper {

    private int statusCode = SC_OK;
    private int contentLength;
    private String contentType;
    private final Map<String, List<Serializable>> headersMap = new TreeMap<String, List<Serializable>>(String.CASE_INSENSITIVE_ORDER);
    private final List cookies = new ArrayList();
    private ServletOutputStream outstr;
    private PrintWriter writer;
    private boolean disableFlushBuffer = true;

    public GenericResponseWrapper(HttpServletResponse response, OutputStream outstr) {
        super(response);
        this.outstr = new FilterServletOutputStream(outstr);
    }

    public ServletOutputStream getOutputStream() {
        return outstr;
    }

    public void setStatus(int code) {
        statusCode = code;
        super.setStatus(code);
    }

    public void sendError(int i, String string) throws IOException {
        statusCode = i;
        super.sendError(i, string);
    }

    public void sendError(int i) throws IOException {
        statusCode = i;
        super.sendError(i);
    }

    public void sendRedirect(String string) throws IOException {
        statusCode = HttpServletResponse.SC_MOVED_TEMPORARILY;
        super.sendRedirect(string);
    }

    public int getStatus() {
        return statusCode;
    }

    public void setContentLength(int length) {
        this.contentLength = length;
        super.setContentLength(length);
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentType(String type) {
        this.contentType = type;
        super.setContentType(type);
    }

    public String getContentType() {
        return contentType;
    }

    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(outstr, getCharacterEncoding()), true);
        }
        return writer;
    }

    @Override
    public void addHeader(String name, String value) {
        List<Serializable> values = this.headersMap.get(name);
        if (values == null) {
            values = new LinkedList<Serializable>();
            this.headersMap.put(name, values);
        }
        values.add(value);
        super.addHeader(name, value);
    }

    @Override
    public void setHeader(String name, String value) {
        List<Serializable> values = new LinkedList<Serializable>();
        values.add(value);
        this.headersMap.put(name, values);
        super.setHeader(name, value);
    }

    @Override
    public void addDateHeader(String name, long date) {
        List<Serializable> values = this.headersMap.get(name);
        if (values == null) {
            values = new LinkedList<Serializable>();
            this.headersMap.put(name, values);
        }
        values.add(date);
        super.addDateHeader(name, date);
    }

    @Override
    public void setDateHeader(String name, long date) {
        List<Serializable> values = new LinkedList<Serializable>();
        values.add(date);
        this.headersMap.put(name, values);
        super.setDateHeader(name, date);
    }

    @Override
    public void addIntHeader(String name, int value) {
        List<Serializable> values = this.headersMap.get(name);
        if (values == null) {
            values = new LinkedList<Serializable>();
            this.headersMap.put(name, values);
        }
        values.add(value);
        super.addIntHeader(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        List<Serializable> values = new LinkedList<Serializable>();
        values.add(value);
        this.headersMap.put(name, values);
        super.setIntHeader(name, value);
    }

    public Collection<Header<? extends Serializable>> getAllHeaders() {
        List<Header<? extends Serializable>> headers = new LinkedList<Header<? extends Serializable>>();
        for (final Map.Entry<String, List<Serializable>> headerEntry : this.headersMap.entrySet()) {
            final String name = headerEntry.getKey();
            for (final Serializable value : headerEntry.getValue()) {
                final Header.Type type = Header.Type.determineType(value.getClass());
                switch (type) {
                    case STRING:
                        headers.add(new Header<String>(name, (String) value));
                        break;
                    case DATE:
                        headers.add(new Header<Long>(name, (Long) value));
                        break;
                    case INT:
                        headers.add(new Header<Integer>(name, (Integer) value));
                        break;
                    default:
                        throw new IllegalArgumentException("No mapping for Header.Type: " + type);
                }
            }
        }

        return headers;
    }

    @Override
    public void addCookie(final Cookie cookie) {
        cookies.add(cookie);
        super.addCookie(cookie);
    }

    public Collection getCookies() {
        return cookies;
    }

    @Override
    public void flushBuffer() throws IOException {
        flush();
        if (!disableFlushBuffer) {
            super.flushBuffer();
        }
    }

    @Override
    public void reset() {
        super.reset();
        cookies.clear();
        headersMap.clear();
        statusCode = SC_OK;
        contentType = null;
        contentLength = 0;
    }

    public void flush() throws IOException {
        if (writer != null) {
            writer.flush();
        }
        outstr.flush();
    }

    public boolean isDisableFlushBuffer() {
        return disableFlushBuffer;
    }

    public void setDisableFlushBuffer(boolean disableFlushBuffer) {
        this.disableFlushBuffer = disableFlushBuffer;
    }
}

