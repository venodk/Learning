package gemini;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SumToZeroTest {
    @Test
    void testIsSumExistsWithNull() {
        assertFalse(SumToZero.isSumExists(null));
    }

    @Test
    void testIsSumExistsWithEmptyList() {
        assertFalse(SumToZero.isSumExists(List.of()));
    }

    @Test
    void testIsSumExistsWithZeroSumList() {
        assertTrue(SumToZero.isSumExists(List.of(-1, 0, 1, 2, -1, -4)));
    }

    @Test
    void testIsSumExistsWithNonZeroSumList() {
        assertFalse(SumToZero.isSumExists(List.of(1, 2, 3, 4, 5)));
    }

    @Test
    void testIsSumExistsWithSingleElementList() {
        assertFalse(SumToZero.isSumExists(List.of(-1)));
    }

    @Test
    void testIsSumExistsWithTwoElementList() {
        assertFalse(SumToZero.isSumExists(List.of(-1, 1)));
    }

    @Test
    void testIsSumExistsWithBigElementList() {
        assertFalse(SumToZero.isSumExists(List.of(Integer.MAX_VALUE, Integer.MAX_VALUE - 1, 0)));
    }

    @Test
    void testIsSumExistsWithBigElementList2() {
        assertTrue(SumToZero.isSumExists(List.of(Integer.MAX_VALUE, Integer.MIN_VALUE, 1)));
    }

    @Test
    void testIsSumExistsWithZeroSumSet() {
        assertTrue(SumToZero.isSumExists(Set.of(-1, 0, 1, 2, -4)));
    }

    @Test
    void testIsSumExistsWithNonZeroSumSet() {
        assertFalse(SumToZero.isSumExists(Set.of(1, 2, 3, 4, 5)));
    }

    // Gemini added test cases

    @Test
    void testIsSumExistsWithDuplicateZeroes() {
        assertTrue(SumToZero.isSumExists(List.of(0, 0, 0)));
    }

    @Test
    void testIsSumExistsWithNegativeNumbers() {
        assertTrue(SumToZero.isSumExists(List.of(-2, -1, 0, 1, 2)));
    }

    @Test
    void testIsSumExistsWithLessThanThreeElements() {
        assertFalse(SumToZero.isSumExists(List.of(1, 2)));
    }
}
