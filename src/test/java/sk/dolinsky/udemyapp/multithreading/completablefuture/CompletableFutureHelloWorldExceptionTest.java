package sk.dolinsky.udemyapp.multithreading.completablefuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import sk.dolinsky.udemyapp.multithreading.service.HelloWorldService;
import sk.dolinsky.udemyapp.multithreading.util.CommonUtil;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CompletableFutureHelloWorldExceptionTest {
  //  @Mock
//  private HelloWorldService helloWorldService;
  @Spy
  private HelloWorldService helloWorldService;

  //@InjectMocks
  CompletableFutureHelloWorldException helloWorldException;

  @BeforeEach
  void setUp() {
    //this.helloWorldService = Mockito.mock(HelloWorldService.class);
    this.helloWorldService = Mockito.spy(new HelloWorldService());
    this.helloWorldException = new CompletableFutureHelloWorldException(this.helloWorldService);
    CommonUtil.stopWatchReset();
  }

  @Test
  void helloWorldThreeAsyncCallsHandle1() {
    //assertEquals("hello", helloWorldService.hello());
    when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception at 'hello()' occurred"));
    //assertThrows(RuntimeException.class, () -> helloWorldService.hello(), "Exception must be triggered");

    //assertEquals(" world!", helloWorldService.world());
    when(helloWorldService.world()).thenCallRealMethod();
    //assertEquals(" world!", helloWorldService.world());

    String result = helloWorldException.helloWorldThreeAsyncCallsHandle();

    assertEquals("RECOVERABLE VALUE [H] WORLD! HI COMPLETABLE FUTURE", result);
  }

  @Test
  void helloWorldThreeAsyncCallsHandle2() {
    when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception at 'hello()' occurred"));
    when(helloWorldService.world()).thenThrow(new RuntimeException("Exception at 'world()' occurred"));

    String result = helloWorldException.helloWorldThreeAsyncCallsHandle();

    assertEquals("RECOVERABLE VALUE [V] HI COMPLETABLE FUTURE", result);
  }

  @Test
  void helloWorldThreeAsyncCallsHandle3() {
    when(helloWorldService.hello()).thenCallRealMethod();
    when(helloWorldService.world()).thenCallRealMethod();

    String result = helloWorldException.helloWorldThreeAsyncCallsHandle();

    assertEquals("HELLO WORLD! HI COMPLETABLE FUTURE", result);
  }

  @Test
  void helloWorldThreeAsyncCallsHandleExceptionally() {
    when(helloWorldService.hello()).thenCallRealMethod();
    when(helloWorldService.world()).thenCallRealMethod();

    String result = helloWorldException.helloWorldThreeAsyncCallsHandleExceptionally();

    assertEquals("HELLO WORLD! HI COMPLETABLE FUTURE", result);
  }

  @Test
  void helloWorldThreeAsyncCallsHandleExceptionally2() {
    when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception at 'hello()' occurred"));
    when(helloWorldService.world()).thenThrow(new RuntimeException("Exception at 'world()' occurred"));

    String result = helloWorldException.helloWorldThreeAsyncCallsHandleExceptionally();

    assertEquals("RECOVERABLE VALUE [V] HI COMPLETABLE FUTURE", result);
  }

  @Test
  void helloWorldThreeAsyncCallsHandleWhenComplete() {
    when(helloWorldService.hello()).thenCallRealMethod();
    when(helloWorldService.world()).thenCallRealMethod();

    String result = helloWorldException.helloWorldThreeAsyncCallsWhenComplete();

    assertEquals("HELLO WORLD! HI COMPLETABLE FUTURE", result);
  }

  @Test
  void helloWorldThreeAsyncCallsHandleWhenComplete2() {
    when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception at 'hello()' occurred"));
    when(helloWorldService.world()).thenThrow(new RuntimeException("Exception at 'world()' occurred"));

    String result = helloWorldException.helloWorldThreeAsyncCallsWhenComplete();

    assertEquals(" HI COMPLETABLE FUTURE", result);
  }
}