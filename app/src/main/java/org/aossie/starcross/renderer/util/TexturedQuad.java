package org.aossie.starcross.renderer.util;

import javax.microedition.khronos.opengles.GL10;


public class TexturedQuad {
    private TexCoordBuffer mTexCoords = null;
    private VertexBuffer mPosition = null;
    private TextureReference mTexture = null;

    public TexturedQuad(TextureReference tex,
                        float px, float py, float pz,
                        float ux, float uy, float uz,
                        float vx, float vy, float vz) {
        mPosition = new VertexBuffer(12);
        mTexCoords = new TexCoordBuffer(12);

        VertexBuffer vertexBuffer = mPosition;
        TexCoordBuffer texCoordBuffer = mTexCoords;

        // Upper left
        vertexBuffer.addPoint(px - ux - vx, py - uy - vy, pz - uz - vz);
        texCoordBuffer.addTexCoords(0, 1);

        // upper left
        vertexBuffer.addPoint(px - ux + vx, py - uy + vy, pz - uz + vz);
        texCoordBuffer.addTexCoords(0, 0);

        // lower right
        vertexBuffer.addPoint(px + ux - vx, py + uy - vy, pz + uz - vz);
        texCoordBuffer.addTexCoords(1, 1);

        // upper right
        vertexBuffer.addPoint(px + ux + vx, py + uy + vy, pz + uz + vz);
        texCoordBuffer.addTexCoords(1, 0);

        mTexture = tex;
    }

    public void draw(GL10 gl) {
        gl.glEnable(GL10.GL_TEXTURE_2D);
        mTexture.bind(gl);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        mPosition.set(gl);
        mTexCoords.set(gl);

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        gl.glDisable(GL10.GL_TEXTURE_2D);
    }
}
