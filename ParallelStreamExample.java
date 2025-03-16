import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

class Student {
    private final String name;
    private final Map<String, Integer> grades;

    public Student(String name, Map<String, Integer> grades) {
        this.name = name;
        this.grades = grades;
    }

    public Map<String, Integer> getGrades() {
        return grades;
    }
}

public class ParallelStreamExample {
    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
                new Student("Student1", Map.of("Math", 90, "Physics", 85)),
                new Student("Student2", Map.of("Math", 95, "Physics", 88)),
                new Student("Student3", Map.of("Math", 88, "Chemistry", 92)),
                new Student("Student4", Map.of("Physics", 78, "Chemistry", 85))
        );

        
        Map<String, Double> subjectAverages = students.parallelStream()
		//Превращаем поток студентов в поток пар "предмет-оценка"
                .flatMap(student -> student.getGrades().entrySet().stream())
		/*groupingByConcurrent() ускоряет группировку за счет
			параллелизма*/
                .collect(Collectors.groupingByConcurrent(
                        Map.Entry::getKey,                                      
                        Collectors.averagingInt(Map.Entry::getValue)
                ));

        //Вывод результата:
        subjectAverages.forEach((subject, avg) ->
                System.out.println(subject + " - " + avg));
    }
}
