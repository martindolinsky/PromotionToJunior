package sk.dolinsky.udemyapp.multithreading.service;

import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.delay;

import sk.dolinsky.udemyapp.multithreading.domain.Review;

public class ReviewServiceImpl implements ReviewService {

  @Override
  public Review retrieveReview(String productId) {
    delay(1000);
    return new Review(200, 4.5);

  }
}
