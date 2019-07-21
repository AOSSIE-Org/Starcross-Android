package org.aossie.starcross.control;

import java.util.ArrayList;
import java.util.Date;

public class ControllerGroup implements Controller {

    private ZoomController zoomController;
    private ManualOrientationController manualDirectionController;
    private final ArrayList<Controller> controllers = new ArrayList<>();
    private CompositeClock transitioningClock = new CompositeClock(new TimeTravelClock());

    public ControllerGroup() {
        zoomController = new ZoomController();
        manualDirectionController = new ManualOrientationController();
        addController(manualDirectionController);
        addController(zoomController);
    }

    @Override
    public void setModel(AstronomerModel model) {
        for (Controller controller : controllers) {
            controller.setModel(model);
        }
        // TODO set clock to model
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

    public void useRealTime() {
        transitioningClock.returnToRealTime();
    }
}