package org.aossie.starcross.util.smoothers;

import android.hardware.SensorListener;


public abstract class SensorSmoother implements SensorListener {

    protected SensorListener listener;

    public SensorSmoother(SensorListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {
    }

    public abstract void onSensorChanged(int sensor, float[] values);
}
