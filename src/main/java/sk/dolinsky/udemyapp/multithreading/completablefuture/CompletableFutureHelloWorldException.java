package sk.dolinsky.udemyapp.multithreading.completablefuture;

import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.delay;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.startTimer;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.timeTaken;
import static sk.dolinsky.udemyapp.multithreading.util.LoggerUtil.log;

import java.util.concurrent.CompletableFuture;
import sk.dolinsky.udemyapp.multithreading.service.HelloWorldService;

public class CompletableFutureHelloWorldException {

  private HelloWorldService hws;

  public CompletableFutureHelloWorldException(HelloWorldService hws) {
    if (hws == null) {
      System.err.println("HelloWorldService is not defined");
    }
    System.out.println("HelloWorldService is applied: " + hws);
    this.hws = hws;
  }

  public String helloWorldThreeAsyncCallsHandle() {
    startTimer();
    CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello());
    CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
    CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
      delay(1000);
      return " Hi Completable Future";
    });

    String hw =  hello
        .handle((result, e) -> {
          if (e != null) {
            log("[hws.hello()]: Exception is: " + e.getMessage());
            return "Recoverable value [H]";
          }
          return result;
        })
        .thenCombine(world, (h, w) -> {
          log("thenCombine: hello(): '" + h + "'; world(): '" + w + "'");
          return h + w;
        })
        .handle((result, e) -> {
          if (e != null) {
            log("[hws.world()]: Exception after world is: " + e.getMessage());
            return "Recoverable value [V]";
          }
          return result;
        })
        .thenCombine(hi, (previous, current) -> {
          log("thenCombine: previous: '" + previous + "'; current: '" + current + "'");
          return previous + current;
        })
        .thenApply(String::toUpperCase)
        .join();
    timeTaken();
    return hw;
  }

  public String helloWorldThreeAsyncCallsHandleExceptionally() {
    startTimer();
    CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello());
    CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
    CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
      delay(1000);
      return " Hi Completable Future";
    });

    String hw =  hello
        .exceptionally(e -> {
          log("[hws.hello()]: Exception is: " + e.getMessage());
          return "Recoverable value [H]";
        } )
        .thenCombine(world, (h, w) -> {
          log("thenCombine: hello(): '" + h + "'; world(): '" + w + "'");
          return h + w;
        })
        .exceptionally(e -> {
          log("[hws.world()]: Exception after world is: " + e.getMessage());
          return "Recoverable value [V]";
        } )
        .thenCombine(hi, (previous, current) -> {
          log("thenCombine: previous: '" + previous + "'; current: '" + current + "'");
          return previous + current;
        })
        .thenApply(String::toUpperCase)
        .join();
    timeTaken();
    return hw;
  }

  public String helloWorldThreeAsyncCallsWhenComplete() {
    startTimer();
    CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello());
    CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
    CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
      delay(1000);
      return " Hi Completable Future";
    });

    String hw =  hello
        .whenComplete((result, e) -> {
          log("[hws.hello()]: Result is: " + result);
          if (e != null) {
            log("[hws.hello()]: Exception is: " + e.getMessage());
          }
        })
        .thenCombine(world, (h, w) -> {
          log("thenCombine: hello(): '" + h + "'; world(): '" + w + "'");
          return h + w;
        })
        .whenComplete((result, e) -> {
          log("[hws.world()]: Result is: " + result);
          if (e != null) {
            log("[hws.world()]: Exception after world is: " + e.getMessage());
          }
        })
        .exceptionally(e -> {
          log("Exception after world2 is: " + e.getMessage());
          return "";
        })
        .thenCombine(hi, (previous, current) -> {
          log("thenCombine: previous: '" + previous + "'; current: '" + current + "'");
          return previous + current;
        })
        .thenApply(String::toUpperCase)
        .join();
    timeTaken();
    return hw;
  }

}
