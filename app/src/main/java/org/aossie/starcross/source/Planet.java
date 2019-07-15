package org.aossie.starcross.source;

import android.util.Log;

import org.aossie.starcross.R;
import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.Geometry;
import org.aossie.starcross.util.HeliocentricCoordinates;
import org.aossie.starcross.util.LatLong;
import org.aossie.starcross.util.MathUtil;
import org.aossie.starcross.util.MiscUtil;
import org.aossie.starcross.util.RaDec;
import org.aossie.starcross.util.TimeConstants;
import org.aossie.starcross.util.TimeUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public enum Planet {
    // The order here is the order in which they are drawn.  To ensure that during
    // conjunctions they display "naturally" order them in reverse distance from Earth.
    Neptune(R.drawable.neptune, R.string.neptune, TimeConstants.MILLISECONDS_PER_HOUR),
    Uranus(R.drawable.uranus, R.string.uranus, 1L * TimeConstants.MILLISECONDS_PER_HOUR),
    Jupiter(R.drawable.jupiter, R.string.jupiter, 1L * TimeConstants.MILLISECONDS_PER_HOUR),
    Saturn(R.drawable.saturn, R.string.saturn, 1L * TimeConstants.MILLISECONDS_PER_HOUR),
    Mars(R.drawable.mars, R.string.mars, 1L * TimeConstants.MILLISECONDS_PER_HOUR),
    Sun(R.drawable.sun, R.string.sun, 1L * TimeConstants.MILLISECONDS_PER_HOUR),
    Mercury(R.drawable.mercury, R.string.mercury, 1L * TimeConstants.MILLISECONDS_PER_HOUR),
    Venus(R.drawable.venus, R.string.venus, 1L * TimeConstants.MILLISECONDS_PER_HOUR),
    Moon(R.drawable.moon, R.string.moon, 1L * TimeConstants.MILLISECONDS_PER_MINUTE);

    private static final String TAG = MiscUtil.getTag(Planet.class);

    // Resource ID to use for a planet's image.
    private int imageResourceId;

    // String ID
    private int nameResourceId;

    private final long updateFreqMs;

    Planet(int imageResourceId, int nameResourceId, long updateFreqMs) {
        this.imageResourceId = imageResourceId;
        this.nameResourceId = nameResourceId;
        this.updateFreqMs = updateFreqMs;
        // Add Color, magnitude, etc.
    }

    public long getUpdateFrequencyMs() {
        return updateFreqMs;
    }

    /**
     * Returns the resource id for the string corresponding to the name of this
     * planet.
     */
    public int getNameResourceId() {
        return nameResourceId;
    }

    /**
     * Returns the resource id for the planet's image.
     */
    public int getImageResourceId(Date time) {
        if (this.equals(Planet.Moon)) {
            return getLunarPhaseImageId(time);
        }
        return this.imageResourceId;
    }

    /**
     * Determine the Moon's phase and return the resource ID of the correct
     * image.
     */
    private int getLunarPhaseImageId(Date time) {
        // First, calculate phase angle:
        // todo
        return R.drawable.moon;
    }

    public OrbitalElements getOrbitalElements(Date date) {
        // Centuries since J2000
        float jc = (float) TimeUtil.julianCenturies(date);

        switch (this) {
            case Mercury: {
                float a = 0.38709927f + 0.00000037f * jc;
                float e = 0.20563593f + 0.00001906f * jc;
                float i = (7.00497902f - 0.00594749f * jc) * Geometry.DEGREES_TO_RADIANS;
                float l =
                        Geometry.mod2pi((252.25032350f + 149472.67411175f * jc) * Geometry.DEGREES_TO_RADIANS);
                float w = (77.45779628f + 0.16047689f * jc) * Geometry.DEGREES_TO_RADIANS;
                float o = (48.33076593f - 0.12534081f * jc) * Geometry.DEGREES_TO_RADIANS;
                return new OrbitalElements(a, e, i, o, w, l);
            }

            case Venus: {
                float a = 0.72333566f + 0.00000390f * jc;
                float e = 0.00677672f - 0.00004107f * jc;
                float i = (3.39467605f - 0.00078890f * jc) * Geometry.DEGREES_TO_RADIANS;
                float l =
                        Geometry.mod2pi((181.97909950f + 58517.81538729f * jc) * Geometry.DEGREES_TO_RADIANS);
                float w = (131.60246718f + 0.00268329f * jc) * Geometry.DEGREES_TO_RADIANS;
                float o = (76.67984255f - 0.27769418f * jc) * Geometry.DEGREES_TO_RADIANS;
                return new OrbitalElements(a, e, i, o, w, l);
            }

            // Note that this is the orbital data for Earth.
            case Sun: {
                float a = 1.00000261f + 0.00000562f * jc;
                float e = 0.01671123f - 0.00004392f * jc;
                float i = (-0.00001531f - 0.01294668f * jc) * Geometry.DEGREES_TO_RADIANS;
                float l =
                        Geometry.mod2pi((100.46457166f + 35999.37244981f * jc) * Geometry.DEGREES_TO_RADIANS);
                float w = (102.93768193f + 0.32327364f * jc) * Geometry.DEGREES_TO_RADIANS;
                float o = 0.0f;
                return new OrbitalElements(a, e, i, o, w, l);
            }

            case Mars: {
                float a = 1.52371034f + 0.00001847f * jc;
                float e = 0.09339410f + 0.00007882f * jc;
                float i = (1.84969142f - 0.00813131f * jc) * Geometry.DEGREES_TO_RADIANS;
                float l =
                        Geometry.mod2pi((-4.55343205f + 19140.30268499f * jc) * Geometry.DEGREES_TO_RADIANS);
                float w = (-23.94362959f + 0.44441088f * jc) * Geometry.DEGREES_TO_RADIANS;
                float o = (49.55953891f - 0.29257343f * jc) * Geometry.DEGREES_TO_RADIANS;
                return new OrbitalElements(a, e, i, o, w, l);
            }

            case Jupiter: {
                float a = 5.20288700f - 0.00011607f * jc;
                float e = 0.04838624f - 0.00013253f * jc;
                float i = (1.30439695f - 0.00183714f * jc) * Geometry.DEGREES_TO_RADIANS;
                float l =
                        Geometry.mod2pi((34.39644051f + 3034.74612775f * jc) * Geometry.DEGREES_TO_RADIANS);
                float w = (14.72847983f + 0.21252668f * jc) * Geometry.DEGREES_TO_RADIANS;
                float o = (100.47390909f + 0.20469106f * jc) * Geometry.DEGREES_TO_RADIANS;
                return new OrbitalElements(a, e, i, o, w, l);
            }

            case Saturn: {
                float a = 9.53667594f - 0.00125060f * jc;
                float e = 0.05386179f - 0.00050991f * jc;
                float i = (2.48599187f + 0.00193609f * jc) * Geometry.DEGREES_TO_RADIANS;
                float l =
                        Geometry.mod2pi((49.95424423f + 1222.49362201f * jc) * Geometry.DEGREES_TO_RADIANS);
                float w = (92.59887831f - 0.41897216f * jc) * Geometry.DEGREES_TO_RADIANS;
                float o = (113.66242448f - 0.28867794f * jc) * Geometry.DEGREES_TO_RADIANS;
                return new OrbitalElements(a, e, i, o, w, l);
            }

            case Uranus: {
                float a = 19.18916464f - 0.00196176f * jc;
                float e = 0.04725744f - 0.00004397f * jc;
                float i = (0.77263783f - 0.00242939f * jc) * Geometry.DEGREES_TO_RADIANS;
                float l =
                        Geometry.mod2pi((313.23810451f + 428.48202785f * jc) * Geometry.DEGREES_TO_RADIANS);
                float w = (170.95427630f + 0.40805281f * jc) * Geometry.DEGREES_TO_RADIANS;
                float o = (74.01692503f + 0.04240589f * jc) * Geometry.DEGREES_TO_RADIANS;
                return new OrbitalElements(a, e, i, o, w, l);
            }

            case Neptune: {
                float a = 30.06992276f + 0.00026291f * jc;
                float e = 0.00859048f + 0.00005105f * jc;
                float i = (1.77004347f + 0.00035372f * jc) * Geometry.DEGREES_TO_RADIANS;
                float l =
                        Geometry.mod2pi((-55.12002969f + 218.45945325f * jc) * Geometry.DEGREES_TO_RADIANS);
                float w = (44.96476227f - 0.32241464f * jc) * Geometry.DEGREES_TO_RADIANS;
                float o = (131.78422574f - 0.00508664f * jc) * Geometry.DEGREES_TO_RADIANS;
                return new OrbitalElements(a, e, i, o, w, l);
            }

            default:
                throw new RuntimeException("Unknown Planet: " + this);
        }
    }

    public static RaDec calculateLunarGeocentricLocation(Date time) {
        // First, calculate the number of Julian centuries from J2000.0.
        float t = (float) ((TimeUtil.calculateJulianDay(time) - 2451545.0f) / 36525.0f);

        // Second, calculate the approximate geocentric orbital elements.
        float lambda =
                218.32f + 481267.881f * t + 6.29f
                        * MathUtil.sin((135.0f + 477198.87f * t) * Geometry.DEGREES_TO_RADIANS) - 1.27f
                        * MathUtil.sin((259.3f - 413335.36f * t) * Geometry.DEGREES_TO_RADIANS) + 0.66f
                        * MathUtil.sin((235.7f + 890534.22f * t) * Geometry.DEGREES_TO_RADIANS) + 0.21f
                        * MathUtil.sin((269.9f + 954397.74f * t) * Geometry.DEGREES_TO_RADIANS) - 0.19f
                        * MathUtil.sin((357.5f + 35999.05f * t) * Geometry.DEGREES_TO_RADIANS) - 0.11f
                        * MathUtil.sin((186.5f + 966404.03f * t) * Geometry.DEGREES_TO_RADIANS);
        float beta =
                5.13f * MathUtil.sin((93.3f + 483202.02f * t) * Geometry.DEGREES_TO_RADIANS) + 0.28f
                        * MathUtil.sin((228.2f + 960400.89f * t) * Geometry.DEGREES_TO_RADIANS) - 0.28f
                        * MathUtil.sin((318.3f + 6003.15f * t) * Geometry.DEGREES_TO_RADIANS) - 0.17f
                        * MathUtil.sin((217.6f - 407332.21f * t) * Geometry.DEGREES_TO_RADIANS);
        float l =
                MathUtil.cos(beta * Geometry.DEGREES_TO_RADIANS)
                        * MathUtil.cos(lambda * Geometry.DEGREES_TO_RADIANS);
        float m =
                0.9175f * MathUtil.cos(beta * Geometry.DEGREES_TO_RADIANS)
                        * MathUtil.sin(lambda * Geometry.DEGREES_TO_RADIANS) - 0.3978f
                        * MathUtil.sin(beta * Geometry.DEGREES_TO_RADIANS);
        float n =
                0.3978f * MathUtil.cos(beta * Geometry.DEGREES_TO_RADIANS)
                        * MathUtil.sin(lambda * Geometry.DEGREES_TO_RADIANS) + 0.9175f
                        * MathUtil.sin(beta * Geometry.DEGREES_TO_RADIANS);
        float ra = Geometry.mod2pi(MathUtil.atan2(m, l)) * Geometry.RADIANS_TO_DEGREES;
        float dec = MathUtil.asin(n) * Geometry.RADIANS_TO_DEGREES;

        return new RaDec(ra, dec);
    }

    public float getPlanetaryImageSize() {
        switch (this) {
            case Sun:
            case Moon:
                return 0.02f;
            case Mercury:
            case Venus:
            case Mars:
                return 0.01f;
            case Jupiter:
                return 0.025f;
            case Uranus:
            case Neptune:
                return 0.015f;
            case Saturn:
                return 0.035f;
            default:
                return 0.02f;
        }
    }


    /**
     * Enum that identifies whether we are interested in rise or set time.
     */
    public enum RiseSetIndicator {RISE, SET}

    // Maximum number of times to calculate rise/set times. If we cannot
    // converge after this many iteretions, we will fail.
    private final static int MAX_ITERATIONS = 25;

    private double calcRiseSetTime(Date d, LatLong loc,
                                   RiseSetIndicator indicator) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UT"));
        cal.setTime(d);

        float sign = (indicator == RiseSetIndicator.RISE ? 1.0f : -1.0f);
        float delta = 5.0f;
        float ut = 12.0f;

        int counter = 0;
        while ((Math.abs(delta) > 0.008) && counter < MAX_ITERATIONS) {
            cal.set(Calendar.HOUR_OF_DAY, (int) MathUtil.floor(ut));
            float minutes = (ut - MathUtil.floor(ut)) * 60.0f;
            cal.set(Calendar.MINUTE, (int) minutes);
            cal.set(Calendar.SECOND, (int) ((minutes - MathUtil.floor(minutes)) * 60.f));

            Date tmp = cal.getTime();
            HeliocentricCoordinates sunCoordinates =
                    HeliocentricCoordinates.getInstance(Planet.Sun, tmp);
            RaDec raDec = RaDec.getInstance(this, tmp, sunCoordinates);

            // GHA = GST - RA. (In degrees.)
            float gst = TimeUtil.meanSiderealTime(tmp, 0);
            float gha = gst - raDec.ra;

            // The value of -0.83 works for the diameter of the Sun and Moon. We
            // assume that other objects are simply points.
            float bodySize = (this == Planet.Sun || this == Planet.Moon) ? -0.83f : 0.0f;
            float hourAngle = calculateHourAngle(bodySize, loc.getLatitude(), raDec.dec);

            delta = (gha + loc.getLongitude() + (sign * hourAngle)) / 15.0f;
            while (delta < -24.0f) {
                delta = delta + 24.0f;
            }
            while (delta > 24.0f) {
                delta = delta - 24.0f;
            }
            ut = ut - delta;

            // I think we need to normalize UT
            while (ut < 0.0f) {
                ut = ut + 24.0f;
            }
            while (ut > 24.0f) {
                ut = ut - 24.0f;
            }

            ++counter;
        }

        // Return failure if we didn't converge.
        if (counter == MAX_ITERATIONS) {
            Log.d(TAG, "Rise/Set calculation didn't converge.");
            return -1.0f;
        }
        return ut;
    }

    public static float calculateHourAngle(float altitude, float latitude,
                                           float declination) {
        float altRads = altitude * MathUtil.DEGREES_TO_RADIANS;
        float latRads = latitude * MathUtil.DEGREES_TO_RADIANS;
        float decRads = declination * MathUtil.DEGREES_TO_RADIANS;
        float cosHa = (MathUtil.sin(altRads) - MathUtil.sin(latRads) * MathUtil.sin(decRads)) /
                (MathUtil.cos(latRads) * MathUtil.cos(decRads));

        return MathUtil.RADIANS_TO_DEGREES * MathUtil.acos(cosHa);
    }
}
