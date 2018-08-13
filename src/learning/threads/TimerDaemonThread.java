/**
 * 
 */
package learning.threads;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author vkunnakkattil
 *
 */
public class TimerDaemonThread {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				System.out.println("Running Run..." + new Date());
			}
		};
		Timer t = new Timer();
		t.scheduleAtFixedRate(tt, 0, 10000);
		
		System.out.println("Schuduled..");
	}

}
