package com.example.gymmembershipgui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    private MemberDatabase database;

    private ClassSchedule schedule;

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

    private String[] fitnessClasses = {"Cardio", "Pilates", "Spinning"};

    private String[] instructors = {"Jeniffer", "Kim", "Dennis", "Denise", "Emma"};

    @FXML
    private RadioButton standard;

    @FXML
    private RadioButton family;

    @FXML
    private RadioButton premium;

    // fitness class variables
    @FXML
    private TextField memberFirstName;

    @FXML
    private TextField memberLastName;

    @FXML
    private DatePicker memberDOB;

    @FXML
    private ChoiceBox<String> locationChoiceBar;

    @FXML
    private ChoiceBox<String> fitnessChoiceBar;

    @FXML
    private ChoiceBox<String> instructorChoiceBar;

    @FXML
    private RadioButton isGuestYes;

    @FXML
    private RadioButton isGuestNo;



    // firstName and lastname should have no extra characters
    // DOB should be 18, should not be today or future day
    // location should be selected
    // not already in the database
    // and then just add.

    public HelloController()
    {
        database = new MemberDatabase();
        schedule = new ClassSchedule();
    }

    private void checkNameAndDate()
    {
        // can copy n paste repeated code here
    }

    @FXML
    public void addMember(ActionEvent event) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");

        String firstname = enterFirstName.getText();
        String lastname = enterLastName.getText();
        if(firstname.equals("") || lastname.equals("")){
            errorAlert.setContentText("Enter a full name.");
            errorAlert.showAndWait();
            clearAllFields();
            return;
        }
        if(!isAlpha(firstname) || !isAlpha(lastname)){
            errorAlert.setContentText("Names should not include special characters");
            errorAlert.showAndWait();
            clearAllFields();
            return;
        }
        if(myDatePicker.getValue() == null)
        {
            errorAlert.setContentText("Enter a date.");
            errorAlert.showAndWait();
            clearAllFields();
            return;
        }
        Date DOB = new Date(myDatePicker.getValue().toString());
        if(DOB.compareTo(new Date()) >=0){
            errorAlert.setContentText("DOB cannot be today or future day");
            errorAlert.showAndWait();
            clearAllFields();
            return;
        }
        if(!DOB.aboveEighteen()){
            errorAlert.setContentText("Member must be older than 18.");
            errorAlert.showAndWait();
            clearAllFields();
            return;
        }
        String location;
        if(myChoiceBar.getValue() == null || myChoiceBar.getValue().equals("")){
            errorAlert.setContentText("Select a location.");
            clearAllFields();
            return;
        }
        else{
            location = myChoiceBar.getValue();
        }

        Member addMember;
        if(standard.isSelected()){
            addMember = new Member(firstname, lastname, DOB.toString(),
                    location);
        }
        else if(family.isSelected()){
            addMember = new Family(firstname, lastname, DOB.toString(),
                    location);
        }
        else{
            addMember = new Premium(firstname, lastname, DOB.toString(),
                    location);
        }
        if(!database.add(addMember)){
            memberTextOutput.appendText("\nMember already present in database.");
        }
        else{
            //added dob to this so easier to test
            memberTextOutput.appendText("\n" + firstname + " " + lastname + " " + DOB.toString() + "added.");
        }
        clearAllFields();
    }

    //only need name and dob to remove
    // but what if user wants to add rando location and shi?? like do we disregard that input?
    @FXML
    public void removeMember(ActionEvent event)
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");

        String firstname = enterFirstName.getText();
        String lastname = enterLastName.getText();
        if(firstname == null || lastname == null){
            errorAlert.setContentText("Enter a full name.");
            errorAlert.showAndWait();
            clearAllFields();
            return;
        }
        if(myDatePicker.getValue() == null)
        {
            errorAlert.setContentText("Enter a date.");
            errorAlert.showAndWait();
            clearAllFields();
            return;
        }

        Date DOB = new Date(myDatePicker.getValue().toString());

        Member removeThem = new Member(firstname, lastname, DOB.toString());
        if(!database.remove(removeThem)) {
            memberTextOutput.appendText("\nMember is not in the database.");
        }
        memberTextOutput.appendText("\nMember is removed.");
        clearAllFields();
    }

    @FXML
    public void checkInGuest(ActionEvent event)
    {

    }

    @FXML
    public void checkInMember(ActionEvent event)
    {

    }

    private void checkCredentials()
    {
        // names are entered
        // dob is entered
        // location, instructor, and fitnessclass are selected
        //
    }


    //c;ears all fields in membership tab besides text area ofc.
    private void clearAllFields()
    {
        enterFirstName.setText("");
        enterLastName.setText("");
        myDatePicker.setValue(null);
        myChoiceBar.setValue(null);
        standard.setSelected(true);
    }

    private boolean isAlpha(String name)
    {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myChoiceBar.getItems().addAll(locations);
        locationChoiceBar.getItems().addAll(locations);
        fitnessChoiceBar.getItems().addAll(fitnessClasses);
        instructorChoiceBar.getItems().addAll(instructors);
    }
}