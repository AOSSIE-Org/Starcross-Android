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

    Matrix3x3(float xx, float xy, float xz,
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
}