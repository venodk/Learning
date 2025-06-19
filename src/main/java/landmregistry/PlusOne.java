package landmregistry;

import java.util.ArrayList;
import java.util.Collections;

public class PlusOne {
    public static ArrayList<Integer> plusOne(ArrayList<Integer> digits) {
        ArrayList<Integer> result = new ArrayList<>(digits); // Copy input
        int n = result.size();

        for (int i = n - 1; i >= 0; i--) {
            int current = result.get(i);
            if (current < 9) {
                result.set(i, current + 1);
                return result;
            }
            result.set(i, 0);
        }

        // If all digits were 9, add 1 at the beginning
        result.set(0, 1); // Set first to 1
        result.add(0); // Extend list size
        return result;
    }

    public static void main(String[] args) {
        ArrayList<Integer> input = new ArrayList<>();
        Collections.addAll(input, 9, 9, 9);
        ArrayList<Integer> output = PlusOne.plusOne(input);
        System.out.println("Input: " + input);   // Input: [9, 9, 9]
        System.out.println("Output: " + output); // Output: [1, 0, 0, 0]
    }
}
