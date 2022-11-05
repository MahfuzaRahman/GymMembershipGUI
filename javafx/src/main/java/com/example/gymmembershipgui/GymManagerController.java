package com.example.gymmembershipgui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class GymManagerController implements Initializable {

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

    public GymManagerController() {
        database = new MemberDatabase();
        schedule = new ClassSchedule();
    }

    @FXML
    public void addMember(ActionEvent event) {

        String firstname = enterFirstName.getText();
        String lastname = enterLastName.getText();

        if(!checkCreds()){
            return;
        }

        Date DOB = new Date(myDatePicker.getValue().toString());
        if(!DOB.aboveEighteen()){
            memberTextOutput.appendText("\nMember must be older than 18.");
            clearAllFields();
            return;
        }
        String location;
        if(myChoiceBar.getValue() == null || myChoiceBar.getValue().equals("")){
            memberTextOutput.appendText("\nSelect a location.");
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

    private boolean checkCreds()
    {
        String firstname = enterFirstName.getText();
        String lastname = enterLastName.getText();

        if(checkEmptyMember()){
            memberTextOutput.appendText("Enter member information.\n");
            return false;
        }
        if(firstname.equals("") || lastname.equals("")){
            memberTextOutput.appendText("\nEnter a full name.");
            clearAllFields();
            return false;
        }
        if(!isAlpha(firstname) || !isAlpha(lastname)){
            memberTextOutput.appendText("\nNames should not include special characters");
            clearAllFields();
            return false;
        }
        if(myDatePicker.getValue() == null)
        {
            memberTextOutput.appendText("\nEnter a valid date.");
            clearAllFields();
            return false;
        }
        Date DOB = new Date(myDatePicker.getValue().toString());
        if(DOB.compareTo(new Date()) >=0){
            memberTextOutput.appendText("\nDOB cannot be today or future day");
            clearAllFields();
            return false;
        }
        return true;
    }

    private boolean checkEmptyMember(){
        String firstName = enterFirstName.getText();
        String lastName = enterLastName.getText();
        if(firstName.equals("") && lastName.equals("") &&
                myDatePicker.getValue() == null && myChoiceBar.getValue() == null)
            return true;
        return false;
    }

    //only need name and dob to remove
    // but what if user wants to add rando location and shi?? like do we disregard that input?
    @FXML
    public void removeMember(ActionEvent event)
    {
        if(!checkCreds()){
            return;
        }
        String firstname = enterFirstName.getText();
        String lastname = enterLastName.getText();

        Date DOB = new Date(myDatePicker.getValue().toString());

        Member removeThem = new Member(firstname, lastname, DOB.toString());
        if(!database.remove(removeThem)) {
            memberTextOutput.appendText("\nMember is not in the database.");
        }
        memberTextOutput.appendText("\nMember is removed.");
        clearAllFields();
    }

    private boolean checkCredsGuest()
    {
        String firstname = memberFirstName.getText();
        String lastname = memberLastName.getText();
        if(firstname.equals("") || lastname.equals("")){
            memberTextOutput.appendText("\nEnter a full name.");
            clearAllFields();
            return false;
        }
        if(!isAlpha(firstname) || !isAlpha(lastname)){
            memberTextOutput.appendText("\nNames should not include special characters");
            clearAllFields();
            return false;
        }
        if(memberDOB.getValue() == null)
        {
            memberTextOutput.appendText("\nEnter a valid date.");
            clearAllFields();
            return false;
        }
        Date DOB = new Date(memberDOB.getValue().toString());
        if(DOB.compareTo(new Date()) >=0){
            memberTextOutput.appendText("\nDOB cannot be today or future day");
            clearAllFields();
            return false;
        }
        if(locationChoiceBar.getValue() == null || locationChoiceBar.getValue().equals("")){
            memberTextOutput.appendText("\nSelect a location.");
            clearAllFields();
            return false;
        }
        if(fitnessChoiceBar.getValue() == null || fitnessChoiceBar.getValue().equals("")){
            memberTextOutput.appendText("\nSelect a fitness class.");
            clearAllFields();
            return false;
        }
        if(instructorChoiceBar.getValue() == null || instructorChoiceBar.getValue().equals("")){
            memberTextOutput.appendText("\nSelect an instructor.");
            clearAllFields();
            return false;
        }
        return true;
    }

    @FXML
    public void checkInGuest(ActionEvent event)
    {
        if(!checkCredsGuest()) {
            return;
        }
        String fname = memberFirstName.getText();
        String lname = memberLastName.getText();
        String dob = memberDOB.getValue().toString();

        Member checkMember = new Member(fname, lname, dob);
        Member findMember = database.findMember(checkMember);
        if(!isClassValid(fitnessChoiceBar.getValue(), instructorChoiceBar.getValue(), locationChoiceBar.getValue())) {
            memberTextOutput.appendText("\nClass does not exist.");
            return;
        }
        if(!isMemberValid(findMember, checkMember))
            return;
        if(!findMember.getLocation().name().equalsIgnoreCase(locationChoiceBar.getValue()) &&
                !(findMember instanceof Family)) {
            Location tryCheckIn = Location.getLocation(locationChoiceBar.getValue());

            memberTextOutput.appendText(findMember.getFirstName() + " " +
                    findMember.getLastName() + " checking in " +
                    tryCheckIn.name() + ", " + tryCheckIn.getZipCode() + ", "
                    + tryCheckIn.getCounty() + " - standard membership " +
                    "location restriction.");
            return;
        }
        schedule.checkInMember
                (fitnessChoiceBar.getValue(), locationChoiceBar.getValue(), instructorChoiceBar.getValue(), findMember);

    }

    @FXML
    public void checkInMember(ActionEvent event)
    {

    }

    @FXML
    public void checkOutGuest(ActionEvent event)
    {

    }

    @FXML
    public void checkOutMember(ActionEvent event)
    {

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

    private boolean isClassValid(String fitnessClass, String instructor,
                                 String location){
        if(!schedule.findFitnessClass(fitnessClass)) {
            return false;
        }
        return true;
    }

    private boolean isMemberValid(Member findMember, Member checkMember){
        if(findMember == null) {
            memberTextOutput.appendText(checkMember.getFirstName() + " " +
                            checkMember.getLastName() + " " + checkMember.getDOB() +
                                        " is not in the database.");
            return false;
        }
        if(findMember.membershipExpired()) {
            memberTextOutput.appendText(findMember.getFirstName() + " " +
                    findMember.getLastName() + " " + findMember.getDOB() +
                    " membership expired.");
            return false;
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