package com.example.gymmembershipgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("GymManagerView.fxml"));
        // scene object = Scene (node, width, height)
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        // stage  is a window
        stage.setTitle("Gym Manager!");
        stage.setScene(scene); // sets scene to stage
        stage.show(); // displays primary stage on screen
    }

    public static void main(String[] args) {
        launch();
    }
}