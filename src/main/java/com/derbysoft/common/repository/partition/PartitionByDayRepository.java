package com.derbysoft.common.repository.partition;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class PartitionByDayRepository<T> extends AbstractTablePartitionRepository<T> {
    @Override
    protected String getTableSuffixForSave(T t) {
        return DateTimeUtils.formatDate(DateTimeUtils.today());
    }

    private static class DateTimeUtils {
        private static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {
            protected synchronized SimpleDateFormat initialValue() {
                return new SimpleDateFormat("yyyyMMdd");
            }
        };

        private static SimpleDateFormat getDayFormatter() {
            return threadLocal.get();
        }

        public static String formatDate(Date date) {
            return getDayFormatter().format(date);
        }

        private static Date today() {
            return new Date();
        }
    }
}
