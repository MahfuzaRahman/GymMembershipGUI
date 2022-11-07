package com.example.gymmembershipgui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * GymManager manages a member database as well as a class schedule.
 * In each gym, the gym manager can add a member to the database as long as
 * their membership is valid. If a member wishes to be removed,
 * the gym manager can remove them from the database. Members can also
 * check in to different classes. They can also drop a class if they
 * would like to. If a member's gym plan permits them, these functions
 * also apply to guests. GymManager's schedule can also check for time
 * conflicts between the classes a member is checked into and a class they
 * want to attend. The gym manager handles all these requests.
 * @author Mahfuza Rahman, Arunima Tripathy
 */
public class GymManager {
    private MemberDatabase database;
    private ClassSchedule schedule;

    /**
     * Creates an instance of GymManager with a database and a class schedule.
     * Generates a schedule of fitness classes for all the gyms managed and
     * a database that holds all the members of all the gyms.
     */
    public GymManager() {
        database = new MemberDatabase();
        schedule = new ClassSchedule();
    }

    /**
     * Takes command line input to add a member to the member database.
     * An instance of Member, Family, or Premium is created depending on the
     * information provided. As long as the DOB is valid, not a future date,
     * and the member is 18 or older, the expiration date and location are
     * valid, and the member is not already in the database, member is added.
     * @param input a String with member name, DOB, expiration, and location.
     */
    public void add(String input) {
        String[] arrInput = input.split(" ", 0);
        Member addMember;
        if(arrInput[0].equals("A")) {
            addMember = new Member(arrInput[1], arrInput[2], arrInput[3],
                    arrInput[4]);
        }
        else if(arrInput[0].equals("AF")) {
            addMember = new Family(arrInput[1], arrInput[2], arrInput[3],
                    arrInput[4]);
        }
        else{
            Date today = new Date();
            addMember = new Premium(arrInput[1], arrInput[2], arrInput[3],
                    today.toString(), arrInput[4]);
        }
        if(!isDOBValid(addMember))
            return;
        if(!isExpirationDateValid(addMember))
            return;
        if(addMember.getLocation() == null) {
            System.out.println(arrInput[4] + ": invalid location!");
            return;
        }
        if(!database.add(addMember)) {
            System.out.println(addMember.getFirstName() + " " +
                    addMember.getLastName() + " is already in the " +
                    "database.");
            return;
        }
        System.out.println(addMember.getFirstName() + " " +
                addMember.getLastName() + " added.");
    }

    /**
     * Takes command line input to remove a member from the member database.
     * If the member is present in the database, they are removed. Otherwise,
     * a message is sent to the user.
     * @param input a String with member's first and last name, and DOB.
     */
    public void remove(String input) {
        String[] arrInput = input.split(" ", 0);
        Member removeThem = new Member(arrInput[1], arrInput[2], arrInput[3]);
        if(!database.remove(removeThem)) {
            System.out.println(removeThem.getFirstName() + " " +
                    removeThem.getLastName() + " is not in the database.");
            return;
        }
        System.out.println(removeThem.getFirstName() + " " +
                removeThem.getLastName() + " removed.");
    }

    /**
     * Takes command line input to review if a member can check in to a class.
     * Checks whether the inputted fitness class exists, if the instructor
     * exists, the location is valid, the membership has not expired,
     * the member is present in the database, and the member is checking into
     * their assigned gym location, if membership type has this restriction.
     * If all these requirements are fulfilled checkFitnessClass() is called.
     * @param input a String with member's first and last name, class, DOB,
     *              fitness class name, instructor, and location.
     */
    public void checkIn(String input) {
        String[] arrInput = input.split(" ", 0);
        String fitnessClass = arrInput[1];
        String instructor = arrInput[2];
        String location = arrInput[3];

        Member checkMember = new Member(arrInput[4], arrInput[5],
                arrInput[6]);
        Member findMember = database.findMember(checkMember);
        if(!isClassValid(fitnessClass, instructor, location))
            return;
        if(!isDOBValid(checkMember))
            return;
        if(!isMemberValid(findMember, checkMember))
            return;
        if(!findMember.getLocation().name().equalsIgnoreCase(location) &&
                !(findMember instanceof Family)) {
            Location tryCheckIn = Location.getLocation(location);
            System.out.println(findMember.getFirstName() + " " +
                    findMember.getLastName() + " checking in " +
                    tryCheckIn.name() + ", " + tryCheckIn.getZipCode() + ", "
                    + tryCheckIn.getCounty() + " - standard membership " +
                    "location restriction.");
            return;
        }
        schedule.checkInMember
                (fitnessClass, location, instructor, findMember);
    }

