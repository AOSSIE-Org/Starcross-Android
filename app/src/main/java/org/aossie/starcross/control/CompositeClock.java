package org.aossie.starcross.control;

import java.util.Date;

class CompositeClock implements Clock {
    private enum Mode {REAL_TIME, TRANSITION, TIME_TRAVEL}

    private static final long TRANSITION_TIME_MILLIS = 2500L;
    private Clock realClock;
    private TimeTravelClock travelClock;
    private Mode mode = Mode.REAL_TIME;
    private long startTime;
    private long endTime;
    private long transitionWallTime;
    private Mode transitionTo;

    CompositeClock(TimeTravelClock travelClock, Clock realClock) {
        this.travelClock = travelClock;
        this.realClock = realClock;
    }

    void goTimeTravel(Date targetDate) {
        startTime = getTimeInMillisSinceEpoch();
        endTime = targetDate.getTime();
        travelClock.setTimeTravelDate(targetDate);
        mode = Mode.TRANSITION;
        transitionTo = Mode.TIME_TRAVEL;
        transitionWallTime = System.currentTimeMillis();
    }

    @Override
    public long getTimeInMillisSinceEpoch() {
        if (mode == Mode.TRANSITION) {
            long elapsedTimeMillis = System.currentTimeMillis() - transitionWallTime;
            if (elapsedTimeMillis > TRANSITION_TIME_MILLIS) {
                mode = transitionTo;
            } else {
                return (long) interpolate(startTime, endTime,
                        ((double) elapsedTimeMillis) / TRANSITION_TIME_MILLIS);
            }
        }
        switch (mode) {
            case REAL_TIME:
                return System.currentTimeMillis();
            case TIME_TRAVEL:
                return travelClock.getTimeInMillis();
        }
        return realClock.getTimeInMillisSinceEpoch();
    }

    private static double interpolate(double start, double end, double lambda) {
        return (start + (3 * lambda * lambda - 2 * lambda * lambda * lambda) * (end - start));
    }
}