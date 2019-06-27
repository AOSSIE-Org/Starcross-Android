package org.aossie.starcross.activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

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

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView skyView;
    private AstronomerModel model;
    private ControllerGroup controller;
    private android.view.GestureDetector gestureDetector;
    private GestureDetector dragZoomRotateDetector;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

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
        // TODO filter works with adding or removing layer From layer manager
//        layerManager.addLayer(new ConstellationsLayer(getAssets(), getResources())); // TODO add constellations to surfaceview
//        layerManager.addLayer(new PlanetsLayer(getResources())); // TODO add planets to surfaceview
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

}
