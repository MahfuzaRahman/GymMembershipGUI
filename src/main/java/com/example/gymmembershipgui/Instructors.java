package com.example.gymmembershipgui;

/**
 * Instructor defines the five teachers who teach fitness classes.
 * Given the name of a teacher, the corresponding instructor is found.
 * @author Mahfuza Rahman, Arunima Tripathy
 */
public enum Instructors {
    JENNIFER,
    KIM,
    DENISE,
    DAVIS,
    EMMA;

    /**
     * Creates an instance of instructors
     */
    Instructors() {}

    /**
     * Given the instructor name as a String, checks if teacher exists.
     * @param findTeacher the instructor to look for, as a String.
     * @return true if the name is a valid name, false otherwise.
     */
    public static boolean isInstructor(String findTeacher) {
        Instructors teachers[] = Instructors.values();
        for(Instructors teach : teachers) {
            if(teach.name().equalsIgnoreCase(findTeacher))
                return true;
        }
        return false;
    }
}
