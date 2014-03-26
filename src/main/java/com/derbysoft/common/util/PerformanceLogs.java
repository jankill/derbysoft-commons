package com.derbysoft.common.util;

import com.derbysoft.common.util.date.Dates;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class PerformanceLogs {

    protected StringBuilder stringBuilder = new StringBuilder();

    public void append(Object name, Date date) {
        if (date == null) {
            return;
        }
        appendInternal(name, Dates.format(date));
    }

    public void append(Object name, Object value) {
        if (value == null) {
            return;
        }
        appendInternal(name, escape(value.toString()));
    }

    protected void appendInternal(Object name, String value) {
        stringBuilder.append("<").append(name).append("=").append(value).append("> ");
    }

    private String escape(String input) {
        String output = input;
        output = StringUtils.replace(output, "\r", "&#13;");
        output = StringUtils.replace(output, "\n", "&#10;");
        output = StringUtils.replace(output, "<", "&lt;");
        output = StringUtils.replace(output, ">", "&gt;");
        output = StringUtils.replace(output, "=", "&eq;");
        return output;
    }

    @Override
    public String toString() {
        return stringBuilder.toString().trim();
    }

}
