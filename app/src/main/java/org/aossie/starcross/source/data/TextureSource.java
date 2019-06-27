package org.aossie.starcross.source.data;

import android.graphics.Bitmap;

public interface TextureSource extends PositionSource {
    Bitmap getImage();

    float[] getVerticalCorner();

    float[] getHorizontalCorner();

    boolean requiresBlending();
}
