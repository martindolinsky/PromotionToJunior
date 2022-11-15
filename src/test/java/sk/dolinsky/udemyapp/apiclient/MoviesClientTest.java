package sk.dolinsky.udemyapp.apiclient;

import static org.junit.jupiter.api.Assertions.*;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.startTimer;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.stopWatchReset;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.timeTaken;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

class MoviesClientTest {

  WebClient webClient;
  MoviesClient moviesClient;

  @BeforeEach
  void setUp() {
    webClient = WebClient.builder()
        .baseUrl("http://localhost:8080/movies").build();
    moviesClient = new MoviesClient(webClient);
    stopWatchReset();
  }

  @RepeatedTest(10)
  void retrieveMovie() {
    startTimer();
    var movieInfoId = 1L;
    var movie = moviesClient.retrieveMovie(movieInfoId);

    System.out.println("Movie: " + movie);
    assert movie != null;
    assertEquals("Batman Begins", movie.getMovieInfo().getName());
    assert movie.getReviewList().size() == 1;

    timeTaken();
  }


  @RepeatedTest(10)
  void retrieveMovies() {
    startTimer();
    var movieInfoIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
    var movies = moviesClient.retrieveMovies(movieInfoIds);

    System.out.println("Movies: " + movies);
    assert movies != null;
    assert movies.size() == 7;

    timeTaken();
  }

  @RepeatedTest(10)
  void retrieveMoviesCF() {
    startTimer();
    var movieInfoIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
    var movies = moviesClient.retrieveMovieListCF(movieInfoIds);

    System.out.println("Movies: " + movies);
    assert movies != null;
    assert movies.size() == 7;

    timeTaken();
  }

  @RepeatedTest(10)
  void retrieveMoviesCFAllOf() {
    startTimer();
    var movieInfoIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
    var movies = moviesClient.retrieveMovieListCFAllOf(movieInfoIds);

    System.out.println("Movies: " + movies);
    assert movies != null;
    assert movies.size() == 7;

    timeTaken();
  }

  @RepeatedTest(10)
  void retrieveMovieCF() {

    startTimer();
    var movieInfoId = 1L;
    var movie = moviesClient.retrieveMovieCF(movieInfoId).join();

    System.out.println("Movie: " + movie);
    assert movie != null;
    assertEquals("Batman Begins", movie.getMovieInfo().getName());
    assert movie.getReviewList().size() == 1;
    timeTaken();
  }
}