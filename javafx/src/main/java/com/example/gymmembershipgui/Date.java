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
     * Given three letter abbreviation for a month return equivalent Months enum.
     * Returns null if an invalid abbreviation is passed.
     * @param monthAbbreviation the three letter abbreviation.
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
     * Checks if two instances of Date() are equal.
     * Compares the year, month, and date of two Date() instances.
     * @param obj the Date() instance that will be cast into a Date().
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
     * Compares two Date() instances.
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

    /**
     * This is the testbed main to test the isValid() method.
     * @param args is the array of characters passed to command line.
     */
    public static void main(String[] args) {
        // test case #1
        Date test = new Date("12/31/1899");
        System.out.println("Test case #1 result: " + test.isValid());

        // test case #2
        test = new Date("0/20/2003");
        System.out.println("Test case #2 result: " + test.isValid());

        // test case #3
        test = new Date("13/8/1977");
        System.out.println("Test case #3 result: " + test.isValid());

        // test case #4
        test = new Date("13/31/2003");
        System.out.println("Test case #4 result: " + test.isValid());

        // test case #5
        test = new Date("-1/31/2003");
        System.out.println("Test case #5 result: " + test.isValid());

        // test case #6
        test = new Date("3/-20/2003");
        System.out.println("Test case #6 result: " + test.isValid());

        // test case #7
        test = new Date("1/31/2020");
        System.out.println("Test case #7 result: " + test.isValid());

        // test case #8
        test = new Date("1/32/2020");
        System.out.println("Test case #8 result: " + test.isValid());

        // test case #9
        test = new Date("1/20/2004");
        System.out.println("Test case #9 result: " + test.isValid());

        // test case #10
        test = new Date("1/20/2003");
        System.out.println("Test case #10 result: " + test.isValid());

        // test case #11
        test = new Date("2/28/2021");
        System.out.println("Test case #11 result: " + test.isValid());

        // test case #12
        test = new Date("2/29/2003");
        System.out.println("Test case #12 result: " + test.isValid());

        // test case #13
        test = new Date("2/30/2011");
        System.out.println("Test case #13 result: " + test.isValid());

        // test case #14
        test = new Date("2/29/2000");
        System.out.println("Test case #14 result: " + test.isValid());

        // test case #15
        test = new Date("2/30/2020");
        System.out.println("Test case #15 result: " + test.isValid());

        // test case #16
        test = new Date("3/30/2021");
        System.out.println("Test case #16 result: " + test.isValid());

        // test case #17
        test = new Date("3/30/2023");
        System.out.println("Test case #17 result: " + test.isValid());

        // test case #18
        test = new Date("3/31/1990");
        System.out.println("Test case #18 result: " + test.isValid());

        // test case #19
        test = new Date("3/31/2023");
        System.out.println("Test case #19 result: " + test.isValid());

        // test case #20
        test = new Date("3/32/2003");
        System.out.println("Test case #20 result: " + test.isValid());

        // test case #21
        test = new Date("4/3/2003");
        System.out.println("Test case #21 result: " + test.isValid());

        // test case #22
        test = new Date("4/31/2003");
        System.out.println("Test case #22 result: " + test.isValid());

        // test case #23
        test = new Date("4/31/2022");
        System.out.println("Test case #23 result: " + test.isValid());

        // test case #24
        test = new Date("5/1/1996");
        System.out.println("Test case #24 result: " + test.isValid());

        // test case #25
        test = new Date("5/1/1999");
        System.out.println("Test case #25 result: " + test.isValid());

        // test case #26
        test = new Date("5/31/2023");
        System.out.println("Test case #26 result: " + test.isValid());

        // test case #27
        test = new Date("5/32/2020");
        System.out.println("Test case #27 result: " + test.isValid());

        // test case #28
        test = new Date("6/30/1999");
        System.out.println("Test case #28 result: " + test.isValid());

        // test case #29
        test = new Date("6/30/2023");
        System.out.println("Test case #29 result: " + test.isValid());

        // test case #30
        test = new Date("6/31/2020");
        System.out.println("Test case #30 result: " + test.isValid());

        // test case #31
        test = new Date("7/15/1977");
        System.out.println("Test case #31 result: " + test.isValid());

        // test case #32
        test = new Date("7/32/2020");
        System.out.println("Test case #32 result: " + test.isValid());

        // test case #33
        test = new Date("8/8/1977");
        System.out.println("Test case #33 result: " + test.isValid());

        // test case #34
        test = new Date("8/32/2020");
        System.out.println("Test case #34 result: " + test.isValid());

        // test case #35
        test = new Date("9/2/2022");
        System.out.println("Test case #35 result: " + test.isValid());

        // test case #36
        test = new Date("9/9/1977");
        System.out.println("Test case #36 result: " + test.isValid());

        // test case #37
        test = new Date("9/30/2023");
        System.out.println("Test case #37 result: " + test.isValid());

        // test case #38
        test = new Date("9/31/2020");
        System.out.println("Test case #38 result: " + test.isValid());

        // test case #39
        test = new Date("10/7/1991");
        System.out.println("Test case #39 result: " + test.isValid());

        // test case #40
        test = new Date("10/32/2020");
        System.out.println("Test case #40 result: " + test.isValid());

        // test case #41
        test = new Date("11/20/2003");
        System.out.println("Test case #41 result: " + test.isValid());

        // test case #42
        test = new Date("11/31/2020");
        System.out.println("Test case #42 result: " + test.isValid());

        // test case #43
        test = new Date("12/1/1989");
        System.out.println("Test case #43 result: " + test.isValid());

        // test case #44
        test = new Date("12/2/2022");
        System.out.println("Test case #44 result: " + test.isValid());

        // test case #45
        test = new Date("12/20/2004");
        System.out.println("Test case #45 result: " + test.isValid());

        // test case #46
        test = new Date("12/31/2023");
        System.out.println("Test case #46 result: " + test.isValid());

        // test case #47
        test = new Date("12/32/1989");
        System.out.println("Test case #47 result: " + test.isValid());

    }
}