    /**
     * Takes command line input to review if a guest can check in to a class.
     * Checks whether the guest is using the family or premium plan, if the
     * class they want to attend is the same as the membership location, and
     * if the member account has a spare guest pass to use. If these
     * requirements are fulfilled, the guest is checked in.
     * @param input a String with guest's first and last name, class, DOB,
     *              fitness class name, instructor, and location.
     */
//    public void checkInGuest(String input) {
//        String[] arrInput = input.split(" ", 0);
//        String className = arrInput[1];
//        String instructor = arrInput[2];
//        String location = arrInput[3];
//        FitnessClass fitClass = new FitnessClass(instructor, className,
//                location);
//
//        Member checkMember = new Member(arrInput[4], arrInput[5],
//                arrInput[6]);
//        Member findMember = database.findMember(checkMember);
//
//        if(!(findMember instanceof Family)){
//            System.out.println("Standard membership - guest check-in is " +
//                    "not allowed.");
//            return;
//        }
//        if(!findMember.getLocation().name().equalsIgnoreCase(location)) {
//            Location tryCheckIn = Location.getLocation(location);
//            System.out.println(findMember.getFirstName() + " " +
//                    findMember.getLastName() + " Guest checking in " +
//                    tryCheckIn.name() + ", " + tryCheckIn.getZipCode() + ", "
//                    + tryCheckIn.getCounty() + " - guest location " +
//                    "restriction.");
//            return;
//        }
//
//        if(((Family) findMember).getNumberOfPasses() == 0){
//            System.out.println(findMember.getFirstName() + " " +
//                    findMember.getLastName() + " ran out of guest pass.");
//            return;
//        }
//        schedule.checkInGuest((Family) findMember, fitClass);
//    }

    /**
     * Checks if the date of birth is a valid date.
     * Checks if date is a valid, calendar date, not today or a future date,
     * and that the member is 18 years or older.
     * @param member the member that wants to be added or check in to a class.
     * @return true if the DOB is valid, false otherwise.
     */
    private boolean isDOBValid(Member member){
        if(!member.getDOB().isValid()) {
            System.out.println("DOB " + member.getDOB().toString() +
                    ": invalid calendar date!");
            return false;
        }
        if(member.getDOB().compareTo(new Date()) >= 0) {
            System.out.println("DOB " + member.getDOB().toString() +
                    ": cannot be today or a future date!");
            return false;
        }
        if(!member.aboveEighteen()) {
            System.out.println("DOB " + member.getDOB().toString() +
                    ": must be 18 or older to join!");
            return false;
        }
        return true;
    }

    /**
     * Checks if a given fitness class exists.
     * Checks if class exists on the fitness schedule by checking if the
     * class name matches that of any on the schedule, if the instructor
     * is an existing instructor, and if the location is an existing location.
     * @param fitnessClass the name of a fitness class.
     * @param instructor the name of the instructor.
     * @param location the location of the fitness class.
     * @return true if class is valid, false otherwise.
     */
    private boolean isClassValid(String fitnessClass, String instructor,
                                 String location){
        if(!schedule.findFitnessClass(fitnessClass)) {
            System.out.println(fitnessClass + " - class does not exist.");
            return false;
        }
        if(!Instructors.isInstructor(instructor)) {
            System.out.println(instructor + " - instructor does not exist.");
            return false;
        }
        if(Location.getLocation(location) == null) {
            System.out.println(location + " - invalid location.");
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
            System.out.println(checkMember.getFirstName() + " " +
                    checkMember.getLastName() + " " + checkMember.getDOB() +
                    " is not in the database.");
            return false;
        }
        if(findMember.membershipExpired()) {
            System.out.println(findMember.getFirstName() + " " +
                    findMember.getLastName() + " " + findMember.getDOB() +
                    " membership expired.");
            return false;
        }
        return true;
    }


