package org.team69.homelessshelterapp.model;

import java.util.HashMap;

/**
 * Created by TomStuff on 3/6/18.
 */

public class ShelterList implements java.io.Serializable {

    private HashMap<String, Shelter> shelters;

    public ShelterList() {
        shelters = new HashMap<>();
    }

    public void addShelter(String ID, Shelter newShelter) {
        shelters.put(ID, newShelter);
    }

    public Shelter findShelterByID(String ID) {
        if (ID != null) {
            return shelters.get(ID);
        }
        return null;
    }

    public HashMap<String, Shelter> getMap() {
        return shelters;
    }
}
