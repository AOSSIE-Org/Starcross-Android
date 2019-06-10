package org.aossie.starcross.touch;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import org.aossie.starcross.util.MiscUtil;

public class GestureInterpreter extends GestureDetector.SimpleOnGestureListener {// TODO
    private static final String TAG = MiscUtil.getTag(GestureInterpreter.class);

    private final Flinger flinger = new Flinger(new Flinger.FlingListener() {
        public void fling(float distanceX, float distanceY) {
        }
    });

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "Tap down");
        flinger.stop();
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG, "Flinging " + velocityX + ", " + velocityY);
        flinger.fling(velocityX, velocityY);
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG, "Tap up");
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "Double tap");
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(TAG, "Confirmed single tap");
        return false;
    }
}
