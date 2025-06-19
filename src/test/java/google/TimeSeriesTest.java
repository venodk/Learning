package google;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeSeriesTest {

    @Test
    void testGivenInput() {
        List<TimeSeries.Pair<Integer, Integer>> expectedResult = new ArrayList<>();
        expectedResult.add(new TimeSeries.Pair<>(0,0));
        expectedResult.add(new TimeSeries.Pair<>(3,1));
        expectedResult.add(new TimeSeries.Pair<>(5,3));
        expectedResult.add(new TimeSeries.Pair<>(7,5));
        expectedResult.add(new TimeSeries.Pair<>(10, 3));
        expectedResult.add(new TimeSeries.Pair<>(12,0));

        List<List<TimeSeries.Pair<Integer, Integer>>> input = getLists();
        assertEquals(expectedResult, TimeSeries.mergeSeries(input));
    }

    private static List<List<TimeSeries.Pair<Integer, Integer>>> getLists() {
        List<TimeSeries.Pair<Integer, Integer>> list1 = new ArrayList<>();
        list1.add(new TimeSeries.Pair<>(0,0));
        list1.add(new TimeSeries.Pair<>(5,2));
        list1.add(new TimeSeries.Pair<>(10,0));

        List<TimeSeries.Pair<Integer, Integer>> list2 = new ArrayList<>();
        list2.add(new TimeSeries.Pair<>(0,0));
        list2.add(new TimeSeries.Pair<>(3,1));
        list2.add(new TimeSeries.Pair<>(7,3));
        list2.add(new TimeSeries.Pair<>(12,0));

        List<List<TimeSeries.Pair<Integer, Integer>>> input = new ArrayList<>();
        input.add(list1);
        input.add(list2);
        return input;
    }
}
