/**
 * 
 */
package learning.externalization;

import java.io.Externalizable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * @author vkunnakkattil
 *
 */
public class ExternalizationTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ToExternalize te = new ToExternalize(12, 45);
		FileOutputStream fos = new FileOutputStream(new File("ToExternalize.ext"));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(te);
		oos.close();
	}

}

class ToExternalize implements Externalizable {
	int x;
	int y;
	ToExternalize(int a, int b) {
		x = a;
		y = b;
	}
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(x);
		out.writeInt(y);
	}
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		x = in.readInt();
		y = in.readInt();
	}
}