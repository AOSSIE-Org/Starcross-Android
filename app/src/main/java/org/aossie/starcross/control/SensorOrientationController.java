package org.aossie.starcross.control;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.Log;

import org.aossie.starcross.util.Constant;
import org.aossie.starcross.util.MiscUtil;
import org.aossie.starcross.util.smoothers.ExponentiallyWeightedSmoother;
import org.aossie.starcross.util.smoothers.PlainSmootherModelAdaptor;

public class SensorOrientationController extends AbstractController
        implements SensorEventListener {

    private static class SensorDampingSettings {
        public float damping;
        public int exponent;

        public SensorDampingSettings(float damping, int exponent) {
            this.damping = damping;
            this.exponent = exponent;
        }
    }

    private final static String TAG = MiscUtil.getTag(SensorOrientationController.class);
    /**
     * Parameters that control the smoothing of the accelerometer and
     * magnetic sensors.
     */
    private static final SensorDampingSettings[] ACC_DAMPING_SETTINGS = new SensorDampingSettings[]{
            new SensorDampingSettings(0.7f, 3),
            new SensorDampingSettings(0.7f, 3),
            new SensorDampingSettings(0.1f, 3),
            new SensorDampingSettings(0.1f, 3),
    };
    private static final SensorDampingSettings[] MAG_DAMPING_SETTINGS = new SensorDampingSettings[]{
            new SensorDampingSettings(0.05f, 3),  // Derived for the Nexus One
            new SensorDampingSettings(0.001f, 4),  // Derived for the unpatched MyTouch Slide
            new SensorDampingSettings(0.0001f, 5),  // Just guessed for Nexus 6
            new SensorDampingSettings(0.000001f, 5)  // Just guessed for Nexus 6
    };

    private SensorManager manager;
    private SensorListener accelerometerSmoother;
    private SensorListener compassSmoother;
    private PlainSmootherModelAdaptor modelAdaptor;
    private Sensor rotationSensor;
    private SharedPreferences sharedPreferences;


    public SensorOrientationController(PlainSmootherModelAdaptor modelAdaptor,
                                       SensorManager manager, SharedPreferences sharedPreferences) {
        this.manager = manager;
        this.modelAdaptor = modelAdaptor;
        this.rotationSensor = manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void start() {

        if (manager != null) {
            if (!sharedPreferences.getBoolean(Constant.SHARED_PREFERENCE_DISABLE_GYRO,
                    false)) {
                Log.d(TAG, "Using rotation sensor");
                manager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_GAME);
            } else {
                // TODO(jontayler): remove this code once enough it's used in few enough phones.
                Log.d(TAG, "Using classic sensors");
                Log.d(TAG, "Exponentially weighted smoothers used");
                String dampingPreference =  Constant.SENSOR_DAMPING_EXTRA_HIGH;
                String speedPreference = Constant.SENSOR_SPEED_SLOW;
                Log.d(TAG, "Sensor damping preference " + dampingPreference);
                Log.d(TAG, "Sensor speed preference " + speedPreference);
                int dampingIndex = 2;
                int sensorSpeed = SensorManager.SENSOR_DELAY_NORMAL;
                accelerometerSmoother = new ExponentiallyWeightedSmoother(
                        modelAdaptor,
                        ACC_DAMPING_SETTINGS[dampingIndex].damping,
                        ACC_DAMPING_SETTINGS[dampingIndex].exponent);
                compassSmoother = new ExponentiallyWeightedSmoother(
                        modelAdaptor,
                        MAG_DAMPING_SETTINGS[dampingIndex].damping,
                        MAG_DAMPING_SETTINGS[dampingIndex].exponent);
                manager.registerListener(accelerometerSmoother,
                        SensorManager.SENSOR_ACCELEROMETER,
                        sensorSpeed);
                manager.registerListener(compassSmoother,
                        SensorManager.SENSOR_MAGNETIC_FIELD,
                        sensorSpeed);
            }
        }
        Log.d(TAG, "Registered sensor listener");
    }

    @Override
    public void stop() {
        Log.d(
                TAG, "Unregistering sensor listeners: " + accelerometerSmoother + ", "
                        + compassSmoother + ", " + this);
        manager.unregisterListener(accelerometerSmoother);
        manager.unregisterListener(compassSmoother);
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor != rotationSensor) {
            return;
        }
        model.setPhoneSensorValues(event.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ignore
    }
}
