package org.aossie.starcross.renderer.util;


import org.aossie.starcross.util.MathUtil;
import org.aossie.starcross.util.Matrix4x4;
import org.aossie.starcross.util.Vector3;

public class SearchHelper {
  public void resize(int width, int height) {
    mHalfScreenWidth = width * 0.5f;
    mHalfScreenHeight = height * 0.5f;
  }
  
  public void setTarget(Vector3 target, String targetName) {
    mTarget = target.copy();
    mTransformedPosition = null;
    mLastUpdateTime = System.currentTimeMillis();
    mTransitionFactor = targetInFocusRadiusImpl() ? 1 : 0;
  }
  
  public void setTransform(Matrix4x4 transformMatrix) {
    mTransformMatrix = transformMatrix;
    mTransformedPosition = null;
  }
  
  public Vector3 getTransformedPosition() {
    if (mTransformedPosition == null && mTransformMatrix != null) {
      // Transform the label position by our transform matrix
      mTransformedPosition = Matrix4x4.transformVector(mTransformMatrix, mTarget);
    }
    return mTransformedPosition;
  }

  
  public void setTargetFocusRadius(float radius) {
    mTargetFocusRadius = radius;
  }
  
  // Returns a number between 0 and 1, 0 meaning that we should draw the UI as if the target
  // is not in focus, 1 meaning it should be fully in focus, and between the two meaning
  // it just transitioned between the two, so we should be drawing the transition.
  public float getTransitionFactor() {
    return mTransitionFactor;
  }
  
  // Checks whether the search target is in the focus or not, and updates the seconds in the state
  // accordingly.
  public void checkState() {
    boolean inFocus = targetInFocusRadiusImpl();
    long time = System.currentTimeMillis();
    float delta = 0.001f * (time - mLastUpdateTime);
    mTransitionFactor += delta * (inFocus ? 1 : -1);
    mTransitionFactor = Math.min(1, Math.max(0, mTransitionFactor));
    mLastUpdateTime = time;
  }

  
  // Returns the distance from the center of the screen, in pixels, if the target is in front of
  // the viewer.  Returns infinity if the point is behind the viewer.
  private float getDistanceFromCenterOfScreen() {
    Vector3 position = getTransformedPosition();
    if (position.z > 0) {
      float dx = position.x * mHalfScreenWidth;
      float dy = position.y * mHalfScreenHeight;
      return MathUtil.sqrt(dx*dx + dy*dy);
    } else {
      return Float.POSITIVE_INFINITY;
    }
  }
  
  private boolean targetInFocusRadiusImpl() {
    float distFromCenter = getDistanceFromCenterOfScreen();
    return 0.5f * mTargetFocusRadius > distFromCenter;
  }
  
  private Vector3 mTarget = new Vector3(0, 0, 0);
  private Vector3 mTransformedPosition = new Vector3(0, 0, 0);
  private float mHalfScreenWidth = 1;
  private float mHalfScreenHeight = 1;
  private Matrix4x4 mTransformMatrix = null;
  private float mTargetFocusRadius = 0;
  private float mTransitionFactor = 0;
  private long mLastUpdateTime = 0;
}
