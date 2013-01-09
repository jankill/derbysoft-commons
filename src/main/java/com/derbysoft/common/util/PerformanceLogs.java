package com.derbysoft.common.util;

public abstract class PerformanceLogs {

    public static void append(StringBuilder builder, Object name, Object value) {
        builder.append("<").append(name).append("=").append(value).append("> ");
    }

    public static String get(StringBuilder builder) {
        return builder.toString();
    }

}
