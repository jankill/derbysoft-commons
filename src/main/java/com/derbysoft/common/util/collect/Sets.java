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

    public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
        return new HashSet<E>(Maps.capacity(expectedSize));
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet(
            Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<? extends E> collection = (Collection<? extends E>) elements;
            return new LinkedHashSet<E>(collection);
        } else {
            LinkedHashSet<E> set = newLinkedHashSet();
            for (E element : elements) {
                set.add(element);
            }
            return set;
        }
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet() {
        return new TreeSet<E>();
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> elements) {
        TreeSet<E> set = newTreeSet();
        for (E element : elements) {
            set.add(element);
        }
        return set;
    }

    public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
        return new TreeSet<E>(comparator);
    }

    public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<? extends E> collection = (Collection<? extends E>) elements;
            return new HashSet<E>(collection);
        }
        return newHashSet(elements.iterator());
    }

    public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
        HashSet<E> set = newHashSet();
        while (elements.hasNext()) {
            set.add(elements.next());
        }
        return set;
    }

    public static boolean isEmpty(Set values) {
        return values == null || values.isEmpty();
    }

    public static boolean isNotEmpty(Set values) {
        return !isEmpty(values);
    }

    public static <E> HashSet<E> newHashSet(E... elements) {
        int capacity = Maps.capacity(elements.length);
        HashSet<E> set = new HashSet<E>(capacity);
        Collections.addAll(set, elements);
        return set;
    }

}
