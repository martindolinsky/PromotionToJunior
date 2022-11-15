package sk.dolinsky.udemyapp.multithreading.service;

import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.delay;

import sk.dolinsky.udemyapp.multithreading.domain.Inventory;
import sk.dolinsky.udemyapp.multithreading.domain.ProductOption;

public class InventoryServiceImpl implements InventoryService {

  @Override
  public Inventory retrieveInventory(ProductOption productOption) {
    delay(500);
    return Inventory.builder()
        .count(2).build();
  }
}
