package sk.dolinsky.udemyapp.multithreading.completablefuture;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.dolinsky.udemyapp.multithreading.domain.Product;
import sk.dolinsky.udemyapp.multithreading.service.InventoryService;
import sk.dolinsky.udemyapp.multithreading.service.InventoryServiceImpl;
import sk.dolinsky.udemyapp.multithreading.service.ProductInfoService;
import sk.dolinsky.udemyapp.multithreading.service.ProductInfoServiceImpl;
import sk.dolinsky.udemyapp.multithreading.service.ReviewService;
import sk.dolinsky.udemyapp.multithreading.service.ReviewServiceImpl;
import sk.dolinsky.udemyapp.multithreading.util.CommonUtil;

class ProductServiceImplCompletableFutureTest {

  private ProductInfoService productInfoService;
  private ReviewService reviewService;
  private InventoryService inventoryService;
  private ProductServiceImplCompletableFuture productService;

  @BeforeEach
  void setUp() {
    productInfoService = new ProductInfoServiceImpl();
    reviewService = new ReviewServiceImpl();
    inventoryService = new InventoryServiceImpl();
    productService = new ProductServiceImplCompletableFuture(productInfoService, reviewService, inventoryService);
    CommonUtil.stopWatchReset();
  }

  @Test
  void retrieveProductDetails() {
    String productId = "ABC123";

    Product product = productService.retrieveProductDetails(productId);

    assertNotNull(product);
    assertTrue(product.getProductInfo().getProductOptions().size() > 0);
    assertNotNull(product.getReview());

  }

  @Test
  void retrieveProductDetailsWithInventory() {
    String productId = "ABC123";

    Product product = productService.retrieveProductDetailsWithInventory(productId);

    product.getProductInfo().getProductOptions()
        .forEach( productOption -> {
          assertNotNull(productOption.getInventory());
        });

    assertNotNull(product);
    assertTrue(product.getProductInfo().getProductOptions().size() > 0);
    assertNotNull(product.getReview());

  }

  @Test
  void retrieveProductDetailsWithInventoryApproach2() {
    String productId = "ABC123";

    Product product = productService.retrieveProductDetailsWithInventoryApproach2(productId);

    product.getProductInfo().getProductOptions()
        .forEach( productOption -> {
          assertNotNull(productOption.getInventory());
        });

    assertNotNull(product);
    assertTrue(product.getProductInfo().getProductOptions().size() > 0);
    assertNotNull(product.getReview());
  }
}