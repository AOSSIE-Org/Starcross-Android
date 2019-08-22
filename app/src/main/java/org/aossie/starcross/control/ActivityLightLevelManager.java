package org.aossie.starcross.control;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;


public class ActivityLightLevelManager implements OnSharedPreferenceChangeListener {
  private ActivityLightLevelChanger lightLevelChanger;
  private SharedPreferences sharedPreferences;
  private enum LightMode {DAY, NIGHT, AUTO}
  public static final String LIGHT_MODE_KEY = "lightmode";
  public ActivityLightLevelManager(ActivityLightLevelChanger lightLevelChanger,
                                   SharedPreferences sharedPreferences) {
    this.lightLevelChanger = lightLevelChanger;
    this.sharedPreferences = sharedPreferences;
  }

  public void onResume() {
    registerWithPreferences();
    LightMode currentMode = getLightModePreference();
    setActivityMode(currentMode);
  }

  private void setActivityMode(LightMode currentMode) {
    switch(currentMode) {
      case DAY:
        lightLevelChanger.setNightMode(false);
        break;
      case NIGHT:
        lightLevelChanger.setNightMode(true);
        break;
      case AUTO:
        throw new UnsupportedOperationException("not implemented yet");
    }
  }

  private LightMode getLightModePreference() {
    String preference = sharedPreferences.getString(LIGHT_MODE_KEY, LightMode.DAY.name());
    return LightMode.valueOf(preference);
  }

  private void registerWithPreferences() {
    sharedPreferences.registerOnSharedPreferenceChangeListener(this);
  }

  public void onPause() {
    unregisterWithPreferences();
  }

  private void unregisterWithPreferences() {
    sharedPreferences.registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (!key.equals(LIGHT_MODE_KEY)) {
      return;
    }
    setActivityMode(getLightModePreference());
  }
}
