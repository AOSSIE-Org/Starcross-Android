package org.aossie.starcross;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.util.Log;

import org.aossie.starcross.util.Constant;
import org.aossie.starcross.util.MiscUtil;
import org.aossie.starcross.util.TypefaceUtil;


public class StarCross extends Application {
    private static final String TAG = MiscUtil.getTag(StarCross.class);

    SharedPreferences preferences;
    SensorManager sensorManager;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        performFeatureCheck();

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF",
                "fonts/proxima_nova.ttf");
    }

    private void performFeatureCheck() {
        if (sensorManager == null) {
            return;
        }
        boolean hasRotationSensor = false;
        if (hasDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)) {
            if (hasDefaultSensor(Sensor.TYPE_ACCELEROMETER) && hasDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
                    && hasDefaultSensor(Sensor.TYPE_GYROSCOPE)) {
                hasRotationSensor = true;
            } else {
                if (hasDefaultSensor(Sensor.TYPE_ACCELEROMETER)) {
                    hasDefaultSensor(
                            Sensor.TYPE_MAGNETIC_FIELD);
                }
            }
        }

        if (!preferences.contains(Constant.SHARED_PREFERENCE_DISABLE_GYRO)) {
            preferences.edit().putBoolean(
                    Constant.SHARED_PREFERENCE_DISABLE_GYRO, !hasRotationSensor).apply();
        }

        int[] importantSensorTypes = {Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE,
                Sensor.TYPE_MAGNETIC_FIELD, Sensor.TYPE_LIGHT, Sensor.TYPE_ROTATION_VECTOR,
                Sensor.TYPE_ORIENTATION};

        for (int sensorType : importantSensorTypes) {
            if (hasDefaultSensor(sensorType)) {
                Log.i(TAG, "No sensor of type " + sensorType);
            } else {
                Log.i(TAG, "Sensor present of type " + sensorType);
            }
        }
    }

    private boolean hasDefaultSensor(int sensorType) {
        if (sensorManager == null) {
            return false;
        }
        Sensor sensor = sensorManager.getDefaultSensor(sensorType);
        if (sensor == null) {
            return false;
        }
        SensorEventListener dummy = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        boolean success = sensorManager.registerListener(
                dummy, sensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.unregisterListener(dummy);
        return success;
    }
}
