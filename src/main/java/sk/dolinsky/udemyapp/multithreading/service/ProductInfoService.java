package sk.dolinsky.udemyapp.multithreading.service;

import sk.dolinsky.udemyapp.multithreading.domain.ProductInfo;

public interface ProductInfoService {

  ProductInfo retrieveProductInfo(String productId);

}
