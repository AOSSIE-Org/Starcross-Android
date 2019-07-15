package org.aossie.starcross.source.data;

import android.graphics.Bitmap;

public interface ImageSource extends PositionSource {

  public Bitmap getImage();
  public float[] getVerticalCorner();
  public float[] getHorizontalCorner();
  public boolean requiresBlending();
}
