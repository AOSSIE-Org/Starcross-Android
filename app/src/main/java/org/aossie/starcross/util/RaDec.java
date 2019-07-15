package org.aossie.starcross.util;

import org.aossie.starcross.source.Planet;

import java.util.Date;

public class RaDec {
    public float ra;        // In degrees
    public float dec;       // In degrees

    public RaDec(float ra, float dec) {
        this.ra = ra;
        this.dec = dec;
    }

    public static RaDec calculateRaDecDist(HeliocentricCoordinates coords) {
        // find the RA and DEC from the rectangular equatorial coords
        float ra = Geometry.mod2pi(MathUtil.atan2(coords.y, coords.x)) * Geometry.RADIANS_TO_DEGREES;
        float dec = MathUtil.atan(coords.z / MathUtil.sqrt(coords.x * coords.x + coords.y * coords.y))
                * Geometry.RADIANS_TO_DEGREES;

        return new RaDec(ra, dec);
    }

    public static RaDec getInstance(Planet planet, Date time,
                                    HeliocentricCoordinates earthCoordinates) {
        if (planet.equals(Planet.Moon)) {
            return Planet.calculateLunarGeocentricLocation(time);
        }

        HeliocentricCoordinates coords = null;
        if (planet.equals(Planet.Sun)) {
            // Invert the view, since we want the Sun in earth coordinates, not the Earth in sun
            // coordinates.
            coords = new HeliocentricCoordinates(earthCoordinates.radius, earthCoordinates.x * -1.0f,
                    earthCoordinates.y * -1.0f, earthCoordinates.z * -1.0f);
        } else {
            coords = HeliocentricCoordinates.getInstance(planet, time);
            coords.Subtract(earthCoordinates);
        }
        HeliocentricCoordinates equ = coords.CalculateEquatorialCoordinates();
        return calculateRaDecDist(equ);
    }
}
