package org.aossie.starcross.renderer;

import android.opengl.GLSurfaceView;

import java.util.LinkedList;
import java.util.Queue;

public class RendererController extends RendererControllerBase {
    public static class AtomicSection extends RendererControllerBase {
        private Queuer queuer = new Queuer();

        private AtomicSection(SkyRenderer renderer) {
            super(renderer);
        }

        @Override
        protected EventQueuer getQueuer() {
            return queuer;
        }

        private Queue<Runnable> releaseEvents() {
            Queue<Runnable> queue = queuer.queue;
            queuer = new Queuer();
            return queue;
        }

        private static class Queuer implements EventQueuer {
            private Queue<Runnable> queue = new LinkedList<Runnable>();

            public void queueEvent(Runnable r) {
                queue.add(r);
            }
        }
    }

    private final EventQueuer queuer;

    @Override
    protected EventQueuer getQueuer() {
        return queuer;
    }

    public RendererController(SkyRenderer renderer, final GLSurfaceView view) {
        super(renderer);
        queuer = new EventQueuer() {
            public void queueEvent(Runnable r) {
                view.queueEvent(r);
            }
        };
    }

    public AtomicSection createAtomic() {
        return new AtomicSection(mRenderer);
    }

    public void queueAtomic(final AtomicSection atomic) {
        queueRunnable(new Runnable() {
            public void run() {
                Queue<Runnable> events = atomic.releaseEvents();
                for (Runnable r : events) {
                    r.run();
                }
            }
        });
    }
}