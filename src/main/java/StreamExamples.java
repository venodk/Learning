import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamExamples {

    public static void main(String[] args) {

        List<Integer> myList = Arrays.asList(10,15,8,49,25,98,32);
        Integer oneList = myList.stream()
                .sorted(Collections.reverseOrder())
                .skip(4).limit(1).findFirst().get();

        List<Employee> employees = new ArrayList<>();
        Employee e1 = new Employee("Ajay",35);
        Employee e2 = new Employee("Vijay",15);
        Employee e6 = new Employee("Vijay",11);
        Employee e5 = new Employee("Vijay",5);
        Employee e3 = new Employee("Zack",30);
        Employee e4 = new Employee("David",51);
        employees.add(e1);
        employees.add(e2);
        employees.add(e3);
        employees.add(e4);
        employees.add(e5);
        employees.add(e6);

        List<Employee> sortedEmployees = employees.stream()
                .sorted(Comparator.comparing(Employee::name).thenComparing(Employee::age))
                .toList();
        var result = employees.stream().collect(Collectors.groupingBy
                (Employee::name, Collectors.averagingInt(Employee::age)));
        Map<String, Employee> bb = employees.stream().collect(Collectors.toMap(Employee::name, Function.identity(), BinaryOperator.minBy(Comparator.comparingInt(Employee::age))));

        Map<String, Employee> bb2 = employees.stream().collect(Collectors.groupingBy(Employee::name, Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(Employee::age)), Optional::get)));

        var age = employees.stream().max(Comparator.comparingInt(Employee::age)).get();

        var intSummaryStatistics = employees.stream().collect(Collectors.summarizingInt(Employee::age));

        /*List<Integer> numbers = Arrays.asList(1,2,3,4,5,6);

        int sumOfEvenNumbers = numbers.stream()
                .filter(n -> n % 2 == 0)
                .mapToInt(Integer::intValue)
                .sum();*/
        System.out.println(sortedEmployees);
        System.out.println(result);
        System.out.println(bb2);
    }

    private record Employee(String name, int age) {}
}
