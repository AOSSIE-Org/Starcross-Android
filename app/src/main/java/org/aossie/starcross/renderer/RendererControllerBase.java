package org.aossie.starcross.renderer;

import org.aossie.starcross.renderer.util.UpdateClosure;

import org.aossie.starcross.source.data.ImageSource;
import org.aossie.starcross.source.data.LineSource;
import org.aossie.starcross.source.data.PointSource;

import java.util.EnumSet;
import java.util.List;

public abstract class RendererControllerBase {
    public static abstract class RenderManager<E> {
        RendererObjectManager manager;

        private RenderManager(RendererObjectManager mgr) {
            manager = mgr;
        }

        public void queueEnabled(final boolean enable, RendererControllerBase controller) {
            controller.queueRunnable(new Runnable() {
                public void run() {
                    manager.enable(enable);
                }
            });
        }

        public abstract void queueObjects(final List<E> objects,
                                          final EnumSet<RendererObjectManager.UpdateType> updateType,
                                          RendererControllerBase controller);
    }

    public static class PointManager extends RenderManager<PointSource> {
        private PointManager(PointObjectManager manager) {
            super(manager);
        }

        @Override
        public void queueObjects(final List<PointSource> points,
                                 final EnumSet<RendererObjectManager.UpdateType> updateType,
                                 RendererControllerBase controller) {
            controller.queueRunnable(new Runnable() {
                public void run() {
                    ((PointObjectManager) manager).updateObjects(points, updateType);
                }
            });
        }
    }

    public static class LineManager extends RenderManager<LineSource> {
        private LineManager(PolyLineObjectManager manager) {
            super(manager);
        }

        @Override
        public void queueObjects(final List<LineSource> lines,
                                 final EnumSet<RendererObjectManager.UpdateType> updateType,
                                 RendererControllerBase controller) {
            controller.queueRunnable(new Runnable() { public void run() {
                ((PolyLineObjectManager) manager).updateObjects(lines, updateType);
            }});
        }
    }

    public static class ImageManager extends RenderManager<ImageSource> {
        private ImageManager(ImageObjectManager manager) {
            super(manager);
        }

        @Override
        public void queueObjects(final List<ImageSource> images,
                                 final EnumSet<RendererObjectManager.UpdateType> updateType,
                                 RendererControllerBase controller) {
            controller.queueRunnable(new Runnable() { public void run() {
                ((ImageObjectManager) manager).updateObjects(images, updateType);
            }});
        }
    }


    protected interface EventQueuer {
        void queueEvent(Runnable r);
    }

    RendererControllerBase(SkyRenderer renderer) {
        mRenderer = renderer;
    }

    final SkyRenderer mRenderer;

    public PointManager createPointManager(int layer) {
        PointManager manager = new PointManager(mRenderer.createPointManager(layer));
        queueAddManager(manager);
        return manager;
    }

    public LineManager createLineManager(int layer) {
        LineManager manager = new LineManager(mRenderer.createPolyLineManager(layer));
        queueAddManager(manager);
        return manager;
    }


    public ImageManager createImageManager(int layer) {
        ImageManager manager = new ImageManager(mRenderer.createImageManager(layer));
        queueAddManager(manager);
        return manager;
    }

    void queueFieldOfView(final float fov) {
        queueRunnable(new Runnable() {
            public void run() {
                mRenderer.setRadiusOfView(fov);
            }
        });
    }

    void queueSetViewOrientation(final float dirX, final float dirY, final float dirZ,
                                 final float upX, final float upY, final float upZ) {
        queueRunnable(new Runnable() {
            public void run() {
                mRenderer.setViewOrientation(dirX, dirY, dirZ, upX, upY, upZ);
            }
        });
    }

    public void addUpdateClosure(final UpdateClosure runnable) {
        queueRunnable(new Runnable() {
            @Override
            public void run() {
                mRenderer.addUpdateClosure(runnable);
            }
        });
    }

    private <E> void queueAddManager(final RenderManager<E> rom) {
        queueRunnable(new Runnable() {
            public void run() {
                mRenderer.addObjectManager(rom.manager);
            }
        });
    }

    abstract protected EventQueuer getQueuer();

    void queueRunnable(final Runnable r) {
        EventQueuer queuer = getQueuer();
        RendererControllerBase.queueRunnable(queuer, r);
    }

    private static void queueRunnable(EventQueuer queuer, final Runnable r) {
        queuer.queueEvent(r);
    }
}