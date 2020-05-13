package com.mygdx.game;

import com.badlogic.gdx.Input;

public class MyTextInputListener implements Input.TextInputListener {

    private String text = "";

    @Override
    public void input (String text) {
        this.text = text;
    }

    @Override
    public void canceled () {
    }

    public String getText() {
        return text;
    }
}
