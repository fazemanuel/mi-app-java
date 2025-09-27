package com.example.demo17.modelo;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


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

    /** Maximum levels completed in the current game session */
    private int maxLevelsCompleted;

    /** Initial time limit for level 1 */
    private static final int INITIAL_TIME_LIMIT = 20;

    /** Minimum time limit allowed */
    private static final int MINIMUM_TIME_LIMIT = 2;

    /** Time reduction every 5 levels */
    private static final int TIME_REDUCTION = 2;

    /** Levels interval for time reduction */
    private static final int LEVEL_INTERVAL = 5;

    private  boolean booleano;


;;
    public GameModel() {
        this.random = new Random();
        this.wordBank = initializeWordBank();
        resetGame();
    }


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
        this.maxLevelsCompleted = 0;
        generateNewWord();
    }



    public void startGame() {
        this.gameRunning = true;
        this.remainingTime = timeLimit;

    }



    public void generateNewWord() {
        if (wordBank.isEmpty()) {
            this.currentWord = "default";
            return;
        }

        // Select word based on level for progressive difficulty
        int wordIndex;
        if (currentLevel <= 5) {
            // Simple words for first 5 levels
            wordIndex = random.nextInt(Math.min(10, wordBank.size()));
        } else if (currentLevel <= 10) {
            // Medium words for levels 6-10
            wordIndex = 10 + random.nextInt(Math.min(10, Math.max(1, wordBank.size() - 10)));
        } else if (currentLevel <= 15) {
            // Complex words for levels 11-15
            wordIndex = 20 + random.nextInt(Math.min(10, Math.max(1, wordBank.size() - 20)));
        } else {
            // Phrases for levels 16+
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


    public void completeLevel(boolean success) {
        this.levelCompleted = success;

        if (success) {
            this.maxLevelsCompleted = Math.max(maxLevelsCompleted, currentLevel);
            advanceToNextLevel();
        } else {
            this.gameRunning = false;
        }
    }



    private void advanceToNextLevel() {
        this.currentLevel++;

        // Reduce time every 5 levels
        if (currentLevel % LEVEL_INTERVAL == 1 && currentLevel > 1) {
            this.timeLimit = Math.max(MINIMUM_TIME_LIMIT, timeLimit - TIME_REDUCTION);
        }

        this.remainingTime = timeLimit;
        generateNewWord();
    }



    public boolean decrementTime() {
        if (remainingTime > 0) {
            remainingTime--;
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


    public void stopGame() {
        this.gameRunning = false;
    }



}