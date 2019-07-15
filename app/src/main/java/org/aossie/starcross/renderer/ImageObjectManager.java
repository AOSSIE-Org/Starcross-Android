package org.aossie.starcross.renderer;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

import org.aossie.starcross.renderer.util.TexCoordBuffer;
import org.aossie.starcross.renderer.util.TextureManager;
import org.aossie.starcross.renderer.util.TextureReference;
import org.aossie.starcross.renderer.util.VertexBuffer;
import org.aossie.starcross.source.data.ImageSource;
import org.aossie.starcross.util.GeocentricCoordinates;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.EnumSet;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class ImageObjectManager extends RendererObjectManager {
    private VertexBuffer mVertexBuffer = new VertexBuffer(false);
    private TexCoordBuffer mTexCoordBuffer = new TexCoordBuffer(false);
    private Image[] mImages = new Image[0];
    private TextureReference[] mTextures = new TextureReference[0];
    private TextureReference[] mRedTextures = new TextureReference[0];

    EnumSet<UpdateType> mUpdates = EnumSet.noneOf(UpdateType.class);

    public ImageObjectManager(int layer, TextureManager manager) {
        super(layer, manager);
    }

    public void updateObjects(List<ImageSource> imageSources, EnumSet<UpdateType> type) {
        if (!type.contains(UpdateType.Reset) && imageSources.size() != mImages.length) {
            return;
        }
        mUpdates.addAll(type);

        int numVertices = imageSources.size() * 4;
        VertexBuffer vertexBuffer = mVertexBuffer;
        vertexBuffer.reset(numVertices);

        TexCoordBuffer texCoordBuffer = mTexCoordBuffer;
        texCoordBuffer.reset(numVertices);

        Image[] images;
        boolean reset = type.contains(UpdateType.Reset) || type.contains(UpdateType.UpdateImages);
        if (reset) {
            images = new Image[imageSources.size()];
        } else {
            images = mImages;
        }

        if (reset) {
            for (int i = 0; i < imageSources.size(); i++) {
                ImageSource is = imageSources.get(i);

                images[i] = new Image();
                images[i].name = "no url";
                images[i].useBlending = false;
                images[i].bitmap = is.getImage();
            }
        }

        // Update the positions in the position and tex coord buffers.
        if (reset || type.contains(UpdateType.UpdatePositions)) {
            for (int i = 0; i < imageSources.size(); i++) {
                ImageSource is = imageSources.get(i);
                GeocentricCoordinates xyz = is.getLocation();
                float px = xyz.x;
                float py = xyz.y;
                float pz = xyz.z;

                float[] u = is.getHorizontalCorner();
                float ux = u[0];
                float uy = u[1];
                float uz = u[2];

                float[] v = is.getVerticalCorner();
                float vx = v[0];
                float vy = v[1];
                float vz = v[2];

                vertexBuffer.addPoint(px - ux - vx, py - uy - vy, pz - uz - vz);
                texCoordBuffer.addTexCoords(0, 1);
                vertexBuffer.addPoint(px - ux + vx, py - uy + vy, pz - uz + vz);
                texCoordBuffer.addTexCoords(0, 0);
                vertexBuffer.addPoint(px + ux - vx, py + uy - vy, pz + uz - vz);
                texCoordBuffer.addTexCoords(1, 1);
                vertexBuffer.addPoint(px + ux + vx, py + uy + vy, pz + uz + vz);
                texCoordBuffer.addTexCoords(1, 0);
            }
        }

        if (type.contains(UpdateType.UpdateImages)) {
            for (int i = 0; i < imageSources.size(); i++) {
                ImageSource is = imageSources.get(i);
                images[i].bitmap = is.getImage();
            }
        }

        mImages = images;
    }

    @Override
    public void reload(GL10 gl, boolean fullReload) {
        Image[] images = mImages;
        boolean reloadBuffers = false;
        boolean reloadImages = false;

        if (fullReload) {
            reloadBuffers = true;
            reloadImages = true;
            mTextures = new TextureReference[images.length];
            mRedTextures = new TextureReference[images.length];
        } else {
            boolean reset = mUpdates.contains(UpdateType.Reset);
            reloadBuffers |= reset || mUpdates.contains(UpdateType.UpdatePositions);
            reloadImages |= reset || mUpdates.contains(UpdateType.UpdateImages);
            mUpdates.clear();
        }

        if (reloadBuffers) {
            mVertexBuffer.reload();
            mTexCoordBuffer.reload();
        }
        if (reloadImages) {
            for (int i = 0; i < mTextures.length; i++) {
                if (mTextures[i] != null) {
                    mTextures[i].delete(gl);
                    mRedTextures[i].delete(gl);
                }

                Bitmap bmp = mImages[i].bitmap;
                mTextures[i] = textureManager().createTexture(gl);
                mTextures[i].bind(gl);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);

                IntBuffer redPixels = createRedImage(bmp);
                mRedTextures[i] = textureManager().createTexture(gl);
                mRedTextures[i].bind(gl);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
                gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bmp.getWidth(), bmp.getHeight(),
                        0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, redPixels);
            }
        }
    }

    @Override
    protected void drawInternal(GL10 gl) {
        if (mVertexBuffer.size() == 0) {
            return;
        }

        gl.glEnable(GL10.GL_TEXTURE_2D);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        mVertexBuffer.set(gl);
        mTexCoordBuffer.set(gl);

        TextureReference[] textures = mTextures;
        TextureReference[] redTextures = mRedTextures;
        for (int i = 0; i < textures.length; i++) {
            if (mImages[i].useBlending) {
                gl.glEnable(GL10.GL_BLEND);
                gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            } else {
                gl.glEnable(GL10.GL_ALPHA_TEST);
                gl.glAlphaFunc(GL10.GL_GREATER, 0.5f);
            }

            if (getRenderState().getNightVisionMode()) {
                redTextures[i].bind(gl);
            } else {
                textures[i].bind(gl);
            }
            ((GL11) gl).glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4 * i, 4);

            if (mImages[i].useBlending) {
                gl.glDisable(GL10.GL_BLEND);
            } else {
                gl.glDisable(GL10.GL_ALPHA_TEST);
            }
        }

        gl.glDisable(GL10.GL_TEXTURE_2D);
    }

    private IntBuffer createRedImage(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int numPixels = width * height;
        int[] pixels = new int[numPixels];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        ByteBuffer redPixelsBB = ByteBuffer.allocateDirect(4 * numPixels);
        IntBuffer redPixels = redPixelsBB.order(ByteOrder.nativeOrder()).asIntBuffer();
        for (int j = 0; j < numPixels; j++) {
            int r = pixels[j] & 0xff;
            int g = (pixels[j] >> 8) & 0xff;
            int b = (pixels[j] >> 16) & 0xff;
            int alphaMask = pixels[j] & 0xff000000;

            redPixels.put(alphaMask | ((r + g + b) / 3));
        }

        redPixels.position(0);
        return redPixels;
    }

    private static class Image {
        String name;
        Bitmap bitmap;
        int textureID;
        boolean useBlending;
    }
}
