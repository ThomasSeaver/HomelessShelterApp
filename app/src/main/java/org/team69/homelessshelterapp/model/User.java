package org.team69.homelessshelterapp.model;

/**
 * Created by obecerra on 3/27/18.
 */

public class User implements java.io.Serializable {

    private String shelterClaimedID;
    private int bedsClaimed;
    private String username;
    private String password;

    public User (String u, String p, String s, int b) {
        username = u;
        password = p;
        shelterClaimedID = s;
        bedsClaimed = b;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String s) {
        username = s;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String s) {
        password = s;
    }


    public int getBedsClaimed() {
        return bedsClaimed;
    }

    public void setBedsClaimed(int i) {
        bedsClaimed = i;
    }

    public String getShelterClaimedID() {
        return shelterClaimedID;
    }

    public void setShelterClaimedID(String s) {
        shelterClaimedID = s;
    }

}
