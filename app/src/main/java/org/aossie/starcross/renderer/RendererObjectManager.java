package org.aossie.starcross.renderer;

import org.aossie.starcross.renderer.util.TextureManager;

import javax.microedition.khronos.opengles.GL10;

public abstract class RendererObjectManager implements Comparable<RendererObjectManager> {
    public enum UpdateType {
        Reset,
        UpdatePositions,
          // Only update positions of existing objects.
        UpdateImages
    }

    private boolean enabled = true;
    private RenderStateInterface renderState = null;
    private int layer;
    private int index;
    private final TextureManager textureManager;
    private static int sIndex = 0;

    public RendererObjectManager(int layer, TextureManager textureManager) {
        this.layer = layer;
        this.textureManager = textureManager;
        synchronized (RendererObjectManager.class) {
            index = sIndex++;
        }
    }

    void enable(boolean enable) {
        enabled = enable;
    }

    public int compareTo(RendererObjectManager rom) {
        if (getClass() != rom.getClass()) {
            return getClass().getName().compareTo(rom.getClass().getName());
        }
        return Integer.compare(index, rom.index);
    }

    final int getLayer() {
        return layer;
    }

    final void draw(GL10 gl) {
        float mMaxRadiusOfView = 360;
        if (enabled && renderState.getRadiusOfView() <= mMaxRadiusOfView) {
            drawInternal(gl);
        }
    }

    final void setRenderState(RenderStateInterface state) {
        renderState = state;
    }

    final RenderStateInterface getRenderState() {
        return renderState;
    }

    TextureManager textureManager() {
        return textureManager;
    }

    public abstract void reload(GL10 gl, boolean fullReload);

    protected abstract void drawInternal(GL10 gl);
}