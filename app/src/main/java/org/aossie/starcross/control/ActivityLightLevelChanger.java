package org.aossie.starcross.control;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class ActivityLightLevelChanger {

  public static interface NightModeable {
    public void setNightMode(boolean nightMode);
  }

  private static final float BRIGHTNESS_DIM = (float) 0.5;

  private NightModeable nightModeable;
  private Activity activity;
  public ActivityLightLevelChanger(Activity activity, @Nullable NightModeable nightmodeable) {
    this.activity = activity;
    this.nightModeable = nightmodeable;
  }

  // current setting.
  public void setNightMode(boolean nightMode) {
    if (nightModeable != null) {
      nightModeable.setNightMode(nightMode);
    }
    Window window = activity.getWindow();
    WindowManager.LayoutParams params = window.getAttributes();
    if (nightMode) {
      params.screenBrightness = BRIGHTNESS_DIM;
      params.buttonBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF;
    } else {
      params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
      params.buttonBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
    }
    window.setAttributes(params);
  }
}