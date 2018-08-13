package asg.onlinetest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Needle {

	public static int count(String needle, InputStream haystack) throws Exception {
		int count = 0;
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(haystack))) {
			/*String line = null;
			while ((line = buffer.readLine()) != null) {
				if (line.contains(needle))
					count++;
			}*/
			count = (int) buffer.lines().filter(line -> line.contains(needle)).count();
		}
		return count;
	}

	public static void main(String[] args) throws Exception {
		String inMessage = "Hello, there!\nHow are you today?\nYes, you over there.";

		try (InputStream inStream = new ByteArrayInputStream(inMessage.getBytes())) {
			System.out.println(Needle.count("there", inStream));
		}
	}
}
