package org.aossie.starcross.renderer.util;

import javax.microedition.khronos.opengles.GL10;

public class ColoredQuad {
  public ColoredQuad(float r, float g, float b, float a,
                     float px, float py, float pz,
                     float ux, float uy, float uz,
                     float vx, float vy, float vz) {
    mPosition = new VertexBuffer(12);
    VertexBuffer vertexBuffer = mPosition;
    
    // Upper left
    vertexBuffer.addPoint(px - ux - vx, py - uy - vy, pz - uz - vz);
    
    // upper left
    vertexBuffer.addPoint(px - ux + vx, py - uy + vy, pz - uz + vz);
    
    // lower right
    vertexBuffer.addPoint(px + ux - vx, py + uy - vy, pz + uz - vz);
    
    // upper right
    vertexBuffer.addPoint(px + ux + vx, py + uy + vy, pz + uz + vz);
    
    mR = r;
    mG = g;
    mB = b;
    mA = a;
  }
  
  public void draw(GL10 gl) {
    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

    // Enable blending if alpha != 1.
    if (mA != 1) {
      gl.glEnable(GL10.GL_BLEND);
      gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    }
    
    gl.glDisable(GL10.GL_TEXTURE_2D);
    
    mPosition.set(gl);
    gl.glColor4f(mR, mG, mB, mA);
    
    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
    
    gl.glEnable(GL10.GL_TEXTURE_2D);

    // Disable blending if alpha != 1.
    if (mA != 1) {
      gl.glDisable(GL10.GL_BLEND);
    }    
  }
  
  private VertexBuffer mPosition = null;
  private float mR, mG, mB, mA;
}
