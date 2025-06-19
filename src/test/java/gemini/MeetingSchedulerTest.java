package gemini;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MeetingSchedulerTest {

    @Test
    void test_canAttendNullMeeting() {
        assertTrue(MeetingScheduler.canAttendAllMeetings(null));
    }

    @Test
    void test_canAttendEmptyMeeting() {
        assertTrue(MeetingScheduler.canAttendAllMeetings(List.of()));
    }

    @Test
    void test_canAttendSingleMeeting() {
        List<Meeting> meetings = new ArrayList<>();
        meetings.add(new Meeting(3, 4));
        assertTrue(MeetingScheduler.canAttendAllMeetings(meetings));
    }

    @Test
    void testCanAttendMultipleMeetingsNoConflict() {
        List<Meeting> meetings = new ArrayList<>();
        meetings.add(new Meeting(7, 10));
        meetings.add(new Meeting(2, 4));
        meetings.add(new Meeting(5, 7));
        assertTrue(MeetingScheduler.canAttendAllMeetings(meetings));
    }

    @Test
    void testCanAttendMultipleMeetingsConflict() {
        List<Meeting> meetings = new ArrayList<>();
        meetings.add(new Meeting(0, 30));
        meetings.add(new Meeting(5, 10));
        meetings.add(new Meeting(15, 20));
        assertFalse(MeetingScheduler.canAttendAllMeetings(meetings)); // Changed to assertFalse
    }

    @Test
    void testCanAttendMeetingsWithAdjacentMeetings() {
        List<Meeting> meetings = new ArrayList<>();
        meetings.add(new Meeting(1, 2));
        meetings.add(new Meeting(2, 3));
        meetings.add(new Meeting(3, 4));
        assertTrue(MeetingScheduler.canAttendAllMeetings(meetings));
    }

    @Test
    void testCanAttendMeetingsWithOverlappingMeetings() {
        List<Meeting> meetings = new ArrayList<>();
        meetings.add(new Meeting(1, 3));
        meetings.add(new Meeting(2, 4));
        assertFalse(MeetingScheduler.canAttendAllMeetings(meetings));
    }

    @Test
    void testMeetingConstructorThrowsExceptionForInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> new Meeting(3, 3));
        assertThrows(IllegalArgumentException.class, () -> new Meeting(4, 3));
        assertThrows(IllegalArgumentException.class, () -> new Meeting(-1, 5));
        assertThrows(IllegalArgumentException.class, () -> new Meeting(5, -1));
    }
}
