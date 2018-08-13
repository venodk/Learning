package learning.inheritence;

public class StaticClassInheritence {

	public static void main(String[] args) {
		Base b = new Base();
		Derived d = new Derived();
		Base e = d;
		Base f = new Derived();
		
		b.printName();
		d.printName();
		e.printName();
		f.printName();
	}

}

class Base {
	public static void printName() {
		System.out.println("Base.printName()");
	}
}

class Derived extends Base {
	public static void printName() {
		System.out.println("Derived.printName()");
	}
}