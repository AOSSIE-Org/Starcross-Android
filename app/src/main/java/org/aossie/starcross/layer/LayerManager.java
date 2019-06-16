package org.aossie.starcross.layer;

import org.aossie.starcross.renderer.RendererController;

import java.util.ArrayList;
import java.util.List;

public class LayerManager {

    private final List<Layer> layers = new ArrayList<>();

    public LayerManager() {
    }

    public void addLayer(Layer layer) {
        this.layers.add(layer);
    }

    public void initialize() {
        for (Layer layer : layers) {
            layer.initialize();
        }
    }

    public void registerWithRenderer(RendererController renderer) {
        for (Layer layer : layers) {
            layer.registerWithRenderer(renderer);
            layer.setVisible(true);
        }
    }
}