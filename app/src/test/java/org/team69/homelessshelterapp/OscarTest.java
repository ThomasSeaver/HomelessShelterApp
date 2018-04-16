package org.team69.homelessshelterapp;
import org.junit.Before;
import org.junit.Test;

import org.team69.homelessshelterapp.model.Shelter;


import static org.junit.Assert.assertEquals;

public class OscarTest {
    private Shelter shelter1;
    private Shelter shelter2;

    @Before
    public void setUp() {
        shelter1 = new Shelter("name1", "1", "f","1",
                "1","a","9","0");
        shelter2 = new Shelter("name2", "Not available", "f","1",
                "1","a","9","0");
    }

    @Test(timeout = 200)
    public void testShelterGetInfoCapacity() {
        assertEquals(shelter1.getInfo(),
                "Capacity: 1\nAccepts: f\nPhone Number: 9\nAddress: a");
    }

    @Test(timeout = 200)
    public void testShelterGetInfoNoCapacity() {
        assertEquals(shelter2.getInfo(),
                "Capacity: Not available\nAccepts: f\nPhone Number: 9\nAddress: a");
    }

}