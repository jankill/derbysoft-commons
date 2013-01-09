package com.derbysoft.common.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
        StringBuffer sb = new StringBuffer();
        Iterator<T> iter = collection.iterator();
        if (iter.hasNext()) {
            sb.append(iter.next());
        }
        while (iter.hasNext()) {
            sb.append(delimit).append(iter.next());
        }
        return sb.toString();
    }

}