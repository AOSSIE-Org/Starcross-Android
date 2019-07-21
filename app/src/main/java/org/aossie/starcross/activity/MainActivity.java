package org.aossie.starcross.activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.aossie.starcross.R;
import org.aossie.starcross.control.AstronomerModel;
import org.aossie.starcross.control.AstronomerModelImpl;
import org.aossie.starcross.control.ControllerGroup;
import org.aossie.starcross.layer.ConstellationsLayer;
import org.aossie.starcross.layer.LayerManager;
import org.aossie.starcross.layer.PlanetsLayer;
import org.aossie.starcross.layer.StarsLayer;
import org.aossie.starcross.renderer.RendererController;
import org.aossie.starcross.renderer.RendererModelUpdateClosure;
import org.aossie.starcross.renderer.SkyRenderer;
import org.aossie.starcross.touch.GestureDetector;
import org.aossie.starcross.touch.GestureInterpreter;
import org.aossie.starcross.touch.MapMover;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView skyView;
    private AstronomerModel model;
    private ControllerGroup controller;
    private android.view.GestureDetector gestureDetector;
    private GestureDetector dragZoomRotateDetector;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // todo get intent search provider

        controller = new ControllerGroup();
        model = new AstronomerModelImpl();

        initializeView();
    }

    private void initializeView() {
        setContentView(R.layout.activity_main);

        SkyRenderer renderer = new SkyRenderer(getResources());

        skyView = findViewById(R.id.surface);
        skyView.setEGLConfigChooser(false);
        skyView.setRenderer(renderer);

        RendererController rendererController = new RendererController(renderer, skyView);
        rendererController.addUpdateClosure(new RendererModelUpdateClosure(model, rendererController));

        LayerManager layerManager = new LayerManager();
        layerManager.addLayer(new StarsLayer(getAssets(), getResources()));
        layerManager.addLayer(new ConstellationsLayer(getAssets(), getResources()));
        layerManager.addLayer(new PlanetsLayer(model, getResources()));
        layerManager.initialize();
        layerManager.registerWithRenderer(rendererController);

        controller.setModel(model);

        MapMover mapMover = new MapMover(model, controller, this);
        gestureDetector = new android.view.GestureDetector(this, new GestureInterpreter(mapMover));
        dragZoomRotateDetector = new GestureDetector(mapMover);
    }

    @Override
    public void onResume() {
        super.onResume();
        skyView.onResume();
        controller.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        skyView.onPause();
        controller.stop();
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

    public void startTimeTravel(View view) { // TODO use call from UI
        Calendar day = Calendar.getInstance(); // random date
        day.set(Calendar.DAY_OF_MONTH, 25);
        day.set(Calendar.MONTH, 7);
        day.set(Calendar.YEAR, 1985);
        controller.goTimeTravel(day.getTime());
    }

    public void stopTimeTravel(View view) {
        controller.useRealTime();
    }
}
