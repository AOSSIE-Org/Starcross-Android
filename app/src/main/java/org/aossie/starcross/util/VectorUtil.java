package org.aossie.starcross.util;

public class VectorUtil {
    private static Vector3 zero() {
        return new Vector3(0, 0, 0);
    }

    public static float dotProduct(Vector3 p1, Vector3 p2) {
        return p1.x * p2.x + p1.y * p2.y + p1.z * p2.z;
    }

    public static Vector3 crossProduct(Vector3 p1, Vector3 p2) {
        return new Vector3(p1.y * p2.z - p1.z * p2.y,
                -p1.x * p2.z + p1.z * p2.x,
                p1.x * p2.y - p1.y * p2.x);
    }

    private static float length(Vector3 v) {
        return MathUtil.sqrt(lengthSqr(v));
    }

    private static float lengthSqr(Vector3 v) {
        return dotProduct(v, v);
    }

    public static Vector3 normalized(Vector3 v) {
        float len = length(v);
        if (len < 0.000001f) {
            return zero();
        }
        return scale(v, 1.0f / len);
    }

    private static Vector3 scale(float factor, Vector3 v) {
        return new Vector3(v.x * factor, v.y * factor, v.z * factor);
    }

    private static Vector3 scale(Vector3 v, float factor) {
        return scale(factor, v);
    }

    public static Vector3 difference(Vector3 v1, Vector3 v2) {
        return sum(v1, negate(v2));
    }

    public static Vector3 negate(Vector3 v) {
        return new Vector3(-v.x, -v.y, -v.z);
    }

    public static Vector3 sum(Vector3 v1, Vector3 v2) {
        return new Vector3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }
}