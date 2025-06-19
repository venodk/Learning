package google;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class TimeSeries {

    public static List<Pair<Integer, Integer>> mergeSeries(List<List<Pair<Integer, Integer>>> series) {
        List<Pair<Integer, Integer>> result = new LinkedList<>();
        if (null == series || series.isEmpty()) return result;

        int sum = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));
        for (int i = 0; i < series.size(); i++) {
            var currentSeries =series.get(i).getFirst();
            pq.add(new int[] {i, 0, currentSeries.key, currentSeries.value});
        }

        while (!pq.isEmpty()) {
            var recentSeries = pq.poll();
            var diff = findValueDiff(recentSeries, series, pq);
            while (!pq.isEmpty() && pq.peek()[2] == recentSeries[2]) {
                var similarSeries = pq.poll();
                diff += findValueDiff(similarSeries, series, pq);
            }
            if (result.isEmpty() || result.getLast().value != sum + diff) {
                sum += diff;
                result.add(new Pair<>(recentSeries[2], sum));
            }
        }

        return result;
    }

    private static int findValueDiff(int[] input, List<List<Pair<Integer, Integer>>> series, PriorityQueue<int[]> pq) {
        int seriesId = input[0];
        int index = input[1];
        int value = input[3];

        if (index > 0) {
            value -= series.get(seriesId).get(index - 1).value;
        }
        if (index < series.get(seriesId).size() - 1) {
            var next = series.get(seriesId).get(index + 1);
            pq.add(new int[] {seriesId, index + 1, next.key, next.value});
        }

        return value;
    }

    public record Pair<K, V>(K key, V value) {}
}
