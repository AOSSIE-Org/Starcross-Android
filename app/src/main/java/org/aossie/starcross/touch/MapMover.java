package org.aossie.starcross.touch;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import org.aossie.starcross.control.AstronomerModel;
import org.aossie.starcross.control.ControllerGroup;
import org.aossie.starcross.util.Geometry;

public class MapMover implements MapGestureDetector.DragRotateZoomGestureDetectorListener {
    private AstronomerModel model;
    private ControllerGroup controllerGroup;
    private float sizeTimesRadiansToDegrees;

    public MapMover(AstronomerModel model, ControllerGroup controllerGroup, Context context) {
        this.model = model;
        this.controllerGroup = controllerGroup;
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int screenLongSize = display.getHeight();
        sizeTimesRadiansToDegrees = screenLongSize * Geometry.RADIANS_TO_DEGREES;
    }

    @Override
    public void onDrag(float xPixels, float yPixels) {
        final float pixelsToRadians = model.getFieldOfView() / sizeTimesRadiansToDegrees;
        controllerGroup.changeUpDown(-yPixels * pixelsToRadians);
        controllerGroup.changeRightLeft(-xPixels * pixelsToRadians);
    }

    @Override
    public void onRotate(float degrees) {
        controllerGroup.rotate(-degrees);
    }

    @Override
    public void onStretch(float ratio) {
        controllerGroup.zoomBy(1.0f / ratio);
    }
}