package com.example.gymmembershipgui;

/**
 * Location defines five gym locations with their zip code and county.
 * Gym locations can be one of the following: Edison, Piscataway, Bridgewater,
 * Franklin, or Somerville. Given the name of the location, Location can check
 * if the town exists.
 * @author Arunima Tripathy, Mahfuza Rahman
 */
public enum Location {
    EDISON("08837", "MIDDLESEX"),
    PISCATAWAY("08854", "MIDDLESEX"),
    BRIDGEWATER("08807", "SOMERSET"),
    FRANKLIN("08873", "SOMERSET"),
    SOMERVILLE("08876", "SOMERSET");

    private final String zipCode;
    private final String county;

    /**
     * Creates an instance of Location with a zipcode and county.
     * @param zipCode the zipcode of the location.
     * @param county the county the location is in.
     */
    Location(String zipCode, String county) {
        this.zipCode = zipCode;
        this.county = county;
    }

    /**
     * Gets the zipcode of the location.
     * @return the zipcode of the location as a String.
     */
    public String getZipCode(){
        return zipCode;
    }

    /**
     * Gets the country of the location.
     * @return the county of the location as a String.
     */
    public String getCounty(){
        return county;
    }

    /**
     * Given a location, the class gets the matching Location constant.
     * @param location the location to search for.
     * @return the corresponding Location constant, null if none correspond.
     */
    public static Location getLocation(String location) {
        Location towns[] = Location.values(); // array of Location constants
        for(Location town : towns) {
            if(town.name().equalsIgnoreCase(location))
                return town;
        }
        return null;
    }

    @Override
    public String toString(){
        return name() + ", " + getZipCode() + ", " + getCounty();
    }

}
