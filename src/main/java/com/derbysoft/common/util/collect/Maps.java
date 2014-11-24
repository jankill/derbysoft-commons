package com.derbysoft.common.util.collect;

import java.util.*;

/**
 * @author zhupan
 * @version 1.8
 */
public final class Maps {

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap<K, V>();
    }

    public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap<K, V>();
    }

    public static boolean isEmpty(Map values) {
        return values == null || values.isEmpty();
    }

    public static boolean isNotEmpty(Map values) {
        return !isEmpty(values);
    }

    public static Map map(Object... keyValuePairs) {
        if (keyValuePairs == null) {
            return new HashMap();
        }
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Map must have an even number of elements");
        }
        Map map = new HashMap(keyValuePairs.length / 2);
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            map.put(keyValuePairs[i], keyValuePairs[i + 1]);
        }
        return java.util.Collections.unmodifiableMap(map);
    }

    static int capacity(int expectedSize) {
        checkArgument(expectedSize >= 0);
        return Math.max(expectedSize * 2, 16);
    }

    private static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

}
