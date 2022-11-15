package sk.dolinsky.udemyapp.multithreading.completablefuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.dolinsky.udemyapp.multithreading.domain.Product;
import sk.dolinsky.udemyapp.multithreading.service.InventoryService;
import sk.dolinsky.udemyapp.multithreading.service.InventoryServiceImpl;
import sk.dolinsky.udemyapp.multithreading.service.ProductInfoService;
import sk.dolinsky.udemyapp.multithreading.service.ProductInfoServiceImpl;
import sk.dolinsky.udemyapp.multithreading.service.ReviewService;
import sk.dolinsky.udemyapp.multithreading.service.ReviewServiceImpl;
import sk.dolinsky.udemyapp.multithreading.util.CommonUtil;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplCompletableFutureExceptionTest {

  @InjectMocks
  ProductServiceImplCompletableFuture pscf;
  @Mock
  private ProductInfoService productInfoService;
  @Mock
  private ReviewService reviewService;
  @Mock
  private InventoryService inventoryService;

  @BeforeEach
  void setUp() {
    productInfoService = mock(ProductInfoServiceImpl.class);
    reviewService = mock(ReviewServiceImpl.class);
    inventoryService = mock(InventoryServiceImpl.class);
    pscf = new ProductServiceImplCompletableFuture(productInfoService, reviewService, inventoryService);
    CommonUtil.stopWatchReset();
  }

  @Test
  void retrieveProductDetailsWithInventoryApproach2() {
    String productId = "ABC123";
    when(productInfoService.retrieveProductInfo(any())).thenCallRealMethod();
    when(reviewService.retrieveReview(any())).thenThrow(new RuntimeException("Excpetion"));
    when(inventoryService.retrieveInventory(any())).thenCallRealMethod();

    Product product = pscf.retrieveProductDetailsWithInventoryApproach2(productId);

    product.getProductInfo().getProductOptions()
        .forEach( productOption -> {
          assertNotNull(productOption.getInventory());
        });

    assertNotNull(product);
    assertTrue(product.getProductInfo().getProductOptions().size() > 0);
    assertNotNull(product.getReview());
    assertEquals(0, product.getReview().getNoOfReviews());
  }
}