package com.example.demo17.eventos;

import javafx.scene.input.KeyEvent;

/**
 * Adapter class for IInputHandler interface.
 * Provides empty default implementations for all methods.
 * Subclasses can override only the methods they need.
 *
 * @author Your Name
 */
public abstract class InputAdapter implements IInputHandler {

    /**
     * Empty implementation. Override to handle key press events.
     *
     * @param event the keyboard event
     */
    @Override
    public void onKeyPressed(KeyEvent event) {
        // Empty default implementation
    }

    /**
     * Empty implementation. Override to handle Enter key press.
     */
    @Override
    public void onEnterPressed() {
        // Empty default implementation
    }

    /**
     * Empty implementation. Override to handle input changes.
     *
     * @param input current input text
     */
    @Override
    public void onInputChanged(String input) {
        // Empty default implementation
    }
}