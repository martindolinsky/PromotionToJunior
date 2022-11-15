package sk.dolinsky.udemyapp.multithreading.completablefuture;

import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.delay;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.startTimer;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.timeTaken;
import static sk.dolinsky.udemyapp.multithreading.util.LoggerUtil.log;

import java.util.concurrent.CompletableFuture;
import sk.dolinsky.udemyapp.multithreading.service.HelloWorldService;

public class CompletableFutureHelloWorld {

  private HelloWorldService hws;

  public CompletableFutureHelloWorld(HelloWorldService hws) {
    this.hws = hws;
  }

  public CompletableFuture<String> helloWorld() {
    return CompletableFuture.supplyAsync(hws::helloWorld)
        .thenApply(String::toUpperCase);
  }

  public String helloWorldMultipleAsyncCalls() {
    startTimer();
    CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello());
    CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());

    String hw =  hello.thenCombine(world, (h, w) -> h + w)
        .thenApply(String::toUpperCase)
        .join();
    timeTaken();
    return hw;
  }

  public String helloWorldThreeAsyncCalls() {
    startTimer();
    CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello());
    CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
    CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
      delay(1000);
      return " Hi Completable Future";
    });

    String hw =  hello.thenCombine(world, (h, w) -> h + w)
        .thenCombine(hi, (previous, current) -> previous + current)
        .thenApply(String::toUpperCase)
        .join();
    timeTaken();
    return hw;
  }

  public CompletableFuture<String> helloWorldThenCompose() {

    return CompletableFuture.supplyAsync(hws::hello)
        .thenCompose((previous) -> hws.worldFuture(previous))
        .thenApply(String::toUpperCase);
  }

  public static void main(String[] args) {
    HelloWorldService hws = new HelloWorldService();
    System.out.println(Runtime.getRuntime().availableProcessors());

    CompletableFuture.supplyAsync(hws::helloWorld)
        .thenApply(String::toUpperCase)
        .thenAccept((result) -> {
          log("Result is: " + result);
        });
    log("Done");
    delay(2000);
  }

}
