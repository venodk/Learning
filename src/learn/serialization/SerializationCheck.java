package learn.serialization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author vkunnakkattil
 *
 */
public class SerializationCheck {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * List<Integer> ll = new LinkedList<>(); 
		 * Iterator<Integer> itr = ll.iterator(); 
		 * ListIterator<Integer> litr = ll.listIterator();
		 */

		/*Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Running Run...");
			}
		});
		t.start();
		*/
		
		/*int [] x = new int[] {20,30};*/
		
		/*PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
		for (int i = 0; i < 10; i++) pq.add(i*2);
		for (int i = 0; i < 10; i++) System.out.println(pq.poll());*/
		FileOutputStream fos;
		
		try {
			Mouse m = new Mouse();
			fos = new FileOutputStream(new File("mouse.ser"));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(m);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
class Mouse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private void writeObject(ObjectOutputStream oos) {
		try {
			System.out.println("Inside private method...");
			/*Exception e = new Exception();
			e.printStackTrace();*/
			oos.defaultWriteObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
