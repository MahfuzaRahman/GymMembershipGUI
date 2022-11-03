package com.example.gymmembershipgui;

/**
 * Family is defined by a number of guest passes and all Member attributes.
 * A Family instance can retrieve the membership fee for a family membership,
 * can use a guest pass, and check out a guest.
 * @author Mahfuza Rahman, Arunima Tripathy
 */
public class Family extends Member {
    protected int guestPasses;
    protected static final double FAMILY_MONTHLY_FEE = 59.99;

    /**
     * Creates an instance of Family with name, birthday, and location.
     * Defines Family membership by adding guest passes.
     * @param fname the first name of member buying a Family membership.
     * @param lname the last name of member buying a Family membership.
     * @param dob the DOB name of member buying a Family membership.
     * @param location the location that the member will exercise at.
     */
    public Family(String fname, String lname, String dob, String location) {
        super(fname, lname, dob, location);
        guestPasses = 1;
    }

    /**
     * Creates an instance of Family with name, DOB, expiration and location.
     * Defines Family membership by adding guest passes.
     * @param fname the first name of member buying a Family membership.
     * @param lname the last name of member buying a Family membership.
     * @param dob the DOB name of member buying a Family membership.
     * @param expire the expiration date of the membership.
     * @param location the location that the member will exercise at.
     */
    public Family(String fname, String lname, String dob, String expire,
                  String location) {
        super(fname, lname, dob, expire, location);
        guestPasses = 1;
    }

    /**
     * Calculates the membership fee.
     * If the membership has expired, fee is the quarterly cost of membership.
     * If the membership has not expired, fee is the quarterly cost
     * and the one-time fee combined.
     * @return the membership fee as a double.
     */
    @Override
    public double membershipFee() {
        if(membershipExpired())
            return QUARTERLY * FAMILY_MONTHLY_FEE;
        return STANDARD_ONE_TIME_FEE + QUARTERLY * FAMILY_MONTHLY_FEE;
    }

    /**
     * Use one Family guest pass.
     * Decrements the number of guest passes.
     */
    public void useGuestPass(){
        guestPasses--;
    }

    /**
     * Check out a guest from a class.
     * Increments the number of guest passes.
     */
    public void checkoutGuest(){
        guestPasses++;
    }

    /**
     * Gets the number of passes the Family membership has.
     * @return the number of passes as an integer.
     */
    public int getNumberOfPasses(){
        return guestPasses;
    }

    /**
     * Creates String with name, DOB, expiration, location, and guest passes.
     * Calls the superclass method for the first and last name, DOB,
     * expiration, and location information and adds on the number of family
     * guest-passes remaining.
     * @return a String with the member's information for a Family plan.
     */
    @Override
    public String toString() {
        return super.toString() + ", (Family) guest-pass remaining: " +
                getNumberOfPasses();
    }

}