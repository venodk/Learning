
package asg.onlinetest;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UniqueName {
	public static String firstUniqueName(String[] names) {
		/*Integer ONE = new Integer(1);
		Integer TWO = new Integer(2);
		HashMap<String, Integer> hm = new HashMap<>();
		List<String> list = new LinkedList<String>();
		
		for (String tmp : names) {
			Integer find = hm.get(tmp);
			if (null == find) {
				hm.put(tmp, ONE);
				list.add(tmp);
			} else if (find.intValue() == 1) {
				hm.put(tmp, new Integer(TWO));
				list.remove(tmp);
			}
		}
		return list.isEmpty()? null:list.get(0);*/
		
		/*Map<String, Long> counters = Arrays.stream(names).collect(Collectors.groupingBy(p -> p, Collectors.counting()));
		for (String tmp : names) {
			if (counters.get(tmp).longValue() == 1)
				return tmp;
		}
		return null;*/
		
		return Arrays.stream(names)
				.collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
				.entrySet().stream().filter(entry -> entry.getValue() == 1L).map(entry -> entry.getKey()).findFirst()
				.get();
	}

	public static void main(String[] args) {
		System.out.println(firstUniqueName(new String[] { "Abbi", "Adeline", "Abbi", "Adalia" }));
	}
}
	