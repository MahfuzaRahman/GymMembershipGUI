package com.example.gymmembershipgui;

/**
 * Premium is defined by a number of guest passes and all Member attributes.
 * A Premium instance can retrieve the membership fee for a premium
 * membership, can use a guest pass, and check out a guest.
 * @author Mahfuza Rahman, Arunima Tripathy
 */
public class Premium extends Family{
    protected static final int ANNUALLY = 12;
    protected static final int FIRST_MONTH_FREE = 1;

    /**
     * Creates an instance of Premium with name, birthday, and location.
     * Defines Premium membership by adding guest passes.
     * @param fname the first name of member buying a Premium membership.
     * @param lname the last name of member buying a Premium membership.
     * @param dob the DOB name of member buying a Premium membership.
     * @param location the location that the member will exercise at.
     */
    public Premium(String fname, String lname, String dob, String location) {
        super(fname, lname, dob, location);
        guestPasses = 3;
    }

    /**
     * Creates an instance of Premium with name, DOB, expiration and location.
     * Defines Premium membership by adding guest passes.
     * @param fname the first name of member buying a Premium membership.
     * @param lname the last name of member buying a Premium membership.
     * @param dob the DOB name of member buying a Premium membership.
     * @param expire the expiration date of the membership.
     * @param location the location that the member will exercise at.
     */
    public Premium(String fname, String lname, String dob, String expire,
                   String location){
        super(fname, lname, dob, expire, location);
        guestPasses = 3;
    }

    /**
     * Calculates the membership fee.
     * If the membership has expired, fee is the quarterly cost of membership.
     * If the membership has not expired, fee is the quarterly cost and the
     * one-time fee combined.
     * @return the membership fee as a double.
     */
    @Override
    public double membershipFee() {
        if(membershipExpired())
            return ANNUALLY * FAMILY_MONTHLY_FEE;
        return (ANNUALLY - FIRST_MONTH_FREE) * FAMILY_MONTHLY_FEE;
    }

    /**
     * Sets the expiration date a year from the current date.
     * @return the expiration date as a Date object.
     */
    @Override
    public Date setExpire(){
        Date today = new Date();
        int newMonth = today.getMonth();
        int newYear = today.getYear() + 1;
        int newDay = today.getDay();

        Date expiration = new Date(newMonth + "/" + newDay + "/" + newYear);
        // it is february and date is invalid
        if (newMonth == 2 && !expiration.isValid()) {
            boolean isleapyear = expiration.isLeapYear();
            newMonth++;
            if (!isleapyear) {  //if it is not a leap year
                newDay = newDay % 28;
            } else {
                newDay = newDay % 29;
            }
            return new Date(newMonth + "/" + newDay + "/" + newYear);
        } else if (!expiration.isValid()) {
            newMonth++;
            newDay = 1;
            return new Date(newMonth + "/" + newDay + "/" + newYear);
        }
        return new Date(newMonth + "/" + newDay + "/" + newYear);
    }

    /**
     * Creates String with standard member plus guest pass info.
     * Includes first name, last name, DOB, expiration date, location info,
     * and the number of guest passes remaining.
     * @return a String with the member's information for a Premium plan.
     */
    @Override
    public String toString() {
        return (getFirstName() + " " + getLastName() + ", DOB: " +
                getDOB().toString() + ", Membership expires " +
                getExpire().toString() + ", Location: " + getLocation().name()
                + ", " +  getLocation().getZipCode() + ", " +
                getLocation().getCounty()) +
                ", (Premium) Guess-pass remaining: " + getNumberOfPasses();
    }
}
