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
}