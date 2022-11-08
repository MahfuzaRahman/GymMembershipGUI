package com.example.gymmembershipgui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Defines a schedule with array of classes and int with num of classes.
 * For each ClassSchedule, a class can be added to the schedule, can find
 * a FitnessClass given the object or the name of the class, and check if
 * a time conflict exists between a member and a class they are already
 * checked into if they have another class they want to join.
 * @author Mahfuza Rahman, Arunima Tripathy
 */

public class ClassSchedule {
    private FitnessClass[] classes;
    private int numClasses;

    private final static int INITIAL_LENGTH = 4;
    private final static int INITIAL_SIZE = 0;

    /**
     * Creates instance of ClassSchedule with list of classes and array size.
     * The list of classes holds each fitness class added to the schedule with
     * an initial capacity of 4 classes. As a class is added, the number of
     * classes in the schedule is adjusted.
     */
    public ClassSchedule(){
        classes = new FitnessClass[INITIAL_LENGTH];
        numClasses = INITIAL_SIZE;
    }

    public void loadClassSchedule(String fileName) throws
            FileNotFoundException{
        File file = new File(fileName);
        Scanner infile = new Scanner(file);
        String input;
        int classIndex = 0;
        int instructorIndex = 1;
        int timeIndex = 2;
        int locationIndex = 3;

        while(infile.hasNextLine()) {
            input = infile.nextLine();
            if(!input.equals("")) {
                String[] arrInput = input.split(" ", 0);
                FitnessClass fitnessClass = new FitnessClass(
                        arrInput[classIndex], arrInput[instructorIndex],
                        arrInput[timeIndex], arrInput[locationIndex]);
                addFitnessClass(fitnessClass);
            }
        }
    }

    /**
     * Adds a new fitness class to the schedule of classes.
     * Creates a new fitness class with the class name, instructor, time, and
     * location. If the classes array is filled to capacity, the array is
     * grown. Then, if the created fitness class is already in the schedule,
     * it is not added. Otherwise, the class is added to the schedule.
     * @param fitnessClass
     * @return true if the class is added, false if already present.
     */
    private boolean addFitnessClass(FitnessClass fitnessClass){
        if (numClasses == classes.length)
            growSchedule();
        if(findFitnessClass(fitnessClass) != null)
            return false;
        classes[numClasses++] = fitnessClass;
        return true;
    }

    /**
     * Increases the capacity of the schedule.
     * Iterates through a temporary array that is 4 greater in capacity
     * than the current classes array. Then, the temporary array copies the
     * classes in the classes array. Then, the classes is reset to the
     * temporary array so that the schedule holds a greater capacity of
     * fitness classes.
     */
    private void growSchedule(){
        FitnessClass[] tempClasses = new FitnessClass[classes.length + 4];
        for (int i = 0; i < numClasses; i++) {
            tempClasses[i] = classes[i];
        }
        classes = tempClasses;
    }

    /**
     * Given a fitness class, the corresponding class from schedule is found.
     * Iterates through the classes array. If any scheduled fitness classes
     * are equal to the inputted fitness class, it is found. Otherwise, it is
     * not found.
     * @param fitnessClass the fitness class that is to be located.
     * @return FitnessClass class if found, null otherwise.
     */
    public FitnessClass findFitnessClass(FitnessClass fitnessClass){
        for (int i = 0; i < classes.length; i++) {
            if (classes[i] != null && classes[i].equals(fitnessClass)){
                return classes[i];}
        }
        return null;
    }

    /**
     * Checks if a class exists on the fitness class schedule.
     * Iterates through the classes array. Checks if any of the scheduled
     * fitness classes match the given fitness class.
     * @param className the name of the FitnessClass to be found.
     * @return true if class is found, false otherwise.
     */
//    public boolean findFitnessClass(String className){
//        for (int i = 0; i < numClasses; i++) {
//            if (classes[i].getClassName().equalsIgnoreCase(className))
//                return true;
//        }
//        return false;
//    }

    /**
     * Checks a member in to a fitness class on the schedule.
     * If the fitness class exists and there are no time conflicts with other
     * fitness classes, the member is added to the list of participants in the
     * fitness class.
     * @param fClass the name of the fitness class.
     * @param location the location of the fitness class.
     * @param instructor the instructor of the fitness class.
     * @param addMember the member to be checked into the fitness class.
     * @return true if the member is checked in, false otherwise.
     */
    public String checkInMember(String fClass, String location,
                                 String instructor, Member addMember){
        return checkFitnessClass(fClass, location, instructor, addMember);
    }

