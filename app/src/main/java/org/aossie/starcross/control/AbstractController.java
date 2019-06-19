package org.aossie.starcross.control;

public abstract class AbstractController implements Controller {

    AstronomerModel model;

    @Override
    public void setModel(AstronomerModel model) {
        this.model = model;
    }
}