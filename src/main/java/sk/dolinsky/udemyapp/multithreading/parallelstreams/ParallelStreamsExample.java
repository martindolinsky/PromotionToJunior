package sk.dolinsky.udemyapp.multithreading.parallelstreams;

import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.delay;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.startTimer;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.timeTaken;
import static sk.dolinsky.udemyapp.multithreading.util.LoggerUtil.log;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import sk.dolinsky.udemyapp.multithreading.util.DataSet;

public class ParallelStreamsExample {

  public static void main(String[] args) {
    List<String> namesList = DataSet.namesList();
    ParallelStreamsExample parallelStreamsExample = new ParallelStreamsExample();
    startTimer();
    List<String> resultList = parallelStreamsExample.stringTransform(namesList);
    log("resultList: " + resultList);
    timeTaken();
  }

  public List<String> stringTransform(List<String> namesList) {
    return namesList
//        .stream()
        .parallelStream()
        .map(this::addNameLengthTransform)
        .collect(Collectors.toList());
  }

  public List<String> stringTransformTwo(List<String> namesList, boolean isParallel) {

    Stream<String> namesStream = namesList.stream();

    if (isParallel)
      namesStream.parallel();
    return namesStream
        .map(this::addNameLengthTransform)
        .collect(Collectors.toList());
  }

  private String addNameLengthTransform(String name) {
    delay(500);
    return name.length() + " - " + name;
  }

}
