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

    /**
     * Adds all the classes on class schedule list to the schedule.
     * @param fileName the name of the file containing all the classes to add.
     * @throws FileNotFoundException if the file is not found.
     */
    public void loadClassSchedule(String fileName) throws
            FileNotFoundException {
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
            if (classes[i] != null && classes[i].equals(fitnessClass))
                return classes[i];
        }
        return null;
    }

    /**
     * Searches for possible conflicting fitness classes.
     * Iterates through classes array. If a fitness class is at the same time
     * as the fitness class given, the class is added to the array containing
     * all conflicting fitness classes.
     * @param fitnessClass the fitness class that must be compared with other
     *                     classes.
     * @return FitnessClass[] containing any conflicting classes.
     */
    public FitnessClass[] findTimeConflict(FitnessClass fitnessClass) {
        FitnessClass[] conflictingClasses = new FitnessClass[numClasses-1];
        String classTime = fitnessClass.getTime();
        int index = 0;
        for(FitnessClass fc : classes) {
            if(fc != null && !fc.equals(fitnessClass) &&
                    fc.getTime().equalsIgnoreCase(classTime))
                conflictingClasses[index++] = fc;
        }
        return conflictingClasses;
    }

    /**
     * Gets the number of FitnessClass objects in the classes array.
     * @return the number of classes as an integer.
     */
    public int getSize() {
        return numClasses;
    }

    /**
     * Checks if the schedule is empty.
     * @return true if there are no classes on the schedule, false otherwise.
     */
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
        schedule += "-end of class list.\n\n";
        return schedule;
    }
}
