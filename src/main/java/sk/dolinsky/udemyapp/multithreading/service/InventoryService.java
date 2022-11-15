package sk.dolinsky.udemyapp.multithreading.service;

import sk.dolinsky.udemyapp.multithreading.domain.Inventory;
import sk.dolinsky.udemyapp.multithreading.domain.ProductOption;

public interface InventoryService {

  Inventory retrieveInventory(ProductOption productOption);

}
