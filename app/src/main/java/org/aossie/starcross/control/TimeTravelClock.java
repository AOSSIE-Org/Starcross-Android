package org.aossie.starcross.control;

import java.util.Date;

import static org.aossie.starcross.util.TimeConstants.MILLISECONDS_PER_DAY;
import static org.aossie.starcross.util.TimeConstants.SECONDS_PER_10MINUTE;
import static org.aossie.starcross.util.TimeConstants.SECONDS_PER_DAY;
import static org.aossie.starcross.util.TimeConstants.SECONDS_PER_HOUR;
import static org.aossie.starcross.util.TimeConstants.SECONDS_PER_MINUTE;
import static org.aossie.starcross.util.TimeConstants.SECONDS_PER_SECOND;
import static org.aossie.starcross.util.TimeConstants.SECONDS_PER_WEEK;

class TimeTravelClock {
    private static class Speed {
        double rate;
        Speed(double rate) {this.rate = rate;}
    }

    private static final long STOPPED = 0;
    private static final Speed[] SPEEDS = {
            new Speed(-SECONDS_PER_WEEK),
            new Speed(-SECONDS_PER_DAY),
            new Speed(-SECONDS_PER_HOUR),
            new Speed(-SECONDS_PER_10MINUTE),
            new Speed(-SECONDS_PER_MINUTE),
            new Speed(-SECONDS_PER_SECOND),
            new Speed(STOPPED),
            new Speed(SECONDS_PER_SECOND),
            new Speed(SECONDS_PER_MINUTE),
            new Speed(SECONDS_PER_10MINUTE),
            new Speed(SECONDS_PER_HOUR),
            new Speed(SECONDS_PER_DAY),
            new Speed(SECONDS_PER_WEEK),
    };
    private static final int STOPPED_INDEX = SPEEDS.length / 2;
    private int speedIndex = STOPPED_INDEX;
    private long timeLastSet;
    private long simulatedTime;

    synchronized void setTimeTravelDate(Date date) {
        pauseTime();
        timeLastSet = System.currentTimeMillis();
        simulatedTime = date.getTime();
    }

    private synchronized void pauseTime() {
        speedIndex = STOPPED_INDEX;
    }

    long getTimeInMillis() {
        long now = System.currentTimeMillis();
        long elapsedTimeMillis = now - timeLastSet;
        double rate = SPEEDS[speedIndex].rate;
        long timeDelta = (long) (rate * elapsedTimeMillis);
        if (Math.abs(rate) >= SECONDS_PER_DAY) {
            long days = (long) (timeDelta / MILLISECONDS_PER_DAY);
            if (days == 0) {
                return simulatedTime;
            }
            timeDelta = days * MILLISECONDS_PER_DAY;
        }
        timeLastSet = now;
        simulatedTime += timeDelta;
        return simulatedTime;
    }
}
