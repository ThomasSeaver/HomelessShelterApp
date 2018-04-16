package org.team69.homelessshelterapp.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Shelter list infoholder for list of shelters, provides access to shelters through abstraction,
 * with additional needed methods for shelter list usage
 * Created by TomStuff on 3/6/18.
 */

public class ShelterList implements java.io.Serializable {

    private static HashMap<String, Shelter> shelters;

    /**
     * Creates shelter list, initializes shelters variable for usage
     */
    public ShelterList() {
        shelters = new HashMap<>();
    }

    /**
     * Adds a shelter to the shelter list, just putting it inside the interior hashmap.
     *
     * @param ID         Shelter unique ID, key to shelter within map
     * @param newShelter the new shelter created to be added to the list
     */
    public void addShelter(String ID, Shelter newShelter) {
        shelters.put(ID, newShelter);
    }

    /**
     * Looks for shelter within list using the ID string we've been using for keys within the map
     *
     * @param ID  Necessary ID to find attached shelter to ID
     * @return    returns shelter, or if nothing is found/ID is bad, null.
     */
    public Shelter findShelterByID(String ID) {
        if (ID != null) {
            return shelters.get(ID);
        }
        return null;
    }

    /**
     * Gets the backing map to the shelterlist; used when direct abstraction is impossible
     * @return the backing map, directly containing the shelters
     */
    public static HashMap<String, Shelter> getMap() {
        return shelters;
    }

    /**
     * Provides a hashmap exclusively of shelters that match the provided constraints of gender,
     * age, and name by iterating over copy of hashmap and removing those that don't match
     * constraint, if constraint is available
     *
     * @param gender Gender constraint; if available, limits shelters to men/women
     * @param age    Age constraint; if available, limits shelters to children/families/young adults
     * @param name   Name constraint; if available, limits shelters to ones containing name variable
     * @return       Hashmap exclusively holding those shelters that match the restrictions
     */
    public HashMap<String, Shelter> getByRestriction(CharSequence gender, CharSequence age,
                                                     CharSequence name){
        HashMap<String, Shelter> searchedList = shelters;
        if (!(gender == null)) {
            Set entries = searchedList.entrySet();
            for(Iterator<Map.Entry<String, Shelter>> it = entries.iterator();
                it.hasNext(); ) {
                Map.Entry<String, Shelter> entry = it.next();
                Shelter shelter = entry.getValue();
                String genderVal = shelter.getGender();
                if (!genderVal.contains(gender) && (genderVal.contains("Men")
                        || genderVal.contains("Women"))){
                    it.remove();
                }
            }
        }
        if (!(age == null)) {
            Set entries = searchedList.entrySet();
            for(Iterator<Map.Entry<String, Shelter>> it = entries.iterator();
                it.hasNext(); ) {
                Map.Entry<String, Shelter> entry = it.next();
                Shelter shelter = entry.getValue();
                String genderVal = shelter.getGender();
                if (!genderVal.contains(age) && (genderVal.contains("Children")
                        || genderVal.contains("Families w/ newborns")
                        || genderVal.contains("Young adults"))){
                    it.remove();
                }
            }
        }
        if (!(name == null)) {
            Set entries = searchedList.entrySet();
            for(Iterator<Map.Entry<String, Shelter>> it = entries.iterator();
                it.hasNext(); ) {
                Map.Entry<String, Shelter> entry = it.next();
                Shelter shelter = entry.getValue();
                String nameVal = shelter.getName();
                if (!nameVal.contains(name)){
                    it.remove();
                }
            }
        }
        return searchedList;
    }
}
