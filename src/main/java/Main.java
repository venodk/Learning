import java.util.*;

public class Main {
    public static void main(String[] args) {
        int[] nums = new int[10];
        int maxElement = Arrays.stream(nums).max().getAsInt();

        StringJoiner joiner = new StringJoiner(", ");
        Set<Integer> result = new HashSet<>();
        result.add(10);
        result.add(1);

        result.stream().sorted().map(x -> x.toString()).forEach(joiner::add);
        String res = joiner.toString();
        System.out.println(res);
    }

    public static Integer[] tryWith(final Collection<Integer> numbers) {
        Integer[] x = numbers.stream().map(e -> e * e).sorted().toArray(Integer[]::new);
        return numbers.parallelStream().sorted().toArray(Integer[]::new);
    }

    private static void caller() {
        tryWith( Set.of(3, 4));
    }
}

