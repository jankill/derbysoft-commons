package com.derbysoft.common.util;

import com.derbysoft.common.util.collect.Collections;
import com.derbysoft.common.util.collect.Lists;
import com.derbysoft.common.util.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;


@Deprecated
public abstract class CollectionsUtils extends org.apache.commons.collections.CollectionUtils {

    public static <T> T get(List<T> list, int index) {
        return Lists.get(list, index);
    }

    public static <T> T first(List<T> list) {
        return Lists.get(list, 0);
    }

    public static <T> T last(List<T> list) {
        return Lists.last(list);
    }

    public static <T> String join(Collection<T> collection, String delimit) {
        return Collections.join(collection, delimit);
    }

    public static Map map(Object... keyvals) {
        return Maps.map(keyvals);
    }


}