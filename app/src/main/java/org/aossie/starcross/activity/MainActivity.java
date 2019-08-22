package org.aossie.starcross.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.aossie.starcross.R;
import org.aossie.starcross.control.ActivityLightLevelChanger;
import org.aossie.starcross.control.ActivityLightLevelManager;
import org.aossie.starcross.control.AstronomerModel;
import org.aossie.starcross.control.AstronomerModelImpl;
import org.aossie.starcross.control.ControllerGroup;
import org.aossie.starcross.control.SensorAccuracyMonitor;
import org.aossie.starcross.control.SensorOrientationController;
import org.aossie.starcross.layer.ConstellationsLayer;
import org.aossie.starcross.layer.LayerManager;
import org.aossie.starcross.layer.PlanetsLayer;
import org.aossie.starcross.layer.StarsLayer;
import org.aossie.starcross.renderer.RendererController;
import org.aossie.starcross.renderer.RendererModelUpdateClosure;
import org.aossie.starcross.renderer.SkyRenderer;
import org.aossie.starcross.search.SearchResult;
import org.aossie.starcross.search.SearchTerm;
import org.aossie.starcross.touch.MapGestureDetector;
import org.aossie.starcross.touch.GestureInterpreter;
import org.aossie.starcross.touch.MapMover;
import org.aossie.starcross.util.Constant;
import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.MiscUtil;
import org.aossie.starcross.util.smoothers.PlainSmootherModelAdaptor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.aossie.starcross.util.Constant.SHARED_PREFERENCE_DISABLE_GYRO;

