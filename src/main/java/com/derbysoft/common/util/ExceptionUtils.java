package com.derbysoft.common.util;

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
    public static final String PROPERTY_KEY = "com.derbysoft.common.util.ExceptionUtils.keyWords";
    public static final List<String> DEFAULT_KEY_WORDS = Arrays.asList("com.derbysoft");
    public static final String SEPARATOR = ",";

    public static final String[] SEARCH_LIST = new String[]{";", " "};
    public static final String[] REPLACEMENT_LIST = new String[]{SEPARATOR, SEPARATOR};

    public static String toString(Exception exception) {
        return toString(exception, getKeyWords());
    }

    public static String toString(Exception exception, Collection<String> keyWords) {
        try {
            StringWriter sw = new StringWriter();
            exception.printStackTrace(new PrintWriter(sw));
            BufferedReader reader = new BufferedReader(new StringReader(sw.toString()));
            StringBuilder sb = new StringBuilder();
            sb.append(reader.readLine());

            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                if (!containsAny(line, keyWords)) {
                    continue;
                }

                sb.append(EOL).append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private static boolean containsAny(String line, Collection<String> keyWords) {
        for (String keyWord : keyWords) {
            if (line.contains(keyWord)) {
                return true;
            }
        }
        return false;
    }

    private static Collection<String> getKeyWords() {
        if (System.getProperties().containsKey(PROPERTY_KEY)) {
            List<String> keyWords = new ArrayList<String>();
            String property = StringUtils.replaceEach(System.getProperty(PROPERTY_KEY), SEARCH_LIST, REPLACEMENT_LIST);
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
}