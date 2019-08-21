package org.aossie.starcross.util;

public final class FixedPoint {
    private FixedPoint() {
    }

    public static final int ONE = 0x00010000;

    public static int floatToFixedPoint(float f) {
        return (int) (65536F * f);
    }
}