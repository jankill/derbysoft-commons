package com.derbysoft.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompressUtils.class);
    public static final String CHARSET = "ISO-8859-1";
    public static final int BUFFER_SIZE = 4096;
    public static final String ZIP_FLAG = String.valueOf(new char[]{Character.MIN_VALUE, Character.MAX_VALUE});
    public static final int DEFAULT_COMPRESS_THRESHOLD = 500;

    public static String smartZip(String input, int compressThreshold) {
        if (StringUtils.length(input) < compressThreshold) {
            return input;
        }
        String output = ZIP_FLAG + zip(input);
        if (output.length() > input.length()) {
            return input;
        }
        return output;
    }

    public static String smartZip(String input) {
        return smartZip(input, DEFAULT_COMPRESS_THRESHOLD);
    }

    public static String smartUnzip(String input) {
        if (StringUtils.startsWith(input, ZIP_FLAG)) {
            return unzip(input.substring(ZIP_FLAG.length()));
        }
        return input;
    }

    public static String zip(String input) {
        try {
            if (StringUtils.isEmpty(input)) {
                return input;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(input.getBytes());
            gzip.close();
            return out.toString(CHARSET);
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.toString(e));
            return null;
        }
    }

    public static String unzip(String input) {
        try {
            if (StringUtils.isEmpty(input)) {
                return input;
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            GZIPInputStream is = new GZIPInputStream(new ByteArrayInputStream(input.getBytes(CHARSET)));
            byte[] buffer = new byte[BUFFER_SIZE];
            int readCount;
            while ((readCount = is.read(buffer)) >= 0) {
                os.write(buffer, 0, readCount);
            }
            return os.toString();
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.toString(e));
            return null;
        }
    }
}
