package sk.dolinsky.udemyapp.multithreading.parallelstreams;

import static org.junit.jupiter.api.Assertions.*;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.startTimer;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.stopWatch;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.timeTaken;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import sk.dolinsky.udemyapp.multithreading.util.DataSet;

class ParallelStreamsExampleTest {

  ParallelStreamsExample parallelStreamsExample;

  @BeforeEach
  void setUp() {
    parallelStreamsExample = new ParallelStreamsExample();
    stopWatch.reset();
  }

  @Test
  void testStringTransform() {

    List<String> inputList = DataSet.namesList();

    startTimer();
    List<String> resultList = parallelStreamsExample.stringTransform(inputList);
    timeTaken();

    assertEquals(4, resultList.size());
    resultList.forEach(name -> {
      assertTrue(name.contains("-"));
    });
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void testStringTransformTwo(boolean isParallel) {

    List<String> inputList = DataSet.namesList();

    startTimer();
    List<String> resultList = parallelStreamsExample.stringTransformTwo(inputList, isParallel);
    timeTaken();

    assertEquals(4, resultList.size());
    resultList.forEach(name -> {
      assertTrue(name.contains("-"));
    });
  }
}