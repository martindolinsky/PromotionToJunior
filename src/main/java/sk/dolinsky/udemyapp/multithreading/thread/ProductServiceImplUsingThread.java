package sk.dolinsky.udemyapp.multithreading.thread;

import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.stopWatch;
import static sk.dolinsky.udemyapp.multithreading.util.LoggerUtil.log;

import sk.dolinsky.udemyapp.multithreading.domain.Product;
import sk.dolinsky.udemyapp.multithreading.domain.ProductInfo;
import sk.dolinsky.udemyapp.multithreading.domain.Review;
import sk.dolinsky.udemyapp.multithreading.service.ProductInfoService;
import sk.dolinsky.udemyapp.multithreading.service.ProductInfoServiceImpl;
import sk.dolinsky.udemyapp.multithreading.service.ProductService;
import sk.dolinsky.udemyapp.multithreading.service.ReviewService;
import sk.dolinsky.udemyapp.multithreading.service.ReviewServiceImpl;

public class ProductServiceImplUsingThread implements ProductService{
  private ProductInfoService productInfoService;
  private ReviewService reviewService;

  public ProductServiceImplUsingThread(ProductInfoService productInfoService, ReviewService reviewService) {
    this.productInfoService = productInfoService;
    this.reviewService = reviewService;
  }

  @Override
  public Product retrieveProductDetails(String productId) throws InterruptedException {
    stopWatch.start();

    ProductInfoRunnable productInfoRunnable = new ProductInfoRunnable(productId);
    Thread productInfoThread = new Thread(productInfoRunnable);

    ReviewRunnable reviewRunnable = new ReviewRunnable(productId);
    Thread reviewThread = new Thread(reviewRunnable);

    productInfoThread.start();
    reviewThread.start();

    productInfoThread.join();
    reviewThread.join();

    ProductInfo productInfo = productInfoRunnable.getProductInfo();
    Review review = reviewRunnable.getReview();

    stopWatch.stop();
    log("Total time taken : " + stopWatch.getTime());
    return new Product(productId, productInfo, review);
  }

  public static void main(String[] args) throws Exception {
    ProductInfoService productInfoService = new ProductInfoServiceImpl();
    ReviewService reviewService = new ReviewServiceImpl();
    ProductService productService = new ProductServiceImplUsingThread(productInfoService, reviewService);
    String productId = "ABC123";
    Product product = productService.retrieveProductDetails(productId);
    log("Product is: " + product);
  }

  private class ProductInfoRunnable implements Runnable {

    private ProductInfo productInfo;

    private String productId;
    public ProductInfoRunnable(String productId) {
      this.productId = productId;
    }

    @Override
    public void run() {
      productInfo = productInfoService.retrieveProductInfo(productId);
    }

    public ProductInfo getProductInfo() {
      return productInfo;
    }
  }

  private class ReviewRunnable implements Runnable {

    private Review review;
    private String productId;

    public ReviewRunnable(String productId) {
      this.productId = productId;
    }

    @Override
    public void run() {
      review = reviewService.retrieveReview(productId);
    }

    public Review getReview() {
      return review;
    }
  }
}

