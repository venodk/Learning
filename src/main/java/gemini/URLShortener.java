package gemini;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class URLShortener {

    private final Map<String, String> shortToLong;
    private final Map<String, String> longToShort;

    private static final String BASE_URL = "shorturl.com/";
    private static final int SHORT_URL_LENGTH = 8;

    public URLShortener() {
        this.shortToLong = new HashMap<>();
        this.longToShort = new HashMap<>();
    }

    public synchronized String generateShortURL(String longURL) {
        if (longURL == null || longURL.isEmpty()) {
            throw new IllegalArgumentException("Long URL cannot be null or empty.");
        }

        if (longToShort.containsKey(longURL)) {
            return longToShort.get(longURL);
        }
        String shortURL = generateShortURL();
        longToShort.put(longURL, shortURL);
        shortToLong.put(shortURL, longURL);
        return shortURL;
    }

    private String generateShortURL() {
        String shortURL;
        do {
            shortURL = BASE_URL + generateRandomString(SHORT_URL_LENGTH);
        } while (shortToLong.containsKey(shortURL));
        return shortURL;
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(0, characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    public synchronized String getLongURL(String shortURL) {
        return shortToLong.get(shortURL);
    }
}
