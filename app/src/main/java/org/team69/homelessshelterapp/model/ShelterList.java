package org.team69.homelessshelterapp.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    public HashMap<String, Shelter> getByRestriction(String gender, String age, String name){
        HashMap<String, Shelter> searchedList = shelters;
        if (!(gender == null)) {
            for(Iterator<Map.Entry<String, Shelter>> it = searchedList.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Shelter> entry = it.next();
                Shelter shelter = entry.getValue();
                if (!shelter.getGender().contains(gender) && (shelter.getGender().contains("Men") || shelter.getGender().contains("Women"))){
                    it.remove();
                }
            }
        }
        if (!(age == null)) {
            for(Iterator<Map.Entry<String, Shelter>> it = searchedList.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Shelter> entry = it.next();
                Shelter shelter = entry.getValue();
                if (!shelter.getGender().contains(age) && (shelter.getGender().contains("Children") || shelter.getGender().contains("Families w/ newborns") || shelter.getGender().contains("Young adults"))){
                    it.remove();
                }
            }
        }
        if (!(name == null)) {
            for(Iterator<Map.Entry<String, Shelter>> it = searchedList.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Shelter> entry = it.next();
                Shelter shelter = entry.getValue();
                if (!shelter.getName().contains(name)){
                    it.remove();
                }
            }
        }
        return searchedList;
    }
}
