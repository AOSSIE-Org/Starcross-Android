package org.aossie.starcross.touch;

import android.util.Log;

import org.aossie.starcross.util.MiscUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class Flinger {
    private static final String TAG = MiscUtil.getTag(Flinger.class);

    public interface FlingListener {
        void fling(float distanceX, float distanceY);
    }

    private FlingListener listener;
    private int updatesPerSecond = 20;
    private int timeIntervalMillis = 1000 / updatesPerSecond;
    private ScheduledExecutorService executor;
    private ScheduledFuture<?> flingTask;

    Flinger(FlingListener listener) {
        this.listener = listener;
        executor = Executors.newScheduledThreadPool(1);
    }

    void fling(float velocityX, float velocityY) {
        Log.d(TAG, "Doing the fling");
        class PositionUpdater implements Runnable {
            private float myVelocityX, myVelocityY;
            private float decelFactor = 1.1f;
            private float TOL = 10;

            private PositionUpdater(float velocityX, float velocityY) {
                this.myVelocityX = velocityX;
                this.myVelocityY = velocityY;
            }

            public void run() {
                if (myVelocityX * myVelocityX + myVelocityY * myVelocityY < TOL) {
                    stop();
                }
                listener.fling(myVelocityX / updatesPerSecond,
                        myVelocityY / updatesPerSecond);
                myVelocityX /= decelFactor;
                myVelocityY /= decelFactor;
            }
        }
        flingTask = executor.scheduleAtFixedRate(new PositionUpdater(velocityX, velocityY),
                0, timeIntervalMillis, TimeUnit.MILLISECONDS);
    }

    void stop() {
        if (flingTask != null) flingTask.cancel(true);
        Log.d(TAG, "Fling stopped");
    }
}