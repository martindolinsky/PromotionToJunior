package sk.dolinsky.udemyapp.multithreading.completablefuture;

import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.stopWatch;
import static sk.dolinsky.udemyapp.multithreading.util.LoggerUtil.log;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import sk.dolinsky.udemyapp.multithreading.domain.Inventory;
import sk.dolinsky.udemyapp.multithreading.domain.Product;
import sk.dolinsky.udemyapp.multithreading.domain.ProductInfo;
import sk.dolinsky.udemyapp.multithreading.domain.ProductOption;
import sk.dolinsky.udemyapp.multithreading.domain.Review;
import sk.dolinsky.udemyapp.multithreading.service.InventoryService;
import sk.dolinsky.udemyapp.multithreading.service.ProductInfoService;
import sk.dolinsky.udemyapp.multithreading.service.ProductInfoServiceImpl;
import sk.dolinsky.udemyapp.multithreading.service.ProductService;
import sk.dolinsky.udemyapp.multithreading.service.ReviewService;
import sk.dolinsky.udemyapp.multithreading.service.ReviewServiceImpl;

public class ProductServiceImplCompletableFuture implements ProductService {

  private ProductInfoService productInfoService;
  private ReviewService reviewService;
  private InventoryService inventoryService;

  public ProductServiceImplCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService) {
    this.productInfoService = productInfoService;
    this.reviewService = reviewService;

  }

  public ProductServiceImplCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService, InventoryService inventoryService) {
    this.productInfoService = productInfoService;
    this.reviewService = reviewService;
    this.inventoryService = inventoryService;
  }

  @Override
  public Product retrieveProductDetails(String productId) {
    stopWatch.start();

    CompletableFuture<ProductInfo> cfProductInfo = CompletableFuture
        .supplyAsync(() -> productInfoService.retrieveProductInfo(productId));

    CompletableFuture<Review> cfReview = CompletableFuture
        .supplyAsync(() -> reviewService.retrieveReview(productId));

    Product product = cfProductInfo
        .thenCombine(cfReview, (productInfo, review) -> new Product(productId, productInfo, review)).join();

    stopWatch.stop();
    log("Total time taken : " + stopWatch.getTime());
    return product;
  }

  public CompletableFuture<Product> retrieveProductDetailsApproach2(String productId) {
    stopWatch.start();

    CompletableFuture<ProductInfo> cfProductInfo = CompletableFuture
        .supplyAsync(() -> productInfoService.retrieveProductInfo(productId));

    CompletableFuture<Review> cfReview = CompletableFuture
        .supplyAsync(() -> reviewService.retrieveReview(productId));

    stopWatch.stop();
    log("Total time taken : " + stopWatch.getTime());

    return cfProductInfo
        .thenCombine(cfReview, (productInfo, review) -> new Product(productId, productInfo, review));

  }

  public Product retrieveProductDetailsWithInventory(String productId) {
    stopWatch.start();

    CompletableFuture<ProductInfo> cfProductInfo = CompletableFuture
        .supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
        .thenApply(productInfo -> {
          productInfo.setProductOptions(updateInventory(productInfo));
          return productInfo;
        });

    CompletableFuture<Review> cfReview = CompletableFuture
        .supplyAsync(() -> reviewService.retrieveReview(productId));

    Product product = cfProductInfo
        .thenCombine(cfReview, (productInfo, review) -> new Product(productId, productInfo, review)).join();

    stopWatch.stop();
    log("Total time taken : " + stopWatch.getTime());
    return product;
  }

  public Product retrieveProductDetailsWithInventoryApproach2(String productId) {
    stopWatch.start();

    CompletableFuture<ProductInfo> cfProductInfo = CompletableFuture
        .supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
        .thenApply(productInfo -> {
          productInfo.setProductOptions(updateInventoryApproach2(productInfo));
          return productInfo;
        });

    CompletableFuture<Review> cfReview = CompletableFuture
        .supplyAsync(() -> reviewService.retrieveReview(productId))
        .exceptionally(e -> {
          log("Handled the Exception in reviewService: " + e.getMessage());
          return Review.builder().noOfReviews(0).overallRating(0.0).build();
        });

    Product product = cfProductInfo
        .thenCombine(cfReview, (productInfo, review) -> new Product(productId, productInfo, review))
        .join();

    stopWatch.stop();
    log("Total time taken : " + stopWatch.getTime());
    return product;
  }

  private List<ProductOption> updateInventory(ProductInfo productInfo) {
    List<ProductOption> productOptionList = productInfo.getProductOptions()
        .stream()
        .map(productOption -> {
          Inventory inventory = inventoryService.retrieveInventory(productOption);
          productOption.setInventory(inventory);
          return productOption;
        }).collect(Collectors.toList());

    return productOptionList;
  }

  private List<ProductOption> updateInventoryApproach2(ProductInfo productInfo) {
    List<CompletableFuture<ProductOption>> productOptionList = productInfo.getProductOptions()
        .stream()
        .map(productOption -> {
           return CompletableFuture.supplyAsync(() -> inventoryService.retrieveInventory(productOption))
              .thenApply(inventory -> {
                productOption.setInventory(inventory);
                return productOption;
              });
        }).collect(Collectors.toList());

    return productOptionList.stream().map(CompletableFuture::join).collect(Collectors.toList());
  }

  public static void main(String[] args) throws Exception {
    ProductInfoService productInfoService = new ProductInfoServiceImpl();
    ReviewService reviewService = new ReviewServiceImpl();
    ProductService productService = new ProductServiceImplCompletableFuture(productInfoService, reviewService);
    String productId = "ABC123";
    Product product = productService.retrieveProductDetails(productId);
    log("Product is: " + product);
  }
}
