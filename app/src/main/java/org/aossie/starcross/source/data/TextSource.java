package org.aossie.starcross.source.data;

public interface LabelSource extends PositionSource {
    String getText();

    void setText(String newText);

    int getFontSize();

    void setFontSizeText(int newFontSize);
}