public class MainActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        View.OnClickListener {
    private GLSurfaceView skyView;
    private AstronomerModel model;
    private String searchTargetName;
    private Animation flashAnimation;
    private LayerManager layerManager;
    private boolean nightMode = false;
    private ConstraintLayout menuCont;
    private ControllerGroup controller;
    private boolean searchMode = false;
    private ImageView menuIcon, searchIcon;
    private PowerManager.WakeLock wakeLock;
    private GestureDetector gestureDetector;
    private SharedPreferences sharedPreferences;
    private RendererController rendererController;
    private MapGestureDetector dragZoomRotateDetector;
    private Calendar calendar = Calendar.getInstance();
    private SensorAccuracyMonitor sensorAccuracyMonitor;
    private Calendar currentDate = Calendar.getInstance();
    private AutoCompleteTextView search_view, search_view_red;
    private ActivityLightLevelManager activityLightLevelManager;
    private static final String TAG = MiscUtil.getTag(MainActivity.class);
    private GeocentricCoordinates searchTarget = GeocentricCoordinates.getInstance(0, 0);
    private TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            setTimeTravelMode(calendar.getTime());
        }
    };
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(year, monthOfYear, dayOfMonth);
            setTime();
        }
    };

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        initObjects();
        initializeView();
        checkForSensors();

        ActivityLightLevelChanger activityLightLevelChanger = new ActivityLightLevelChanger(this,
                new ActivityLightLevelChanger.NightModeable() {
                    @Override
                    public void setNightMode(boolean nightMode1) {
                        MainActivity.this.rendererController.queueNightVisionMode(nightMode1);
                    }
                });
        activityLightLevelManager = new ActivityLightLevelManager(activityLightLevelChanger, sharedPreferences);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
    }

    private void initObjects() {
        model = new AstronomerModelImpl();
        flashAnimation = AnimationUtils.loadAnimation(this, R.anim.flash);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        SensorManager sensorManager = (SensorManager) getApplicationContext()
                .getSystemService(Context.SENSOR_SERVICE);

        controller = new ControllerGroup(
                new SensorOrientationController(new PlainSmootherModelAdaptor(model, sharedPreferences),
                        sensorManager, sharedPreferences));

        sensorAccuracyMonitor = new SensorAccuracyMonitor(sensorManager, this, sharedPreferences);

        layerManager = new LayerManager();
        layerManager.addLayer(new StarsLayer(getAssets(), getResources()));
        layerManager.addLayer(new ConstellationsLayer(getAssets(), getResources()));
        layerManager.addLayer(new PlanetsLayer(model, getResources(), sharedPreferences));

        layerManager.initialize();
    }

    private void checkForSensors() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null && sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
                && sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            sharedPreferences.edit().putBoolean(Constant.AUTO_MODE_PREF_KEY, false).apply();
            setAutoMode(false);
            return;
        }
    }

    private void initializeView() {
        setContentView(R.layout.activity_main);
        addPreferencesFromResource(R.xml.settings);
        search_view = findViewById(R.id.search_view);
        search_view_red = findViewById(R.id.search_view_red);
        menuCont = findViewById(R.id.menu_cont);
        menuIcon = findViewById(R.id.menu);
        searchIcon = findViewById(R.id.search);
        ImageView closeIcon = findViewById(R.id.close);
        menuIcon.setOnClickListener(this);
        searchIcon.setOnClickListener(this);
        closeIcon.setOnClickListener(this);

        skyView = findViewById(R.id.surface);
        skyView.setEGLConfigChooser(false);
        SkyRenderer renderer = new SkyRenderer(getResources());
        skyView.setRenderer(renderer);

        rendererController = new RendererController(renderer, skyView);
        rendererController.addUpdateClosure(
                new RendererModelUpdateClosure(model, rendererController));
        layerManager.registerWithRenderer(rendererController);
        controller.setModel(model);

        MapMover mapMover = new MapMover(model, controller, this);

        gestureDetector = new GestureDetector(this, new GestureInterpreter(mapMover));
        dragZoomRotateDetector = new MapGestureDetector(mapMover);
    }


    @Override
    public void onBackPressed() {
        if (menuCont.getVisibility() == View.VISIBLE) {
            closeMenu();
        } else if (searchMode) {
            cancelSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        wakeLock.acquire();
        skyView.onResume();
        controller.start();
        activityLightLevelManager.onResume();
        if (controller.isAutoMode()) {
            sensorAccuracyMonitor.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorAccuracyMonitor.stop();
        activityLightLevelManager.onPause();
        controller.stop();
        skyView.onPause();
        wakeLock.release();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean eventAbsorbed = false;
        if (gestureDetector.onTouchEvent(event)) {
            eventAbsorbed = true;
        }
        if (dragZoomRotateDetector.onTouchEvent(event)) {
            eventAbsorbed = true;
        }
        return eventAbsorbed;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        controller.rotate(event.getX() * 10);
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if (state == null) return;
        searchMode = state.getBoolean(Constant.BUNDLE_SEARCH_MODE);
        float x = state.getFloat(Constant.BUNDLE_X_TARGET);
        float y = state.getFloat(Constant.BUNDLE_Y_TARGET);
        float z = state.getFloat(Constant.BUNDLE_Z_TARGET);
        searchTarget = new GeocentricCoordinates(x, y, z);
        searchTargetName = state.getString(Constant.BUNDLE_TARGET_NAME);
        if (searchMode) {
            rendererController.queueEnableSearchOverlay(searchTarget, searchTargetName);
        }
        nightMode = state.getBoolean(Constant.BUNDLE_NIGHT_MODE, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constant.BUNDLE_SEARCH_MODE, searchMode);
        outState.putFloat(Constant.BUNDLE_X_TARGET, searchTarget.x);
        outState.putFloat(Constant.BUNDLE_Y_TARGET, searchTarget.y);
        outState.putFloat(Constant.BUNDLE_Z_TARGET, searchTarget.z);
        outState.putString(Constant.BUNDLE_TARGET_NAME, searchTargetName);
        outState.putBoolean(Constant.BUNDLE_NIGHT_MODE, nightMode);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        closeMenu();
        switch (key) {
            case Constant.AUTO_MODE_PREF_KEY:
                boolean autoMode = sharedPreferences.getBoolean(key, true);
                if (!autoMode) {
                    Toast.makeText(MainActivity.this, R.string.set_manual, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, R.string.set_auto, Toast.LENGTH_SHORT).show();
                }
                setAutoMode(autoMode);
                break;
            case SHARED_PREFERENCE_DISABLE_GYRO:
                onPause();
                onResume();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                openMenu();
                break;
            case R.id.close:
                closeMenu();
                break;
            case R.id.search:
                searchSkyObject();
                break;
        }
    }

    public void setTimeTravelMode(Date newTime) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd G  HH:mm:ss z");
        Toast.makeText(this,
                String.format(getString(R.string.time_travel_start_message_alt), dateFormatter.format(newTime)),
                Toast.LENGTH_LONG).show();
        flashTheScreen();
        controller.goTimeTravel(newTime);
    }

    private void flashTheScreen() {
        final View view = findViewById(R.id.mask);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(flashAnimation);
    }

    private void doSearchWithoutIntent(String queryString) {
        hideSoftKeyboard(this);
        if (searchMode) {
            cancelSearch();
        }
        searchMode = true;
        List<SearchResult> results = layerManager.searchByObjectName(queryString);
        if (results.size() == 0) {
            Toast.makeText(this, "No Search Results", Toast.LENGTH_LONG).show();
        } else {
            final SearchResult result = results.get(0);
            activateSearchTarget(result.coords, result.capitalizedName);
        }
    }

    private void openMenu() {
        hideSoftKeyboard(this);
        menuCont.setVisibility(View.VISIBLE);
    }

    private void closeMenu() {
        hideSoftKeyboard(this);
        menuCont.setVisibility(View.GONE);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private String[] getSuggestions(String query) {
        List<String> list = new ArrayList<>();
        Set<SearchTerm> results = layerManager.getObjectNamesMatchingPrefix(query);

        for (SearchTerm result : results) {
            list.add(result.query);
        }
        String[] r = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            r[i] = list.get(i);
        }
        return r;
    }

    private void setAutoMode(boolean auto) {
        controller.setAutoMode(auto);
        if (auto) {
            sensorAccuracyMonitor.start();
        } else {
            sensorAccuracyMonitor.stop();
        }
    }

    private void cancelSearch() {
        rendererController.queueDisableSearchOverlay();
        searchMode = false;
        hideSoftKeyboard(this);
    }

    public void activateSearchTarget(GeocentricCoordinates target, final String searchTerm) {
        searchTarget = target;
        searchTargetName = searchTerm;
        rendererController.queueViewerUpDirection(model.getZenith().copy());
        rendererController.queueEnableSearchOverlay(target.copy(), searchTerm);
        boolean autoMode = sharedPreferences.getBoolean(Constant.AUTO_MODE_PREF_KEY, true);
        if (!autoMode) {
            controller.teleport(target);
        }
    }

    public AstronomerModel getModel() {
        return model;
    }

    public void switchNightMode(View view) {
        closeMenu();

        nightMode = !nightMode;

        if (nightMode) {
            search_view.setText("");
            search_view.setVisibility(View.INVISIBLE);

            menuIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_red));
            searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_red));
        } else {
            search_view_red.setText("");
            search_view_red.setVisibility(View.INVISIBLE);


            menuIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));
            searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_search));
        }

        sharedPreferences.edit().putString(ActivityLightLevelManager.LIGHT_MODE_KEY,
                nightMode ? "NIGHT" : "DAY").commit();
    }

    public void searchSkyObject() {
        String[] rt = getSuggestions("");
        closeMenu();

        search_view.setAdapter(new ArrayAdapter<>(this, R.layout.item, rt));
        search_view_red.setAdapter(new ArrayAdapter<>(this, R.layout.item_red, rt));
        search_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doSearchWithoutIntent(search_view.getText().toString());
            }
        });
        search_view_red.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doSearchWithoutIntent(search_view_red.getText().toString());
            }
        });


        if (nightMode) {
            search_view_red.setText("");
            search_view_red.setVisibility(View.VISIBLE);
            search_view_red.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(search_view_red, InputMethodManager.SHOW_IMPLICIT);
        } else {
            search_view.setText("");
            search_view.setVisibility(View.VISIBLE);
            search_view.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(search_view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void openDatePicker(View view) {
        closeMenu();

        new DatePickerDialog(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, onDateSetListener,
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH))
                .show();

    }

    public void setTime() {
        new TimePickerDialog(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, onTimeSetListener,
                currentDate.get(Calendar.HOUR_OF_DAY),
                currentDate.get(Calendar.MINUTE), true)
                .show();
    }
}
