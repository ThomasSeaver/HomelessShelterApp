package org.team69.homelessshelterapp.model;

/**
 * Created by TomStuff on 3/6/18.
 */

public class Shelter implements java.io.Serializable{
    private String name;
    private String capacity;
    private String gender;
    private String longitude;
    private String latitude;
    private String address;
    private String phoneNumber;
    private String claimedRooms;

    /**
     *
     * @param name of the shelter
     * @param capacity of the shelter
     * @param gender to know which genders are allowed
     * @param longitude of the shelter
     * @param latitude of the shelter
     * @param address of the shelter
     * @param phoneNumber to contact shelter
     * @param claimedRooms to know how many beds are free
     */
    public Shelter(String name, String capacity, String gender, String longitude,
                   String latitude, String address, String phoneNumber, String claimedRooms) {
        this.name = name;
        this.capacity = capacity;
        this.gender = gender;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.claimedRooms = claimedRooms;
    }

    /**
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     *
     * @return the capacity
     */
    public String getCapacity() { return capacity; }

    /**
     *
     * @return the gender
     */
    public String getGender() { return gender; }

    /**
     *
     * @return the longitude
     */
    public String getLongitude() {return longitude; }

    /**
     *
     * @return the latitude
     */
    public String getLatitude() {return latitude; }

    /**
     *
     * @return the address
     */
    public String getAddress() {return address; }

    /**
     *
     * @return the phonenumber
     */
    public String getPhoneNumber() {return phoneNumber; }

    /**
     *
     * @return the number of claimed rooms
     */
    public String getClaimedRooms() {return claimedRooms; }

    /**
     *
     * @return the record
     */
    public String getRecord() {return (name + "," + capacity + "," + gender + "," + longitude +
            "," + latitude + "," + address + "," + phoneNumber + "," + claimedRooms);}

    /**
     *
     * @param newClaims the number of beds to be claimed
     */
    public void claimRooms(int newClaims) {claimedRooms =
            String.valueOf(Integer.parseInt(claimedRooms) + newClaims);}

    /**
     *
     * @param released the number of rooms to be released
     */
    public void releaseRooms(int released) {claimedRooms =
            String.valueOf(Integer.parseInt(claimedRooms) - released);}
}
