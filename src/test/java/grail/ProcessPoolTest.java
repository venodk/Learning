package grail;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ProcessPoolTest {

    @Test
    void testNoDuplicateIds() {
        ProcessPool pool = new ProcessPool();
        int id1 = pool.getId();
        int id2 = pool.getId();
        int id3 = pool.getId();
        assertNotEquals(-1, id1);
        assertNotEquals(-1, id2);
        assertNotEquals(-1, id3);
        assertNotEquals(id1, id2);
        assertNotEquals(id2, id3);
        assertNotEquals(id1, id3);
    }

    @Test
    void testNoDuplicatesAfterRelease() {
        ProcessPool pool = new ProcessPool();
        int id1 = pool.getId();
        int id2 = pool.getId();
        int id3 = pool.getId();
        assertNotEquals(-1, id1);
        assertNotEquals(-1, id2);
        assertNotEquals(-1, id3);
        pool.releaseId(id1);
        pool.releaseId(id2);
        pool.releaseId(id3);
        int id4 = pool.getId();
        assertNotEquals(id4, id1);
        assertNotEquals(id4, id2);
        assertNotEquals(id4, id3);
    }

    @Test
    void testIdsReusedAfterRelease() {
        ProcessPool pool = new ProcessPool(3);
        int id1 = pool.getId();
        int id2 = pool.getId();
        int id3 = pool.getId();
        assertNotEquals(-1, id1);
        assertNotEquals(-1, id2);
        assertNotEquals(-1, id3);
        pool.releaseId(id1);
        pool.releaseId(id2);
        pool.releaseId(id3);
        int id4 = pool.getId();
        assertEquals(id4, id1);
        int id5 = pool.getId();
        assertEquals(id5, id2);
    }

    @Test
    void testExhaustedProcessPool() {
        ProcessPool pool = new ProcessPool(3);
        int id1 = pool.getId();
        int id2 = pool.getId();
        int id3 = pool.getId();
        assertNotEquals(-1, id1);
        assertNotEquals(-1, id2);
        assertNotEquals(-1, id3);
        int id4 = pool.getId();
        assertEquals(-1, id4);
        int id5 = pool.getId();
        assertEquals(-1, id5);
    }
}
