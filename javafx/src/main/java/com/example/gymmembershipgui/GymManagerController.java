package com.example.gymmembershipgui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.controlsfx.control.action.Action;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * GymManagerController manages a member database as well as a class schedule.
 * In each gym, the gym manager can add a member to the database as long as
 * their membership is valid. If a member wishes to be removed, the gym
 * manager can remove them from the database. Members can also check in to
 * different classes. They can also drop a class if they would like to. If a
 * member's gym plan permits them, these functions also apply to guests.
 * The gym manager's schedule can also check for time conflicts between the
 * classes a member is checked into and a class they want to attend. The gym
 * manager handles all these requests.
 * @author Mahfuza Rahman, Arunima Tripathy
 */
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

    /**
     * Creates an instance of GymManagerController with database and schedule.
     * Generates a schedule of fitness classes for all the gyms managed and
     * a database that holds all the members of all the gyms.
     */
    public GymManagerController() {
        database = new MemberDatabase();
        schedule = new ClassSchedule();
        locations = new String[] {"Bridgewater", "Edison", "Franklin",
                "Piscataway", "Somerville"};
        fitnessClasses = new String[] {"Cardio", "Pilates", "Spinning"};
        instructors = new String[] {"Jennifer", "Kim", "Davis", "Denise",
                "Emma"};
    }

    /**
     * Takes GUI input from the Membership tab to add a member to database.
     * If sufficient input is not provided, a message is sent to the user. An
     * instance of Member, Family, or Premium is created depending on the
     * information provided. As long as the DOB is valid, not a future date,
     * and the member is 18 or older, the expiration date and location are
     * valid, and the member is not already in the database, member is added.
     * @param event the event that indicates that the 'Add' button on the
     *              Membership tab was clicked.
     */
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

        String bDay = DOB.toString();
        Member addMember;
        if(standard.isSelected())
            addMember = new Member(firstName, lastName, bDay, location);
        else if(family.isSelected())
            addMember = new Family(firstName, lastName, bDay, location);
        else
            addMember = new Premium(firstName, lastName, bDay, location);

        if(!database.add(addMember))
            output.appendText(name + " is already in the database.\n");
        else
            output.appendText(name + " added.\n");
        clearAllFields();
    }

    /**
     * Takes GUI input from Membership tab to remove a member from database.
     * If sufficient input is not provided, a message is sent to the user. If
     * the member is present in the database, they are removed. Otherwise,
     * a message is sent to the user.
     * @param event the event that indicates that the 'Remove' button on the
     *              Membership tab was clicked.
     */
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

    /**
     * Checks if sufficient, valid membership information is input.
     * Checks if any credentials were entered, if a full name was entered,
     * if all the characters in a name were alphabetic, and if the entered
     * DOB is valid for a membership. If the previous checks were not true,
     * then the credentials were not valid. If they were true, the credentials
     * were valid.
     * @param firstName the first name input in the Membership tab.
     * @param lastName the last name input in the Membership tab.
     * @return true if the credentials are valid, false otherwise.
     */
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

    /**
     * Checks if any information was entered in the Membership tab.
     * If no information was entered, the member is empty.
     * @param firstName the first name input in the Membership tab.
     * @param lastName the last name input in the Membership tab.
     * @return true if the membership information is empty, false otherwise.
     */
    private boolean checkEmptyMember(String firstName, String lastName){
        if(firstName.equals("") && lastName.equals("") &&
                memberDOBPicker.getValue() == null &&
                memberLocationChoiceBar.getValue() == null)
            return true;
        return false;
    }

    /**
     * Takes GUI input from the Fitness Class tab to check in to a class.
     * Checks a guest or member in to a fitness class depending on whether
     * the 'Yes' or 'No' button is clicked to indicate if a guest is checking
     * in to a class.
     * @param event the event that indicates that the 'Check-in' button on the
     *              Fitness Class tab was clicked.
     */
    @FXML
    public void checkIn(ActionEvent event){
        if(isGuestYes.isSelected())
            checkInGuest(event);
        else
            checkInMember(event);
    }

    /**
     * Takes GUI input from the Fitness Class tab to check out of a class.
     * Checks a guest or member out of a fitness class depending on whether
     * the 'Yes' or 'No' button is clicked to indicate if a guest is checking
     * out of a class.
     * @param event the event that indicates that the 'Check-out' button on
     *              the Fitness Class tab was clicked.
     */
    @FXML
    public void checkOut(ActionEvent event){
        if(isGuestYes.isSelected())
            checkOutGuest(event);
        else
            checkOutMember(event);
    }

    /**
     * Takes GUI input from the Fitness Class tab to check guest into a class.
     * Checks whether the guest is using the family or premium plan, if the
     * class they want to attend is the same as the membership location, and
     * if the member account has a spare guest pass to use. If these
     * requirements are fulfilled, the guest is checked in.
     * @param event the event that indicates that the 'Check-in' button and
     *              'Yes' button on the Fitness Class tab was clicked.
     */
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
        FitnessClass fitnessClass = new FitnessClass(instructor, fitness,
                location);
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

        output.appendText(schedule.findFitnessClass(fitnessClass).
                checkInGuest((Family) findMember));
        clearAllFieldsFitness();
    }

    /**
     * Takes GUI input from the Fitness Class tab to check member into class.
     * Checks whether the inputted fitness class exists, if the instructor
     * exists, the location is valid, the membership has not expired,
     * the member is present in the database, and the member is checking into
     * their assigned gym location, if membership type has this restriction.
     * If all these requirements are fulfilled, the member is checked in.
     * @param event the event that indicates that the 'Check-in' button and
     *              'No' button on the Fitness Class tab was clicked.
     */
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
        FitnessClass fitnessClass = new FitnessClass(instructor, fitness,
                location);
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
        } else
            findTimeConflicts(fitnessClass, findMember);
        clearAllFieldsFitness();
    }

    /**
     * Searches for possible conflicting fitness classes given a class.
     * Iterates through the classes in the fitness class schedule. If a
     * fitness class is at the same time as the time given and the member is
     * in the class, there is a time conflict.
     * @param fitnessClass the fitness class the member wants to check in to.
     * @param member the member that would like to check in to the class.
     */
    private void findTimeConflicts(FitnessClass fitnessClass, Member member){
        fitnessClass = schedule.findFitnessClass(fitnessClass);
        String time = fitnessClass.getTime();
        String fitness = fitnessClass.getClassName();
        String instructor = fitnessClass.getInstructorName();
        Location location = Location.getLocation(fitnessClass.getLocation());
        FitnessClass[] conflictingClasses =
                schedule.findTimeConflict(fitnessClass);
        for(FitnessClass fc: conflictingClasses){
            if(fc != null && fc.findMember(member) != null){
                output.appendText("Time conflict - " + fitness + " - " +
                        instructor + ", " + time + ", " + location + "\n");
                return;
            }
        }
        output.appendText(fitnessClass.checkInMember(member));
    }

    /**
     * Checks a guest out of a fitness class.
     * Checks whether the fitness class exists, that the member whose
     * membership is being used is present in the database, and the guest
     * checked into the class in the first place. If these requirements are
     * fulfilled, the guest is removed from the class and the guest
     * pass is incremented by one.
     * @param event the event that indicates that the 'Check-out' button and
     *              'Yes' button on the Fitness Class tab was clicked.
     */
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

    /**
     * Checks a member out of a fitness class.
     * Checks whether the class exists, that the member is present in the
     * database, and the member checked into the class in the first place. If
     * these requirements are fulfilled, the member is removed from the
     * fitness class.
     * @param event the event that indicates that the 'Check-out' button and
     *              'No' button on the Fitness Class tab was clicked.
     */
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

    /**
     * Checks if any information was entered in the Fitness Class tab.
     * If no information was entered, the member is empty.
     * @param firstName the first name input in the Fitness Class tab.
     * @param lastName the last name input in the Fitness Class tab.
     * @return true if the membership information is empty, false otherwise.
     */
    private boolean checkEmptyMemberFitness(String firstName,
                                            String lastName){
        if(firstName.equals("") && lastName.equals("") &&
                memberDOBPicker.getValue() == null &&
                memberLocationChoiceBar.getValue() == null)
            return true;
        return false;
    }

    /**
     * Checks if sufficient, valid membership information is input.
     * Checks if any credentials were entered, if a full name was entered,
     * if all the characters in a name were alphabetic, if the entered DOB is
     * valid for a membership, and if class information is input. If the
     * previous checks were not true, then the credentials were not valid. If
     * they were true, the credentials were valid.
     * @param firstName the first name input in the Fitness Class tab.
     * @param lastName the last name input in the Fitness Class tab.
     * @return true if the credentials are valid, false otherwise.
     */
    private boolean checkCredentialsClass(String firstName, String lastName){
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
        if(!checkClassSelections())
            return false;
        return true;
    }

    /**
     * Checks if any class information was selected.
     * If no location, fitness choice, or instructor is chosen, then no
     * class selections were made.
     * @return true if all selections are made, false otherwise.
     */
    private boolean checkClassSelections(){
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

    /**
     * Clears all the fields in the Membership tab.
     * Clears the first name, last name, DOB, location, and resets the
     * membership type to standard.
     */
    private void clearAllFields() {
        enterFirstName.setText("");
        enterLastName.setText("");
        memberDOBPicker.setValue(null);
        memberLocationChoiceBar.setValue(null);
        memberLocationChoiceBar.setVisible(true);
        standard.setSelected(true);
    }

    /**
     * Clears all the fields in the Fitness Class tab.
     * Clears the first name, last name, DOB, location, fitness choice,
     * instructor name, and resets the guest setting to 'No'.
     */
    private void clearAllFieldsFitness() {
        memberFirstName.setText("");
        memberLastName.setText("");
        classMemberDOBPicker.setValue(null);
        classLocationChoiceBar.setValue(null);
        fitnessChoiceBar.setValue(null);
        instructorChoiceBar.setValue(null);
        isGuestNo.setSelected(true);
    }

    /**
     * Checks whether the inputted name contains valid characters.
     * Parses through the letters of the name and ensures each letter is an
     * alphabetic character. Any added member is not allowed to spaces,
     * special characters, or numbers in their name.
     * @param name the name inputted in the first or last name field.
     * @return true if all the characters are alphabetic letters, false
     * otherwise.
     */
    private boolean isAlpha(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c))
                return false;
        }
        return true;
    }

    /**
     * Checks if a given fitness class exists.
     * Checks if class exists on the fitness schedule by checking if the
     * combination of the selected fitness class, instructor, and location
     * matches that of one on the schedule of classes. If the class does not
     * exist on the schedule, the class is not valid.
     * @param className the fitness class name selected.
     * @param instructor the name of the instructor selected.
     * @param location the location that is selected.
     * @return true if the chosen class exists on the schedule, false
     * otherwise.
     */
    private boolean isClassValid(String className, String instructor,
                                 String location){
        FitnessClass fitnessClass = new FitnessClass(instructor, className,
                location);
        if(schedule.findFitnessClass(fitnessClass) == null){
            output.appendText(className + " by " + instructor + " does not"
                    + " exist at " + location + "\n");
            clearAllFieldsFitness();
            return false;
        }
        return true;
    }

    /**
     * Checks if a given member is a valid member.
     * Checks if a member exists in the gym's member database, or if the
     * membership has expired.
     * @param findMember the member searched for through the database.
     * @param checkMember the member who must be found in the database.
     * @return true if the member is in the database or their membership
     * is not expired, false otherwise.
     */
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

    /**
     * Displays the members in the member database.
     * @param event the event that indicates that the 'Print' button on the
     *              Membership Hub tab was clicked.
     */
    @FXML
    public void onPrintClicked(ActionEvent event){
        output.appendText(database.print());
    }

    /**
     * Displays the list of members in database ordered by county and zipcode.
     * @param event the event that indicates that the 'Print by
     *              County/Zipcode' button on the Membership Hub tab was
     *              clicked.
     */
    @FXML
    public void onPrintCountyClicked(ActionEvent event){
        output.appendText(database.printByCounty());
    }

    /**
     * Displays the list of members in database ordered last and first name.
     * @param event the event that indicates that the 'Print by Last/First
     *              Names' button on the Membership Hub tab was clicked.
     */
    @FXML
    public void onPrintNameClicked(ActionEvent event){
        output.appendText(database.printByName());
    }

    /**
     * Displays the list of members in database ordered by expiration date.
     * @param event the event that indicates that the 'Print by Expiration
     *              Date' button on the Membership Hub tab was clicked.
     */
    @FXML
    public void onPrintExpDateClicked(ActionEvent event){
        output.appendText(database.printByExpirationDate());
    }

    /**
     * Displays the list of members in database with their membership fees.
     * @param event the event that indicates that the 'Print by Membership
     *              Fee' button on the Membership Hub tab was clicked.
     */
    @FXML
    public void onPrintFeeClicked(ActionEvent event){
        output.appendText(database.printByMembershipFee());
    }

    /**
     * Adds members on the member list to the database.
     * @param event the event that indicates that the 'Load Memberlist From
     *              File' button on the Membership Hub tab was clicked.
     */
    @FXML
    public void onLoadMemberListClicked(ActionEvent event) {
        String file = "./javafx/src/main/java/com/example/" +
                "gymmembershipgui/memberList.txt";
        try {
            output.appendText(database.loadMemberList(file));
        } catch (FileNotFoundException e) {
            output.appendText("Member list file not found.");
        }

    }

    /**
     * Displays all the fitness classes on the schedule.
     * @param event the event that indicates that the 'Show All Classes'
     *              button on the Membership Hub tab was clicked.
     */
    @FXML
    public void showAllClasses(ActionEvent event){
        if(schedule.isEmpty()){
            output.appendText("There are no fitness classes " +
                    "loaded on the schedule.\n");
        } else {
            output.appendText(schedule.toString());
        }
    }

    /**
     * Adds classes on the class schedule list to the schedule of classes.
     * @param event the event that indicates that the 'Load ClassSchedule From
     *              File' button on the Membership Hub tab was clicked.
     */
    @FXML
    public void onLoadClassScheduleClicked(ActionEvent event){
        String file = "./javafx/src/main/java/com/example/" +
                "gymmembershipgui/classSchedule.txt";
        try {
            schedule.loadClassSchedule(file);
            output.appendText(schedule.toString());
        } catch (FileNotFoundException e) {
            output.appendText("Class schedule file not found.");
        }

    }

    /**
     * Initializes choice boxes on the GUI with corresponding elements.
     * Adds the location choices to the Location choice bar on the
     * Membership tab, the location choices to the Location choice bar on the
     * Fitness Class tab, the fitness choices to the Fitness choice bar on the
     * Fitness Class tab, and the instructor choices to the Instructor choice
     * bar on the Fitness Class tab.
     * @param url the url to the path of the root object.
     * @param resourceBundle used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        memberLocationChoiceBar.getItems().addAll(locations);
        classLocationChoiceBar.getItems().addAll(locations);
        fitnessChoiceBar.getItems().addAll(fitnessClasses);
        instructorChoiceBar.getItems().addAll(instructors);
    }
}