    /**
     * Checks if the expiration is a valid date.
     * Checks if date is a calendar date and if membership is not expired.
     * @param member the member that wants to be added or check in to a class.
     * @return true if the expiration date is valid, false otherwise.
     */
    private boolean isExpirationDateValid(Member member){
        if(!member.getExpire().isValid()) {
            System.out.println("Expiration date " +
                    member.getExpire().toString() +
                    ": invalid calendar date!");
            return false;
        }
        return true;
    }

    /**
     * Displays the fitness class schedule with class instructor and time.
     */
    public void schedule() {
        System.out.println(schedule + "-end of class list.\n");
    }

    /**
     * Checks a member out of a fitness class.
     * Checks whether the class exists, the instructor exists, the location
     * exists, that the member is present in the database, that the
     * combination of the class name, instructor, and location is a valid
     * class, and the member checked into the class in the first place. If
     * these requirements are fulfilled, the member is removed from the
     * fitness class.
     * @param input a String with member's first and last name, class, DOB,
     *              fitness class name, instructor, and location.
     */
    public void doneWithClass(String input) {
        String[] arrInput = input.split(" ", 0);
        String fitnessClass = arrInput[1];
        String instructor = arrInput[2];
        String location = arrInput[3];

        Member checkMember = new Member(arrInput[4], arrInput[5],
                arrInput[6]);
        Member findMember = database.findMember(checkMember);

        if(!isClassValid(fitnessClass, instructor, location))
            return;
        if(!isDOBValid(checkMember))
            return;
        if(findMember == null) {
            System.out.println(checkMember.getFirstName() + " " +
                    checkMember.getLastName() + " " + checkMember.getDOB() +
                    " is not in the database.");
            return;
        }
        FitnessClass foundFitClass = schedule.findFitnessClass
                (new FitnessClass(instructor, fitnessClass, location));
        if(foundFitClass == null) {
            System.out.println(fitnessClass + " by " + instructor +
                    " does not exist at " + location);
            return;
        }
        if(foundFitClass.findMember(findMember) == null) {
            System.out.println(findMember.getFirstName() + " " +
                    findMember.getLastName() + " did not check in.");
            return;
        }
        foundFitClass.removeMember(findMember);
        System.out.println(findMember.getFirstName() + " " +
                findMember.getLastName() + " done with the class.");
    }

    /**
     * Checks a guest out of a class.
     * Checks whether the inputted class exists, the instructor exists,
     * the location exists, that the membership is present in the database,
     * and that the combination of the class, instructor, and location is a
     * valid class. If these requirements are fulfilled, the guest is removed
     * from the FitnessClass and the guest pass is incremented by one.
     * @param input a String with member's first and last name, class, DOB,
     *              fitness class name, instructor, and location.
     */
    public void doneWithGuestClass(String input) {
        String[] arrInput = input.split(" ", 0);
        String fitnessClass = arrInput[1];
        String instructor = arrInput[2];
        String location = arrInput[3];

        Member checkMember = new Member(arrInput[4], arrInput[5],
                arrInput[6]);
        Member findMember = database.findMember(checkMember);

        if(!isClassValid(fitnessClass, instructor, location))
            return;
        if(!isDOBValid(checkMember))
            return;
        if(findMember == null) {
            System.out.println(checkMember.getFirstName() + " " +
                    checkMember.getLastName() + " " + checkMember.getDOB() +
                    " is not in the database.");
            return;
        }
        FitnessClass foundFitClass = schedule.findFitnessClass
                (new FitnessClass(instructor, fitnessClass, location));
        if(foundFitClass == null) {
            System.out.println(fitnessClass + " by " + instructor + " does " +
                    "not exist at " + location);
            return;
        }
        foundFitClass.removeGuest((Family) findMember);
        System.out.println(findMember.getFirstName() + " " +
                findMember.getLastName() + " Guest done with the class.");
    }

    /**
     * Loads the classSchedule.txt file into the schedule.
     * If the file is found, each line in the file is read and used to add a
     * FitnessClass to schedule. If not, an exception is thrown.
     * @throws FileNotFoundException to inputCommand method.
     */
    public void loadSchedule() throws FileNotFoundException {
        File file = new File("src/classSchedule.txt");
        Scanner infile = new Scanner(file);
        String input;

        while(infile.hasNextLine()) {
            input = infile.nextLine();
            String[] arrInput = input.split(" ", 0);
            schedule.addFitnessClass(arrInput[0], arrInput[1], arrInput[2],
                    arrInput[3]);
        }
        System.out.println("\n-Fitness classes loaded-");
        schedule();
    }

