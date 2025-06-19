package gemini;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LongestCommonPrefixTest {

    @Test
    void testNullInput() {
        assertEquals("", LongestCommonPrefix.find(null));
    }

    @Test
    void testEmptyInput() {
        assertEquals("", LongestCommonPrefix.find(List.of("")));
    }

    @Test
    void testAllMatch() {
        List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("aaa");
        list.add("aaa");
        assertEquals("aaa", LongestCommonPrefix.find(list));
    }

    @Test
    void testVaryingLengthMatch() {
        List<String> list = new ArrayList<>();
        list.add("aaab");
        list.add("aaac");
        list.add("aaade");
        assertEquals("aaa", LongestCommonPrefix.find(list));
    }

    @Test
    void testVaryingLengthWithEmptyWordMatch() {
        List<String> list = new ArrayList<>();
        list.add("aaab");
        list.add("aaac");
        list.add("aaad");
        list.add("");
        assertEquals("", LongestCommonPrefix.find(list));
    }

    @Test
    void testVaryingLengthWithNullMatch() {
        List<String> list = new ArrayList<>();
        list.add("aaab");
        list.add("aaac");
        list.add("aaad");
        list.add(null);
        assertEquals("", LongestCommonPrefix.find(list));
    }

    /** improvement suggested by AI. */
    @Test
    void testVaryingLengthNoMatch() {
        List<String> list = new ArrayList<>();
        list.add("dog");
        list.add("racecar");
        list.add("car");
        assertEquals("", LongestCommonPrefix.find(list));
    }

    @Test
    void testSingleWord() {
        List<String> list = new ArrayList<>();
        list.add("flower");
        assertEquals("flower", LongestCommonPrefix.find(list));
    }
}
