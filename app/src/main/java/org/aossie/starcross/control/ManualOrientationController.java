package org.aossie.starcross.control;

import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.Matrix3x3;
import org.aossie.starcross.util.Vector3;
import org.aossie.starcross.util.Geometry;

public class ManualOrientationController extends AbstractController {
    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    void changeRightLeft(float radians) {
        if (!enabled) {
            return;
        }
        AstronomerModel.Pointing pointing = model.getPointing();
        GeocentricCoordinates pointingXyz = pointing.getLineOfSight();
        GeocentricCoordinates topXyz = pointing.getPerpendicular();
        Vector3 horizontalXyz = Geometry.vectorProduct(pointingXyz, topXyz);
        Vector3 deltaXyz = Geometry.scaleVector(horizontalXyz, radians);
        Vector3 newPointingXyz = Geometry.addVectors(pointingXyz, deltaXyz);
        newPointingXyz.normalize();
        model.setPointing(newPointingXyz, topXyz);
    }

    void changeUpDown(float radians) {
        if (!enabled) {
            return;
        }
        AstronomerModel.Pointing pointing = model.getPointing();
        GeocentricCoordinates pointingXyz = pointing.getLineOfSight();
        GeocentricCoordinates topXyz = pointing.getPerpendicular();
        Vector3 deltaXyz = Geometry.scaleVector(topXyz, -radians);
        Vector3 newPointingXyz = Geometry.addVectors(pointingXyz, deltaXyz);
        newPointingXyz.normalize();
        Vector3 deltaUpXyz = Geometry.scaleVector(pointingXyz, radians);
        Vector3 newUpXyz = Geometry.addVectors(topXyz, deltaUpXyz);
        newUpXyz.normalize();
        model.setPointing(newPointingXyz, newUpXyz);
    }

    void rotate(float degrees) {
        if (!enabled) {
            return;
        }
        AstronomerModel.Pointing pointing = model.getPointing();
        GeocentricCoordinates pointingXyz = pointing.getLineOfSight();
        Matrix3x3 rotation = Geometry.calculateRotationMatrix(degrees, pointingXyz);
        GeocentricCoordinates topXyz = pointing.getPerpendicular();
        Vector3 newUpXyz = Geometry.matrixVectorMultiply(rotation, topXyz);
        newUpXyz.normalize();
        model.setPointing(pointingXyz, newUpXyz);
    }
}