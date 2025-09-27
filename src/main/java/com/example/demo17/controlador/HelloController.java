package com.example.demo17.controlador;

import com.example.demo17.modelo.GameModel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


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



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeGameModel();
        setupEventHandlers();
        setupTimer();
        startNewGame();
    }



    private void initializeGameModel() {
        this.gameModel = new GameModel();
    }



    private void setupEventHandlers() {
        // Button click handler - Traditional EventHandler implementation
        EventHandler<ActionEvent> buttonClickHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleValidation();
            }
        };
        btnValidate.setOnAction(buttonClickHandler);

        // Enter key handler for text field - Traditional EventHandler implementation
        EventHandler<KeyEvent> keyPressHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                handleKeyPressed(event);
            }
        };
        textInput.setOnKeyPressed(keyPressHandler);

        // Focus text field initially using traditional Runnable
        Runnable focusTextFieldTask = new Runnable() {
            @Override
            public void run() {
                textInput.requestFocus();
            }
        };
        Platform.runLater(focusTextFieldTask);
    }



    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleValidation();
        }
    }



    private void setupTimer() {
        // Traditional EventHandler for timer updates
        EventHandler<ActionEvent> timerUpdateHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateTimer();
            }
        };

        KeyFrame timerFrame = new KeyFrame(Duration.seconds(1), timerUpdateHandler);
        gameTimer = new Timeline(timerFrame);
        gameTimer.setCycleCount(Timeline.INDEFINITE);
    }



    private void startNewGame() {
        gameModel.resetGame();
        gameModel.startGame();
        updateView();
        startTimer();

        // Clear and focus text input
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



    private void updateTimer() {
        if (!gameModel.isGameRunning()) {
            stopTimer();
            return;
        }

        boolean timeRemaining = gameModel.decrementTime();
        updateTimeDisplay();

        if (!timeRemaining) {
            // Time ran out
            stopTimer();
            handleTimeExpired();
        }
    }



    private void handleTimeExpired() {
        if (validating) return;
        validating = true;

        String userInput = textInput.getText().trim();
        boolean isCorrect = gameModel.validateInput(userInput);

        gameModel.completeLevel(isCorrect);

        if (isCorrect) {
            showSuccessMessage("¡Correcto! Tiempo justo.");
            proceedToNextLevel();
        } else {
            showFailureMessage("Tiempo agotado. Palabra incorrecta o incompleta.");
            handleGameOver();
        }
    }



    private void handleValidation() {
        if (validating) {
            return;
        }


        validating = true;
        stopTimer();

        String userInput = textInput.getText().trim();
        boolean isCorrect = gameModel.validateInput(userInput);

        gameModel.completeLevel(isCorrect);

        if (isCorrect) {
            showSuccessMessage("¡Correcto! ¡Nivel superado!");
            proceedToNextLevel();
        } else {
            showFailureMessage("Incorrecto. La palabra era: " + gameModel.getCurrentWord());
            handleGameOver();
        }
    }



    private void proceedToNextLevel() {
        // Traditional EventHandler for pause between levels
        EventHandler<ActionEvent> pauseHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameModel.isGameRunning()) {
                    updateView();
                    textInput.clear();
                    textInput.requestFocus();
                    startTimer();
                    validating = false;
                }
            }
        };

        KeyFrame pauseFrame = new KeyFrame(Duration.seconds(1.5), pauseHandler);
        Timeline pause = new Timeline(pauseFrame);
        pause.play();
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

        // Change progress bar color based on remaining time
        if (progress > 0.5) {
            progressTime.setStyle("-fx-accent: #4CAF50;"); // Green
        } else if (progress > 0.2) {
            progressTime.setStyle("-fx-accent: #FF9800;"); // Orange
        } else {
            progressTime.setStyle("-fx-accent: #F44336;"); // Red
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
                        "Niveles completados: %d\n" +
                                "Palabra final: %s\n" +
                                "¿Deseas jugar de nuevo?",
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
                    System.out.println("Timer stopped: " + (gameTimer.getStatus() == Animation.Status.STOPPED));
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
        }
    }
}