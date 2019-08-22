package org.aossie.starcross.util;

public class Vector3 {
    public float x;
    public float y;
    public float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(float[] xyz) throws IllegalArgumentException {
        if (xyz.length != 3) {
            throw new IllegalArgumentException("Trying to create 3 vector from array of length: " + xyz.length);
        }
        this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2];
    }

    public Vector3 copy() {
        return new Vector3(x, y, z);
    }

    public void assign(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void assign(Vector3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    private float length() {
        return MathUtil.sqrt(length2());
    }

    public float length2() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public void normalize() {
        float norm = this.length();
        this.x = this.x / norm;
        this.y = this.y / norm;
        this.z = this.z / norm;
    }

    public void scale(float scale) {
        this.x = this.x * scale;
        this.y = this.y * scale;
        this.z = this.z * scale;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Vector3)) return false;
        Vector3 other = (Vector3) object;
        return other.x == x && other.y == y && other.z == z;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(x) + Float.floatToIntBits(y) + Float.floatToIntBits(z);
    }
}