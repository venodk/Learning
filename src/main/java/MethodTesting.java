import java.util.ArrayList;
import java.util.List;

public class MethodTesting {
    public static void main(String[] args) {
        Parent obj = new Child();
        obj.parentMethod(); // Compilation error
        List<Parent> myList = new ArrayList<>();
    }
}

class Parent {
    public void parentMethod() {
        this.display();
    }
    private void display() {
        System.out.println("Parent method");
    }
}
class Child extends Parent {
    private void display() {
        System.out.println("Child method");
    }
}
