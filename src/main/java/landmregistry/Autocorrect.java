package landmregistry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Autocorrect {
    public static String autocorrect(String input) {
        // Match standalone "u", "you", "youuu..." with optional extra 'u's
        // don't replace if it is in the middle of a word. case-insensitive
        Pattern pattern = Pattern.compile("\\b(u|you+)(?=\\b)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll("your client");
    }

    public static void main(String[] args) {
        String message1 = "Hey u, did you see that?";
        String message2 = "YOUUU should respond to your emails!";
        String message3 = "You guys rock!";
        String message4 = "Bayou is a nice place.";

        System.out.println(autocorrect(message1)); // "Hey your client, did your client see that?"
        System.out.println(autocorrect(message2)); // "your client should respond to your emails!"
        System.out.println(autocorrect(message3)); // "your client guys rock!"
        System.out.println(autocorrect(message4)); // "Bayou is a nice place." (unchanged)
    }
}