    /**
     * Checks if a class with the inputted parameters exists.
     * Creates a FitnessClass object using the fClass, location, instructor.
     * Using this object, searches schedule for the equivalent FitnessClass.
     * If a class is not found, that means no class with the given combination
     * of name, teacher, and location exist. Also checks if the member is
     * already present in the class they want to attend. Once the class is
     * found and the member is not already checked into it, a check for time
     * conflicts with other classes occurs.
     * @param fClass the name of the class a user wants to attend as a String.
     * @param location the location of the class as a String.
     * @param instructor the instructor of the class as a String.
     * @param addMember the member to be added to a class as a Member object.
     */
//    private boolean checkFitnessClass(String fClass, String location,
//                                   String instructor, Member addMember) {
//        FitnessClass findClass = new FitnessClass(fClass, instructor,
//                "", location);
//        FitnessClass foundClass = findFitnessClass(findClass);
//
//        if(foundClass == null) {
//            System.out.println(fClass + " by " + instructor + " does not " +
//                    "exist at " + location);
//            return false;
//        }
//
//        Member inClassAlready = foundClass.findMember(addMember);
//        if(inClassAlready != null) {
//            System.out.println(addMember.getFirstName() + " " +
//                    addMember.getLastName() + " already checked in.");
//            return false;
//        }
//        return checkTimeConflict(foundClass, addMember);
//    }
//    private String checkFitnessClass(String fClass, String location,
//                                      String instructor, Member addMember) {
//        FitnessClass findClass = new FitnessClass(fClass, instructor,
//                "", location);
//        FitnessClass foundClass = findFitnessClass(findClass);
//
//        String returnVal;
//
//        if(foundClass == null) {
//            returnVal = (fClass + " by " + instructor + " does not " +
//                    "exist at " + location);
//            return returnVal;
//        }
//
//        Member inClassAlready = foundClass.findMember(addMember);
//        if(inClassAlready != null) {
//            returnVal = (addMember.getFirstName() + " " +
//                    addMember.getLastName() + " already checked in.");
//            return returnVal;
//        }
//        return checkTimeConflict(foundClass, addMember);
//    }

    /**
     * Checks if a time conflict exists between a member and a class.
     * Then, the time of the fitness class is found and a time conflict is
     * searched for. If a conflict is not found, the member is checked into
     * the class.
     * @param course the FitnessClass a member wants to attend.
     * @param member the member that wants to be added to a class.
     */
//    private boolean checkTimeConflict(FitnessClass course, Member member) {
//        String timeOfCourse = course.getTime();
//        FitnessClass isConflict = findTimeConflict(timeOfCourse,
//                member);
//        if(isConflict != null) {
//            System.out.println("Time conflict - " + course.getClassName() +
//                    " - " + course.getInstructorName() + ", " +
//                    course.getTime() + ", " +
//                    Location.getLocation(course.getLocation()));
//            return false;
//        }
//        course.checkInMember(member);
//        System.out.print(member.getFirstName() + " " + member.getLastName() +
//                " checked in ");
//        System.out.println(course + "\n");
//        return true;
//    }

    private String checkTimeConflict(FitnessClass course, Member member) {
        String timeOfCourse = course.getTime();
        FitnessClass isConflict = findTimeConflict(timeOfCourse,
                member);
        String returnVal;
        if(isConflict != null) {
            returnVal = "Time conflict - " + course.getClassName() +
                    " - " + course.getInstructorName() + ", " +
                    course.getTime() + ", " +
                    Location.getLocation(course.getLocation());
            return returnVal;
        }
        course.checkInMember(member);
        returnVal = member.getFirstName() + " " + member.getLastName() +
                " checked in " + course + "\n";
        return returnVal;
    }


    /**
     * Checks a guest into a fitness class.
     * Prints the class the guest is checked into.
     * @param guest the member who must have a Family or Premium membership
     *              and whose guest must be checked into a fitness class
     * @param fitClass the class the guest wants to check into.
     * @return true when the guest is checked in.
     */
//    public String checkInGuest(Family guest, FitnessClass fitClass){
//        FitnessClass fitnessClass = findFitnessClass(fitClass);
//        fitnessClass.checkInGuest(guest);
//        String output;
//        output = (guest.getFirstName() + " " +
//                guest.getLastName() + " (guest) checked in " +
//                fitnessClass.getClassName() + " - " +
//                fitnessClass.getInstructorName() + ", " +
//                fitnessClass.getTime() + ", " + fitnessClass.getLocation());
//        output += (fitnessClass.getClassParticipantsAndGuests());
//        return output;
//    }


    /**
     * Searches for a possible conflicting fitness class.
     * Iterates through classes array. If a fitness class is at the same time
     * as the time given and the member is in the class, there is a time
     * conflict.
     * @param time the time of the class as a String object.
     * @param member the member to find a conflict for.
     * @return FitnessClass if time conflict exists, null otherwise.
     */
    private FitnessClass[] findTimeConflict(String time) {
        FitnessClass[] conflictingClasses = new FitnessClass[numClasses];

        for(FitnessClass fc : classes) {
            if(fc != null && fc.getTime().equalsIgnoreCase(time)) {
                conflictingClasses;
            }
        }
        return null;
    }

    /**
     * Gets the number of FitnessClass objects in the classes array.
     * @return the number of classes as an integer.
     */
    public int getSize() {
        return numClasses;
    }

    public boolean isEmpty(){
        return numClasses == 0;
    }

    /**
     * Creates a String that contains all fitness classes on the schedule.
     * @return String containing fitness classes and their details.
     */
    @Override
    public String toString() {
        String schedule = "-Fitness classes loaded-\n";
        for(FitnessClass fc : classes) {
            if(fc != null)
                schedule += fc + "\n";
        }
        schedule += "-end of class list.\n";
        return schedule;
    }
}
