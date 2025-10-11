package com.example.demo17.eventos;

/**
 * Adapter class for IValidationHandler interface.
 * Provides empty default implementations for all methods.
 * Subclasses can override only the methods they need.
 *
 * @author Your Name
 */
public abstract class ValidationAdapter implements IValidationHandler {

    /**
     * Empty implementation. Override to handle validation requests.
     */
    @Override
    public void onValidationRequested() {
        // Empty default implementation
    }

    /**
     * Empty implementation. Override to handle validation success.
     *
     * @param message success message
     */
    @Override
    public void onValidationSuccess(String message) {
        // Empty default implementation
    }

    /**
     * Empty implementation. Override to handle validation failure.
     *
     * @param message error message
     */
    @Override
    public void onValidationFailure(String message) {
        // Empty default implementation
    }
}