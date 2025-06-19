package gemini.meeting;

// Gemini suggested to use separate class to record booking instead of int[2]
public record Booking(int startTime, int endTime) {
    public Booking {
        if (startTime < 0 || endTime < 0) {
            throw new IllegalArgumentException("Start or end time must be non-negative.");
        }
        if (startTime >= endTime) {
            throw new IllegalArgumentException("Start time must be less than end time.");
        }
    }
}
