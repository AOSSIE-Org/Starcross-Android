package org.aossie.starcross.renderer.util;

import javax.microedition.khronos.opengles.GL10;

public interface TextureReference {
    void bind(GL10 gl);
    void delete(GL10 gl);
}