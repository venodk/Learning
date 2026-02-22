package gemini;

import java.util.List;

public class LongestCommonPrefix {
    public static String find(final List<String> words) {
        if (words == null || words.isEmpty() || words.stream().anyMatch(w -> w == null || w.isEmpty())) {
            return "";
        }

        /**
         * Don't do binary search,
         * may be the mid-value match for all words, but there might char don't match on the left of mid
         * this case cannot identify with binary search
         * ("abcde", "accde"). Mid-value c matches, but there is a miss in the left of mid(b != c)
        **/

        int minLength = words.stream().mapToInt(String::length).min().getAsInt();
        for (int i = 0; i < minLength; i++) {
            char c = words.getFirst().charAt(i);
            for (int j = 1; j < words.size(); j++) {
                if (words.get(j).charAt(i) != c) {
                    return words.getFirst().substring(0, i);
                }
            }
        }
        return words.getFirst().substring(0, minLength);
    }
}
