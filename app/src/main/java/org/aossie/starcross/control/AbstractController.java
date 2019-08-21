package org.aossie.starcross.control;

import android.util.Log;

public abstract class AbstractController implements Controller {

    AstronomerModel model;
    boolean enabled = true;

    @Override
    public void setModel(AstronomerModel model) {
        this.model = model;
    }
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}