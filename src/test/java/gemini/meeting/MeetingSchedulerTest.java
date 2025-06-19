package gemini.meeting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MeetingSchedulerTest {

    @Test
    void testCreateMeetingSchedulerWithValidNumberOfRooms() {
        MeetingScheduler scheduler = new MeetingScheduler(3);
        // Verify that the scheduler is initialized with the correct number of rooms.
        assertEquals(3, scheduler.meetingRooms.size());
    }

    @Test
    void testCreateMeetingSchedulerWithInvalidNumberOfRooms() {
        // Verify that an IllegalArgumentException is thrown for invalid input.
        assertThrows(IllegalArgumentException.class, () -> new MeetingScheduler(0));
        assertThrows(IllegalArgumentException.class, () -> new MeetingScheduler(-1));
    }

    @Test
    void testBookMeetingRoom_ValidBooking() {
        MeetingScheduler scheduler = new MeetingScheduler(3);
        int roomId = 1;
        int startTime = 10;
        int endTime = 13;
        // Verify that a booking can be made successfully.
        assertDoesNotThrow(() -> scheduler.bookMeetingRoom(roomId, startTime, endTime));
        assertFalse(scheduler.isMeetingRoomAvailable(roomId, startTime, endTime));
    }

    @Test
    void testBookMeetingRoom_RoomNotFound() {
        MeetingScheduler scheduler = new MeetingScheduler(3);
        int nonExistentRoomId = 4;
        int startTime = 10;
        int endTime = 12;
        // Verify that a RoomNotFoundException is thrown for a non-existent room.
        assertThrows(RoomNotFoundException.class, () -> scheduler.bookMeetingRoom(nonExistentRoomId, startTime, endTime));
    }

    @Test
    void testBookMeetingRoom_InvalidTime() {
        MeetingScheduler scheduler = new MeetingScheduler(3);
        int roomId = 1;
        int startTime = 12;
        int endTime = 10;
        // Verify that an IllegalArgumentException is thrown for invalid time.
        assertThrows(IllegalArgumentException.class, () -> scheduler.bookMeetingRoom(roomId, startTime, endTime));
    }

    @Test
    void testBookMeetingRoom_RoomNotAvailable() {
        MeetingScheduler scheduler = new MeetingScheduler(3);
        int roomId = 1;
        int startTime1 = 10;
        int endTime1 = 12;
        int startTime2 = 11;
        int endTime2 = 13;
        scheduler.bookMeetingRoom(roomId, startTime1, endTime1);
        // Verify that a room cannot be double-booked.
        assertThrows(IllegalArgumentException.class, () -> scheduler.bookMeetingRoom(roomId, startTime2, endTime2));
        assertFalse(scheduler.isMeetingRoomAvailable(roomId, startTime2, endTime2));
    }

    @Test
    void testIsMeetingRoomAvailable_RoomNotFound() {
        MeetingScheduler scheduler = new MeetingScheduler(3);
        int nonExistentRoomId = 4;
        int startTime = 10;
        int endTime = 12;
        // Verify that a RoomNotFoundException is thrown for a non-existent room.
        assertThrows(RoomNotFoundException.class, () -> scheduler.isMeetingRoomAvailable(nonExistentRoomId, startTime, endTime));
    }

    @Test
    void testIsMeetingRoomAvailable_Available() {
        MeetingScheduler scheduler = new MeetingScheduler(3);
        int roomId = 1;
        int startTime1 = 10;
        int endTime1 = 12;
        int startTime2 = 13;
        int endTime2 = 15;
        scheduler.bookMeetingRoom(roomId, startTime1, endTime1);
        // Verify that the room is available for a non-overlapping time slot.
        assertTrue(scheduler.isMeetingRoomAvailable(roomId, startTime2, endTime2));
    }

    @Test
    void testIsMeetingRoomAvailable_NotAvailable() {
        MeetingScheduler scheduler = new MeetingScheduler(3);
        int roomId = 1;
        int startTime1 = 10;
        int endTime1 = 12;
        int startTime2 = 11;
        int endTime2 = 13;
        scheduler.bookMeetingRoom(roomId, startTime1, endTime1);
        // Verify that the room is not available for an overlapping time slot.
        assertFalse(scheduler.isMeetingRoomAvailable(roomId, startTime2, endTime2));
    }
    // Room Class Tests
    @Test
    void testRoomConstructor() {
        Room room = new Room("Meeting Room 1", 1);
        assertEquals("Meeting Room 1", room.getName());
        assertEquals(1, room.getRoomId());
    }

    @Test
    void testBookRoom_ValidBooking() {
        Room room = new Room("Meeting Room 1", 1);
        int startTime = 10;
        int endTime = 12;
        assertDoesNotThrow(() -> room.book(startTime, endTime));
    }

    @Test
    void testBookRoom_InvalidTime() {
        Room room = new Room("Meeting Room 1", 1);
        int startTime = 12;
        int endTime = 10;
        assertThrows(IllegalArgumentException.class, () -> room.book(startTime, endTime));
    }

    @Test
    void testBookRoom_RoomNotAvailable() {
        Room room = new Room("Meeting Room 1", 1);
        int startTime1 = 10;
        int endTime1 = 12;
        int startTime2 = 11;
        int endTime2 = 13;
        room.book(startTime1, endTime1);
        assertThrows(IllegalArgumentException.class, () -> room.book(startTime2, endTime2));
    }

    @Test
    void testIsAvailable_Available() {
        Room room = new Room("Meeting Room 1", 1);
        int startTime1 = 10;
        int endTime1 = 12;
        int startTime2 = 13;
        int endTime2 = 15;
        room.book(startTime1, endTime1);
        assertTrue(room.isAvailable(startTime2, endTime2));
    }

    @Test
    void testIsAvailable_NotAvailable() {
        Room room = new Room("Meeting Room 1", 1);
        int startTime1 = 10;
        int endTime1 = 12;
        int startTime2 = 11;
        int endTime2 = 13;
        room.book(startTime1, endTime1);
        assertFalse(room.isAvailable(startTime2, endTime2));
    }

    @Test
    void testIsAvailable_EdgeCases() {
        Room room = new Room("Meeting Room 1", 1);
        room.book(10, 20);
        // Test for adjacent bookings
        assertTrue(room.isAvailable(20, 25));
        assertTrue(room.isAvailable(5, 10));

        //Test for overlapping at the start
        assertFalse(room.isAvailable(15, 25));
        assertFalse(room.isAvailable(10, 15));

        // Test for completely overlapping
        assertFalse(room.isAvailable(11, 19));
    }

    @Test
    void testRoomGetName() {
        Room room = new Room("Meeting Room 1", 1);
        assertEquals("Meeting Room 1", room.getName());
    }

    @Test
    void testRoomGetRoomId() {
        Room room = new Room("Meeting Room 1", 1);
        assertEquals(1, room.getRoomId());
    }
    // Booking Record Tests
    @Test
    void testValidBooking() {
        Booking booking = new Booking(10, 20);
        assertEquals(10, booking.startTime());
        assertEquals(20, booking.endTime());
    }

    @Test
    void testInvalidStartTimeNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Booking(-10, 20));
        assertEquals("Start or end time must be non-negative.", exception.getMessage());
    }

    @Test
    void testInvalidEndTimeNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Booking(10, -20));
        assertEquals("Start or end time must be non-negative.", exception.getMessage());
    }

    @Test
    void testInvalidStartTimeEqualToEndTime() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Booking(10, 10));
        assertEquals("Start time must be less than end time.", exception.getMessage());
    }

    @Test
    void testInvalidStartTimeGreaterThanEndTime() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Booking(20, 10));
        assertEquals("Start time must be less than end time.", exception.getMessage());
    }

    @Test
    void testValidBookingWithZeroStartTime() {
        Booking booking = new Booking(0, 1);
        assertEquals(0, booking.startTime());
        assertEquals(1, booking.endTime());
    }

    @Test
    void testValidBookingWithLargeValues() {
        Booking booking = new Booking(Integer.MAX_VALUE - 1, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE - 1, booking.startTime());
        assertEquals(Integer.MAX_VALUE, booking.endTime());
    }
}
