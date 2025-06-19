import java.util.*;

public class GoodNotes {

    // apple, banana, cabbage
    // abage
    public static String generateString(List<String> inputs) {
        Map<Character, List<Character>> charMap = new HashMap<>();
        Set<Character> endChars = new HashSet<>();
        for (String input : inputs) {
            Character prev = null;
            for (Character ch : input.toCharArray()) {
                if (prev != null) {
                    var set = charMap.computeIfAbsent(prev, k -> new ArrayList<>());
                    set.add(ch);
                }
                prev = ch;
            }
            endChars.add(input.charAt(input.length() - 1));
        }

        //System.out.println(charMap);

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int nextIndex = random.nextInt(inputs.size());
        String startingString = inputs.get(nextIndex);
        Character firstChar = startingString.toCharArray()[0];
        sb.append(firstChar);

        while (true) {
            List<Character> charSet = charMap.get(firstChar);
            if (charSet == null) {
                break;
            }
            nextIndex = random.nextInt(charSet.size());
            Character next = charSet.get(nextIndex);
            if (next == null) {
                break;
            }
            sb.append(next);
            if (nextIndex <= charSet.size() * 0.1 && endChars.contains(next)) {
                break;
            }

            firstChar = next;
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        List<String> inputs = new ArrayList<>();
        inputs.add("apple");
        inputs.add("banana");
        inputs.add("cabbage");

        for (int i = 0; i < 100; i++) {
            String output = generateString(inputs);
            System.out.println(output);
        }
    }
}
