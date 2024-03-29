package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {

    private Future<String> future;

    @BeforeEach
    public void setUp() {
        future = new Future<>();
    }

    @Test
    public void testResolve() throws InterruptedException {
        String str = "someResult";
        future.resolve(str);
        assertTrue(future.isDone());
        assertTrue(str.equals(future.get()));
    }

    @Test
    public void testIsDone() {
        String str = "Test";
        assertFalse(future.isDone());
        future.resolve(str);
        assertTrue(future.isDone());
    }

    @Test
    public void testGet() throws InterruptedException {
        assertFalse(future.isDone());
        future.resolve("");
        future.get();
        assertTrue(future.isDone());
    }

    @Test
    public void testGetWithParams() throws InterruptedException {
        assertFalse(future.isDone());
        future.get(100, TimeUnit.MILLISECONDS);
        assertFalse(future.isDone());
        future.resolve("Test");
        assertEquals(future.get(100, TimeUnit.MILLISECONDS), "Test");
    }
}