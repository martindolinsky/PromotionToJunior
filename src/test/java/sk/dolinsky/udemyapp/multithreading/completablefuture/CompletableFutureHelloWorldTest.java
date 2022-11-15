package sk.dolinsky.udemyapp.multithreading.completablefuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.startTimer;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.timeTaken;

import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.dolinsky.udemyapp.multithreading.service.HelloWorldService;
import sk.dolinsky.udemyapp.multithreading.util.CommonUtil;

class CompletableFutureHelloWorldTest {

  HelloWorldService hws;
  CompletableFutureHelloWorld cfhw;

  @BeforeEach
  void setUp() {
    hws = new HelloWorldService();
    cfhw = new CompletableFutureHelloWorld(hws);
    CommonUtil.stopWatchReset();
  }

  @Test
  void helloWorld() {

    CompletableFuture<String> completableFuture = cfhw.helloWorld();

    completableFuture.thenAccept(s -> {
      assertEquals("HELLO WORLD", s);
      assertNotEquals("HELLO WORLD1", s);
    }).join();
  }

  @Test
  void helloWorldMultipleAsyncCalls() {
    String helloWorld = cfhw.helloWorldMultipleAsyncCalls();
    assertEquals("HELLO WORLD!", helloWorld);
  }

  @Test
  void helloWorldThreeAsyncCalls() {
    String helloWorld = cfhw.helloWorldThreeAsyncCalls();
    assertEquals("HELLO WORLD! HI COMPLETABLE FUTURE", helloWorld);
  }

  @Test
  void helloWorldThenCompose() {
    startTimer();
    CompletableFuture<String> completableFuture = cfhw.helloWorldThenCompose();

    completableFuture.thenAccept(s -> {
      assertEquals("HELLO WORLD!", s);
      assertNotEquals("HELLO WORLD1", s);
    }).join();

    timeTaken();
  }
}