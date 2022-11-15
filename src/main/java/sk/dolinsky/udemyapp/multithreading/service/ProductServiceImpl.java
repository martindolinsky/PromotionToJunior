package sk.dolinsky.udemyapp.multithreading.service;

import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.*;
import static sk.dolinsky.udemyapp.multithreading.util.LoggerUtil.log;

import sk.dolinsky.udemyapp.multithreading.domain.Product;
import sk.dolinsky.udemyapp.multithreading.domain.ProductInfo;
import sk.dolinsky.udemyapp.multithreading.domain.Review;

public class ProductServiceImpl implements ProductService {

  private ProductInfoService productInfoService;
  private ReviewService reviewService;

  public ProductServiceImpl(ProductInfoService productInfoService, ReviewService reviewService) {
    this.productInfoService = productInfoService;
    this.reviewService = reviewService;
  }

  @Override
  public Product retrieveProductDetails(String productId) {
    stopWatch.start();

    ProductInfo productInfo = productInfoService.retrieveProductInfo(productId);
    Review review = reviewService.retrieveReview(productId);

    stopWatch.stop();
    log("Total time taken : " + stopWatch.getTime());
    return new Product(productId, productInfo, review);
  }

  public static void main(String[] args) throws Exception {
    ProductInfoService productInfoService = new ProductInfoServiceImpl();
    ReviewService reviewService = new ReviewServiceImpl();
    ProductService productService = new ProductServiceImpl(productInfoService, reviewService);
    String productId = "ABC123";
    Product product = productService.retrieveProductDetails(productId);
    log("Product is: " + product);
  }
}
