package com.example.demo17.modelo;

import com.example.demo17.eventos.ILevelHandler;
import com.example.demo17.eventos.ITimerHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Model class for the Fast Writing game.
 * Manages game state, word generation, level progression, and timer.
 * Now supports event listeners for level and timer events.
 *
 * @author Your Name
 */
public class GameModel {

    /** List of words and phrases used in the game */
    private final List<String> wordBank;

    /** Random generator for selecting words */
    private final Random random;

    /** Current game level */
    private int currentLevel;

    /** Current word/phrase to be typed */
    private String currentWord;

    /** Time limit for the current level in seconds */
    private int timeLimit;

    /** Remaining time in seconds */
    private int remainingTime;

    /** Indicates if the game is currently running */
    private boolean gameRunning;

    /** Indicates if the current level is completed successfully */
    private boolean levelCompleted;

    /** Maximum levels completed successfully in the current game session */
    private int currentGameLevelsCompleted;

    /** Historical maximum levels completed across all game sessions */
    private int maxLevelsCompleted;

    /** Initial time limit for level 1 */
    private static final int INITIAL_TIME_LIMIT = 20;

    /** Minimum time limit allowed */
    private static final int MINIMUM_TIME_LIMIT = 2;

    /** Time reduction every 5 levels */
    private static final int TIME_REDUCTION = 2;

    /** Levels interval for time reduction */
    private static final int LEVEL_INTERVAL = 5;

    // ========================================
    // NEW: Event Listeners
    // ========================================

    /** Registered listeners for level events */
    private final List<ILevelHandler> levelHandlers = new ArrayList<>();

    /** Registered listeners for timer events */
    private final List<ITimerHandler> timerHandlers = new ArrayList<>();


    /**
     * Constructor initializes the game model.
     */
    public GameModel() {
        this.random = new Random();
        this.wordBank = initializeWordBank();
        this.maxLevelsCompleted = 0;
    }


    // ========================================
    // NEW: Listener Registration Methods
    // ========================================

    /**
     * Registers a level event handler.
     *
     * @param handler the handler to register
     */
    public void addLevelHandler(ILevelHandler handler) {
        if (handler != null && !levelHandlers.contains(handler)) {
            levelHandlers.add(handler);
        }
    }

    /**
     * Unregisters a level event handler.
     *
     * @param handler the handler to remove
     */
    public void removeLevelHandler(ILevelHandler handler) {
        levelHandlers.remove(handler);
    }

    /**
     * Registers a timer event handler.
     *
     * @param handler the handler to register
     */
    public void addTimerHandler(ITimerHandler handler) {
        if (handler != null && !timerHandlers.contains(handler)) {
            timerHandlers.add(handler);
        }
    }

    /**
     * Unregisters a timer event handler.
     *
     * @param handler the handler to remove
     */
    public void removeTimerHandler(ITimerHandler handler) {
        timerHandlers.remove(handler);
    }

    /**
     * Clears all registered handlers.
     */
    public void clearAllHandlers() {
        levelHandlers.clear();
        timerHandlers.clear();
    }


    // ========================================
    // NEW: Event Notification Methods
    // ========================================

    /**
     * Notifies all registered handlers that a level was completed.
     *
     * @param level the completed level
     * @param success true if passed
     */
    private void notifyLevelCompleted(int level, boolean success) {
        for (ILevelHandler handler : levelHandlers) {
            handler.onLevelCompleted(level, success);
        }
    }

    /**
     * Notifies all registered handlers that the level changed.
     *
     * @param newLevel the new level number
     */
    private void notifyLevelChanged(int newLevel) {
        for (ILevelHandler handler : levelHandlers) {
            handler.onLevelChanged(newLevel);
        }
    }

    /**
     * Notifies all registered handlers that the game ended.
     *
     * @param finalLevel the final level reached
     */
    private void notifyGameEnded(int finalLevel) {
        for (ILevelHandler handler : levelHandlers) {
            handler.onGameEnded(finalLevel);
        }
    }

    /**
     * Notifies all registered handlers of a timer tick.
     *
     * @param remainingTime seconds remaining
     */
    private void notifyTimerTick(int remainingTime) {
        for (ITimerHandler handler : timerHandlers) {
            handler.onTimerTick(remainingTime);
        }
    }

    /**
     * Notifies all registered handlers that the timer expired.
     */
    private void notifyTimerExpired() {
        for (ITimerHandler handler : timerHandlers) {
            handler.onTimerExpired();
        }
    }

    /**
     * Notifies all registered handlers that the timer started.
     *
     * @param duration initial duration in seconds
     */
    private void notifyTimerStarted(int duration) {
        for (ITimerHandler handler : timerHandlers) {
            handler.onTimerStarted(duration);
        }
    }


