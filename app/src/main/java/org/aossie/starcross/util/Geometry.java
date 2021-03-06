package org.aossie.starcross.util;

import java.util.Date;

public class Geometry {
    public static final float DEGREES_TO_RADIANS = MathUtil.PI / 180.0f;
    public static final float RADIANS_TO_DEGREES = 180.0f / MathUtil.PI;

    private Geometry() {
    }

    public static Vector3 vectorProduct(Vector3 v1, Vector3 v2) {
        return new Vector3(v1.y * v2.z - v1.z * v2.y,
                -v1.x * v2.z + v1.z * v2.x,
                v1.x * v2.y - v1.y * v2.x);
    }

    public static Vector3 scaleVector(Vector3 v, float scale) {
        return new Vector3(scale * v.x, scale * v.y, scale * v.z);
    }

    public static Vector3 addVectors(Vector3 first, Vector3 second) {
        return new Vector3(first.x + second.x, first.y + second.y, first.z + second.z);
    }

    public static Vector3 matrixVectorMultiply(Matrix3x3 m, Vector3 v) {
        return new Vector3(m.xx * v.x + m.xy * v.y + m.xz * v.z,
                m.yx * v.x + m.yy * v.y + m.yz * v.z,
                m.zx * v.x + m.zy * v.y + m.zz * v.z);
    }

    public static Matrix3x3 calculateRotationMatrix(float degrees, Vector3 axis) {
        float cosD = MathUtil.cos(degrees * Geometry.DEGREES_TO_RADIANS);
        float sinD = MathUtil.sin(degrees * Geometry.DEGREES_TO_RADIANS);
        float oneMinusCosD = 1f - cosD;

        float x = axis.x;
        float y = axis.y;
        float z = axis.z;

        float xs = x * sinD;
        float ys = y * sinD;
        float zs = z * sinD;

        float xm = x * oneMinusCosD;
        float ym = y * oneMinusCosD;
        float zm = z * oneMinusCosD;

        float xym = x * ym;
        float yzm = y * zm;
        float zxm = z * xm;

        return new Matrix3x3(x * xm + cosD, xym + zs, zxm - ys,
                xym - zs, y * ym + cosD, yzm + xs,
                zxm + ys, yzm - xs, z * zm + cosD);
    }

    public static float mod2pi(float x) {
        float factor = x / MathUtil.TWO_PI;
        float result = MathUtil.TWO_PI * (factor - abs_floor(factor));
        if (result < 0.0) {
            result = MathUtil.TWO_PI + result;
        }
        return result;
    }

    private static float abs_floor(float x) {
        float result;
        if (x >= 0.0)
            result = MathUtil.floor(x);
        else
            result = MathUtil.ceil(x);
        return result;
    }

    public static float cosineSimilarity(Vector3 v1, Vector3 v2) {
        // We might want to optimize this implementation at some point.
        return scalarProduct(v1, v2)
                / MathUtil.sqrt(scalarProduct(v1, v1)
                * scalarProduct(v2, v2));
    }

    public static float scalarProduct(Vector3 v1, Vector3 v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static Matrix3x3 matrixMultiply(Matrix3x3 m1, Matrix3x3 m2) {
        return new Matrix3x3(m1.xx*m2.xx + m1.xy*m2.yx + m1.xz*m2.zx,
                m1.xx*m2.xy + m1.xy*m2.yy + m1.xz*m2.zy,
                m1.xx*m2.xz + m1.xy*m2.yz + m1.xz*m2.zz,
                m1.yx*m2.xx + m1.yy*m2.yx + m1.yz*m2.zx,
                m1.yx*m2.xy + m1.yy*m2.yy + m1.yz*m2.zy,
                m1.yx*m2.xz + m1.yy*m2.yz + m1.yz*m2.zz,
                m1.zx*m2.xx + m1.zy*m2.yx + m1.zz*m2.zx,
                m1.zx*m2.xy + m1.zy*m2.yy + m1.zz*m2.zy,
                m1.zx*m2.xz + m1.zy*m2.yz + m1.zz*m2.zz);
    }

    public static RaDec calculateRADecOfZenith(Date utc, LatLong location) {
        // compute overhead RA in degrees
        float my_ra = TimeUtil.meanSiderealTime(utc, location.getLongitude());
        float my_dec = location.getLatitude();
        return new RaDec(my_ra, my_dec);
    }

}