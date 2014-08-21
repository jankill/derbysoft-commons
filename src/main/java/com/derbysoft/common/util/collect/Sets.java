package com.derbysoft.common.util.collect;

import java.util.*;

/**
 * @author zhupan
 * @version 1.8
 */
public class Sets {

    public static <E> HashSet<E> newHashSet() {
        return new HashSet<E>();
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return new LinkedHashSet<E>();
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet() {
        return new TreeSet<E>();
    }

    public static boolean isEmpty(Set values) {
        return values == null || values.isEmpty();
    }

    public static <E> HashSet<E> newHashSet(E... elements) {
        int capacity = Maps.capacity(elements.length);
        HashSet<E> set = new HashSet<E>(capacity);
        Collections.addAll(set, elements);
        return set;
    }

}
