package sk.dolinsky.udemyapp.multithreading.service;

import sk.dolinsky.udemyapp.multithreading.domain.Review;

public interface ReviewService {

  Review retrieveReview(String productId);


}
