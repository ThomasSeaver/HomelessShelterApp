package org.team69.homelessshelterapp.model;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

public class ParkerTest {
    private Shelter shelter1;
    private ShelterList shelterList1;
    private Shelter nullshelter;
    private Shelter sheltertest1;

    @Before
    public void setUp() {
        Shelter shelter1 = new Shelter("n", "x", "f", "1", "1", "2", "3", "4");
        ShelterList shelterList1 = new ShelterList();
        shelterList1.addShelter("shelter1",shelter1);
        Shelter sheltertest1 = shelterList1.findShelterByID("shelter1");
        Shelter nullshelter = shelterList1.findShelterByID(null);
    }
    @Test(timeout = 200)
    public void testfindshelterID0() {
        assertEquals(sheltertest1,shelter1);
    }
    @Test(timeout = 200)
    public void testfindshelterID1() {
        assertEquals(nullshelter,null);
    }

}
