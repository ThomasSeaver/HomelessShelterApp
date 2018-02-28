package org.team69.homelessshelterapp.model;

import java.util.HashMap ;

/**
 * Created by obecerra on 2/26/18.
 */

public class UserPassMap {

    private HashMap<String, String> backingHashMap;

    public UserPassMap() {
        backingHashMap = new HashMap<>();
    }

    public String getPassword(String key) {
        return backingHashMap.get(key);
    }

    public void addTo(String user, String pass) {
        backingHashMap.put(user, pass);
    }

    public HashMap<String, String> getBackingHashMap() {
        return backingHashMap;
    }

}
