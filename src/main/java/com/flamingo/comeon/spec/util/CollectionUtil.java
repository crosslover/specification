package com.flamingo.comeon.spec.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple Collection util
 *
 * @author wyh
 */
public class CollectionUtil {

    public static <E> List<E> newArrayList(E... elements) {
        checkNotNull(elements); // for GWT
        ArrayList<E> list = new ArrayList<E>();
        Collections.addAll(list, elements);
        return list;
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    public static boolean isEmpty(List<?> array) {
        return (array == null || array.size() == 0);
    }

}
