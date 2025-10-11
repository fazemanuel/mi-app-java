package com.example.demo17.eventos;

/**
 * Interface for handling validation events in the game.
 * Defines callbacks for validation requests and their results.
 *
 * @author Your Name
 */
public interface IValidationHandler {

    /**
     * Called when user requests validation of their input.
     * This can be triggered by button click or Enter key.
     */
    void onValidationRequested();

    /**
     * Called when validation succeeds.
     *
     * @param message success message to display to user
     */
    void onValidationSuccess(String message);

    /**
     * Called when validation fails.
     *
     * @param message error message to display to user
     */
    void onValidationFailure(String message);
}