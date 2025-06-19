package gemini.meeting;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final String name;
    private final int roomId;
    private final List<Booking> bookings;

    public Room(String name, int roomId) {
        this.name = name;
        this.roomId = roomId;
        bookings = new ArrayList<>();
    }

    public void book(int startTime, int endTime) {
        if (isAvailable(startTime, endTime)) {
            bookings.add(new Booking(startTime, endTime));
        } else {
            throw new IllegalArgumentException("cannot schedule meeting for the given time.");
        }
    }

    public boolean isAvailable(int startTime, int endTime) {
        if (startTime >= endTime) throw new IllegalArgumentException("End time must be greater than start time.");
        for (Booking booking : bookings) {
            if (booking.startTime() < endTime && booking.endTime() > startTime) {
                return false;
            }
        }
        return true;
    }

    public String getName() { return this.name; }
    public int getRoomId() { return this.roomId; }
}
