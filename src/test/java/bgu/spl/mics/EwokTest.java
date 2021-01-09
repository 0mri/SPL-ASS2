package bgu.spl.mics;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bgu.spl.mics.application.passiveObjects.Ewok;

public class EwokTest {

    private Ewok e;
    @BeforeEach
    public void setUp() {
        e = new Ewok(1);
    }

    @Test
    public void acquireTest() {
        e.acquire();
        assertFalse(e.isAvailable());
    }

    @Test
    public void releaseTest() {
        e.release();
        assertTrue(e.isAvailable());
    }

}