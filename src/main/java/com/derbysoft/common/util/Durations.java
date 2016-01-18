package com.derbysoft.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.time.Duration;

public class Durations {
    public static Duration of(Object input) {
        return of(input, null);
    }
    public static Duration of(Object input, Duration defaultValue) {
        try {
            Validate.notNull(input, "Input can not be null");
            if (StringUtils.startsWith(input.toString(), "P")) {
                return Duration.parse(input.toString());
            }
            return Duration.ofMillis(Long.valueOf(input.toString()));
        } catch (Exception e) {
            if (defaultValue == null) {
                throw e;
            }
            return defaultValue;
        }
    }
}
