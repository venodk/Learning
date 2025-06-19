package gemini;

public record Meeting(int start, int end){
    public Meeting {
        if (start < 0 || end < 0) {
            throw new IllegalArgumentException("Start and end times must be non-negative.");
        }
        if (start >= end) {
            throw new IllegalArgumentException("Start time must be less than end time.");
        }
    }
}
