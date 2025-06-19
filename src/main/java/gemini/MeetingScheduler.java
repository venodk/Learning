package gemini;

import java.util.List;

public class MeetingScheduler {

    public static boolean canAttendAllMeetings(List<Meeting> meetings) {
        if (meetings == null || meetings.isEmpty()) {
            return true;
        }

        meetings.sort((a, b) -> Integer.compare(a.start(), b.start()));
        Meeting prevMeeting = null;
        for (Meeting current : meetings) {
            if (prevMeeting != null) {
                if (prevMeeting.end() > current.start()) return false;
            }
            prevMeeting = current;
        }
        return true;
    }
}
