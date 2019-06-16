package org.aossie.starcross.layer;

import org.aossie.starcross.renderer.RendererController;

public interface Layer {
    void initialize();

    void registerWithRenderer(RendererController controller);

    int getLayerDepthOrder();

    void setVisible(boolean visible);
}