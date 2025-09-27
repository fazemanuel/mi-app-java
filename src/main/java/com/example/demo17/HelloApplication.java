package com.example.demo17;

import com.example.demo17.controlador.HelloController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {




    /** Reference to the controller for cleanup purposes */
    private HelloController controller;


    @Override
    public void start(Stage stage) throws IOException {


        // Load the FXML file and create the loader
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        try {
            // Load the scene from FXML
            Scene scene = new Scene(fxmlLoader.load());

            // Get reference to controller for cleanup
            controller = fxmlLoader.getController();

            // Configure the primary stage
            setupPrimaryStage(stage, scene);

        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
            throw e;
        }
    }


    private void setupPrimaryStage(Stage stage, Scene scene) {
        // Set window properties
        stage.setTitle("Escritura Rápida - Fast Writing Game");
        stage.setScene(scene);
        stage.setResizable(false); // Fixed size for consistent UI

        // Set minimum size to prevent UI issues
        stage.setMinWidth(600);
        stage.setMinHeight(400);



        // Center the window on screen
        stage.centerOnScreen();

        // Show the stage
        stage.show();


    }


    private void handleApplicationClose() {

        controller.shutdown();


        // Any additional cleanup can be added here
        System.out.println("Fast Writing Game application closing...");
    }



    @Override
    public void stop() throws Exception {
        handleApplicationClose();
        super.stop();
    }




    public static void main(String[] args) {
        // Print application startup information
        System.out.println("Starting Fast Writing Game...");
        System.out.println("Java Version: " + System.getProperty("java.version"));

        try {
            // Launch the JavaFX application
            launch(args);
        } catch (Exception e) {
            System.err.println("Error launching application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}