package com.example.gymmembershipgui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField enterFirstName;

    @FXML
    private TextField enterLastName;

    @FXML
    private Button addMember;

    @FXML
    private TextField memberTextOutput;

    @FXML
    protected void onFirstNameEntered() {

    }

    @FXML
    public void submit(ActionEvent event)
    {
        String firstname = enterFirstName.getText();
        String lastname = enterLastName.getText();

        System.out.println(firstname + " " + lastname);
        //memberTextOutput.setText(firstname + lastname);

    }
}