package sk.dolinsky.udemyapp.multithreading.service;

import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.delay;

import java.util.List;
import sk.dolinsky.udemyapp.multithreading.domain.ProductInfo;
import sk.dolinsky.udemyapp.multithreading.domain.ProductOption;

public class ProductInfoServiceImpl implements ProductInfoService {

  @Override
  public ProductInfo retrieveProductInfo(String productId) {
    delay(1000);
    List<ProductOption> productOptions = List.of(new ProductOption(1, "64GB", "Black", 699.99),
        new ProductOption(2, "128GB", "Black", 749.99),
        new ProductOption(3, "64GB", "Black", 699.99),
        new ProductOption(4, "128GB", "Black", 749.99));
    return ProductInfo.builder().productId(productId)
        .productOptions(productOptions)
        .build();
  }
}
