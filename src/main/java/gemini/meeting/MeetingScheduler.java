package gemini.meeting;


import java.util.HashMap;
import java.util.Map;

public class MeetingScheduler {
    Map<Integer, Room> meetingRooms;

    public MeetingScheduler(int numOfRooms) {
        // Gemini suggestion for validation.
        if (numOfRooms <= 0) {
            throw new IllegalArgumentException("Number of rooms must be greater than 0.");
        }

        this.meetingRooms = new HashMap<>();
        for (int i = 1; i <= numOfRooms; i++) {
            meetingRooms.put(i, new Room("Meeting Room " + i, i));
        }
    }

    public void bookMeetingRoom(int roomId, int startTime, int endTime) {
        Room room = getMeetingRoom(roomId); // Get the room first.
        room.book(startTime, endTime); // Then, book.
    }

    public boolean isMeetingRoomAvailable(int roomId, int startTime, int endTime) {
        return getMeetingRoom(roomId).isAvailable(startTime, endTime);
    }

    private Room getMeetingRoom(int roomId) {
        if (!meetingRooms.containsKey(roomId)) {
            throw new RoomNotFoundException("Meeting Room " + roomId + " not found.");
        }
        return meetingRooms.get(roomId);
    }
}
