package com.example.gymmembershipgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * This is the class that runs the GUI for the Gym Membership project.
 * It loads the .fxml file which is connected to the GymManagerController for
 * its behavior.
 * @author Arunima Tripathy, Mahfuza Rahman
 */
public class GymManagerMain extends Application {
    /**
     * Main method that runs the GUI for the Gym Manager.
     * @param args the array of characters passed to the command line.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Generates the GUI for the Gym Manager.
     * @param stage the container for the Gym Manager window.
     * @throws IOException the general exceptions that are thrown.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GymManagerMain.class.
                getResource("GymManagerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Gym Manager");
        stage.setScene(scene);
        stage.show();
    }
}