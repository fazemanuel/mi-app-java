package com.example.demo17.eventos;

import javafx.scene.input.KeyEvent;

/**
 * Interface for handling keyboard input events.
 * Provides callbacks for key presses and input changes.
 *
 * @author Your Name
 */
public interface IInputHandler {

    /**
     * Called when any key is pressed in the input field.
     *
     * @param event the keyboard event containing key information
     */
    void onKeyPressed(KeyEvent event);

    /**
     * Called specifically when the Enter key is pressed.
     * This typically triggers validation.
     */
    void onEnterPressed();

    /**
     * Called when the text in the input field changes.
     *
     * @param input the current text in the input field
     */
    void onInputChanged(String input);
}