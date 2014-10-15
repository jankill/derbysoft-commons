package com.derbysoft.common.util;

public class SeparatorStringBuilder {

    private StringBuilder stringBuilder = new StringBuilder();

    private boolean isFirst = true;

    private String separator;

    public SeparatorStringBuilder(String separator) {
        this.separator = separator;
    }

    public void append(Object value) {
        append(value, separator);
    }

    public void append(Object value, String separator) {
        if (!isFirst) {
            stringBuilder.append(separator);
        }
        stringBuilder.append(value);
        isFirst = false;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
