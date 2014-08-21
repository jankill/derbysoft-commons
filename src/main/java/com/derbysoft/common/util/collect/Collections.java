package com.derbysoft.common.util.collect;

import java.util.Collection;
import java.util.Iterator;

public abstract class Collections extends org.apache.commons.collections.CollectionUtils {
    public static <T> String join(Collection<T> collection, String delimit) {
        if (isEmpty(collection)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<T> iterator = collection.iterator();
        if (iterator.hasNext()) {
            sb.append(iterator.next());
        }
        while (iterator.hasNext()) {
            sb.append(delimit).append(iterator.next());
        }
        return sb.toString();
    }
}
