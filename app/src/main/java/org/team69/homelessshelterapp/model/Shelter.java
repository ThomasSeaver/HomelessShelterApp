package org.team69.homelessshelterapp.model;

import com.google.android.gms.maps.model.LatLng;

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
        private LatLng coordinates;

        public Shelter(String name, String capacity, String gender, String longitude, String latitude, String address, String phoneNumber, String claimedRooms) {
            this.name = name;
            this.capacity = capacity;
            this.gender = gender;
            this.longitude = longitude;
            this.latitude = latitude;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.claimedRooms = claimedRooms;
            this.coordinates = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        }

        public String getName() { return name; }
        public String getCapacity() { return capacity; }
        public String getGender() { return gender; }
        public String getLongitude() {return longitude; }
        public String getLatitude() {return latitude; }
        public String getAddress() {return address; }
        public String getPhoneNumber() {return phoneNumber; }
        public String getClaimedRooms() {return claimedRooms; }
        public LatLng getCoordinates() {return coordinates; }
        public String getRecord() {return (name + "," + capacity + "," + gender + "," + longitude + "," + latitude + "," + address + "," + phoneNumber + "," + claimedRooms);}
        public void claimRooms(int newClaims) {claimedRooms = String.valueOf(Integer.parseInt(claimedRooms) + newClaims);}
        public void releaseRooms(int released) {claimedRooms = String.valueOf(Integer.parseInt(claimedRooms) - released);}
        public String getInfo() {
            return "Capacity: " + (capacity.equals("Not available") ? capacity : String.valueOf(Integer.parseInt(capacity) - Integer.parseInt(claimedRooms)))
                + "\nAccepts: " + gender
                + "\nPhone Number: " + phoneNumber
                + "\nAddress: " + address;
        }
}
