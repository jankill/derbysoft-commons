package com.derbysoft.common.util;

import com.derbysoft.common.util.collect.Collections;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ExceptionUtils {
    final static String EOL = "\n";
    public static final String KEY_WORDS_KEY = "com.derbysoft.common.util.ExceptionUtils.keyWords";
    public static final List<String> DEFAULT_KEY_WORDS = Arrays.asList("com.derbysoft");

    public static final String MIN_LINE_COUNT_KEY = "com.derbysoft.common.util.ExceptionUtils.minLineCount";
    public static final int DEFAULT_MIN_LINE_COUNT = 3;

    public static final String MAX_LINE_COUNT_KEY = "com.derbysoft.common.util.ExceptionUtils.maxLineCount";
    public static final int DEFAULT_MAX_LINE_COUNT = 20;


    public static final String SEPARATOR = ",";

    public static String toString(Throwable throwable) {
        return toString(throwable, getKeyWords());
    }

    public static String toString(Throwable throwable, Collection<String> keyWords) {
        try {
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            BufferedReader reader = new BufferedReader(new StringReader(sw.toString()));
            StringBuilder sb = new StringBuilder();
            sb.append(reader.readLine());

            int lineCount = 1;
            int minLineCount = getMinLineCount();
            int maxLineCount = getMaxLineCount();
            while (true) {
                String line = reader.readLine();
                if (line == null || lineCount >= maxLineCount) {
                    break;
                }

                if (lineCount < minLineCount) {
                    lineCount++;
                    sb.append(EOL).append(line);
                    continue;
                }
                if (!containsAny(line, keyWords)) {
                    continue;
                }
                lineCount++;
                sb.append(EOL).append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private static boolean containsAny(String line, Collection<String> keyWords) {
        if (Collections.isEmpty(keyWords)) {
            return true;
        }
        for (String keyWord : keyWords) {
            if (line.contains(keyWord)) {
                return true;
            }
        }
        return false;
    }

    private static Collection<String> getKeyWords() {
        if (System.getProperties().containsKey(KEY_WORDS_KEY)) {
            List<String> keyWords = new ArrayList<String>();
            String property = StringUtils.trimToEmpty(System.getProperty(KEY_WORDS_KEY));
            for (String value : StringUtils.split(property, SEPARATOR)) {
                if (StringUtils.isBlank(value)) {
                    continue;
                }
                keyWords.add(value);
            }
            return keyWords;
        } else {
            return DEFAULT_KEY_WORDS;
        }
    }

    public static int getMinLineCount() {
        try {
            if (System.getProperties().containsKey(MIN_LINE_COUNT_KEY)) {
                return Integer.parseInt(System.getProperty(MIN_LINE_COUNT_KEY));
            }
            return DEFAULT_MIN_LINE_COUNT;
        } catch (Exception e) {
            return DEFAULT_MIN_LINE_COUNT;
        }
    }

    public static int getMaxLineCount() {
        try {
            if (System.getProperties().containsKey(MAX_LINE_COUNT_KEY)) {
                return Integer.parseInt(System.getProperty(MAX_LINE_COUNT_KEY));
            }
            return DEFAULT_MAX_LINE_COUNT;
        } catch (Exception e) {
            return DEFAULT_MAX_LINE_COUNT;
        }
    }
}