package com.example.demo17.eventos;

/**
 * Adapter class for ILevelHandler interface.
 * Provides empty default implementations for all methods.
 * Subclasses can override only the methods they need.
 *
 * @author Your Name
 */
public abstract class LevelAdapter implements ILevelHandler {

    /**
     * Empty implementation. Override to handle level completion.
     *
     * @param level the level number
     * @param success true if passed
     */
    @Override
    public void onLevelCompleted(int level, boolean success) {
        // Empty default implementation
    }

    /**
     * Empty implementation. Override to handle level changes.
     *
     * @param newLevel the new level number
     */
    @Override
    public void onLevelChanged(int newLevel) {
        // Empty default implementation
    }

    /**
     * Empty implementation. Override to handle game end.
     *
     * @param finalLevel final level reached
     */
    @Override
    public void onGameEnded(int finalLevel) {
        // Empty default implementation
    }
}