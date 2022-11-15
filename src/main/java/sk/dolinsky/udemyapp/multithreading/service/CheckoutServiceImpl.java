package sk.dolinsky.udemyapp.multithreading.service;

import static sk.dolinsky.udemyapp.multithreading.domain.checkout.CheckoutStatus.*;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.startTimer;
import static sk.dolinsky.udemyapp.multithreading.util.CommonUtil.timeTaken;

import java.util.List;
import java.util.stream.Collectors;
import sk.dolinsky.udemyapp.multithreading.domain.checkout.Cart;
import sk.dolinsky.udemyapp.multithreading.domain.checkout.CartItem;
import sk.dolinsky.udemyapp.multithreading.domain.checkout.CheckoutResponse;

public class CheckoutServiceImpl implements CheckoutService {

  private PriceValidatorService priceValidatorService;

  public CheckoutServiceImpl(PriceValidatorService priceValidatorService) {
    this.priceValidatorService = priceValidatorService;
  }

  @Override
  public CheckoutResponse checkout(Cart cart) {
    startTimer();
    List<CartItem> priceValidationList = cart.getCartItemList().parallelStream()
        .map(cartItem -> {
          boolean isPriceInvalid = priceValidatorService.isCartItemInvalid(cartItem);
          cartItem.setExpired(isPriceInvalid);
          return cartItem;
        })
        .filter(CartItem::isExpired)
        .collect(Collectors.toList());

    if (!priceValidationList.isEmpty()) {
      return new CheckoutResponse(FAILURE, priceValidationList);
    }
    timeTaken();
    return new CheckoutResponse(SUCCESS);
  }
}
