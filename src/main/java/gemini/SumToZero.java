package gemini;

import java.util.Collection;

public class SumToZero {
    public static boolean isSumExists(Collection<Integer> numbers) {
        if (numbers == null || numbers.isEmpty()) return false;

        Integer[] sortedNumbers = numbers.stream().sorted().toArray(Integer[]::new);
        for (int i = 0; i < sortedNumbers.length; i++) {
            int left = i + 1;
            int right = sortedNumbers.length - 1;
            while (left < right) {
                int sum = sortedNumbers[i] + sortedNumbers[left] + sortedNumbers[right];
                if (sum == 0) {
                    return true;
                } else if (sum < 0 ) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return false;
    }

    /**
     * Gemini suggestion,
     * 1. see how they converted a collection to Integer array
     * 2. How the array is sorted

    public static boolean isSumExists(Collection<Integer> numbers) {
        if (numbers == null || numbers.size() < 3) { // Changed to size()
            return false;
        }

        Integer[] sortedNumbers = numbers.toArray(new Integer[0]); // toArray(new Integer[0])
        Arrays.sort(sortedNumbers); // Use Arrays.sort()
        for (int i = 0; i < sortedNumbers.length - 2; i++) { // Loop until length - 2
            int left = i + 1;
            int right = sortedNumbers.length - 1;
            while (left < right) {
                int sum = sortedNumbers[i] + sortedNumbers[left] + sortedNumbers[right];
                if (sum == 0) {
                    return true;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return false;
    }
    **/
}
