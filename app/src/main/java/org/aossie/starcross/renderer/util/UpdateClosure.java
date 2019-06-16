package org.aossie.starcross.renderer.util;

public interface UpdateClosure extends Comparable<UpdateClosure> {
    void run();
}