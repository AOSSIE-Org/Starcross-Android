package org.aossie.starcross.util.smoothers;

import android.content.SharedPreferences;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.Log;

import org.aossie.starcross.control.AstronomerModel;
import org.aossie.starcross.util.Constant;
import org.aossie.starcross.util.MiscUtil;
import org.aossie.starcross.util.Vector3;

public class PlainSmootherModelAdaptor implements SensorListener {
    private static final String TAG = MiscUtil.getTag(PlainSmootherModelAdaptor.class);
    private Vector3 magneticValues = Constant.INITIAL_SOUTH.copy();
    private Vector3 acceleration = Constant.INITIAL_DOWN.copy();
    private AstronomerModel model;
    private boolean reverseMagneticZaxis;

    public PlainSmootherModelAdaptor(AstronomerModel model, SharedPreferences sharedPreferences) {
        this.model = model;
        reverseMagneticZaxis = false;
    }

    @Override
    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
            acceleration.x = values[0];
            acceleration.y = values[1];
            acceleration.z = values[2];
        } else if (sensor == SensorManager.SENSOR_MAGNETIC_FIELD) {
            magneticValues.x = values[0];
            magneticValues.y = values[1];
            magneticValues.z = reverseMagneticZaxis ? values[2] : -values[2];
        }
        model.setPhoneSensorValues(acceleration, magneticValues);
    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {
    }
}
