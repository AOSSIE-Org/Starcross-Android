package org.aossie.starcross.control;

public interface Controller {
    void setEnabled(boolean enabled);

    void setModel(AstronomerModel model);

    void start();

    void stop();
}