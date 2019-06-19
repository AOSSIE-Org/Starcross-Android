package org.aossie.starcross.renderer.util;

public abstract class AbstractUpdateClosure implements UpdateClosure {
    @Override
    public int compareTo(UpdateClosure that) {
        int thisHashCode = this.hashCode();
        int thatHashCode = that.hashCode();

        if (thisHashCode == thatHashCode) {
            return 0;
        }
        return (thisHashCode < thatHashCode) ? -1 : 1;
    }
}