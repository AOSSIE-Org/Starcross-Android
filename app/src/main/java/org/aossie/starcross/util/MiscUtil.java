package org.aossie.starcross.util;

public class MiscUtil {
    private MiscUtil() {
    }

    public static String getTag(Object o) {
        if (o instanceof Class<?>) {
            return "Starcross." + ((Class<?>) o).getSimpleName();
        }
        return "Starcross." + o.getClass().getSimpleName();
    }
}