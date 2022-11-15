package sk.dolinsky.udemyapp.multithreading.service;

import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.delay;

import sk.dolinsky.udemyapp.multithreading.domain.checkout.CartItem;

public class PriceValidatorService {

  public boolean isCartItemInvalid(CartItem cartItem){
    int cartId = cartItem.getItemId();
    delay(500);
    if (cartId == 7 || cartId == 9 || cartId == 11) {
      return true;
    }
    return false;
  }
}