    // ========================================
    // Existing Methods (Some Modified)
    // ========================================

    private List<String> initializeWordBank() {
        return new ArrayList<>(Arrays.asList(
                // Simple words (levels 1-5)
                "casa", "perro", "gato", "mesa", "libro",
                "agua", "fuego", "tierra", "cielo", "mar",

                // Medium words (levels 6-10)
                "computadora", "telefono", "universidad", "programacion", "desarrollo",
                "tecnologia", "innovacion", "creatividad", "conocimiento", "aprendizaje",

                // Complex words (levels 11-15)
                "extraordinario", "implementacion", "funcionalidad", "caracteristicas", "especificaciones",
                "documentacion", "metodologia", "arquitectura", "optimizacion", "rendimiento",

                // Phrases (levels 16+)
                "la programacion es divertida", "java es un lenguaje potente",
                "el desarrollo web moderno", "interfaces graficas intuitivas",
                "experiencia de usuario excelente", "codigo limpio y mantenible",
                "patrones de diseño efectivos", "algoritmos y estructuras de datos",
                "desarrollo orientado a eventos", "aplicaciones escalables y robustas"
        ));
    }

    public void resetGame() {
        this.currentLevel = 1;
        this.timeLimit = INITIAL_TIME_LIMIT;
        this.remainingTime = timeLimit;
        this.gameRunning = false;
        this.levelCompleted = false;
        this.currentGameLevelsCompleted = 0;
        generateNewWord();
    }

    public void startGame() {
        this.gameRunning = true;
        this.remainingTime = timeLimit;

        // NEW: Notify timer started
        notifyTimerStarted(timeLimit);
    }

    public void generateNewWord() {
        if (wordBank.isEmpty()) {
            this.currentWord = "default";
            return;
        }

        int wordIndex;
        if (currentLevel <= 5) {
            wordIndex = random.nextInt(Math.min(10, wordBank.size()));
        } else if (currentLevel <= 10) {
            wordIndex = 10 + random.nextInt(Math.min(10, Math.max(1, wordBank.size() - 10)));
        } else if (currentLevel <= 15) {
            wordIndex = 20 + random.nextInt(Math.min(10, Math.max(1, wordBank.size() - 20)));
        } else {
            wordIndex = 30 + random.nextInt(Math.max(1, wordBank.size() - 30));
        }

        this.currentWord = wordBank.get(Math.min(wordIndex, wordBank.size() - 1));
    }

    public boolean validateInput(String userInput) {
        if (userInput == null) {
            return false;
        }
        return userInput.trim().equals(currentWord);
    }

    /**
     * MODIFIED: Now notifies listeners of level completion.
     */
    public void completeLevel(boolean success) {
        this.levelCompleted = success;

        // NEW: Notify level completed
        notifyLevelCompleted(currentLevel, success);

        if (success) {
            this.currentGameLevelsCompleted = currentLevel;
            this.maxLevelsCompleted = Math.max(maxLevelsCompleted, currentLevel);
            advanceToNextLevel();
        } else {
            this.gameRunning = false;

            // NEW: Notify game ended
            notifyGameEnded(currentLevel);
        }
    }

    /**
     * MODIFIED: Now notifies listeners of level change.
     */
    private void advanceToNextLevel() {
        this.currentLevel++;

        if (currentLevel % LEVEL_INTERVAL == 1 && currentLevel > 1) {
            this.timeLimit = Math.max(MINIMUM_TIME_LIMIT, timeLimit - TIME_REDUCTION);
        }

        this.remainingTime = timeLimit;
        generateNewWord();

        // NEW: Notify level changed
        notifyLevelChanged(currentLevel);

        // NEW: Notify timer started for new level
        notifyTimerStarted(timeLimit);
    }

    /**
     * MODIFIED: Now notifies listeners of timer ticks and expiration.
     */
    public boolean decrementTime() {
        if (remainingTime > 0) {
            remainingTime--;

            // NEW: Notify timer tick
            notifyTimerTick(remainingTime);

            if (remainingTime == 0) {
                // NEW: Notify timer expired
                notifyTimerExpired();
            }

            return remainingTime > 0;
        }
        return false;
    }

    public double getTimeProgress() {
        if (timeLimit == 0) return 0.0;
        return (double) remainingTime / timeLimit;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public int getMaxLevelsCompleted() {
        return maxLevelsCompleted;
    }

    public int getCurrentGameLevelsCompleted() {
        return currentGameLevelsCompleted;
    }

    public void stopGame() {
        this.gameRunning = false;
    }
}