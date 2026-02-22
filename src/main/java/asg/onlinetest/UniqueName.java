
package asg.onlinetest;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UniqueName {
	public static String firstUniqueName(String[] names) {
		return Arrays.stream(names)
				.collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
				.entrySet().stream().filter(entry -> entry.getValue() == 1L).map(Map.Entry::getKey).findFirst()
				.orElseGet(String::new);
	}

	public static void main(String[] args) {
		System.out.println(firstUniqueName(new String[] { "Abbi", "Adeline", "Abbi", "Adalia" }));
	}
}
	