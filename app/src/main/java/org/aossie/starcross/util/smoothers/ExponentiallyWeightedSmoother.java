package org.aossie.starcross.util.smoothers;

import android.hardware.SensorListener;
import android.util.Log;

import org.aossie.starcross.util.MathUtil;
import org.aossie.starcross.util.MiscUtil;

public class ExponentiallyWeightedSmoother extends SensorSmoother {
    private static final String TAG = MiscUtil.getTag(ExponentiallyWeightedSmoother.class);
    private float alpha;
    private int exponent;

    public ExponentiallyWeightedSmoother(SensorListener listener, float alpha, int exponent) {
        super(listener);
        this.alpha = alpha;
        this.exponent = exponent;
    }

    private float[] last = new float[3];
    private float[] current = new float[3];

    @Override
    public void onSensorChanged(int sensor, float[] values) {
        for (int i = 0; i < 3; ++i) {
            last[i] = current[i];
            float diff = values[i] - last[i];
            float correction = diff * alpha;
            for (int j = 1; j < exponent; ++j) {
                correction *= MathUtil.abs(diff);
            }
            if (correction > MathUtil.abs(diff) ||
                    correction < -MathUtil.abs(diff)) correction = diff;
            current[i] = last[i] + correction;
        }
        listener.onSensorChanged(sensor, current);
    }
}
