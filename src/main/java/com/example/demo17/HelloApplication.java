package com.example.demo17;

import com.example.demo17.controlador.HelloController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class for Fast Writing Game.
 * Initializes JavaFX and loads the main view.
 *
 * @author Your Name
 */
public class HelloApplication extends Application {

    /** Reference to the controller for cleanup purposes */
    private HelloController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load());
            controller = fxmlLoader.getController();
            setupPrimaryStage(stage, scene);
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
            throw e;
        }
    }

    private void setupPrimaryStage(Stage stage, Scene scene) {
        stage.setTitle("Escritura Rápida - Fast Writing Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.centerOnScreen();
        stage.show();
    }

    private void handleApplicationClose() {
        controller.shutdown();
        System.out.println("Fast Writing Game application closing...");
    }

    @Override
    public void stop() throws Exception {
        handleApplicationClose();
        super.stop();
    }

    public static void main(String[] args) {
        System.out.println("Starting Fast Writing Game...");
        System.out.println("Java Version: " + System.getProperty("java.version"));

        try {
            launch(args);
        } catch (Exception e) {
            System.err.println("Error launching application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}