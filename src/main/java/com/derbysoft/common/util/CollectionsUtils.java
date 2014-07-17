package com.derbysoft.common.util;

import java.util.*;

public abstract class CollectionsUtils extends org.apache.commons.collections.CollectionUtils {

    public static <T> T get(List<T> list, int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be negative: " + index);
        }
        if (list == null || index > list.size() - 1) {
            return null;
        }
        return list.get(index);
    }

    public static <T> T first(List<T> list) {
        return get(list, 0);
    }

    public static <T> T last(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    public static <T> String join(Collection<T> collection, String delimit) {
        if (isEmpty(collection)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<T> iter = collection.iterator();
        if (iter.hasNext()) {
            sb.append(iter.next());
        }
        while (iter.hasNext()) {
            sb.append(delimit).append(iter.next());
        }
        return sb.toString();
    }

    public static Map map(Object... keyvals) {
        if (keyvals == null) {
            return new HashMap();
        }
        if (keyvals.length % 2 != 0) {
            throw new IllegalArgumentException("Map must have an even number of elements");
        }
        Map m = new HashMap(keyvals.length / 2);
        for (int i = 0; i < keyvals.length; i += 2) {
            m.put(keyvals[i], keyvals[i + 1]);
        }
        return Collections.unmodifiableMap(m);
    }


}