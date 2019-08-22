package org.aossie.starcross.util;

public class Matrix3x3 implements Cloneable {
    float xx;
    float xy;
    float xz;
    float yx;
    float yy;
    float yz;
    float zx;
    float zy;
    float zz;

    public Matrix3x3(Vector3 v1, Vector3 v2, Vector3 v3, boolean columnVectors) {
        if (columnVectors) {
            this.xx = v1.x;
            this.yx = v1.y;
            this.zx = v1.z;
            this.xy = v2.x;
            this.yy = v2.y;
            this.zy = v2.z;
            this.xz = v3.x;
            this.yz = v3.y;
            this.zz = v3.z;
        } else {
            this.xx = v1.x;
            this.xy = v1.y;
            this.xz = v1.z;
            this.yx = v2.x;
            this.yy = v2.y;
            this.yz = v2.z;
            this.zx = v3.x;
            this.zy = v3.y;
            this.zz = v3.z;
        }
    }

    public Matrix3x3(Vector3 v1, Vector3 v2, Vector3 v3) {
        this(v1, v2, v3, true);
    }

    public Matrix3x3(float xx, float xy, float xz,
              float yx, float yy, float yz,
              float zx, float zy, float zz) {
        this.xx = xx;
        this.xy = xy;
        this.xz = xz;
        this.yx = yx;
        this.yy = yy;
        this.yz = yz;
        this.zx = zx;
        this.zy = zy;
        this.zz = zz;
    }

    public static Matrix3x3 getIdMatrix() {
        return new Matrix3x3(1, 0, 0, 0, 1, 0, 0, 0, 1);
    }
}