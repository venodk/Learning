package gemini.meeting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    @Test
    void testValidBooking() {
        var booking = assertDoesNotThrow(() -> new Booking(10, 20));
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
        Booking booking = assertDoesNotThrow(() -> new Booking(0, 1));
        assertEquals(0, booking.startTime());
        assertEquals(1, booking.endTime());
    }

    @Test
    void testValidBookingWithLargeValues() {
        Booking booking = assertDoesNotThrow(() -> new Booking(Integer.MAX_VALUE - 1, Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE - 1, booking.startTime());
        assertEquals(Integer.MAX_VALUE, booking.endTime());
    }
}
