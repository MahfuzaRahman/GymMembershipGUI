package com.example.gymmembershipgui;

import java.util.ArrayList;

/**
 * FitnessClass is defined by attendees, instructor, name, time, and location.
 * For each fitness class, members and guests can be checked-in through the
 * class's database. Members and guests can also be dropped from the class.
 * Additionally, if either member or guest needs to be found in the class,
 * they can be searched for through the participants and guests.
 * @author Mahfuza Rahman, Arunima Tripathy
 */

public class FitnessClass {
    private ArrayList<Member> participants;
    private ArrayList<Family> guests;
    private String instructorName;
    private String className;
    private Time classTime;
    private Location location;

    /**
     * Creates an instance of FitnessClass.
     * @param instructorName the name of the instructor for fitness class.
     * @param className the name of the class.
     * @param location the location at which this class runs.
     */
    public FitnessClass(String instructorName, String className,
                        String location) {
        participants = null;
        this.instructorName = instructorName.toUpperCase();
        this.className = className.toUpperCase();
        this.location = Location.getLocation(location);
        this.classTime = null;
    }

    /**
     * Creates an instance of FitnessClass.
     * @param className the name of the class.
     * @param instructorName the name of the instructor for this class.
     * @param classTime the time the class takes place.
     * @param location the location at which this class runs.
     */
    public FitnessClass(String className, String instructorName,
                        String classTime, String location){
        participants = new ArrayList<>();
        guests = new ArrayList<>();
        this.instructorName = instructorName.toUpperCase();
        this.className = className.toUpperCase();
        this.classTime = Time.getTimeEnum(classTime);
        this.location = Location.getLocation(location);
    }

    /**
     * Gets the instructor's name.
     * @return the name of the instructor as a String.
     */
    public String getInstructorName() {
        return instructorName;
    }

    /**
     * Gets the class name.
     * @return the name of the class as a String.
     */
    public String getClassName(){
        return className;
    }

    /**
     * Gets the location of the class.
     * @return the location of the class as a String.
     */
    public String getLocation(){
        return location.name();
    }

    /**
     * Gets the time the class takes place.
     * @return the time as a String.
     */
    public String getTime()
    {
        return classTime.getTime();
    }

    /**
     * Checks in a member to the fitness class.
     * Adds a member to the list of participants.
     * @param member the member that needs to be checked in.
     * @return true if member is added, false if member is already checked in.
    */
    public boolean checkInMember(Member member){
        if(participants.contains(member))
            return false;
        if((!(member instanceof Family)) && member.getLocation() !=
                Location.getLocation(getLocation()))
            return false;
        if(!member.aboveEighteen())
            return false;
        if(!member.getDOB().isValid())
            return false;
        if(member.membershipExpired())
            return false;
        return participants.add(member);
    }

    /**
     * Checks in a guest to the fitness class.
     * Uses the guest pass for the family membership passed as a parameter.
     * Then, adds the guest to the list of guests in the class.
     * @param guest the guest that is being added to the fitness class.
     * @return true once the member is added to guests arrayList.
     */
    public boolean checkInGuest(Family guest){
        if(guest.getNumberOfPasses() == 0)
            return false;
        guest.useGuestPass();
        return guests.add(guest);
    }

    /**
     * Finds a member in the fitness class.
     * Checks if the member is already in the fitness class. If member is in
     * the class, they are found in the list of participants.
     * @param member the member that needs to be searched for.
     * @return Member if member is found, null otherwise.
     */
    public Member findMember(Member member) {
        int memberIndex = participants.indexOf(member);
        if(memberIndex == -1)
            return null;
        return participants.get(memberIndex);
    }

    /**
     * Deletes a member from the fitness class.
     * Member is removed from the list of participants in the class.
     * @param member the member that is being removed.
     * @return true if member has been removed, false if member not found.
     */
    public boolean removeMember(Member member) {
        if(!participants.contains(member))
            return false;
        return participants.remove(member);
    }

    /**
     * Checks out a guest from the fitness class.
     * A guest is removed from the list of guests in the class.
     * @param guest the guest to be removed from the fitness class.
     * @return true if guest is removed, false otherwise.
     */
    public boolean removeGuest(Family guest) {
        if(!guests.contains(guest))
            return false;
        guest.checkoutGuest();
        return guests.remove(guest);
    }

    /**
     * Checks if two instances of FitnessClass are equal.
     * If the className, instructorName, and location of the two
     * FitnessClasses are equal, then the two are equal.
     * @param obj the FitnessClass instance to be cast as a FitnessClass.
     * @return true if the mentioned attributes match, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        FitnessClass checkClass = FitnessClass.class.cast(obj);
        if(!className.equalsIgnoreCase(checkClass.getClassName())) {
            return false;
        }
        if(!instructorName.equalsIgnoreCase(checkClass.getInstructorName())) {
            return false;
        }
        if(!location.name().equals(checkClass.getLocation())) {
            return false;
        }
        return true;
    }

    /**
     * Gets the participants and guests of a class.
     * Iterates through the list of participants and adds the information of
     * each individual to body. If body has a length greater than 1, it is
     * given a header. Then iterates through the guests array if the size of
     * the array is greater than 0.
     * @return String of participant and guest information.
     */
    public String getClassParticipantsAndGuests(){
        String body = "";
        String header = "";
        for(int i = 0; i < participants.toArray().length; i++)
            body += "\n\t" + participants.toArray()[i];
        if(body.length() > 0){
            header += "- Participants -";
            body += "\n";
        }
        if(guests.size() > 0)
            body += "- Guests -";
        for(int i = 0; i < guests.toArray().length; i++)
            body += "\n\t" + guests.toArray()[i];
        return (header + body + "\n");
    }

    /**
     * Creates a String to display the fitness class attributes.
     * Includes class name, instructor name, class time, and members.
     * @return a String describing all the attributes of a fitness class.
     */
    @Override
    public String toString() {
        String header = (className + " - " + instructorName + ", "
                + classTime.getTime() + ", " + getLocation());
        String body = "";
        for(int i = 0; i < participants.toArray().length; i++)
            body += "\n\t" + participants.toArray()[i];
        if(body.length() > 0)
            header += "\n- Participants -";
        if(guests.size() > 0)
            body += "\n- Guests -";
        for(int i = 0; i < guests.toArray().length; i++)
            body += "\n\t" + guests.toArray()[i];
        return (header + body);
    }
}
