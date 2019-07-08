package org.aossie.starcross.positioning;

import org.aossie.starcross.util.Geometry;
import org.aossie.starcross.util.MathUtil;

public class OrbitalElements {
    private final static float EPSILON = 1.0e-6f;
    final float distance;
    final float eccentricity;
    final float inclination;
    final float ascendingNode;
    final float perihelion;
    private final float meanLongitude;

    public OrbitalElements(float d, float e, float i, float a, float p, float l) {
        this.distance = d;
        this.eccentricity = e;
        this.inclination = i;
        this.ascendingNode = a;
        this.perihelion = p;
        this.meanLongitude = l;
    }

    float getAnomaly() {
        return calculateTrueAnomaly(meanLongitude - perihelion, eccentricity);
    }

    private static float calculateTrueAnomaly(float m, float e) {
        float e0 = m + e * MathUtil.sin(m) * (1.0f + e * MathUtil.cos(m));
        float e1;

        int counter = 0;
        do {
            e1 = e0;
            e0 = e1 - (e1 - e * MathUtil.sin(e1) - m) / (1.0f - e * MathUtil.cos(e1));
            if (counter++ > 100) {
                break;
            }
        } while (MathUtil.abs(e0 - e1) > EPSILON);

        float v =
                2f * MathUtil.atan(MathUtil.sqrt((1 + e) / (1 - e))
                        * MathUtil.tan(0.5f * e0));
        return Geometry.mod2pi(v);
    }
}
