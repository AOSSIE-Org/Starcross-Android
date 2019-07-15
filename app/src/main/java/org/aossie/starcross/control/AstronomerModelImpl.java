package org.aossie.starcross.control;

import org.aossie.starcross.util.Vector3;

import java.util.Date;

public class AstronomerModelImpl implements AstronomerModel {

    private float fieldOfView = 80;
    private Pointing pointing = new Pointing();

    public AstronomerModelImpl() {
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
    public Pointing getPointing() {
        return pointing;
    }

    @Override
    public void setPointing(Vector3 lineOfSight, Vector3 perpendicular) {
        this.pointing.updateLineOfSight(lineOfSight);
        this.pointing.updatePerpendicular(perpendicular);
    }

    @Override
    public Date getTime() {
        return new Date(System.currentTimeMillis());
    }
}