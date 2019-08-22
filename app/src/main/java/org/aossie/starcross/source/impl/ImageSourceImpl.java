package org.aossie.starcross.source.impl;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;

import org.aossie.starcross.source.data.ImageSource;
import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.Vector3;
import org.aossie.starcross.util.VectorUtil;

public class ImageSourceImpl extends AbstractSource implements ImageSource {

  static Vector3 up = new Vector3(0.0f, 1.0f, 0.0f);


  // These two vectors, along with Source.xyz, determine the position of the
  // image object.  The corners are as follows
  //
  //  xyz-u+v   xyz+u+v
  //     +---------+     ^
  //     |   xyz   |     | v
  //     |    .    |     .
  //     |         |
  //     +---------+
  //  xyz-u-v    xyz+u-v
  //
  //          .--->
  //            u
  public float ux, uy, uz;
  public float vx, vy, vz;

  public Bitmap image;

  public boolean requiresBlending = false;

  private final float imageScale;
  private final Resources resources;


  public ImageSourceImpl(float ra, float dec, Resources res, int id) {
    this(ra, dec, res, id, up, 1.0f);
  }

  public ImageSourceImpl(float ra, float dec, Resources res, int id, Vector3 upVec) {
    this(ra, dec, res, id, upVec, 1.0f);
  }

  public ImageSourceImpl(float ra, float dec, Resources res, int id, Vector3 upVec,
                         float imageScale) {
    this(GeocentricCoordinates.getInstance(ra, dec), res, id, upVec, imageScale);
  }

  public ImageSourceImpl(GeocentricCoordinates coords, Resources res, int id, Vector3 upVec,
                         float imageScale) {
    super(coords, Color.WHITE);
    this.imageScale = imageScale;

    this.resources = res;
    setUpVector(upVec);
    setImageId(id);
  }

  public void setImageId(int imageId) {
    Options opts = new Options();
    opts.inScaled = false;

    this.image = BitmapFactory.decodeResource(resources, imageId, opts);
    if (image == null) {
      throw new RuntimeException("Coud not decode image " + imageId);
    }
  }

  public Bitmap getImage() {
    return image;
  }

  public float[] getHorizontalCorner() {
    return new float[] {ux, uy, uz};
  }

  public float[] getVerticalCorner() {
    return new float[] {vx, vy, vz};
  }

  public boolean requiresBlending() {
    return requiresBlending;
  }

  protected Resources getResources() {
    return resources;
  }

  public void setUpVector(Vector3 upVec) {
    Vector3 p = this.getLocation();
    Vector3 u = VectorUtil.negate(VectorUtil.normalized(VectorUtil.crossProduct(p, upVec)));
    Vector3 v = VectorUtil.crossProduct(u, p);

    v.scale(imageScale);
    u.scale(imageScale);

    ux = u.x;
    uy = u.y;
    uz = u.z;

    vx = v.x;
    vy = v.y;
    vz = v.z;
  }
}
