package org.aossie.starcross.control;

public class RealClock implements Clock {
  @Override
  public long getTimeInMillisSinceEpoch() {
    return System.currentTimeMillis();
  }
}
