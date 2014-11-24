package com.derbysoft.common.util.collect;

import java.util.*;

/**
 * @author zhupan
 * @version 1.8
 */
public final class Lists {

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<E>();
    }

    public static <E> ArrayList<E> newArrayList(E... elements) {
        checkNotNull(elements);
        int capacity = computeArrayListCapacity(elements.length);
        ArrayList<E> list = new ArrayList<E>(capacity);
        Collections.addAll(list, elements);
        return list;
    }

    public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
        checkNotNull(elements);
        if (elements instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<? extends E> collection = (Collection<? extends E>) elements;
            return new ArrayList<E>(collection);
        }
        return newArrayList(elements.iterator());
    }

    public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
        checkNotNull(elements);
        ArrayList<E> list = newArrayList();
        while (elements.hasNext()) {
            list.add(elements.next());
        }
        return list;
    }

    public static boolean isEmpty(List values) {
        return values == null || values.isEmpty();
    }

    public static boolean isNotEmpty(List values) {
        return !isEmpty(values);
    }

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

    private static int computeArrayListCapacity(int arraySize) {
        checkArgument(arraySize >= 0);
        return (int) Math.min(5L + arraySize + (arraySize / 10), Integer.MAX_VALUE);
    }

    private static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    private static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

}
