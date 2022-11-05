package com.example.gymmembershipgui;

/**
 * Time defines the times classes are held: morning, afternoon, and evening.
 * Each time is defined with its hour and minute. If a specific Time is called
 * on in another class, its time can be printed in the hh:mm format.
 * @author Arunima Tripathy, Mahfuza Rahman
 */
public enum Time {
    MORNING(9, 30),
    AFTERNOON(14, 00),
    EVENING(18, 30);

    private final int hour;
    private final int minute;

    /**
     * Creates an instance of Time with an hour and minute.
     * @param hour the hour of the Time enum.
     * @param minute the minute of the Time enum.
     */
    Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Creates a String that gets both the hour and minute of the time of day.
     * @return a String that displays the time of day.
     */
    public String getTime() {
        if(minute == 0)
            return (hour + ":00");
        return (hour + ":" + minute);
    }

    /**
     * Given the time as a string, the matching Time constant is returned.
     * @param inputTime the time as a String.
     * @return the corresponding Time constant, null if not found.
     */
    public static Time getTimeEnum(String inputTime) {
        Time timeOfDay[] = Time.values();
        for(Time t : timeOfDay) {
            if(t.name().equalsIgnoreCase(inputTime))
                return t;
        }
        return null;
    }

}
