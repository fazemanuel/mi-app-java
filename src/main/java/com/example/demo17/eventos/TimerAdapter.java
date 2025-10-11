package com.example.demo17.eventos;

/**
 * Adapter class for ITimerHandler interface.
 * Provides empty default implementations for all methods.
 * Subclasses can override only the methods they need.
 *
 * @author Your Name
 */
public abstract class TimerAdapter implements ITimerHandler {

    /**
     * Empty implementation. Override to handle timer ticks.
     *
     * @param remainingTime seconds remaining
     */
    @Override
    public void onTimerTick(int remainingTime) {
        // Empty default implementation
    }

    /**
     * Empty implementation. Override to handle timer expiration.
     */
    @Override
    public void onTimerExpired() {
        // Empty default implementation
    }

    /**
     * Empty implementation. Override to handle timer start.
     *
     * @param duration initial duration in seconds
     */
    @Override
    public void onTimerStarted(int duration) {
        // Empty default implementation
    }
}