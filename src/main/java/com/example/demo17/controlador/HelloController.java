package com.example.demo17.controlador;

import com.example.demo17.eventos.*;
import com.example.demo17.modelo.GameModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the main game view.
 * Now uses event-driven architecture with custom interfaces.
 * Implements handler classes as inner classes for better encapsulation.
 *
 * @author Your Name
 */
public class HelloController implements Initializable {

    /** Progress bar showing remaining time */
    @FXML
    private ProgressBar progressTime;

    /** Label displaying remaining seconds */
    @FXML
    private Label labelSeconds;

    /** Label showing the word/phrase to type */
    @FXML
    private Label labelWord;

    /** Text field for user input */
    @FXML
    private TextField textInput;

    /** Label showing current level */
    @FXML
    private Label labelLevel;

    /** Button to validate input */
    @FXML
    private Button btnValidate;

    /** Game model instance */
    private GameModel gameModel;

    /** Timeline for countdown timer */
    private Timeline gameTimer;

    /** Flag to prevent multiple simultaneous validations */
    private boolean validating = false;

    // ========================================
    // NEW: Handler instances
    // ========================================

    /** Handles validation events */
    private ValidationHandlerImpl validationHandler;

    /** Handles input events */
    private InputHandlerImpl inputHandler;

    /** Handles timer events */
    private TimerHandlerImpl timerHandler;

    /** Handles level events */
    private LevelHandlerImpl levelHandler;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeGameModel();
        initializeHandlers();      // NEW
        registerHandlers();         // NEW
        setupEventHandlers();       // MODIFIED
        setupTimer();
        startNewGame();
    }


    private void initializeGameModel() {
        this.gameModel = new GameModel();
    }


    // ========================================
    // NEW: Initialize handler instances
    // ========================================

    /**
     * Creates instances of all event handlers.
     */
    private void initializeHandlers() {
        validationHandler = new ValidationHandlerImpl();
        inputHandler = new InputHandlerImpl();
        timerHandler = new TimerHandlerImpl();
        levelHandler = new LevelHandlerImpl();
    }


    /**
     * Registers handlers with the game model.
     */
    private void registerHandlers() {
        gameModel.addLevelHandler(levelHandler);
        gameModel.addTimerHandler(timerHandler);
    }


    /**
     * MODIFIED: Now uses the custom handlers instead of anonymous classes.
     */
    private void setupEventHandlers() {
        // Use validation handler for button click
        btnValidate.setOnAction(event -> validationHandler.onValidationRequested());

        // Use input handler for key events
        textInput.setOnKeyPressed(event -> inputHandler.onKeyPressed(event));

        // Focus text field initially
        Platform.runLater(() -> textInput.requestFocus());
    }


    private void setupTimer() {
        KeyFrame timerFrame = new KeyFrame(Duration.seconds(1), event -> updateTimer());
        gameTimer = new Timeline(timerFrame);
        gameTimer.setCycleCount(Timeline.INDEFINITE);
    }


    private void startNewGame() {
        gameModel.resetGame();
        gameModel.startGame();
        updateView();
        startTimer();

        textInput.clear();
        textInput.requestFocus();
        validating = false;
    }


    private void startTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        gameTimer.play();
    }


    private void stopTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }


    /**
     * MODIFIED: Simplified, timer events are now handled by TimerHandlerImpl.
     */
    private void updateTimer() {
        if (!gameModel.isGameRunning()) {
            stopTimer();
            return;
        }

        gameModel.decrementTime(); // This will trigger timer events
    }


    private void handleGameOver() {
        gameModel.stopGame();
        gameTimer.stop();
        showGameOverDialog();
    }


    private void updateView() {
        updateTimeDisplay();
        updateWordDisplay();
        updateLevelDisplay();
    }


    private void updateTimeDisplay() {
        int remainingTime = gameModel.getRemainingTime();
        double progress = gameModel.getTimeProgress();

        labelSeconds.setText(remainingTime + "s");
        progressTime.setProgress(progress);

        if (progress > 0.5) {
            progressTime.setStyle("-fx-accent: #4CAF50;");
        } else if (progress > 0.2) {
            progressTime.setStyle("-fx-accent: #FF9800;");
        } else {
            progressTime.setStyle("-fx-accent: #F44336;");
        }
    }


    private void updateWordDisplay() {
        labelWord.setText(gameModel.getCurrentWord());
    }


    private void updateLevelDisplay() {
        labelLevel.setText("Nivel " + gameModel.getCurrentLevel());
    }


    private void showSuccessMessage(String message) {
        showAlert(Alert.AlertType.INFORMATION, "¡Éxito!", message);
    }


    private void showFailureMessage(String message) {
        showAlert(Alert.AlertType.ERROR, "¡Fallo!", message);
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void showGameOverDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Juego Terminado!");
        alert.setHeaderText("Resumen de la Partida");

        String summary = String.format(
                "Niveles completados esta partida: %d\n" +
                        "Récord histórico: %d\n" +
                        "Palabra final: %s\n" +
                        "¿Deseas jugar de nuevo?",
                gameModel.getCurrentGameLevelsCompleted(),
                gameModel.getMaxLevelsCompleted(),
                gameModel.getCurrentWord()
        );

        alert.setContentText(summary);

        ButtonType playAgain = new ButtonType("Jugar de Nuevo");
        ButtonType exit = new ButtonType("Salir");

        alert.getButtonTypes().setAll(playAgain, exit);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == playAgain) {
            startNewGame();
        } else {
            Platform.exit();
            System.exit(0);
        }
    }


    public void shutdown() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (gameModel != null) {
            gameModel.stopGame();
            gameModel.clearAllHandlers(); // NEW: Clean up handlers
        }
    }


