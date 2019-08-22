package org.aossie.starcross.control;

import android.hardware.SensorManager;

import org.aossie.starcross.util.Constant;
import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.Geometry;
import org.aossie.starcross.util.LatLong;
import org.aossie.starcross.util.Matrix3x3;
import org.aossie.starcross.util.RaDec;
import org.aossie.starcross.util.Vector3;

import java.util.Date;

import static org.aossie.starcross.util.Geometry.addVectors;
import static org.aossie.starcross.util.Geometry.calculateRADecOfZenith;
import static org.aossie.starcross.util.Geometry.matrixMultiply;
import static org.aossie.starcross.util.Geometry.matrixVectorMultiply;
import static org.aossie.starcross.util.Geometry.scalarProduct;
import static org.aossie.starcross.util.Geometry.scaleVector;
import static org.aossie.starcross.util.Geometry.vectorProduct;

public class AstronomerModelImpl implements AstronomerModel {
    private float fieldOfView = 80;
    private Clock clock = new RealClock();
    private static final float TOL = 0.01f;
    private boolean useRotationVector = false;
    private boolean autoUpdatePointing = true;
    private Pointing pointing = new Pointing();
    private long celestialCoordsLastUpdated = -1;
    private float[] rotationVector = new float[]{1, 0, 0, 0};
    private Vector3 upCelestial = new Vector3(0, 1, 0);
    private Vector3 screenInPhoneCoords = SCREEN_UP_IN_PHONE_COORDS;
    private LatLong location = new LatLong(0f, 0f);
    private Matrix3x3 axesPhoneInverseMatrix = Matrix3x3.getIdMatrix();
    private Matrix3x3 axesMagneticCelestialMatrix = Matrix3x3.getIdMatrix();
    private Vector3 acceleration = Constant.INITIAL_DOWN.copy();
    private Vector3 magneticField = Constant.INITIAL_SOUTH.copy();
    private static final Vector3 AXIS_OF_EARTHS_ROTATION = new Vector3(0, 0, 1);
    private static final long MINIMUM_TIME_BETWEEN_CELESTIAL_COORD_UPDATES_MILLIS = 60000L;
    private static final Vector3 SCREEN_UP_IN_PHONE_COORDS = new Vector3(0, 1, 0);
    private static final Vector3 POINTING_DIR_IN_PHONE_COORDS = new Vector3(0, 0, -1);

    public AstronomerModelImpl() {
        setMagneticDeclinationCalculator();
    }

    @Override
    public void setAutoUpdatePointing(boolean autoUpdatePointing) {
        this.autoUpdatePointing = autoUpdatePointing;
    }

    @Override
    public float getFieldOfView() {
        return fieldOfView;
    }

    @Override
    public void setFieldOfView(float degrees) {
        fieldOfView = degrees;
    }

    @Override
    public Date getTime() {
        return new Date(clock.getTimeInMillisSinceEpoch());
    }

    @Override
    public void setPhoneSensorValues(Vector3 acceleration, Vector3 magneticField) {
        if (magneticField.length2() < TOL || acceleration.length2() < TOL) {
            return;
        }
        this.acceleration.assign(acceleration);
        this.magneticField.assign(magneticField);
        useRotationVector = false;
    }

    @Override
    public void setPhoneSensorValues(float[] rotationVector) {
        System.arraycopy(rotationVector, 0, this.rotationVector, 0, Math.min(rotationVector.length, 4));
        useRotationVector = true;
    }

    @Override
    public GeocentricCoordinates getZenith() {
        calculateLocalNorthAndUpInCelestialCoords(false);
        return GeocentricCoordinates.getInstanceFromVector3(upCelestial);
    }

    @Override
    public void setMagneticDeclinationCalculator() {
        calculateLocalNorthAndUpInCelestialCoords(true);
    }

    private void calculatePointing() {
        if (!autoUpdatePointing) {
            return;
        }

        calculateLocalNorthAndUpInCelestialCoords(false);
        calculateLocalNorthAndUpInPhoneCoordsFromSensors();

        Matrix3x3 transform = matrixMultiply(axesMagneticCelestialMatrix, axesPhoneInverseMatrix);

        Vector3 viewInSpaceSpace = matrixVectorMultiply(transform, POINTING_DIR_IN_PHONE_COORDS);
        Vector3 screenUpInSpaceSpace = matrixVectorMultiply(transform, screenInPhoneCoords);

        pointing.updateLineOfSight(viewInSpaceSpace);
        pointing.updatePerpendicular(screenUpInSpaceSpace);
    }

    private void calculateLocalNorthAndUpInCelestialCoords(boolean forceUpdate) {
        long currentTime = clock.getTimeInMillisSinceEpoch();
        if (!forceUpdate &&
                Math.abs(currentTime - celestialCoordsLastUpdated) <
                        MINIMUM_TIME_BETWEEN_CELESTIAL_COORD_UPDATES_MILLIS) {
            return;
        }
        celestialCoordsLastUpdated = currentTime;
        RaDec up = calculateRADecOfZenith(getTime(), location);
        upCelestial = GeocentricCoordinates.getInstance(up);
        Vector3 z = AXIS_OF_EARTHS_ROTATION;
        float zDotu = scalarProduct(upCelestial, z);
        /** North along the ground in celestial coordinates. */
        Vector3 trueNorthCelestial = addVectors(z, scaleVector(upCelestial, -zDotu));
        trueNorthCelestial.normalize();

        Matrix3x3 rotationMatrix = Geometry.calculateRotationMatrix(0, upCelestial);

        Vector3 magneticNorthCelestial = matrixVectorMultiply(rotationMatrix,
                trueNorthCelestial);
        Vector3 magneticEastCelestial = vectorProduct(magneticNorthCelestial, upCelestial);

        axesMagneticCelestialMatrix = new Matrix3x3(magneticNorthCelestial,
                upCelestial,
                magneticEastCelestial);
    }

    private void calculateLocalNorthAndUpInPhoneCoordsFromSensors() {
        Vector3 magneticNorthPhone;
        Vector3 magneticEastPhone;
        Vector3 upPhone;
        if (useRotationVector) {
            float[] rotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector);
            magneticNorthPhone = new Vector3(rotationMatrix[3], rotationMatrix[4], rotationMatrix[5]);
            upPhone = new Vector3(rotationMatrix[6], rotationMatrix[7], rotationMatrix[8]);
            magneticEastPhone = new Vector3(rotationMatrix[0], rotationMatrix[1], rotationMatrix[2]);
        } else {
            Vector3 down = acceleration.copy();
            down.normalize();
            Vector3 magneticFieldToNorth = magneticField.copy();
            magneticFieldToNorth.scale(-1);
            magneticFieldToNorth.normalize();
            magneticNorthPhone = addVectors(magneticFieldToNorth,
                    scaleVector(down, -scalarProduct(magneticFieldToNorth, down)));
            magneticNorthPhone.normalize();
            upPhone = scaleVector(down, -1);
            magneticEastPhone = vectorProduct(magneticNorthPhone, upPhone);
        }
        axesPhoneInverseMatrix = new Matrix3x3(magneticNorthPhone, upPhone, magneticEastPhone, false);
    }

    @Override
    public Pointing getPointing() {
        calculatePointing();
        return pointing;
    }

    @Override
    public void setPointing(Vector3 lineOfSight, Vector3 perpendicular) {
        this.pointing.updateLineOfSight(lineOfSight);
        this.pointing.updatePerpendicular(perpendicular);
    }

    @Override
    public void setClock(Clock clock) {
        this.clock = clock;
        calculateLocalNorthAndUpInCelestialCoords(true);
    }
}