package asg.onlinetest;
/**
 * 
 */
import java.util.*;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
/**
 * @author vkunnakkattil
 *
 */
public class ASGTest {

	/**
	 * @param args
	 */

	public static Boolean canViewAll(Collection<Movie> movies) {
		if (movies.isEmpty())
			return true;

		List<Movie> result = movies.stream().sorted((o1, o2) -> o1.getStart().compareTo(o2.getStart()))
				.collect(Collectors.toList());

		Movie prev = null;
		for (Movie current : result) {
			if (null != prev) {
				if (prev.getEnd().compareTo(current.getStart()) > 0)
					return false;
			}
			prev = current;
		}
		return true;
	}

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("y-M-d H:m");

        ArrayList<Movie> movies = new ArrayList<Movie>();
        movies.add(new Movie(sdf.parse("2015-01-01 20:00"), sdf.parse("2015-01-01 21:30")));
        movies.add(new Movie(sdf.parse("2015-01-01 21:30"), sdf.parse("2015-01-01 23:30")));
        movies.add(new Movie(sdf.parse("2015-01-01 21:30"), sdf.parse("2015-01-01 23:00")));

        System.out.println(ASGTest.canViewAll(movies));
    }

}

class Movie {
    private Date start, end;
    
    public Movie(Date start, Date end) {
        this.start = start;
        this.end = end;
    }
    
    public Date getStart() {
        return this.start;
    }
    
    public Date getEnd() {
        return this.end;
    } 
}
