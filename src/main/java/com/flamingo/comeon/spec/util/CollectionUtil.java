package com.flamingo.comeon.spec.util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple Collection util
 *
 * @author wyh
 */
public class CollectionUtil {

    public static <E> ArrayList<E> newArrayList(E... elements) {
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
}
