package org.aossie.starcross.renderer;

class ManagerReloadData {
    RendererObjectManager manager;
    boolean fullReload;

    ManagerReloadData(RendererObjectManager manager, boolean fullReload) {
        this.manager = manager;
        this.fullReload = fullReload;
    }
}