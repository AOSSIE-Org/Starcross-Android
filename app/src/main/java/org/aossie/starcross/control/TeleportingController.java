package org.aossie.starcross.control;

import android.util.Log;

import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.MiscUtil;
import org.aossie.starcross.util.Vector3;
import org.aossie.starcross.util.VectorUtil;

public class TeleportingController extends AbstractController {
    private static final String TAG = MiscUtil.getTag(TeleportingController.class);

    public void teleport(final GeocentricCoordinates targetXyz) {
        Log.d(TAG, "Teleporting to target " + targetXyz);
        AstronomerModel.Pointing pointing = model.getPointing();
        final GeocentricCoordinates hereXyz = pointing.getLineOfSight();
        if (targetXyz.equals(hereXyz)) {
            return;
        }

        Vector3 hereTopXyz = pointing.getPerpendicular();
        hereTopXyz.normalize();
        final Vector3 normal = VectorUtil.crossProduct(hereXyz, hereTopXyz);
        Vector3 newUpXyz = VectorUtil.crossProduct(normal, targetXyz);

        model.setPointing(targetXyz, newUpXyz);
    }

    @Override
    public void start() {
        // Nothing to do.
    }

    @Override
    public void stop() {
        // Nothing to do.
        // We could consider aborting the teleport, but it's OK for now.
    }
}
