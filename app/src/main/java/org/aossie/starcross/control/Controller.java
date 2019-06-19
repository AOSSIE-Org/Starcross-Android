package org.aossie.starcross.control;

public interface Controller {
    void setModel(AstronomerModel model);

    void start();

    void stop();
}