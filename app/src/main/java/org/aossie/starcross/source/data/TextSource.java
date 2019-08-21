package org.aossie.starcross.source.data;

public interface TextSource extends Colorable, PositionSource {
    String getText();

    void setText(String newText);

    int getFontSize();

    public float getOffset();
}