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

        public Shelter(String name, String capacity, String gender, String longitude, String latitude, String address, String phoneNumber, String claimedRooms) {
            this.name = name;
            this.capacity = capacity;
            this.gender = gender;
            this.longitude = longitude;
            this.latitude = latitude;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.claimedRooms = claimedRooms;
        }

        public String getName() { return name; }
        public String getCapacity() { return capacity; }
        public String getGender() { return gender; }
        public String getLongitude() {return longitude; }
        public String getLatitude() {return latitude; }
        public String getAddress() {return address; }
        public String getPhoneNumber() {return phoneNumber; }
        public String getClaimedRooms() {return claimedRooms; }
        public String getRecord() {return (name + "," + capacity + "," + gender + "," + longitude + "," + latitude + "," + address + "," + phoneNumber + "," + claimedRooms);}
        public void claimRooms(int newClaims) {claimedRooms = String.valueOf(Integer.parseInt(claimedRooms) + newClaims);}
        public void releaseRooms(int released) {claimedRooms = String.valueOf(Integer.parseInt(claimedRooms) - released);}
}
