package org.team69.homelessshelterapp;

import org.junit.Before;
import org.junit.Test;
import org.team69.homelessshelterapp.model.Shelter;
import org.team69.homelessshelterapp.model.ShelterList;

import java.util.HashMap;

import static org.junit.Assert.*;


/**
 * Test file for method in shelter list, specifically getByRestriction, testing based on single
 * criteria whether the method returns as expected with a generic shelter list.
 * Created by TomStuff on 4/15/18.
 */

public class ShelterMapSearchTest {
    private ShelterList list;
    private HashMap<String, Shelter> maleSearch;
    private HashMap<String, Shelter> femaleSearch;
    private HashMap<String, Shelter> childSearch;
    private HashMap<String, Shelter> familySearch;
    private HashMap<String, Shelter> youngAdultSearch;
    //private HashMap<String, Shelter> unspecifiedSearch;

    @Before
    public void setUp() {
        Shelter maleShelter;
        Shelter femaleShelter;
        Shelter childShelter;
        Shelter familyShelter;
        Shelter youngAdultShelter;
        Shelter unspecifiedShelter;

        list = new ShelterList();
        maleShelter = new Shelter("Male shelter", "0", "Men", "0.00",
                "0.00", "1000 Main St", "404-404-4000", "0");
        femaleShelter = new Shelter("Female shelter", "0", "Women", "0.00",
                "0.00", "1001 Main St", "404-404-4001", "0");
        childShelter = new Shelter("Child shelter", "0", "Children", "0.00",
                "0.00", "1002 Main St", "404-404-4002", "0");
        familyShelter = new Shelter("Family shelter", "0", "Families w/ newborns", "0.00",
                "0.00", "1003 Main St", "404-404-4003", "0");
        youngAdultShelter = new Shelter("Young Adults shelter", "0", "Young adults", "0.00",
                "0.00", "1004 Main St", "404-404-4004", "0");
        unspecifiedShelter = new Shelter("Unspecified gender shelter", "0", "All genders", "0.00",
                "0.00", "1005 Main St", "404-404-4005", "0");
        maleSearch = new HashMap<>();
        femaleSearch = new HashMap<>();
        childSearch = new HashMap<>();
        familySearch = new HashMap<>();
        youngAdultSearch = new HashMap<>();
        //unspecifiedSearch = new HashMap<String, Shelter>();
        list.addShelter("0", maleShelter);
        list.addShelter("1", femaleShelter);
        list.addShelter("2", childShelter);
        list.addShelter("3", familyShelter);
        list.addShelter("4", youngAdultShelter);
        list.addShelter("5", unspecifiedShelter);
        maleSearch.put("0", maleShelter);
        maleSearch.put("2", childShelter);
        maleSearch.put("3", familyShelter);
        maleSearch.put("4", youngAdultShelter);
        maleSearch.put("5", unspecifiedShelter);
        femaleSearch.put("1", femaleShelter);
        femaleSearch.put("2", childShelter);
        femaleSearch.put("3", familyShelter);
        femaleSearch.put("4", youngAdultShelter);
        femaleSearch.put("5", unspecifiedShelter);
        childSearch.put("0", maleShelter);
        childSearch.put("1", femaleShelter);
        childSearch.put("2", childShelter);
        childSearch.put("5", unspecifiedShelter);
        familySearch.put("0", maleShelter);
        familySearch.put("1", femaleShelter);
        familySearch.put("3", familyShelter);
        familySearch.put("5", unspecifiedShelter);
        youngAdultSearch.put("0", maleShelter);
        youngAdultSearch.put("1", femaleShelter);
        youngAdultSearch.put("4", youngAdultShelter);
        youngAdultSearch.put("5", unspecifiedShelter);
        //unspecifiedSearch.put("5", unspecifiedShelter);
    }

    @Test
    public void male_finds_male_shelters() throws Exception {
        assertEquals(maleSearch, list.getByRestriction("Men", null, null));
    }

    @Test
    public void female_finds_female_shelters() throws Exception {
        assertEquals(femaleSearch, list.getByRestriction("Women", null, null));
    }

    @Test
    public void child_finds_child_shelters() throws Exception {
        assertEquals(childSearch, list.getByRestriction(null, "Children", null));
    }

    @Test
    public void family_finds_family_shelters() throws Exception {
        assertEquals(familySearch, list.getByRestriction(null,
                "Families w/ newborns", null));
    }

    @Test
    public void youngAdult_finds_youngAdult_shelters() throws Exception {
        assertEquals(youngAdultSearch, list.getByRestriction(null,
                "Young adults", null));
    }

}
