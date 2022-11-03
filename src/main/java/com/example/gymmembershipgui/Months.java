package com.example.gymmembershipgui;

/**
 * Months defines each calendar month with its order, abbreviation, and days.
 * As long as a month is called, other classes can access the number of days
 * in the corresponding month, its month order, and its month abbreviation.
 * @author Arunima Tripathy, Mahfuza Rahman
 */
public enum Months {
    JANUARY(31, 1, "Jan"),
    FEBRUARY(29, 2, "Feb"),
    MARCH(31, 3, "Mar" ),
    APRIL(30, 4 , "Apr"),
    MAY(31, 5, "May"),
    JUNE(30, 6, "Jun"),
    JULY(31, 7, "Jul"),
    AUGUST(31, 8, "Aug"),
    SEPTEMBER(30, 9, "Sep"),
    OCTOBER(31, 10, "Oct"),
    NOVEMBER(30, 11, "Nov"),
    DECEMBER(31, 12, "Dec");

    private final int numOfDays;
    private final int monthOrder;
    private final String abbreviation;

    /**
     * Creates an instance of each Month to represent Months enums.
     * @param numOfDays the number of days in a month as an integer.
     * @param monthOrder the ordinal number of a month as an integer.
     * @param abbreviation the three letter abbreviation as String.
     */
    Months(int numOfDays, int monthOrder, String abbreviation) {
        this.numOfDays = numOfDays;
        this.monthOrder = monthOrder;
        this.abbreviation = abbreviation;
    }

    /**
     * Gets the number of days in a month.
     * @return the number of days as an integer.
     */
    public int getNumOfDays() {
        return this.numOfDays;
    }

    /**
     * Gets the month order of the month.
     * @return the month order as an integer.
     */
    public int getMonthOrder() {
        return this.monthOrder;
    }

    /**
     * Gets the three letter abbreviation of a month.
     * @return the abbreviation as a String.
     */
    public String getAbbreviation() {
        return this.abbreviation;
    }

}
