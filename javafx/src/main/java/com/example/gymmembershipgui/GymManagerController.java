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

    @FXML
    private MenuItem printExpDate;


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
        String firstname = enterFirstName.getText().substring(0,1).toUpperCase() + enterFirstName.getText().substring(1).toLowerCase();
        String lastname = enterLastName.getText().substring(0,1).toUpperCase() + enterLastName.getText().substring(1).toLowerCase();

        if(!checkCreds(firstname, lastname)){
            return;
        }
        Date DOB = new Date(myDatePicker.getValue().toString());
        String location;
        if(myChoiceBar.getValue() == null || myChoiceBar.getValue().equals("")){
            memberTextOutput.appendText("Select a location.\n");
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
            memberTextOutput.appendText("Member already present in database.\n");
        }
        else{
            //added dob to this so easier to test
            memberTextOutput.appendText(firstname + " " + lastname + " " + DOB.toString() + " added.\n");
        }
        clearAllFields();
    }

    private boolean checkCreds(String firstName, String lastName) {
//        String firstname = enterFirstName.getText();
//        String lastname = enterLastName.getText();

        if(checkEmptyMember(firstName, lastName)){
            memberTextOutput.appendText("Enter member information.\n");
            return false;
        }
        if(firstName.equals("") || lastName.equals("")){
            memberTextOutput.appendText("Enter a full name.\n");
            clearAllFields();
            return false;
        }
        if(!isAlpha(firstName) || !isAlpha(lastName)){
            memberTextOutput.appendText("Names should not include special characters.\n");
            clearAllFields();
            return false;
        }
        if(myDatePicker.getValue() == null) {
            memberTextOutput.appendText("Enter a valid date.\n");
            clearAllFields();
            return false;
        }
        Date DOB = new Date(myDatePicker.getValue().toString());
        if(DOB.compareTo(new Date()) >=0){
            memberTextOutput.appendText("DOB cannot be today or future day.\n");
            clearAllFields();
            return false;
        }
        if(!DOB.aboveEighteen()){
            memberTextOutput.appendText("Member must be older than 18.\n");
            clearAllFields();
            return false;
        }
        return true;
    }

    private boolean checkEmptyMember(String firstName, String lastName){
//        String firstName = enterFirstName.getText();
//        String lastName = enterLastName.getText();
        if(firstName.equals("") && lastName.equals("") &&
                myDatePicker.getValue() == null && myChoiceBar.getValue() == null)
            return true;
        return false;
    }

    //only need name and dob to remove
    // but what if user wants to add rando location and shi?? like do we disregard that input?
    @FXML
    public void removeMember(ActionEvent event) {
        String firstName = enterFirstName.getText().substring(0,1).toUpperCase() + enterFirstName.getText().substring(1).toLowerCase();
        String lastName = enterLastName.getText().substring(0,1).toUpperCase() + enterLastName.getText().substring(1).toLowerCase();

        if(!checkCreds(firstName, lastName)){
            return;
        }

        Date DOB = new Date(myDatePicker.getValue().toString());

        Member removeThem = new Member(firstName, lastName, DOB.toString());
        if(!database.remove(removeThem)) {
            memberTextOutput.appendText("Member is not in the database.\n");
        }
        memberTextOutput.appendText("Member is removed.\n");
        clearAllFields();
    }

    private boolean checkCredsFitness() {
        String firstName = enterFirstName.getText().substring(0,1).toUpperCase() + enterFirstName.getText().substring(1).toLowerCase();
        String lastName = enterLastName.getText().substring(0,1).toUpperCase() + enterLastName.getText().substring(1).toLowerCase();
        if(firstName.equals("") || lastName.equals("")){
            memberTextOutput.appendText("Enter a full name.\n");
            clearAllFieldsFitness();
            return false;
        }
        if(!isAlpha(firstName) || !isAlpha(lastName)){
            memberTextOutput.appendText("Names should not include special characters.\n");
            clearAllFieldsFitness();
            return false;
        }
        if(memberDOB.getValue() == null) {
            memberTextOutput.appendText("Enter a valid date.\n");
            clearAllFieldsFitness();
            return false;
        }
        Date DOB = new Date(memberDOB.getValue().toString());
        if(DOB.compareTo(new Date()) >=0){
            memberTextOutput.appendText("DOB cannot be today or future day.\n");
            clearAllFieldsFitness();
            return false;
        }
        if(!DOB.aboveEighteen()){
            memberTextOutput.appendText("Member must be older than 18.\n");
            clearAllFieldsFitness();
            return false;
        }
        if(locationChoiceBar.getValue() == null || locationChoiceBar.getValue().equals("")){
            memberTextOutput.appendText("Select a location.\n");
            clearAllFieldsFitness();
            return false;
        }
        if(fitnessChoiceBar.getValue() == null || fitnessChoiceBar.getValue().equals("")){
            memberTextOutput.appendText("Select a fitness class.\n");
            clearAllFieldsFitness();
            return false;
        }
        if(instructorChoiceBar.getValue() == null || instructorChoiceBar.getValue().equals("")){
            memberTextOutput.appendText("Select an instructor.\n");
            clearAllFieldsFitness();
            return false;
        }
        return true;
    }

    @FXML
    public void checkIn(ActionEvent event){
        if(isGuestYes.isSelected()){
            checkInGuest(event);
        }
        else{
            checkInMember(event);
        }
    }

    @FXML
    public void checkOut(ActionEvent event){
        if(isGuestYes.isSelected()){
            checkOutGuest(event);
        }
        else{
            checkOutMember(event);
        }
    }

    private void checkInGuest(ActionEvent event) {
        if(!checkCredsFitness()) {
            return;
        }
        String fname = memberFirstName.getText();
        String lname = memberLastName.getText();
        Date DOB = new Date(memberDOB.getValue().toString());

        Member checkMember = new Member(fname, lname, DOB.toString());
        Member findMember = database.findMember(checkMember);
        if(!isClassValid(fitnessChoiceBar.getValue(), instructorChoiceBar.getValue(), locationChoiceBar.getValue())) {
            memberTextOutput.appendText("Class does not exist.\n");
            clearAllFieldsFitness();
            return;
        }
        if(!isMemberValid(findMember, checkMember)) {
            clearAllFieldsFitness();
            return;
        }
        if(!findMember.getLocation().name().equalsIgnoreCase(locationChoiceBar.getValue()) &&
                !(findMember instanceof Family)) {
            Location tryCheckIn = Location.getLocation(locationChoiceBar.getValue());

            memberTextOutput.appendText(findMember.getFirstName() + " " +
                    findMember.getLastName() + " checking in " +
                    tryCheckIn.name() + ", " + tryCheckIn.getZipCode() + ", "
                    + tryCheckIn.getCounty() + " - standard membership " +
                    "location restriction.\n");
            clearAllFieldsFitness();
            return;
        }
        memberTextOutput.appendText(schedule.checkInMember(fitnessChoiceBar.getValue(),
                locationChoiceBar.getValue(), instructorChoiceBar.getValue(), findMember));
        clearAllFieldsFitness();
    }

    private void checkInMember(ActionEvent event) {
        if(!checkCredsFitness()) {
            return;
        }
        Date DOB = new Date(memberDOB.getValue().toString());

        Member checkMember = new Member(memberFirstName.getText(), memberLastName.getText(),
                DOB.toString());
        Member findMember = database.findMember(checkMember);
        if(!isClassValid(fitnessChoiceBar.getValue(), instructorChoiceBar.getValue(), locationChoiceBar.getValue())){
            memberTextOutput.appendText("Class does not exist.\n");
            clearAllFieldsFitness();
            return;}
        if(!isMemberValid(findMember, checkMember)){
            clearAllFieldsFitness();
            return;}
        if(!findMember.getLocation().name().equalsIgnoreCase(locationChoiceBar.getValue()) &&
                !(findMember instanceof Family)) {
            Location tryCheckIn = Location.getLocation(locationChoiceBar.getValue());

            memberTextOutput.appendText(findMember.getFirstName() + " " +
                    findMember.getLastName() + " checking in " +
                    tryCheckIn.name() + ", " + tryCheckIn.getZipCode() + ", "
                    + tryCheckIn.getCounty() + " - standard membership " +
                    "location restriction.\n");
            clearAllFieldsFitness();
            return;
        }
        memberTextOutput.appendText(schedule.checkInMember
                (fitnessChoiceBar.getValue(), locationChoiceBar.getValue(), instructorChoiceBar.getValue(), findMember) + "\n");
        clearAllFieldsFitness();
    }

    private void checkOutGuest(ActionEvent event) {
        if(!checkCredsFitness()) {
            return;
        }
        Date DOB = new Date(memberDOB.getValue().toString());
        Member checkMember = new Member(memberFirstName.getText(), memberLastName.getText(),
                DOB.toString());
        Member findMember = database.findMember(checkMember);

        if(!isClassValid(fitnessChoiceBar.getValue(), instructorChoiceBar.getValue(), locationChoiceBar.getValue())){
            memberTextOutput.appendText("Class does not exist.\n");
            clearAllFieldsFitness();
            return;}
        if(findMember == null) {
            memberTextOutput.appendText(checkMember.getFirstName() + " " +
                    checkMember.getLastName() + " " + checkMember.getDOB() +
                    " is not in the database.\n");
            clearAllFieldsFitness();
            return;
        }
        FitnessClass foundFitClass = schedule.findFitnessClass
                (new FitnessClass(instructorChoiceBar.getValue(), fitnessChoiceBar.getValue(), locationChoiceBar.getValue()));
        if(foundFitClass == null) {
            memberTextOutput.appendText(fitnessChoiceBar.getValue() + " by " + instructorChoiceBar.getValue() + " does " +
                    "not exist at " + locationChoiceBar.getValue() + "\n");
            clearAllFieldsFitness();
            return;
        }
        foundFitClass.removeGuest((Family) findMember);
        memberTextOutput.appendText(findMember.getFirstName() + " " +
                findMember.getLastName() + " Guest done with the class.\n");
        clearAllFieldsFitness();
    }

    private void checkOutMember(ActionEvent event)
    {
        if(!checkCredsFitness()) {
            return;
        }

        Date DOB = new Date(memberDOB.getValue().toString());
        Member checkMember = new Member(memberFirstName.getText(), memberLastName.getText(),
                DOB.toString());
        Member findMember = database.findMember(checkMember);

        if(!isClassValid(fitnessChoiceBar.getValue(), instructorChoiceBar.getValue(), locationChoiceBar.getValue())){
            memberTextOutput.appendText("Class does not exist.\n");
            clearAllFieldsFitness();
            return;}
        if(findMember == null) {
            memberTextOutput.appendText(checkMember.getFirstName() + " " +
                    checkMember.getLastName() + " " + checkMember.getDOB() +
                    " is not in the database.\n");
            clearAllFieldsFitness();
            return;
        }
        FitnessClass foundFitClass = schedule.findFitnessClass
                (new FitnessClass(instructorChoiceBar.getValue(), fitnessChoiceBar.getValue(), locationChoiceBar.getValue()));
        if(foundFitClass == null) {
            memberTextOutput.appendText(fitnessChoiceBar.getValue() + " by " + instructorChoiceBar.getValue() +
                    " does not exist at " + locationChoiceBar.getValue() + ".\n");
            clearAllFieldsFitness();
            return;
        }
        if(foundFitClass.findMember(findMember) == null) {
            memberTextOutput.appendText(findMember.getFirstName() + " " +
                    findMember.getLastName() + " did not check in.\n");
            clearAllFieldsFitness();
            return;
        }
        foundFitClass.removeMember(findMember);
        memberTextOutput.appendText(findMember.getFirstName() + " " +
                findMember.getLastName() + " done with the class.\n");
        clearAllFieldsFitness();
    }

    //c;ears all fields in membership tab besides text area ofc.
    private void clearAllFields() {
        enterFirstName.setText("");
        enterLastName.setText("");
        myDatePicker.setValue(null);
        myChoiceBar.setValue(null);
        standard.setSelected(true);
    }

    private void clearAllFieldsFitness() {
        memberFirstName.setText("");
        memberLastName.setText("");
        memberDOB.setValue(null);
        locationChoiceBar.setValue(null);
        fitnessChoiceBar.setValue(null);
        instructorChoiceBar.setValue(null);
        isGuestNo.setSelected(true);
    }


    private boolean isAlpha(String name) {
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
                                        " is not in the database.\n");
            return false;
        }
        if(findMember.membershipExpired()) {
            memberTextOutput.appendText(findMember.getFirstName() + " " +
                    findMember.getLastName() + " " + findMember.getDOB() +
                    " membership expired.\n");
            return false;
        }
        return true;
    }

    @FXML
    public void onPrintExpDateClicked(){
        memberTextOutput.appendText(database.printByExpirationDate());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myChoiceBar.getItems().addAll(locations);
        locationChoiceBar.getItems().addAll(locations);
        fitnessChoiceBar.getItems().addAll(fitnessClasses);
        instructorChoiceBar.getItems().addAll(instructors);
    }
}