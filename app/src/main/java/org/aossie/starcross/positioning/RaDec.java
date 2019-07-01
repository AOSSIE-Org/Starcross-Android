package org.aossie.starcross.positioning;

import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.Geometry;
import org.aossie.starcross.util.MathUtil;

public class RaDec {
    private float dec;

    private RaDec(float ra, float dec) {
        this.dec = dec;
    }

    public static RaDec calculateRaDecDist(HeliocentricCoordinates coords) {
        float ra = Geometry.mod2pi(MathUtil.atan2(coords.y, coords.x)) * Geometry.RADIANS_TO_DEGREES;
        float dec = MathUtil.atan(coords.z / MathUtil.sqrt(coords.x * coords.x + coords.y * coords.y))
                * Geometry.RADIANS_TO_DEGREES;
        return new RaDec(ra, dec);
    }

    static RaDec getInstance(GeocentricCoordinates coords) {
        float raRad = MathUtil.atan2(coords.y, coords.x);
        if (raRad < 0) raRad += MathUtil.TWO_PI;
        float decRad = MathUtil.atan2(coords.z,
                MathUtil.sqrt(coords.x * coords.x + coords.y * coords.y));

        return new RaDec(raRad * Geometry.RADIANS_TO_DEGREES,
                decRad * Geometry.RADIANS_TO_DEGREES);
    }

    public boolean isCircumpolarFor(LatLong loc) {
        if (loc.getLatitude() > 0.0f) {
            return (this.dec > (90.0f - loc.getLatitude()));
        } else {
            return (this.dec < (-90.0f - loc.getLatitude()));
        }
    }

    public boolean isNeverVisible(LatLong loc) {
        if (loc.getLatitude() > 0.0f) {
            return (this.dec < (loc.getLatitude() - 90.0f));
        } else {
            return (this.dec > (90.0f + loc.getLatitude()));
        }
    }
}
