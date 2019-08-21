package org.aossie.starcross.source.impl;

import org.aossie.starcross.source.data.TextSource;
import org.aossie.starcross.util.GeocentricCoordinates;

public class TextSourceImpl extends AbstractSource implements TextSource {
  public String label;
  public final float offset;
  public int fontSize;

  public TextSourceImpl(float ra, float dec, String label, int color) {
    this(GeocentricCoordinates.getInstance(ra, dec), label, color);
  }

  public TextSourceImpl(GeocentricCoordinates coords, String label, int color) {
    this(coords, label, color, 0.02f, 15);
  }

  public TextSourceImpl(GeocentricCoordinates coords, String label, int color, float offset,
                        int fontSize) {

    super(coords, color);
    this.label = label;
//    Preconditions.checkArgument(!"".equals(label.trim()));

    this.offset = offset;
    this.fontSize = fontSize;
  }

  @Override
  public String getText() {
    return label;
  }

  @Override
  public int getFontSize() {
    return fontSize;
  }

  @Override
  public float getOffset() {
    return offset;
  }

  @Override
  public void setText(String newText) {
    label = newText;
  }
}
