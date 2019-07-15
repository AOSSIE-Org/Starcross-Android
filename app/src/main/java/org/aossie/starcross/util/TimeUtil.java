package org.aossie.starcross.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class TimeUtil {
    private TimeUtil() {
    }

    /**
     * Calculate the number of Julian Centuries from the epoch 2000.0
     * (equivalent to Julian Day 2451545.0).
     */
    public static double julianCenturies(Date date) {
        double jd = calculateJulianDay(date);
        double delta = jd - 2451545.0;
        return delta / 36525.0;
    }

    public static double calculateJulianDay(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.setTime(date);

        double hour = cal.get(Calendar.HOUR_OF_DAY)
                + cal.get(Calendar.MINUTE) / 60.0f
                + cal.get(Calendar.SECOND) / 3600.0f;

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        double jd = 367.0 * year - Math.floor(7.0 * (year
                + Math.floor((month + 9.0) / 12.0)) / 4.0)
                + Math.floor(275.0 * month / 9.0) + day
                + 1721013.5 + hour / 24.0;
        return jd;
    }

    /**
     * Calculate local mean sidereal time in degrees. Note that longitude is
     * negative for western longitude values.
     */
    public static float meanSiderealTime(Date date, float longitude) {
        // First, calculate number of Julian days since J2000.0.
        double jd = calculateJulianDay(date);
        double delta = jd - 2451545.0f;

        // Calculate the global and local sidereal times
        double gst = 280.461f + 360.98564737f * delta;
        double lst = normalizeAngle(gst + longitude);

        return (float) lst;
    }

    /**
     * Normalize the angle to the range 0 <= value < 360.
     */
    public static double normalizeAngle(double angle) {
        double remainder = angle % 360;
        if (remainder < 0) remainder += 360;
        return remainder;
    }
}
