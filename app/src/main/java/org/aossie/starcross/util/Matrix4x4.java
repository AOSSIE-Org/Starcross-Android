package org.aossie.starcross.util;

public class Matrix4x4 {
    private float[] values = new float[16];

    private Matrix4x4(float[] contents) {
        if ((contents.length != 16)) throw new AssertionError();
        System.arraycopy(contents, 0, values, 0, 16);
    }

    public static Matrix4x4 createPerspectiveProjection(float width, float height, float fovyInRadians) {
        float near = 0.01f;
        float far = 10000.0f;

        float inverseAspectRatio = height / width;

        float oneOverTanHalfRadiusOfView = 1.0f / MathUtil.tan(fovyInRadians);

        return new Matrix4x4(new float[]{
                inverseAspectRatio * oneOverTanHalfRadiusOfView, 0, 0, 0,
                0, oneOverTanHalfRadiusOfView, 0, 0,
                0, 0, -(far + near) / (far - near), -1,
                0, 0, -2 * far * near / (far - near), 0
        });
    }

    public static Matrix4x4 createView(Vector3 lookDir, Vector3 up, Vector3 right) {
        return new Matrix4x4(new float[]{
                right.x, up.x, -lookDir.x, 0,
                right.y, up.y, -lookDir.y, 0,
                right.z, up.z, -lookDir.z, 0,
                0, 0, 0, 1
        });
    }

    public float[] getFloatArray() {
        return values;
    }

    public static Vector3 transformVector(Matrix4x4 mat, Vector3 v) {
        Vector3 trans = multiplyMV(mat, v);
        float[] m = mat.values;
        float w = m[3]*v.x + m[7]*v.y + m[11]*v.z + m[15];
        float oneOverW = 1.0f / w;
        trans.x *= oneOverW;
        trans.y *= oneOverW;
        // Don't transform z, we just leave it as a "pseudo-depth".
        return trans;
    }

    public static Vector3 multiplyMV(Matrix4x4 mat, Vector3 v) {
        float[] m = mat.values;
        return new Vector3(
                m[0]*v.x + m[4]*v.y + m[8]*v.z + m[12],
                m[1]*v.x + m[5]*v.y + m[9]*v.z + m[13],
                m[2]*v.x + m[6]*v.y + m[10]*v.z + m[14]);
    }

    public static Matrix4x4 createRotation(float angle, Vector3 axis) {
        float[] m = new float[16];

        float xSqr = axis.x * axis.x;
        float ySqr = axis.y * axis.y;
        float zSqr = axis.z * axis.z;

        float sinAngle = MathUtil.sin(angle);

        float cosAngle = MathUtil.cos(angle);
        float oneMinusCosAngle = 1 - cosAngle;

        float xSinAngle = axis.x * sinAngle;
        float ySinAngle = axis.y * sinAngle;
        float zSinAngle = axis.z * sinAngle;

        float zOneMinusCosAngle = axis.z * oneMinusCosAngle;

        float xyOneMinusCosAngle = axis.x * axis.y * oneMinusCosAngle;
        float xzOneMinusCosAngle = axis.x * zOneMinusCosAngle;
        float yzOneMinusCosAngle = axis.y * zOneMinusCosAngle;

        m[0] = xSqr + (ySqr + zSqr) * cosAngle;
        m[1] = xyOneMinusCosAngle + zSinAngle;
        m[2] = xzOneMinusCosAngle - ySinAngle;
        m[3] = 0;

        m[4] = xyOneMinusCosAngle - zSinAngle;
        m[5] = ySqr + (xSqr + zSqr) * cosAngle;
        m[6] = yzOneMinusCosAngle + xSinAngle;
        m[7] = 0;

        m[8] = xzOneMinusCosAngle + ySinAngle;
        m[9] = yzOneMinusCosAngle - xSinAngle;
        m[10] = zSqr + (xSqr + ySqr) * cosAngle;
        m[11] = 0;

        m[12] = 0;
        m[13] = 0;
        m[14] = 0;
        m[15] = 1;

        return new Matrix4x4(m);
    }


    public static Matrix4x4 createIdentity() {
        return createScaling(1, 1, 1);
    }


    public static Matrix4x4 createScaling(float x, float y, float z) {
        return new Matrix4x4(new float[]{
                x, 0, 0, 0,
                0, y, 0, 0,
                0, 0, z, 0,
                0, 0, 0, 1});
    }

    public static Matrix4x4 multiplyMM(Matrix4x4 mat1, Matrix4x4 mat2) {
        float[] m = mat1.values;
        float[] n = mat2.values;

        return new Matrix4x4(new float[]{
                m[0] * n[0] + m[4] * n[1] + m[8] * n[2] + m[12] * n[3],
                m[1] * n[0] + m[5] * n[1] + m[9] * n[2] + m[13] * n[3],
                m[2] * n[0] + m[6] * n[1] + m[10] * n[2] + m[14] * n[3],
                m[3] * n[0] + m[7] * n[1] + m[11] * n[2] + m[15] * n[3],

                m[0] * n[4] + m[4] * n[5] + m[8] * n[6] + m[12] * n[7],
                m[1] * n[4] + m[5] * n[5] + m[9] * n[6] + m[13] * n[7],
                m[2] * n[4] + m[6] * n[5] + m[10] * n[6] + m[14] * n[7],
                m[3] * n[4] + m[7] * n[5] + m[11] * n[6] + m[15] * n[7],

                m[0] * n[8] + m[4] * n[9] + m[8] * n[10] + m[12] * n[11],
                m[1] * n[8] + m[5] * n[9] + m[9] * n[10] + m[13] * n[11],
                m[2] * n[8] + m[6] * n[9] + m[10] * n[10] + m[14] * n[11],
                m[3] * n[8] + m[7] * n[9] + m[11] * n[10] + m[15] * n[11],

                m[0] * n[12] + m[4] * n[13] + m[8] * n[14] + m[12] * n[15],
                m[1] * n[12] + m[5] * n[13] + m[9] * n[14] + m[13] * n[15],
                m[2] * n[12] + m[6] * n[13] + m[10] * n[14] + m[14] * n[15],
                m[3] * n[12] + m[7] * n[13] + m[11] * n[14] + m[15] * n[15]});
    }

    public static Matrix4x4 createTranslation(float x, float y, float z) {
        return new Matrix4x4(new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                x, y, z, 1});
    }



}