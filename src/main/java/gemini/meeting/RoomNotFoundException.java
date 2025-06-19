package gemini.meeting;

//Custom Exception - gemini suggestion
public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(String message) {
        super(message);
    }
}
