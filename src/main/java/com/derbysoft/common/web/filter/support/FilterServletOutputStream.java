package com.derbysoft.common.web.filter.support;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FilterServletOutputStream extends ServletOutputStream {

    private OutputStream stream;

    public FilterServletOutputStream(final OutputStream stream) {
        this.stream = stream;
    }

    public void write(final int b) throws IOException {
        stream.write(b);
    }

    public void write(final byte[] b) throws IOException {
        stream.write(b);
    }

    public void write(final byte[] b, final int off, final int len) throws IOException {
        stream.write(b, off, len);
    }
}
