package sk.dolinsky.udemyapp.multithreading.service;

import java.util.concurrent.ExecutionException;
import sk.dolinsky.udemyapp.multithreading.domain.Product;

public interface ProductService {

  Product retrieveProductDetails(String productId) throws Exception;

}
