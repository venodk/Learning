import java.util.Arrays;

public class BinarySearch {
    public static void main(String[] args) {
        int[] nums = new int[] {1,2,4,5};

        System.out.println("Search result for 0 - " + Arrays.binarySearch(nums, 0));
        System.out.println("Search result for 3 - " + Arrays.binarySearch(nums, 3));
        System.out.println("Search result for 5 - " + Arrays.binarySearch(nums, 5));
        System.out.println("Search result for 10 - " + Arrays.binarySearch(nums, 10));
    }
}
