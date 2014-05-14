package com.derbysoft.common.util.date;

import org.apache.commons.lang3.Validate;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.*;

public class DateRangeIterator implements Iterable<LocalDate>, Iterator<LocalDate> {
    public enum Option {
        EXCLUDE_FROM{
            @Override
            void apply(DateRangeIterator iterator) {
                iterator.offset += STEP;
            }
        },
        EXCLUDE_TO {
            @Override
            void apply(DateRangeIterator iterator) {
                iterator.max -= STEP;
            }
        };

        abstract void apply(DateRangeIterator iterator);
    }
    public static final int STEP = 1;

    private LocalDate from;

    private int offset = 0;
    private int max;

    public static DateRangeIterator of(LocalDate from, LocalDate to, Option ... options) {
        validate(from, to);
        return new DateRangeIterator(from, to, options);
    }

    public static DateRangeIterator of(Date from, Date to, Option ... options) {
        validate(from, to);
        return new DateRangeIterator(LocalDates.of(from), LocalDates.of(to), options);
    }

    public static DateRangeIterator of(String from, String to, Option ... options) {
        validate(from, to);
        return new DateRangeIterator(LocalDates.of(from), LocalDates.of(to), options);
    }

    private static void validate(Object from, Object to) {
        Validate.notNull(from, "From is required");
        Validate.notNull(to, "To is required");
    }

    private DateRangeIterator(LocalDate from, LocalDate to, Option ... options) {
        this.from = from;
        this.max = Days.daysBetween(from, to).getDays();
        for (Option option : options) {
            option.apply(this);
        }
    }

    @Override
    public boolean hasNext() {
        return offset <= max;
    }

    @Override
    public LocalDate next() {
        return from.plusDays(offset++);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<LocalDate> iterator() {
        return this;
    }
}
