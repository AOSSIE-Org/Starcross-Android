package org.aossie.starcross.util;

public class GeocentricCoordinates extends Vector3 {
    public GeocentricCoordinates(float x, float y, float z) {
        super(x, y, z);
    }

    public void updateFromRaDec(RaDec raDec) {
        updateFromRaDec(raDec.ra, raDec.dec);
    }

    private void updateFromRaDec(float ra, float dec) {
        float raRadians = ra * Geometry.DEGREES_TO_RADIANS;
        float decRadians = dec * Geometry.DEGREES_TO_RADIANS;

        this.x = MathUtil.cos(raRadians) * MathUtil.cos(decRadians);
        this.y = MathUtil.sin(raRadians) * MathUtil.cos(decRadians);
        this.z = MathUtil.sin(decRadians);
    }

    public static GeocentricCoordinates getInstance(float ra, float dec) {
        GeocentricCoordinates coords = new GeocentricCoordinates(0.0f, 0.0f, 0.0f);
        coords.updateFromRaDec(ra, dec);
        return coords;
    }

    public static GeocentricCoordinates getInstance(RaDec raDec) {
        return getInstance(raDec.ra, raDec.dec);
    }

    @Override
    public GeocentricCoordinates copy() {
        return new GeocentricCoordinates(x, y, z);
    }
}