package org.aossie.starcross.renderer;

import org.aossie.starcross.control.AstronomerModel;
import org.aossie.starcross.renderer.util.AbstractUpdateClosure;

public class RendererModelUpdateClosure extends AbstractUpdateClosure {

    private RendererController rendererController;
    private AstronomerModel model;

    public RendererModelUpdateClosure(AstronomerModel model, RendererController rendererController) {
        this.model = model;
        this.rendererController = rendererController;
    }

    @Override
    public void run() {
        AstronomerModel.Pointing pointing = model.getPointing();
        float directionX = pointing.getLineOfSightX();
        float directionY = pointing.getLineOfSightY();
        float directionZ = pointing.getLineOfSightZ();

        float upX = pointing.getPerpendicularX();
        float upY = pointing.getPerpendicularY();
        float upZ = pointing.getPerpendicularZ();

        rendererController.queueSetViewOrientation(directionX, directionY, directionZ, upX, upY, upZ);

        float fieldOfView = model.getFieldOfView();
        rendererController.queueFieldOfView(fieldOfView);
    }
}