package sk.dolinsky.udemyapp.streams;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Streams {

  public static void main(String[] args) {
    var numbersList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
    var wordsList = List.of("Udemy", "Streams", "Java", "Catalina", "Globallogic", "Course");
    printPowersOfEvenNumbersUsingStreams(numbersList);
    System.out.println();
    System.out.println(getSumOfList(numbersList));
    System.out.println();
    System.out.println(getSortedList(wordsList));
    System.out.println();
    printPowersOfOddNumbersUsingPredicates(numbersList);
    System.out.println();
    printUpperCased(wordsList);


  }

  private static void printPowersOfEvenNumbersUsingStreams(List<Integer> numbers) {
    numbers.stream()
        .filter(n -> n % 2 == 0)
        .map(number -> number * number)
        .forEach(System.out::println);

  }

  private static int getSumOfList(List<Integer> numbers) {
    return numbers.stream()
        .reduce(0, Integer::sum);
  }

  private static List<String> getSortedList(List<String> list) {
    return list.stream()
        .sorted(Comparator.naturalOrder())
        .collect(Collectors.toList());
  }

  private static void printPowersOfOddNumbersUsingPredicates(List<Integer> numbers) {
    Predicate<Integer> isEvenPredicate = x -> x % 2 != 0;
    Function<Integer, Integer> squareFunction = x -> x * x;
    Consumer consumer = System.out::println;

    numbers.stream()
        .filter(isEvenPredicate)
        .map(squareFunction)
        .forEach(consumer);
  }

  private static void printUpperCased(List<String> list) {
    list.stream()
        .map(String::toUpperCase)
        .forEach(System.out::println);
  }

}
