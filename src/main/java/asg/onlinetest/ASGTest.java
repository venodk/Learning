package asg.onlinetest;

import java.util.*;
import java.text.SimpleDateFormat;
/**
 * @author vkunnakkattil
 *
 */
public class ASGTest {

	/**
     */

	public static Boolean canViewAll(Collection<Movie> movies) {
		if (movies.isEmpty())
			return true;

		List<Movie> result = movies.stream().sorted(Comparator.comparing(Movie::start)).toList();

		Movie prev = null;
		for (Movie current : result) {
			if (null != prev) {
				if (prev.end().compareTo(current.start()) > 0)
					return false;
			}
			prev = current;
		}
		return true;
	}

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("y-M-d H:m");

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie(sdf.parse("2015-01-01 20:00"), sdf.parse("2015-01-01 21:30")));
        movies.add(new Movie(sdf.parse("2015-01-01 21:30"), sdf.parse("2015-01-01 23:30")));
        movies.add(new Movie(sdf.parse("2015-01-01 21:30"), sdf.parse("2015-01-01 23:00")));

        System.out.println(ASGTest.canViewAll(movies));
    }

}

record Movie(Date start, Date end) { }
