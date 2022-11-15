package sk.dolinsky.udemyapp.streams;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Course {

  private String name;
  private String category;
  private int reviewScore;
  private int noOfStudents;

  public static void main(String[] args) {
    var courses = List.of(
        new Course("Spring", "Framework", 98, 20000),
        new Course("Spring Boot", "Framework", 95, 18000),
        new Course("API", "Microservices", 97, 22000),
        new Course("Microservices", "Microservices", 96, 25000),
        new Course("Fullstack", "Fullstack", 91, 14000),
        new Course("AWS", "Cloud", 92, 21000),
        new Course("Azure", "Cloud", 99, 20000),
        new Course("Docker", "Cloud", 92, 20000),
        new Course("Kubernetes", "Cloud", 91, 20000)
    );

    Predicate<Course> reviewScoreGreaterThan95Predicate = course -> course.getReviewScore() > 95;
    Predicate<Course> reviewScoreGreaterThan90Predicate = course -> course.getReviewScore() > 90;
    Predicate<Course> reviewScoreLessThan90Predicate = course -> course.getReviewScore() < 92;

    System.out.println(courses.stream().allMatch(reviewScoreGreaterThan95Predicate));
    System.out.println(courses.stream().noneMatch(reviewScoreGreaterThan90Predicate));
    System.out.println(courses.stream().anyMatch(reviewScoreLessThan90Predicate));

    Comparator<Course> comparingByNoOfStudentsAsc = Comparator.comparing(Course::getNoOfStudents)
        .thenComparingInt(Course::getReviewScore);
    Comparator<Course> comparingByNoOfStudentsDesc = Comparator.comparing(Course::getNoOfStudents)
        .reversed();

    System.out.println("Courses:\n" + courses);
    System.out.println(
        "Sorted by NoOfStudents ASC:\n" + courses.stream().sorted(comparingByNoOfStudentsAsc)
            .collect(Collectors.toList()));
    System.out.println(
        "Sorted by NoOfStudents DESC:\n" + courses.stream().sorted(comparingByNoOfStudentsDesc)
            .collect(Collectors.toList()));

    System.out.println(
        "takeWhile reviewScore >= 95:\n" +
            courses.stream().takeWhile(course -> course.getReviewScore() >= 95)
                .collect(Collectors.toList())
    );

    System.out.println(
        "dropWhile reviewScore >= 95:\n" +
            courses.stream().dropWhile(course -> course.getReviewScore() >= 95)
                .collect(Collectors.toList())
    );

    System.out.println(
        "findFirst reviewScore == 82:\n" +
            courses.stream().filter(course -> course.getReviewScore() == 82).findFirst().orElse(null)
    );

    System.out.println(
        "groupingBy Category:\n" +
        courses.stream().collect(Collectors.groupingBy(Course::getCategory))
    );

    System.out.println(
        "groupingBy and counting by Category:\n" +
            courses.stream().collect(Collectors.groupingBy(Course::getCategory,
                Collectors.counting()))
    );

    System.out.println(
        "split courses and flatMap:\n" +
            courses.stream().map(c -> c.getName().split("")).flatMap(Arrays::stream).collect(Collectors.toList())
    );

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        for (int i = 0; i < 10; i++) {
          System.out.print(Thread.currentThread().getId() + ":" + i + " ");
        }
      }
    };

    Runnable runnable2 = () -> {
      for (int i = 0; i < 10; i++) {
        System.out.print(Thread.currentThread().getId() + ":" + i + " ");
      }
    };

    Thread thread = new Thread(runnable);
    thread.start();

    Thread thread1 = new Thread(runnable2);
    thread1.start();

  }
}


