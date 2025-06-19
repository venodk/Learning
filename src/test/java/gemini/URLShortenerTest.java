package gemini;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class URLShortenerTest {

    @Test
    void testGenerateShortURLWithNewURL() {
        URLShortener shortener = new URLShortener();
        String longURL = "https://www.example.com/very/long/url";
        String shortURL = shortener.generateShortURL(longURL);
        assertNotNull(shortURL);
        // gemini improvement
        assertTrue(shortURL.startsWith("shorturl.com/"));
        assertEquals(longURL, shortener.getLongURL(shortURL));
    }

    @Test
    void testGenerateShortURLWithExistingURL() {
        URLShortener shortener = new URLShortener();
        String longURL = "https://www.example.com/very/long/url";
        String shortURL1 = shortener.generateShortURL(longURL);
        String shortURL2 = shortener.generateShortURL(longURL);
        assertNotNull(shortURL1);
        assertEquals(shortURL1, shortURL2);
    }

    @Test
    void testGetLongURLWithExistingURL() {
        URLShortener shortener = new URLShortener();
        String longURL = "https://www.example.com/very/long/url";
        String shortURL = shortener.generateShortURL(longURL);
        assertNotNull(shortURL);
        String retrievedLongURL = shortener.getLongURL(shortURL);
        assertEquals(longURL, retrievedLongURL);
    }

    @Test
    void testGetLongURLWithNonExistingURL() {
        URLShortener shortener = new URLShortener();
        String longURL = "https://www.example.com/very/long/url";
        String shortURL = shortener.generateShortURL(longURL);
        assertNotNull(shortURL);
        String shortURL2 = shortener.getLongURL("https://www.example.com/");
        assertNull(shortURL2);
    }

    // gemini improvements based on exception logic
    @Test
    void testGenerateShortURLThrowsExceptionForNullInput() {
        URLShortener shortener = new URLShortener();
        assertThrows(IllegalArgumentException.class, () -> shortener.generateShortURL(null));
    }

    @Test
    void testGenerateShortURLThrowsExceptionForEmptyInput() {
        URLShortener shortener = new URLShortener();
        assertThrows(IllegalArgumentException.class, () -> shortener.generateShortURL(""));
    }
}
