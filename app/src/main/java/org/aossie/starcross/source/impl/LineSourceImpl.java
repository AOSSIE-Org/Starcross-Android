package org.aossie.starcross.source.impl;

import android.graphics.Color;

import org.aossie.starcross.source.data.LineSource;
import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.RaDec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LineSourceImpl extends AbstractSource implements LineSource {

  public final List<GeocentricCoordinates> vertices;
  public final List<RaDec> raDecs;
  public final float lineWidth;

  public LineSourceImpl() {
    this(Color.WHITE, new ArrayList<GeocentricCoordinates>(), 0f);
  }

  public LineSourceImpl(int color) {
    this(color, new ArrayList<GeocentricCoordinates>(), 0f);
  }

  public LineSourceImpl(int color, List<GeocentricCoordinates> vertices, float lineWidth) {
    super();

    this.vertices = vertices;
    this.raDecs = new ArrayList<RaDec>();
    this.lineWidth = lineWidth;
  }

  public float getLineWidth() {
    return lineWidth;
  }
  public List<GeocentricCoordinates> getVertices() {
    List<GeocentricCoordinates> result;
    if (vertices != null) {
      result = vertices;
    } else {
      result = new ArrayList<GeocentricCoordinates>();
    }
    return Collections.unmodifiableList(result);
  }
}
