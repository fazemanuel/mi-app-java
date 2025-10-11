package com.example.demo17.eventos;

/**
 * Interface for handling level progression events.
 * Defines callbacks for level state changes in the game.
 *
 * @author Your Name
 */
public interface ILevelHandler {

    /**
     * Called when a level is completed (successfully or not).
     *
     * @param level the level number that was completed
     * @param success true if the level was passed, false otherwise
     */
    void onLevelCompleted(int level, boolean success);

    /**
     * Called when the game advances to the next level.
     *
     * @param newLevel the new level number
     */
    void onLevelChanged(int newLevel);

    /**
     * Called when the game ends (after failing a level).
     *
     * @param finalLevel the final level reached before game over
     */
    void onGameEnded(int finalLevel);
}