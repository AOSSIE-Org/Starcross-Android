package org.aossie.starcross.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import org.aossie.starcross.util.MiscUtil;
import org.aossie.starcross.util.TimeConstants;

public class SensorAccuracyMonitor implements SensorEventListener {
  private static final String TAG = MiscUtil.getTag(SensorAccuracyMonitor.class);
  private static final String LAST_CALIBRATION_WARNING_PREF_KEY = "Last calibration warning time";

  private SensorManager sensorManager;
  private Sensor compassSensor;
  private SharedPreferences sharedPreferences;


  public SensorAccuracyMonitor(
          SensorManager sensorManager, Context context, SharedPreferences sharedPreferences) {
    Log.d(TAG, "Creating new accuracy monitor");
    this.sensorManager = sensorManager;
    this.sharedPreferences = sharedPreferences;
    compassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
  }

  private boolean started = false;
  private boolean hasReading = false;

  /**
   * Starts monitoring.
   */
  public void start() {
    if (started) {
      return;
    }
    Log.d(TAG, "Starting monitoring compass accuracy");
    if (compassSensor != null) {
      sensorManager.registerListener(this, compassSensor, SensorManager.SENSOR_DELAY_UI);
    }
    started = true;
  }

  /**
   * Stops monitoring.  It's important this is called to disconnect from the sensors and
   * ensure the app does not needlessly consume power when in the background.
   */
  public void stop() {
    Log.d(TAG, "Stopping monitoring compass accuracy");
    started = false;
    hasReading = false;
    sensorManager.unregisterListener(this);
  }


  @Override
  public void onSensorChanged(SensorEvent event) {
    if (!hasReading) {
      onAccuracyChanged(event.sensor, event.accuracy);
    }
  }

  private static final long MIN_INTERVAL_BETWEEN_WARNINGS =
      60 * TimeConstants.MILLISECONDS_PER_SECOND;

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    hasReading = true;
    if (accuracy == SensorManager.SENSOR_STATUS_ACCURACY_HIGH
        || accuracy == SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM) {
      return;  // OK
    }
    Log.d(TAG, "Compass accuracy insufficient");
    long nowMillis = System.currentTimeMillis();
    long lastWarnedMillis = sharedPreferences.getLong(LAST_CALIBRATION_WARNING_PREF_KEY, 0);
    if (nowMillis - lastWarnedMillis < MIN_INTERVAL_BETWEEN_WARNINGS) {
      Log.d(TAG, "...but too soon to warn again");
      return;
    }
    sharedPreferences.edit().putLong(LAST_CALIBRATION_WARNING_PREF_KEY, nowMillis).apply();

  }
}
