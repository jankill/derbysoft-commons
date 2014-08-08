package com.derbysoft.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhupan panos.zhu@gmail.com
 * @version 1.8
 */
public class CodeGenerator {

    private static final int RADIX = 36;

    private static final int SUFFIX_LEN = 3;

    private static final long MIN_SEQUENCE = (long) Math.pow(RADIX, SUFFIX_LEN);

    private static final long MAX_SEQUENCE = MIN_SEQUENCE + MIN_SEQUENCE;

    private static final AtomicLong sequence = new AtomicLong(MIN_SEQUENCE);

    public static String generate() {
        String value = Long.toString(sequence.getAndIncrement(), RADIX);
        sequence.compareAndSet(MAX_SEQUENCE, MIN_SEQUENCE);
        return (Long.toString(System.currentTimeMillis(), RADIX) + StringUtils.right(value, SUFFIX_LEN)).toUpperCase();
    }

}
