import java.util.*;

public class FindNthMax {

    public static int findMax(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (int num : nums) {
            pq.add(num);
            if (pq.size() > k) {
                pq.poll();
            }
        }
        return pq.peek();
        /*TreeSet<Integer> set = new TreeSet<>(Comparator.reverseOrder());
        for (int num : nums) {
            set.add(num);
        }

        if (set.size() < k) {
            return set.first();
        }

        int i = 0;
        while (i < k - 1 && !set.isEmpty()) {
            set.remove(set.first());
            i++;
        }
        return set.first();*/
    }

    public static void main(String[] args) {
        int[] input = new int[]{1, 2, 3, 4};
        int result = FindNthMax.findMax(input, 3);
        System.out.println(result);

        input = new int[]{1, 2};
        result = FindNthMax.findMax(input, 3);
        System.out.println(result);

        input = new int[]{1, 1, 2, 2, 3, 3};
        result = FindNthMax.findMax(input, 3);
        System.out.println(result);

        input = new int[]{4, 7, 1, 9, 3, 7, 5, 6};
        result = FindNthMax.findMax(input, 3);
        System.out.println(result);

    }
}
