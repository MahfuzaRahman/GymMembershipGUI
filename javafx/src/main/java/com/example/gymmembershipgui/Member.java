package com.example.gymmembershipgui;

/**
 * Member defines members given a name, birthday, expiration, and location.
 * A member can be checked to see if they are 18 years of age or older. Their
 * membership expiration dates can also be checked to see if their membership
 * has expired. Members can also be compared based on their name, and they can
 * be checked to see if they are equal to another member based on their name
 * and date of birth.
 * @author Arunima Tripathy, Mahfuza Rahman
 */
public class Member implements Comparable<Member> {
    private String fname;
    private String lname;
    private Date dob;
    private Date expire;
    private Location location;

    protected static final double STANDARD_ONE_TIME_FEE = 29.99;
    private static final double STANDARD_MONTHLY_FEE = 39.99;
    protected static final double QUARTERLY = 3;
    protected static final int MONTHS_IN_A_YEAR = 12;

    /**
     * Creates instance of Member with name, DOB, location.
     * @param fname the member's first name.
     * @param lname the member's last name.
     * @param dob the member's date of birth in mm/dd/yyyy format.
     * @param location the member's gym location.
     */
    public Member(String fname, String lname, String dob, String location) {
        this.fname = fname.substring(0,1).toUpperCase() +
                fname.substring(1).toLowerCase();
        this.lname = lname.substring(0,1).toUpperCase() +
                lname.substring(1).toLowerCase();
        this.dob = new Date(dob);
        this.expire = setExpire();
        this.location = Location.getLocation(location);
    }

    /**
     * Creates instance of Member with name, DOB, expiration, location.
     * @param fname the member's first name.
     * @param lname the member's last name.
     * @param dob the member's date of birth in mm/dd/yyyy format.
     * @param expire the member's expiration date in mm/dd/yyyy format.
     * @param location the member's gym location.
     */
    public Member(String fname, String lname, String dob,
                  String expire, String location) {
        this.fname = fname.substring(0,1).toUpperCase() +
                fname.substring(1).toLowerCase();
        this.lname = lname.substring(0,1).toUpperCase() +
                lname.substring(1).toLowerCase();
        this.dob = new Date(dob);
        this.expire = new Date(expire);
        this.location = Location.getLocation(location);
    }

    /**
     * Creates an instance of Member with a first name and last name and DOB.
     * @param fname the member's first name.
     * @param lname the member's last name.
     * @param dob the member's date of birth in mm/dd/yyyy format.
     */
    public Member(String fname, String lname, String dob) {
        this.fname = fname.substring(0,1).toUpperCase() +
                fname.substring(1).toLowerCase();
        this.lname = lname.substring(0,1).toUpperCase() +
                lname.substring(1).toLowerCase();
        this.dob = new Date(dob);
        this.expire = null;
        this.location = null;
    }

    /**
     * Gets the member's first name.
     * @return the first name as a String.
     */
    public String getFirstName() {
        return fname;
    }

    /**
     * Gets the member's last name.
     * @return the last name as a String.
     */
    public String getLastName() {
        return lname;
    }

    /**
     * Gets the member's date of birth.
     * @return the date of birth as a Date object.
     */
    public Date getDOB() {
        return dob;
    }

    /**
     * Gets the member's expiration date.
     * @return the expiration date as a Date object.
     */
    public Date getExpire() {
        return expire;
    }

    /**
     * Gets the member's gym location.
     * @return the location as a Location instance.
     */
    public Location getLocation(){
        return location;
    }

    /**
     * Sets the member expiration date three months from today's date.
     * @return the expiration date as a Date object.
     */
    public Date setExpire(){
        Date today = new Date();
        int newMonth = today.getMonth() + 3;
        int newYear = today.getYear();
        int newDay = today.getDay();
        if (newMonth > MONTHS_IN_A_YEAR) {
            newMonth = newMonth % MONTHS_IN_A_YEAR;
            newYear++;
        }

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
     * Checks if the membership has expired.
     * Compares the member's expiration date with the current date.
     * @return true if the membership has expired, false otherwise.
     */
    public boolean membershipExpired(){
        return expire.compareTo(new Date()) < 0;
    }

    /**
     * Calculates the membership fee for a standard membership.
     * The membership fee includes the one-time fee for registering for the
     * membership as well as the monthly fees that will be paid each quarter,
     * or every 3 months.
     * @return the standard membership fee for the first quarter.
     */
    public double membershipFee(){
        return STANDARD_ONE_TIME_FEE + QUARTERLY * STANDARD_MONTHLY_FEE;
    }

    /**
     * Checks if a member is 18, or above the age of 18.
     * Compares the date of birth of a member to their eighteenth birthdate.
     * @return true if a member is 18+, false otherwise.
     */
    public boolean aboveEighteen(){
        Date todayDate = new Date();
        Date eighteenthBirthday = new Date(this.getDOB().getMonth() +
                "/" + this.getDOB().getDay() + "/" +
                (this.getDOB().getYear() + 18));
        if (eighteenthBirthday.compareTo(todayDate) == 1)
            return false;
        return true;
    }

    /**
     * Checks if two instances of Member are equal.
     * Checks if the member passed as a parameter is null.
     * If member is not null, compares the first and last name and DOB.
     * @param obj the Member instance that will be cast into a Member.
     * @return true if the members are the same, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        Member memberCompare = Member.class.cast(obj);
        if(memberCompare == null)
            return false;
        boolean dobCompare = dob.equals(memberCompare.getDOB());
        return this.compareTo(memberCompare) == 0 && dobCompare;
    }

    /**
     * Compares two members based on the last and first names.
     * @param member the member that is being compared.
     * @return 0 if members are equal, a positive integer if member is
     * alphabetically greater than member passed as a parameter, or a negative
     * integer if member is alphabetically before than the member passed as a
     * parameter.
     */
    @Override
    public int compareTo(Member member) {
        int compareLname = lname.compareToIgnoreCase(member.getLastName());
        if(compareLname == 0) {
            int compareFname =
                    fname.compareToIgnoreCase(member.getFirstName());
            if(compareFname == 0) {
                int compareDOB = dob.compareTo(member.getDOB());
                if(compareDOB == 0) {
                    return 0;
                }
                return compareDOB;
            }
            return compareFname;
        }
        return compareLname;
    }

    /**
     * Creates String with last and first name, DOB, expiration, and location.
     * @return a String with the member's information.
     */
    @Override
    public String toString() {
        if(membershipExpired())
            return (fname + " " + lname + ", DOB: " + dob.toString() +
                    ", Membership expired " + expire.toString() +
                    ", Location: " + location.name() + ", " +
                    location.getZipCode() + ", " + location.getCounty());
        return (fname + " " + lname + ", DOB: " + dob.toString() +
                ", Membership expires " + expire.toString() + ", Location: "
                + location.name() + ", " +  location.getZipCode() + ", "
                + location.getCounty());
    }
}