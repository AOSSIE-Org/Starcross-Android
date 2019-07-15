package org.aossie.starcross.control;

import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.Vector3;

import java.util.Date;

public interface AstronomerModel {

    class Pointing {
        private final GeocentricCoordinates lineOfSight;
        private final GeocentricCoordinates perpendicular;

        Pointing(GeocentricCoordinates lineOfSight,
                 GeocentricCoordinates perpendicular) {
            this.lineOfSight = lineOfSight.copy();
            this.perpendicular = perpendicular.copy();
        }

        Pointing() {
            this(new GeocentricCoordinates(1, 0, 0), new GeocentricCoordinates(0, 1, 0));
        }

        GeocentricCoordinates getLineOfSight() {
            return lineOfSight.copy();
        }

        GeocentricCoordinates getPerpendicular() {
            return perpendicular.copy();
        }

        public float getLineOfSightX() {
            return lineOfSight.x;
        }

        public float getLineOfSightY() {
            return lineOfSight.y;
        }

        public float getLineOfSightZ() {
            return lineOfSight.z;
        }

        public float getPerpendicularX() {
            return perpendicular.x;
        }

        public float getPerpendicularY() {
            return perpendicular.y;
        }

        public float getPerpendicularZ() {
            return perpendicular.z;
        }

        void updatePerpendicular(Vector3 newPerpendicular) {
            perpendicular.assign(newPerpendicular);

        }

        void updateLineOfSight(Vector3 newLineOfSight) {
            lineOfSight.assign(newLineOfSight);
        }
    }

    float getFieldOfView();

    void setFieldOfView(float degrees);

    Pointing getPointing();

    void setPointing(Vector3 lineOfSight, Vector3 perpendicular);

    Date getTime();
}