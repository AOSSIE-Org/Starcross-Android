package org.aossie.starcross.util;

public class MathUtil {
    private MathUtil() {
    }

    public static final float PI = (float) Math.PI;
    public static final float TWO_PI = 2f * PI;
    public static final float DEGREES_TO_RADIANS = PI / 180;
    public static final float RADIANS_TO_DEGREES = 180 / PI;

    public static float sqrt(float x) {
        return (float) Math.sqrt(x);
    }

    public static float sin(float x) {
        return (float) Math.sin(x);
    }

    public static float cos(float x) {
        return (float) Math.cos(x);
    }

    public static float tan(float x) {
        return sin(x) / cos(x);
    }

    public static float asin(float x) {
        return (float) Math.asin(x);
    }

    public static float acos(float x) {
        return (float) Math.acos(x);
    }

    public static float atan(float x) {
        return (float) Math.atan(x);
    }

    public static float atan2(float y, float x) {
        return (float) Math.atan2(y, x);
    }

    public static float abs(float x) {
        return Math.abs(x);
    }

    static float floor(float x) {
        return (float) Math.floor(x);
    }

    static float ceil(float x) {
        return (float) Math.ceil(x);
    }
}