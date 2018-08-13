package work.exception;

import java.io.IOException;

public class ExceptionTest {
	public static void foo() throws Exception {
		try {
			//System.exit(0);
			//if (true) return;
			System.out.println("Try Block. ");
			throw new IOException("From Try block.");
			
		} catch (IOException e) {
			System.out.println("Catch Block.");
			throw new IOException("From Catch Block.");
		} finally {
			System.out.println("Finally Block.");
		}
	}
	
	public static void main(String[] args) throws Exception {
		foo();
	}

}
