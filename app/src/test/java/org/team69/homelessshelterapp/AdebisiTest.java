package org.team69.homelessshelterapp;
import org.junit.Before;
import org.junit.Test;
import org.team69.homelessshelterapp.model.Shelter;

//no more methods in model that use loops or if statements
//test claimed and released rooms methods

import static org.junit.Assert.assertEquals;

public class AdebisiTest {
    private Shelter testShelter1;
    private Shelter testShelter2;

    @Before
    public void setUp() {
        testShelter1 = new Shelter("testShelter1", "10", "f","10",
                "10","123 Main Street Smalltown, USA","888888888","7");
        testShelter2 = new Shelter("testShelter2", "45", "f","5",
                "5","123 Main Street Smalltown, USA","888888888","20");
        testShelter1.claimRooms(3);
        testShelter2.releaseRooms(7);
    }

    @Test(timeout = 200)
    public void testShelterGetClaimedRooms() {
        assertEquals(testShelter1.getClaimedRooms(),
                "10");
    }

    @Test(timeout = 200)
    public void testShelterGetClaimedReleasedRooms() {
        assertEquals(testShelter2.getClaimedRooms(),
                "13");
    }

}