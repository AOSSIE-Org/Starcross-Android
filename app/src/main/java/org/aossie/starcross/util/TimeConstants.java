package org.aossie.starcross.util;

public class TimeConstants {
    public static final long MILLISECONDS_PER_SECOND = 1000L;
    public static final long MILLISECONDS_PER_MINUTE = 60000L;
    public static final long MILLISECONDS_PER_HOUR = 3600000L;
    public static final long SECONDS_PER_HOUR = 3600L;
    public static final long SECONDS_PER_DAY = 24 * SECONDS_PER_HOUR;
    public static final long SECONDS_PER_WEEK = 7 * SECONDS_PER_DAY;
    public static final double SECONDS_PER_SIDERIAL_DAY = 86164.0905;
    public static final double MILLISECONDS_PER_SIDEREAL_DAY =
            MILLISECONDS_PER_SECOND * SECONDS_PER_SIDERIAL_DAY;
    public static final double SECONDS_PER_SIDERIAL_WEEK = 7 * 86164.0905;
}
