package com.derbysoft.common.web.filter.support;

import org.apache.commons.lang3.StringUtils;

public enum TrimModel {
    TRIM {
        @Override
        public String trim(String parameter) {
            return StringUtils.trim(parameter);
        }
    },
    TRIM_TO_NULL {
        @Override
        public String trim(String parameter) {
            return StringUtils.trimToNull(parameter);
        }
    },
    TRIM_TO_EMPTY {
        @Override
        public String trim(String parameter) {
            return StringUtils.trimToEmpty(parameter);
        }
    };

    public abstract String trim(String parameter);
}