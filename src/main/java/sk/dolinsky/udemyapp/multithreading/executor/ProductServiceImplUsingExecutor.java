package sk.dolinsky.udemyapp.multithreading.executor;

import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.stopWatch;
import static sk.dolinsky.udemyapp.multithreading.util.LoggerUtil.log;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import sk.dolinsky.udemyapp.multithreading.domain.Product;
import sk.dolinsky.udemyapp.multithreading.domain.ProductInfo;
import sk.dolinsky.udemyapp.multithreading.domain.Review;
import sk.dolinsky.udemyapp.multithreading.service.ProductInfoService;
import sk.dolinsky.udemyapp.multithreading.service.ProductInfoServiceImpl;
import sk.dolinsky.udemyapp.multithreading.service.ProductService;
import sk.dolinsky.udemyapp.multithreading.service.ReviewService;
import sk.dolinsky.udemyapp.multithreading.service.ReviewServiceImpl;

public class ProductServiceImplUsingExecutor implements ProductService {

  static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  private ProductInfoService productInfoService;
  private ReviewService reviewService;

  public ProductServiceImplUsingExecutor(ProductInfoService productInfoService, ReviewService reviewService) {
    this.productInfoService = productInfoService;
    this.reviewService = reviewService;
  }

  @Override
  public Product retrieveProductDetails(String productId) throws InterruptedException, ExecutionException {
    stopWatch.start();

    Future<ProductInfo> productInfoFuture = executorService.submit(() -> productInfoService.retrieveProductInfo(productId));
    Future<Review> reviewFuture = executorService.submit(() -> reviewService.retrieveReview(productId));

    ProductInfo productInfo = productInfoFuture.get();
    Review review = reviewFuture.get();

    stopWatch.stop();
    log("Total time taken : " + stopWatch.getTime());
    return new Product(productId, productInfo, review);
  }

  public static void main(String[] args) throws Exception {
    ProductInfoService productInfoService = new ProductInfoServiceImpl();
    ReviewService reviewService = new ReviewServiceImpl();
    ProductService productService = new ProductServiceImplUsingExecutor(productInfoService, reviewService);
    String productId = "ABC123";
    Product product = productService.retrieveProductDetails(productId);
    log("Product is: " + product);
    executorService.shutdown();
  }
}