// ========================================
// NEW: Inner Classes - Event Handlers
// ========================================

    /**
     * Implementation of validation handler.
     * Handles user input validation and its results.
     */
    private class ValidationHandlerImpl implements IValidationHandler {

        @Override
        public void onValidationRequested() {
            if (validating) {
                return; // Prevent multiple simultaneous validations
            }

            validating = true;
            stopTimer();

            String userInput = textInput.getText().trim();
            boolean isCorrect = gameModel.validateInput(userInput);

            gameModel.completeLevel(isCorrect);

            if (isCorrect) {
                onValidationSuccess("¡Correcto! ¡Nivel superado!");
            } else {
                onValidationFailure("Incorrecto. La palabra era: " + gameModel.getCurrentWord());
            }
        }

        @Override
        public void onValidationSuccess(String message) {
            showSuccessMessage(message);
            proceedToNextLevel();
        }

        @Override
        public void onValidationFailure(String message) {
            showFailureMessage(message);
            handleGameOver();
        }

        /**
         * Proceeds to the next level after a brief pause.
         */
        private void proceedToNextLevel() {
            KeyFrame pauseFrame = new KeyFrame(Duration.seconds(1.5), event -> {
                if (gameModel.isGameRunning()) {
                    validating = false;
                    updateView();
                    textInput.clear();
                    textInput.requestFocus();
                    startTimer();
                }
            });

            Timeline pause = new Timeline(pauseFrame);
            pause.play();
        }
    }


    /**
     * Implementation of input handler using adapter pattern.
     * Handles keyboard events in the text field.
     */
    private class InputHandlerImpl extends InputAdapter {

        @Override
        public void onKeyPressed(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER) {
                onEnterPressed();
            }
        }

        @Override
        public void onEnterPressed() {
            // Trigger validation when Enter is pressed
            validationHandler.onValidationRequested();
        }

        @Override
        public void onInputChanged(String input) {
            // Could be used for real-time validation feedback
            // Currently not implemented, but available for future use
        }
    }


    /**
     * Implementation of timer handler using adapter pattern.
     * Handles timer tick and expiration events.
     */
    private class TimerHandlerImpl extends TimerAdapter {

        @Override
        public void onTimerTick(int remainingTime) {
            // Update UI on every tick
            Platform.runLater(() -> {
                updateTimeDisplay();
            });
        }

        @Override
        public void onTimerExpired() {
            // Handle time expiration
            Platform.runLater(() -> {
                handleTimeExpired();
            });
        }

        @Override
        public void onTimerStarted(int duration) {
            // Could be used to show animations or effects when timer starts
            // Currently not implemented, but available for future use
        }

        /**
         * Handles the case when time runs out.
         */
        private void handleTimeExpired() {
            if (validating) {
                return; // Already validating
            }

            validating = true;

            String userInput = textInput.getText().trim();
            boolean isCorrect = gameModel.validateInput(userInput);

            gameModel.completeLevel(isCorrect);

            if (isCorrect) {
                showSuccessMessage("¡Correcto! Tiempo justo.");
                validationHandler.onValidationSuccess("¡Correcto! Tiempo justo.");
            } else {
                showFailureMessage("Tiempo agotado. Palabra incorrecta o incompleta.");
                validationHandler.onValidationFailure("Tiempo agotado.");
            }
        }
    }


    /**
     * Implementation of level handler.
     * Handles level progression and game end events.
     */
    private class LevelHandlerImpl implements ILevelHandler {

        @Override
        public void onLevelCompleted(int level, boolean success) {
            // Log level completion
            System.out.println("Level " + level + " completed: " + (success ? "SUCCESS" : "FAILED"));

            // Could be used for statistics, achievements, etc.
        }


        @Override
        public void onLevelChanged(int newLevel) {
            // Update UI when level changes
            Platform.runLater(() -> {
                updateView();
                textInput.clear();
                textInput.requestFocus();
            });

            // Could be used for level-up animations or sound effects
            System.out.println("Advanced to level: " + newLevel);
        }

        @Override
        public void onGameEnded(int finalLevel) {
            // Handle game over scenario
            Platform.runLater(() -> {
                System.out.println("Game ended at level: " + finalLevel);
                // The game over dialog is shown by handleGameOver()
            });
        }
    }

} // End of HelloController class
