package org.aossie.starcross.layer;

import android.content.res.Resources;
import android.util.Log;

import org.aossie.starcross.renderer.RendererController;
import org.aossie.starcross.renderer.RendererControllerBase;
import org.aossie.starcross.renderer.RendererObjectManager;
import org.aossie.starcross.renderer.util.UpdateClosure;
import org.aossie.starcross.source.data.ImageSource;
import org.aossie.starcross.source.data.LineSource;
import org.aossie.starcross.source.data.PointSource;
import org.aossie.starcross.util.MiscUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("unchecked")
public abstract class AbstractLayer implements Layer {
    private static final String TAG = MiscUtil.getTag(AbstractLayer.class);

    private final ReentrantLock renderMapLock = new ReentrantLock();
    private final HashMap<Class<?>, RendererControllerBase.RenderManager<?>> renderMap = new HashMap<>();
    private final Resources resources;

    private RendererController renderer;

    AbstractLayer(Resources resources) {
        this.resources = resources;
    }

    protected Resources getResources() {
        return resources;
    }

    @Override
    public void registerWithRenderer(RendererController rendererController) {
        this.renderMap.clear();
        this.renderer = rendererController;
        updateLayerForControllerChange();
    }

    protected abstract void updateLayerForControllerChange();

    @Override
    public void setVisible(boolean visible) {
        renderMapLock.lock();
        try {
            if (renderer == null) {
                Log.w(TAG, "Renderer not set - aborting " + this.getClass().getSimpleName());
                return;
            }

            RendererController.AtomicSection atomic = renderer.createAtomic();
            for (Entry<Class<?>, RendererControllerBase.RenderManager<?>> entry : renderMap.entrySet()) {
                entry.getValue().queueEnabled(visible, atomic);
            }
            renderer.queueAtomic(atomic);
        } finally {
            renderMapLock.unlock();
        }
    }

    void addUpdateClosure(UpdateClosure closure) {
        if (renderer != null) {
            renderer.addUpdateClosure(closure);
        }
    }

    void redraw(final ArrayList<PointSource> pointSources,
                final ArrayList<LineSource> lineSources,
                final ArrayList<ImageSource> imageSources,
                EnumSet<RendererObjectManager.UpdateType> updateTypes) {
        if (renderer == null) {
            Log.w(TAG, "Renderer not set - aborting: " + this.getClass().getSimpleName());
            return;
        }

        renderMapLock.lock();
        try {
            RendererController.AtomicSection atomic = renderer.createAtomic();
            setSources(pointSources, updateTypes, PointSource.class, atomic);
            setSources(lineSources, updateTypes, LineSource.class, atomic);
            setSources(imageSources, updateTypes, ImageSource.class, atomic);
            renderer.queueAtomic(atomic);
        } finally {
            renderMapLock.unlock();
        }
    }

    private <E> void setSources(ArrayList<E> sources,
                                EnumSet<RendererObjectManager.UpdateType> updateType,
                                Class<E> clazz,
                                RendererController.AtomicSection atomic) {
        RendererControllerBase.RenderManager<E> manager = (RendererControllerBase.RenderManager<E>) renderMap.get(clazz);
        if (sources == null || sources.isEmpty()) {
            if (manager != null) {
                manager.queueObjects(Collections.<E>emptyList(), updateType, atomic);
            }
            return;
        }

        if (manager == null) {
            manager = createRenderManager(clazz, atomic);
            if (manager == null) {
                renderMap.put(clazz, manager);
            }
        }
        manager.queueObjects(sources, updateType, atomic);
    }

    private <E> RendererControllerBase.RenderManager<E> createRenderManager(Class<E> clazz, RendererControllerBase controller) {
        if (clazz.equals(PointSource.class)) {
            return (RendererControllerBase.RenderManager<E>) controller.createPointManager(getLayerDepthOrder());
        } else if (clazz.equals(LineSource.class)) {
            return (RendererControllerBase.RenderManager<E>) controller.createLineManager(getLayerDepthOrder());

        } else if (clazz.equals(ImageSource.class)) {
            return (RendererControllerBase.RenderManager<E>) controller.createImageManager(getLayerDepthOrder());
        }
        throw new IllegalStateException("Unknown source type: " + clazz);
    }
}