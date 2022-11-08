package com.example.gymmembershipgui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class GymManagerController implements Initializable {
    private MemberDatabase database;
    private ClassSchedule schedule;
    private String[] locations;
    private String[] fitnessClasses;
    private String[] instructors;
    @FXML
    private TextField enterFirstName;
    @FXML
    private TextField enterLastName;
    @FXML
    private TextArea output;
    @FXML
    private DatePicker memberDOBPicker;
    @FXML
    private ChoiceBox<String> memberLocationChoiceBar;
    @FXML
    private RadioButton standard;
    @FXML
    private RadioButton family;
    @FXML
    private TextField memberFirstName;
    @FXML
    private TextField memberLastName;
    @FXML
    private DatePicker classMemberDOBPicker;
    @FXML
    private ChoiceBox<String> classLocationChoiceBar;
    @FXML
    private ChoiceBox<String> fitnessChoiceBar;
    @FXML
    private ChoiceBox<String> instructorChoiceBar;
    @FXML
    private RadioButton isGuestYes;
    @FXML
    private RadioButton isGuestNo;

    public GymManagerController() {
        database = new MemberDatabase();
        schedule = new ClassSchedule();
        locations = new String[] {"Bridgewater", "Edison", "Franklin",
                "Piscataway", "Somerville"};
        fitnessClasses = new String[] {"Cardio", "Pilates", "Spinning"};
        instructors = new String[] {"Jennifer", "Kim", "Davis", "Denise",
                "Emma"};
    }

    @FXML
    public void addMember(ActionEvent event) {
        String firstName = enterFirstName.getText();
        String lastName = enterLastName.getText();
        String name = firstName + " " + lastName;

        if(!checkMemberCredentials(firstName, lastName))
            return;

        Date DOB = new Date(memberDOBPicker.getValue().toString());
        String location;
        if(memberLocationChoiceBar.getValue() == null){
            output.appendText("Select a location.\n");
            clearAllFields();
            return;
        }
        else
            location = memberLocationChoiceBar.getValue();

        Member addMember;
        if(standard.isSelected()){
            addMember = new Member(firstName, lastName, DOB.toString(),
                    location);
        }
        else if(family.isSelected()){
            addMember = new Family(firstName, lastName, DOB.toString(),
                    location);
        }
        else {
            addMember = new Premium(firstName, lastName, DOB.toString(),
                    location);
        }

        if(!database.add(addMember))
            output.appendText(name + " is already in the database.\n");
        else
            output.appendText(name + " added.\n");

        clearAllFields();
    }

    @FXML
    public void removeMember(ActionEvent event){
        String firstName = enterFirstName.getText();
        String lastName = enterLastName.getText();
        String name = firstName + " " + lastName;

        if(!checkMemberCredentials(firstName, lastName))
            return;

        if(memberLocationChoiceBar.getValue() != null ||
                !standard.isSelected()) {
            output.appendText("Try again. Only input name and date of " +
                    "birth when removing a member.\n");
            clearAllFields();
            return;
        }

        Date DOB = new Date(memberDOBPicker.getValue().toString());
        Member removeMember = new Member(firstName, lastName, DOB.toString());
        if(!database.remove(removeMember))
            output.appendText(name + " is not in the database.\n");
        else
            output.appendText(name + " removed.\n");
        clearAllFields();
    }

    private boolean checkMemberCredentials(String firstName, String lastName){
        if(checkEmptyMember(firstName, lastName)){
            output.appendText("Enter member information.\n");
            return false;
        }
        if(firstName.equals("") || lastName.equals("")){
            output.appendText("Enter a full name.\n");
            clearAllFields();
            return false;
        }
        if(!isAlpha(firstName) || !isAlpha(lastName)){
            output.appendText("Names should not include " +
                    "special characters, spaces, or numbers.\n");
            clearAllFields();
            return false;
        }
        if(memberDOBPicker.getValue() == null) {
            output.appendText("Enter a date of birth.\n");
            clearAllFields();
            return false;
        }

        Date DOB = new Date(memberDOBPicker.getValue().toString());
        if(DOB.compareTo(new Date()) >= 0){
            output.appendText("DOB " + DOB + ": cannot be today or a " +
                    "future date.\n");
            clearAllFields();
            return false;
        }
        if(!DOB.aboveEighteen()){
            output.appendText("DOB " + DOB + ": must be 18 or older to " +
                    "join!\n");
            clearAllFields();
            return false;
        }
        return true;
    }

    private boolean checkEmptyMember(String firstName, String lastName){
        if(firstName.equals("") && lastName.equals("") &&
                memberDOBPicker.getValue() == null &&
                memberLocationChoiceBar.getValue() == null)
            return true;
        return false;
    }

    @FXML
    public void checkIn(ActionEvent event){
        if(isGuestYes.isSelected())
            checkInGuest(event);
        else
            checkInMember(event);
    }

    @FXML
    public void checkOut(ActionEvent event){
        if(isGuestYes.isSelected())
            checkOutGuest(event);
        else
            checkOutMember(event);
    }

    private void checkInGuest(ActionEvent event) {
        String firstName = memberFirstName.getText();
        String lastName = memberLastName.getText();
        String name = firstName + " " + lastName;
        String instructor = instructorChoiceBar.getValue();
        String fitness = fitnessChoiceBar.getValue();
        String location = classLocationChoiceBar.getValue();

        if(!checkCredentialsClass(firstName, lastName))
            return;

        Date DOB = new Date(classMemberDOBPicker.getValue().toString());
        FitnessClass fitnessClass = new FitnessClass(instructor, fitness, location);
        Member checkMember = new Member(firstName, lastName, DOB.toString());
        Member findMember = database.findMember(checkMember);

        if(!isClassValid(fitness, instructor, location))
            return;
        if(!isMemberValid(findMember, checkMember))
            return;
        if(!(findMember instanceof Family)){
            output.appendText("Standard membership - guest check-in is " +
                    "not allowed.\n");
            clearAllFieldsFitness();
            return;
        }

        Location checkInLoc = Location.getLocation(location);
        if(!findMember.getLocation().name().equalsIgnoreCase(location)) {
            output.appendText(name + " Guest checking in " + checkInLoc +
                    " - guest location restriction.\n");
            clearAllFieldsFitness();
            return;
        }

        output.appendText(schedule.findFitnessClass(fitnessClass).checkInGuest((Family) findMember));
        clearAllFieldsFitness();
    }

    private void checkInMember(ActionEvent event) {
        String firstName = memberFirstName.getText();
        String lastName = memberLastName.getText();
        String name = firstName + " " + lastName;
        String instructor = instructorChoiceBar.getValue();
        String fitness = fitnessChoiceBar.getValue();
        String location = classLocationChoiceBar.getValue();

        if(!checkCredentialsClass(firstName, lastName))
            return;

        Date DOB = new Date(classMemberDOBPicker.getValue().toString());
        FitnessClass fitnessClass = new FitnessClass(instructor, fitness, location);
        Member checkMember = new Member(firstName, lastName, DOB.toString());
        Member findMember = database.findMember(checkMember);

        if(!isClassValid(fitness, instructor, location))
            return;
        if(!isMemberValid(findMember, checkMember))
            return;

        Location checkInLoc = Location.getLocation(location);
        if((!(findMember instanceof Family)) && findMember.getLocation() !=
                checkInLoc){
            output.appendText(name + " checking in " + checkInLoc +
                    " - standard membership location restriction.\n");
        }

        findTimeConflicts(fitnessClass, findMember);
        clearAllFieldsFitness();
    }

    private void findTimeConflicts(FitnessClass fitnessClass, Member member){
        String time = fitnessClass.getTime();
        String fitness = fitnessClass.getClassName();
        String instructor = fitnessClass.getInstructorName();
        Location location = Location.getLocation(fitnessClass.getLocation());
        String name = member.getFirstName() + " " + member.getLastName();
        FitnessClass[] conflictingClasses = schedule.findTimeConflict(fitnessClass);
        for(FitnessClass fc: conflictingClasses){
            if(fc != null && fc.findMember(member) != null){
                output.appendText("Time conflict - " + fitness +
                        " - " + instructor + ", " + time + ", " + location);
            }
        }
        output.appendText(fitnessClass.checkInMember(member));
    }

    private void checkOutGuest(ActionEvent event) {
        String firstName = memberFirstName.getText();
        String lastName = memberLastName.getText();
        String name = firstName + " " + lastName;
        String instructor = instructorChoiceBar.getValue();
        String fitness = fitnessChoiceBar.getValue();
        String location = classLocationChoiceBar.getValue();

        if(!checkCredentialsClass(firstName, lastName))
            return;

        Date DOB = new Date(classMemberDOBPicker.getValue().toString());
        Member checkMember = new Member(firstName, lastName, DOB.toString());
        Member findMember = database.findMember(checkMember);

        if(!isClassValid(fitness, instructor, location))
            return;
        if(!isMemberValid(findMember, checkMember))
            return;
        if(!(findMember instanceof Family)){
            output.appendText("Standard membership - guest check-in is " +
                    "not allowed.\n");
            clearAllFieldsFitness();
            return;
        }
        FitnessClass fitnessClass = schedule.findFitnessClass
                (new FitnessClass(instructor, fitness, location));
        if(fitnessClass.findGuest((Family)findMember) == null) {
            output.appendText(findMember.getFirstName() + " " +
                    findMember.getLastName() + " did not check in.\n");
            clearAllFieldsFitness();
            return;
        }
        fitnessClass.removeGuest((Family) findMember);
        output.appendText(name + " Guest done with the class.\n");
        clearAllFieldsFitness();
    }

    private void checkOutMember(ActionEvent event) {
        String firstName = memberFirstName.getText();
        String lastName = memberLastName.getText();
        String name = firstName + " " + lastName;
        String instructor = instructorChoiceBar.getValue();
        String fitness = fitnessChoiceBar.getValue();
        String location = classLocationChoiceBar.getValue();

        if(!checkCredentialsClass(firstName, lastName))
            return;

        Date DOB = new Date(classMemberDOBPicker.getValue().toString());
        Member checkMember = new Member(firstName, lastName, DOB.toString());
        Member findMember = database.findMember(checkMember);

        if(!isClassValid(fitness, instructor, location))
            return;
        if(!isMemberValid(findMember, checkMember))
            return;

        FitnessClass fitnessClass = schedule.findFitnessClass
                (new FitnessClass(instructor, fitness, location));
        if(fitnessClass.findMember(findMember) == null) {
            output.appendText(findMember.getFirstName() + " " +
                    findMember.getLastName() + " did not check in.\n");
            clearAllFieldsFitness();
            return;
        }
        fitnessClass.removeMember(findMember);
        output.appendText(name + " done with the class.\n");
        clearAllFieldsFitness();
    }

    private boolean checkEmptyMemberFitness(String firstName, String lastName){
        if(firstName.equals("") && lastName.equals("") &&
                memberDOBPicker.getValue() == null &&
                memberLocationChoiceBar.getValue() == null)
            return true;
        return false;
    }

    private boolean checkCredentialsClass(String firstName, String lastName) {
        if(checkEmptyMemberFitness(firstName, lastName)){
            output.appendText("Enter member information.\n");
            return false;
        }
        if(firstName.equals("") || lastName.equals("")){
            output.appendText("Enter a full name.\n");
            clearAllFieldsFitness();
            return false;
        }
        if(!isAlpha(firstName) || !isAlpha(lastName)){
            output.appendText("Names should not include " +
                    "special characters, spaces, or numbers.\n");
            clearAllFieldsFitness();
            return false;
        }
        if(classMemberDOBPicker.getValue() == null) {
            output.appendText("Enter a valid date.\n");
            clearAllFieldsFitness();
            return false;
        }
        Date DOB = new Date(classMemberDOBPicker.getValue().toString());
        if(DOB.compareTo(new Date()) >=0){
            output.appendText("DOB " + DOB + ": cannot be today or a " +
                    "future date.\n");
            clearAllFieldsFitness();
            return false;
        }
        if(!DOB.aboveEighteen()){
            output.appendText("DOB " + DOB + ": must be 18 or older to " +
                    "join!\n");
            clearAllFieldsFitness();
            return false;
        }
        if(classLocationChoiceBar.getValue() == null){
            output.appendText("Select a location.\n");
            clearAllFieldsFitness();
            return false;
        }
        if(fitnessChoiceBar.getValue() == null){
            output.appendText("Select a fitness class.\n");
            clearAllFieldsFitness();
            return false;
        }
        if(instructorChoiceBar.getValue() == null){
            output.appendText("Select an instructor.\n");
            clearAllFieldsFitness();
            return false;
        }
        return true;
    }

    private void clearAllFields() {
        enterFirstName.setText("");
        enterLastName.setText("");
        memberDOBPicker.setValue(null);
        memberLocationChoiceBar.setValue(null);
        memberLocationChoiceBar.setVisible(true);
        standard.setSelected(true);
    }

    private void clearAllFieldsFitness() {
        memberFirstName.setText("");
        memberLastName.setText("");
        classMemberDOBPicker.setValue(null);
        classLocationChoiceBar.setValue(null);
        fitnessChoiceBar.setValue(null);
        instructorChoiceBar.setValue(null);
        isGuestNo.setSelected(true);
    }

    private boolean isAlpha(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c))
                return false;
        }
        return true;
    }

    private boolean isClassValid(String className, String instructor,
                                 String location){
        FitnessClass fitnessClass = new FitnessClass(instructor, className,
                location);
        if(schedule.findFitnessClass(fitnessClass) == null){
            output.appendText(className + " by " + instructor + " does not"
                    + " exist at " + location);
            clearAllFieldsFitness();
            return false;
        }
        return true;
    }

    private boolean isMemberValid(Member findMember, Member checkMember){
        if(findMember == null) {
            output.appendText(checkMember.getFirstName() + " " +
                    checkMember.getLastName() + " " + checkMember.getDOB() +
                    " is not in the database.\n");
            clearAllFieldsFitness();
            return false;
        }
        if(findMember.membershipExpired()) {
            output.appendText(findMember.getFirstName() + " " +
                    findMember.getLastName() + " " + findMember.getDOB() +
                    " membership expired.\n");
            clearAllFieldsFitness();
            return false;
        }
        return true;
    }

    @FXML
    public void onPrintClicked(){
        output.appendText(database.print());
    }

    @FXML
    public void onPrintCountyClicked(){
        output.appendText(database.printByCounty());
    }

    @FXML
    public void onPrintNameClicked(){
        output.appendText(database.printByName());
    }

    @FXML
    public void onPrintExpDateClicked(){
        output.appendText(database.printByExpirationDate());
    }

    @FXML
    public void onPrintFeeClicked(){
        output.appendText(database.printByMembershipFee());
    }

    @FXML
    public void onLoadMemberListClicked(){
        String file = "./javafx/src/main/java/com/example/gymmembershipgui/memberList.txt";
        try{
            output.appendText(database.loadMemberList(file));
        } catch (FileNotFoundException e) {
            output.appendText("Member list file not found.");
        }

    }

    @FXML
    public void showAllClasses(){
        if(schedule.isEmpty()){
            output.appendText("There are no fitness classes " +
                    "loaded on the schedule.\n");
        } else {
            output.appendText(schedule.toString());
        }
    }

    @FXML
    public void onLoadClassScheduleClicked(){
        String file = "./javafx/src/main/java/com/example/gymmembershipgui/classSchedule.txt";
        try {
            schedule.loadClassSchedule(file);
            output.appendText(schedule.toString());
        } catch (FileNotFoundException e) {
            output.appendText("Class schedule file not found.");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        memberLocationChoiceBar.getItems().addAll(locations);
        classLocationChoiceBar.getItems().addAll(locations);
        fitnessChoiceBar.getItems().addAll(fitnessClasses);
        instructorChoiceBar.getItems().addAll(instructors);
    }
}