package org.team69.homelessshelterapp.model;

/**
 * Info holder for all information about user, specifically username, password, and information
 * about what beds have been claimed and where
 *
 * Created by obecerra on 3/27/18.
 */

public class User implements java.io.Serializable {

    private String shelterClaimedID;
    private int bedsClaimed;
    private String username;
    private String password;

    /**
     * User constructor, creates user from base information
     *
     * @param u instance username
     * @param p instance password
     * @param s instance shelter beds claimed at
     * @param b instance number of beds claimed
     */
    public User (String u, String p, String s, int b) {
        username = u;
        password = p;
        shelterClaimedID = s;
        bedsClaimed = b;
    }

    /** getter
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /** setter
     *
     * @param s new username to set for user
     */
    public void setUsername(String s) {
        username = s;
    }

    /** getter
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /** setter
     *
     * @param s new password to set for user
     */
    public void setPassword(String s) {
        password = s;
    }

    /** getter
     *
     * @return number of beds claimed at any shelter
     */
    public int getBedsClaimed() {
        return bedsClaimed;
    }

    /** setter
     *
     * @param i new number of beds claimed
     */
    public void setBedsClaimed(int i) {
        bedsClaimed = i;
    }

    /** getter
     *
     * @return ID of shelter beds currently claimed at
     */
    public String getShelterClaimedID() {
        return shelterClaimedID;
    }

    /** setter
     *
     * @param s new ID where user is now claiming beds
     */
    public void setShelterClaimedID(String s) {
        shelterClaimedID = s;
    }

}
