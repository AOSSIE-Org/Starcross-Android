package org.aossie.starcross.control;

import android.util.Log;

import org.aossie.starcross.util.GeocentricCoordinates;

import java.util.ArrayList;
import java.util.Date;

public class ControllerGroup implements Controller {
    private AstronomerModel model;
    private boolean usingAutoMode = true;
    private ZoomController zoomController;
    private TeleportingController teleportingController;
    private ManualOrientationController manualDirectionController;
    private final ArrayList<Controller> controllers = new ArrayList<>();
    private SensorOrientationController sensorOrientationController;
    private TimeTravelClock timeTravelClock = new TimeTravelClock();
    private CompositeClock transitioningClock = new CompositeClock(timeTravelClock, new RealClock());

    public ControllerGroup(SensorOrientationController sensorOrientationController) {
        this.sensorOrientationController = sensorOrientationController;
        addController(sensorOrientationController);
        manualDirectionController = new ManualOrientationController();
        addController(manualDirectionController);
        zoomController = new ZoomController();
        addController(zoomController);
        teleportingController = new TeleportingController();
        addController(teleportingController);
        setAutoMode(true);
    }

    @Override
    public void setEnabled(boolean enabled) {
        for (Controller controller : controllers) {
            controller.setEnabled(enabled);
        }
    }

    @Override
    public void setModel(AstronomerModel model) {
        for (Controller controller : controllers) {
            controller.setModel(model);
        }
        this.model = model;
        model.setAutoUpdatePointing(usingAutoMode);
        model.setClock(transitioningClock);
    }

    public void setAutoMode(boolean enabled) {
        manualDirectionController.setEnabled(!enabled);
        sensorOrientationController.setEnabled(enabled);
        if (model != null) {
            model.setAutoUpdatePointing(enabled);
        }
        usingAutoMode = enabled;
    }

    @Override
    public void start() {
        for (Controller controller : controllers) {
            controller.start();
        }
    }

    @Override
    public void stop() {
        for (Controller controller : controllers) {
            controller.stop();
        }
    }

    public void changeRightLeft(float radians) {
        manualDirectionController.changeRightLeft(radians);
    }

    public void changeUpDown(float radians) {
        manualDirectionController.changeUpDown(radians);
    }

    public void rotate(float degrees) {
        manualDirectionController.rotate(degrees);
    }

    private void addController(Controller controller) {
        controllers.add(controller);
    }

    public void zoomBy(float ratio) {
        zoomController.zoomBy(ratio);
    }

    public void goTimeTravel(Date d) {
        transitioningClock.goTimeTravel(d);
    }

    public boolean isAutoMode() {
        return usingAutoMode;
    }

    public void teleport(GeocentricCoordinates target) {
        teleportingController.teleport(target);
    }

}