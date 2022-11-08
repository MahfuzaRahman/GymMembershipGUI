package com.example.gymmembershipgui;

import java.util.Calendar;

/**
 * Date defines a date given a month, day, and year, and checks its validity.
 * Given a month in mm/dd/yyyy format as a String, this class can check if it
 * is a valid, calendar date (that is, whether the date would exist on a
 * calendar). This class can also check whether a year is a leap year.
 * Additionally, it can compare two different dates and check if the two are
 * equivalent dates.
 * @author Arunima Tripathy, Mahfuza Rahman
 */
public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;

    private static final int QUADRENNIAL = 4;
    private static final int CENTENNIAL = 100;
    private static final int QUARTER_CENTENNIAL = 400;
    private static final int MONTHS_IN_A_YEAR = 12;
    private static final int FEB_DAYS_LEAP_YEAR = 29;
    private static final int FEB_DAYS_NON_LEAP_YEAR = 28;
    private static final int FIRST_DAY_OF_MONTH = 1;
    private static final int MONTH_INDEX = 1;
    private static final int DAY_INDEX = 2;
    private static final int YEAR_INDEX = 5;
    private static final int EQUAL = 0;
    private static final int LESS_THAN = -1;
    private static final int GREATER_THAN = 1;

    /**
     * Creates an instance of Date with month, day, and year of current date.
     * This is the default constructor for the Date class.
     */
    public Date() {
        Calendar todayDate = Calendar.getInstance();
        String dateString = todayDate.getTime().toString();
        String[] dateArray = dateString.split(" ", 0);
        String monthString = dateArray[MONTH_INDEX];
        String dayString = dateArray[DAY_INDEX];
        String yearString = dateArray[YEAR_INDEX];
        year = Integer.parseInt(yearString);
        month = findMonth(monthString).getMonthOrder();
        day = Integer.parseInt(dayString);
    }

    /**
     * Creates an instance of Date with month, day, and year given the date.
     * @param date the date as a String in mm/dd/yyyy format.
     */
    public Date(String date) {
        String[] dateArray;
        if(date.contains("/"))
        {
            dateArray = date.split("/", 0);
            month = Integer.parseInt(dateArray[0]);
            day = Integer.parseInt(dateArray[1]);
            year = Integer.parseInt(dateArray[2]);
        }
        else
        {
            dateArray = date.split("-", 0);
            month = Integer.parseInt(dateArray[1]);
            day = Integer.parseInt(dateArray[2]);
            year = Integer.parseInt(dateArray[0]);
        }
    }

    /**
     * Gets the year of the date.
     * @return the year as an integer.
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the month of the date.
     * @return the month as an integer
     */
    public int getMonth() {
        return month;
    }

    /**
     * Gets the day of the date.
     * @return the day as an integer.
     */
    public int getDay() {
        return day;
    }

    /**
     * Checks year, month, and day to see if Date is a valid calendar date.
     * Given a Date, the month is first checked to see if the month is valid.
     * If the month is February, the year is checked to see if it is a leap
     * year. If it's a leap year, the day is checked to see if it is valid
     * day. For months other than February, the day is checked to see if it is
     * valid.
     * @return true if date is valid date, false otherwise.
     */
    public boolean isValid() {
        Months thisMonth = findMonth(this.month);
        if(year < 1900)
            return false;
        if (month < Months.JANUARY.getMonthOrder() ||
                month > MONTHS_IN_A_YEAR)
            return false;
        if(day < FIRST_DAY_OF_MONTH)
            return false;
        if (month == Months.FEBRUARY.getMonthOrder()) {
            if (isLeapYear()) {
                if (day > FEB_DAYS_LEAP_YEAR)
                    return false;
            } else {
                if (day > FEB_DAYS_NON_LEAP_YEAR)
                    return false;
            }
        }
        if (day > thisMonth.getNumOfDays()){
            return false;
        }
        return true;
    }

    /**
     * Given three letter abbreviation for a month, finds equivalent Month.
     * Returns null if an invalid abbreviation is passed.
     * @param monthAbbreviation the three letter abbreviation of the month.
     * @return prospective Month for given abbreviation of month.
     */
    private Months findMonth(String monthAbbreviation) {
        Months year[] = Months.values();
        for (Months month : year) {
            if (month.getAbbreviation().equals(monthAbbreviation))
                return month;
        }
        return null;
    }

    /**
     * Checks if the date is 18 years or older.
     * @return true if date is at least 18 years ago, false otherwise.
     */
    public boolean aboveEighteen(){
        Date todayDate = new Date();
        Date eighteenthBirthday = new Date(getMonth() +
                "/" + getDay() + "/" +
                (getYear() + 18));
        if (eighteenthBirthday.compareTo(todayDate) == 1)
            return false;
        return true;
    }

    /**
     * Given month number of a month (1-12), finds matching Months constant.
     * @param monthNumber the number value of a month.
     * @return the equivalent Months enum, null if month does not exist.
     */
    private Months findMonth(int monthNumber) {
        Months year[] = Months.values(); // array with all possible months
        for (Months month : year) {
            if (month.getMonthOrder() == monthNumber)
                return month;
        }
        return null;
    }

    /**
     * Checks if the year of the Date is a leap year.
     * Determine if a year is a leap year by checking if year is divisible by
     * 4. If year is divisible by 4, checks if year is divisible by 100. If
     * year is divisible by 100, checks if year is divisible by 400. If year
     * is divisible by 400, year is a leap year. If previous requirements are
     * not fulfilled, year is not a leap year.
     * @return true if year is a leap year, false otherwise.
     */
    public boolean isLeapYear() {
        if (year % QUADRENNIAL == 0) {
            if (year % CENTENNIAL == 0) {
                if (year % QUARTER_CENTENNIAL == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Checks if two instances of Date are equal.
     * Compares the year, month, and date of two Date instances.
     * @param obj the Date instance that will be cast into a Date.
     * @return true if date is the same, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        Date dateCompare = Date.class.cast(obj);
        if(dateCompare == null)
            return false;
        boolean compareYear = (this.year == dateCompare.getYear());
        boolean compareMonth = (this.month == dateCompare.getMonth());
        boolean compareDay = (this.day == dateCompare.getDay());
        return (compareYear && compareMonth) && compareDay;
    }

    /**
     * Compares two Date instances.
     * Checks to see if compared date is before, after, or same as given date.
     * Years are compared to check if compared year is before/after given
     * year. If same year, the months are compared. If same month, the days
     * are compared. If the days are the same, Date is equal.
     * @param date the Date instance to be compared.
     * @return 1 if given date is after compared date, -1 if before, 0 if
     * equal.
     */
    @Override
    public int compareTo(Date date) {
        if (this.year > date.getYear())
            return GREATER_THAN;
        if (this.year < date.getYear())
            return LESS_THAN;
        if (this.month > date.getMonth())
            return GREATER_THAN;
        if (this.month < date.getMonth())
            return LESS_THAN;
        if (this.day > date.getDay())
            return GREATER_THAN;
        if (this.day < date.getDay())
            return LESS_THAN;
        return EQUAL; // date must be equal
    }

    /**
     * Creates a String of the date in mm/dd/yyyy format.
     * @return the date in mm/dd/yyyy format.
     */
    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }
}