    /**
     * Loads the memberList.txt file into the database.
     * If the file is found, each line in the file is read and used to add
     * a member to the database. If not, an exception is thrown.
     * @throws FileNotFoundException to inputCommand method.
     */
    public void loadMembers() throws FileNotFoundException {
        File file = new File("src/memberList.txt");
        Scanner infile = new Scanner(file);
        String input;

        while(infile.hasNextLine()) {
            input = infile.nextLine();
            String[] arrInput = input.split("\\s+", 0);
            Member addMember = new Member(arrInput[0], arrInput[1],
                    arrInput[2], arrInput[3], arrInput[4]);
            database.add(addMember);
        }
        System.out.println("-list of members loaded-");
        System.out.println(database + "-end of list-\n");
    }

    /**
     * Handles each command line operation in the Gym Manager.
     * Depending on the command, a member can be added or removed from the
     * database, and checked in or dropped from a fitness class.
     * Guests can also be checked in or dropped from a fitness class.
     * The database can be sorted by name, expiration date, DOB, or fee.
     * It can also handle exceptions thrown by loadSchedule and
     * loadMember. It also terminates the Gym Manager.
     * @param input the command line operation to perform.
     */
    public void inputCommand(String input) {
        String command = input.length() == 1 ? input : input.substring(0, 2);
        switch(command){
            case "A ":
                add(input);
                break;
            case "AF":
                add(input);
                break;
            case "AP":
                add(input);
                break;
            case "R ":
                remove(input);
                break;
            case "P":
                if(!isDatabaseEmptyMessage())
                    database.print();
                break;
            case "PC":
                if(!isDatabaseEmptyMessage())
                    database.printByCounty();
                break;
            case "PN":
                if(!isDatabaseEmptyMessage())
                    database.printByName();
                break;
            case "PD":
                if(!isDatabaseEmptyMessage())
                    database.printByExpirationDate();
                break;
            case "PF":
                if(!isDatabaseEmptyMessage())
                    database.printByMembershipFee();
                break;
            case "S":
                if(!isScheduleEmptyMessage()){
                    System.out.println("\n-Fitness classes-");
                    schedule();}
                break;
            case "C ":
                checkIn(input);
                break;
            case "CG":
                //checkInGuest(input);
                break;
            case "D ":
                doneWithClass(input);
                break;
            case "DG":
                doneWithGuestClass(input);
                break;
            case "Q":
                break;
            case "LS":
                try {
                    loadSchedule();
                }
                catch(FileNotFoundException e) {
                    System.out.println("Schedule file not found");
                }
                break;
            case "LM":
                try {
                    loadMembers();
                }
                catch(FileNotFoundException e) {
                    System.out.println("Member list file not found");
                }
                break;
            default:
                System.out.println(input + " is an invalid command!");
        }
    }

    /**
     * Displays a message informing the user if the database is empty.
     * @return true if database is empty, false otherwise.
    */
    private boolean isDatabaseEmptyMessage() {
        if(database.getSize() == 0) {
            System.out.println("Member database is empty!");
            return true;
        }
        return false;
    }

    /**
     * Displays a message informing the user if the schedule is empty.
     * @return true if schedule is empty, false otherwise.
     */
    private boolean isScheduleEmptyMessage() {
        if(schedule.getSize() == 0) {
            System.out.println("Fitness class schedule is empty.");
            return true;
        }
        return false;
    }

    /**
     * Reads and handles command line operations.
     */
    public void run() {
        System.out.println("Gym Manager running...");
        Scanner myScanner;
        String input = "";

        do {
            myScanner = new Scanner(System.in);
            while(myScanner.hasNextLine()) {
                input = myScanner.nextLine();
                if(input.length() > 0)
                    inputCommand(input);
                else
                    System.out.println();
                if(input.equals("Q"))
                    break;
            }
        } while(!input.equals("Q"));
        System.out.println("Gym Manager terminated.");
    }
}
