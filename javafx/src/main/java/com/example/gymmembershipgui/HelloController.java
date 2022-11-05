package com.example.gymmembershipgui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private TextField enterFirstName;

    @FXML
    private TextField enterLastName;

    @FXML
    private Button addMember;

    @FXML
    private TextArea memberTextOutput;

    @FXML
    private DatePicker myDatePicker;

    @FXML
    private ChoiceBox<String> myChoiceBar;

    private String[] locations = {"Bridgewater", "Edison", "Franklin", "Piscataway", "Somerville"};

    @FXML
    private RadioButton standard;

    @FXML
    private RadioButton family;

    @FXML
    private RadioButton premium;

    @FXML
    public void submit(ActionEvent event) {
        String firstname = enterFirstName.getText();
        String lastname = enterLastName.getText();
        String location;

        System.out.println(firstname + " " + lastname);
        memberTextOutput.setText(firstname + lastname);
        Date myDate = new Date(myDatePicker.getValue().toString()); // tostring has - instead of /

        System.out.println(myDate.toString());

        if(myChoiceBar.getValue() == null || myChoiceBar.getValue().equals("")){
            memberTextOutput.setText("select a location");
        }
        else{
            location = myChoiceBar.getValue();
            System.out.println(location);
        }

        //radio button shit
        if(standard.isSelected())
        {
            //create member object
        }
        else if(family.isSelected())
        {
            //create family
        }
        else{
            //create premium
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myChoiceBar.getItems().addAll(locations);
    }
}