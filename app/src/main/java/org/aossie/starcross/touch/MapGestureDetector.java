package org.aossie.starcross.touch;

import android.view.MotionEvent;

import org.aossie.starcross.util.MathUtil;

public class MapGestureDetector {
    private enum State {READY, DRAGGING, DRAGGING2}

    private float last1X;
    private float last1Y;
    private float last2X;
    private float last2Y;
    private State currentState = State.READY;
    private DragRotateZoomGestureDetectorListener listener;

    public interface DragRotateZoomGestureDetectorListener {
        void onDrag(float xPixels, float yPixels);

        void onStretch(float ratio);

        void onRotate(float radians);
    }

    public MapGestureDetector(DragRotateZoomGestureDetectorListener listener) {
        this.listener = listener;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int actionCode = ev.getAction() & MotionEvent.ACTION_MASK;
        if (actionCode == MotionEvent.ACTION_DOWN || currentState == State.READY) {
            currentState = State.DRAGGING;
            last1X = ev.getX();
            last1Y = ev.getY();
            return true;
        }

        if (actionCode == MotionEvent.ACTION_MOVE && currentState == State.DRAGGING) {
            float current1X = ev.getX();
            float current1Y = ev.getY();
            listener.onDrag(current1X - last1X, current1Y - last1Y);
            last1X = current1X;
            last1Y = current1Y;
            return true;
        }

        if (actionCode == MotionEvent.ACTION_MOVE && currentState == State.DRAGGING2) {
            int pointerCount = ev.getPointerCount();
            if (pointerCount != 2) {
                return false;
            }
            float current1X = ev.getX(0);
            float current1Y = ev.getY(0);
            float current2X = ev.getX(1);
            float current2Y = ev.getY(1);

            float distanceMovedX1 = current1X - last1X;
            float distanceMovedY1 = current1Y - last1Y;
            float distanceMovedX2 = current2X - last2X;
            float distanceMovedY2 = current2Y - last2Y;

            listener.onDrag((distanceMovedX1 + distanceMovedX2) / 2,
                    (distanceMovedY1 + distanceMovedY2) / 2);

            float vectorLastX = last1X - last2X;
            float vectorLastY = last1Y - last2Y;
            float vectorCurrentX = current1X - current2X;
            float vectorCurrentY = current1Y - current2Y;

            float lengthRatio = MathUtil.sqrt(normSquared(vectorCurrentX, vectorCurrentY)
                    / normSquared(vectorLastX, vectorLastY));
            listener.onStretch(lengthRatio);
            float angleLast = MathUtil.atan2(vectorLastX, vectorLastY);
            float angleCurrent = MathUtil.atan2(vectorCurrentX, vectorCurrentY);
            float angleDelta = angleCurrent - angleLast;
            listener.onRotate(angleDelta * MathUtil.RADIANS_TO_DEGREES);

            last1X = current1X;
            last1Y = current1Y;
            last2X = current2X;
            last2Y = current2Y;
            return true;
        }

        if (actionCode == MotionEvent.ACTION_UP) {
            currentState = State.READY;
            return true;
        }

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN && currentState == State.DRAGGING) {
            int pointerCount = ev.getPointerCount();
            if (pointerCount != 2) {
                return false;
            }
            currentState = State.DRAGGING2;
            last1X = ev.getX(0);
            last1Y = ev.getY(0);
            last2X = ev.getX(1);
            last2Y = ev.getY(1);
            return true;
        }

        if (actionCode == MotionEvent.ACTION_POINTER_UP && currentState == State.DRAGGING2) {
            currentState = State.READY;
            return true;
        }
        return false;
    }

    private static float normSquared(float x, float y) {
        return (x * x + y * y);
    }
}