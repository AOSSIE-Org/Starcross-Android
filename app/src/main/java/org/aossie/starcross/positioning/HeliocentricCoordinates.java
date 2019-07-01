package org.aossie.starcross.positioning;

import org.aossie.starcross.util.MathUtil;
import org.aossie.starcross.util.Vector3;

public class HeliocentricCoordinates extends Vector3 {

    private float radius;
    private static final float OBLIQUITY = 23.439281f * MathUtil.PI / 180.0f;

    private HeliocentricCoordinates(float radius, float xh, float yh, float zh) {
        super(xh, yh, zh);
        this.radius = radius;
    }

    public void Subtract(HeliocentricCoordinates other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
    }

    public HeliocentricCoordinates CalculateEquatorialCoordinates() {
        return new HeliocentricCoordinates(this.radius,
                this.x,
                this.y * MathUtil.cos(OBLIQUITY) - this.z * MathUtil.sin(OBLIQUITY),
                this.y * MathUtil.sin(OBLIQUITY) + this.z * MathUtil.cos(OBLIQUITY));
    }

    public float DistanceFrom(HeliocentricCoordinates other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        float dz = this.z - other.z;
        return MathUtil.sqrt(dx * dx + dy * dy + dz * dz);
    }

    static HeliocentricCoordinates getInstance(OrbitalElements elem) {
        float anomaly = elem.getAnomaly();
        float ecc = elem.eccentricity;
        float radius = elem.distance * (1 - ecc * ecc) / (1 + ecc * MathUtil.cos(anomaly));
        float per = elem.perihelion;
        float asc = elem.ascendingNode;
        float inc = elem.inclination;
        float xh = radius *
                (MathUtil.cos(asc) * MathUtil.cos(anomaly + per - asc) -
                        MathUtil.sin(asc) * MathUtil.sin(anomaly + per - asc) *
                                MathUtil.cos(inc));
        float yh = radius *
                (MathUtil.sin(asc) * MathUtil.cos(anomaly + per - asc) +
                        MathUtil.cos(asc) * MathUtil.sin(anomaly + per - asc) *
                                MathUtil.cos(inc));
        float zh = radius * (MathUtil.sin(anomaly + per - asc) * MathUtil.sin(inc));

        return new HeliocentricCoordinates(radius, xh, yh, zh);
    }
}
