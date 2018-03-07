package org.team69.homelessshelterapp.model;

/**
 * Created by TomStuff on 3/6/18.
 */

public class Shelter implements java.io.Serializable{
        private String name;
        private String capacity;
        private String gender;
        private double longitude;
        private double latitude;
        private String address;
        private String phoneNumber;

        public Shelter(String name, String capacity, String gender, double longitude, double latitude, String address, String phoneNumber) {
            this.name = name;
            this.capacity = capacity;
            this.gender = gender;
            this.longitude = longitude;
            this.latitude = latitude;
            this.address = address;
            this.phoneNumber = phoneNumber;
        }

        public String getName() { return name; }
        public String getCapacity() { return capacity; }
        public String getGender() { return gender; }
        public double getLongitude() {return longitude; }
        public double getLatitude() {return latitude; }
        public String getAddress() {return address; }
        public String getPhoneNumber() {return phoneNumber; }
}
