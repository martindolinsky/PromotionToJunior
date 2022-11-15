package sk.dolinsky.udemyapp.apiclient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import sk.dolinsky.udemyapp.multithreading.domain.movie.Movie;
import sk.dolinsky.udemyapp.multithreading.domain.movie.MovieInfo;
import sk.dolinsky.udemyapp.multithreading.domain.movie.Review;

@RequiredArgsConstructor
public class MoviesClient {

  private final WebClient webClient;

  public Movie retrieveMovie(Long movieInfoId) {
    var movieInfo = invokeMovieInfoService(movieInfoId);
    var review = invokeReviewService(movieInfoId);
    return new Movie(movieInfo, review);
  }

  public List<Movie> retrieveMovies(List<Long> movieInfoIds) {
    return movieInfoIds
        .stream()
        .map(this::retrieveMovie)
        .collect(Collectors.toList());

  }

  public List<Movie> retrieveMovieListCF(List<Long> movieInfoIds) {

    var movieFutures = movieInfoIds
        .stream()
        .map(this::retrieveMovieCF)
        .collect(Collectors.toList());

    return movieFutures
        .stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());

  }

  public List<Movie> retrieveMovieListCFAllOf(List<Long> movieInfoIds) {

    var movieFutures = movieInfoIds
        .stream()
        .map(this::retrieveMovieCF)
        .collect(Collectors.toList());

    var cfAllOf = CompletableFuture.allOf(movieFutures.toArray(new CompletableFuture[movieFutures.size()]));

    return cfAllOf
        .thenApply(v -> movieFutures
            .stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList()))
        .join();

  }


  public CompletableFuture<Movie> retrieveMovieCF(Long movieInfoId) {
    var movieInfo = CompletableFuture.supplyAsync(() -> invokeMovieInfoService(movieInfoId));
    var review = CompletableFuture.supplyAsync(() -> invokeReviewService(movieInfoId));
    return movieInfo.thenCombine(review, Movie::new);
  }

  public MovieInfo invokeMovieInfoService(Long movieInfoId) {
    var moviesInfoUrl = "/v1/movie_infos/{movieInfoId}";
    return webClient
        .get()
        .uri(moviesInfoUrl, movieInfoId)
        .retrieve()
        .bodyToMono(MovieInfo.class)
        .block();
  }

  public List<Review> invokeReviewService(Long movieInfoId) {
    var reviewUri = UriComponentsBuilder.fromUriString("/v1/reviews")
        .queryParam("movieInfoId", movieInfoId)
        .buildAndExpand()
        .toString();

    return webClient
        .get()
        .uri(reviewUri)
        .retrieve()
        .bodyToFlux(Review.class)
        .collectList()
        .block();

  }


}
