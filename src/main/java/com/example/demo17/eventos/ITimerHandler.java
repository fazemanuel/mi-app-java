package com.example.demo17.eventos;

/**
 * Interface for handling timer events in the game.
 * Provides callbacks for timer lifecycle events.
 *
 * @author Your Name
 */
public interface ITimerHandler {

    /**
     * Called on every timer tick (every second).
     *
     * @param remainingTime seconds remaining in current level
     */
    void onTimerTick(int remainingTime);

    /**
     * Called when the timer reaches zero.
     * Indicates that time has run out for the current level.
     */
    void onTimerExpired();

    /**
     * Called when the timer starts or restarts.
     *
     * @param duration initial duration in seconds
     */
    void onTimerStarted(int duration);
}