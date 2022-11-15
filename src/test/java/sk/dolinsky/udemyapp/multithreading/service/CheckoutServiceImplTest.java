package sk.dolinsky.udemyapp.multithreading.service;

import static org.junit.jupiter.api.Assertions.*;
import static sk.dolinsky.udemyapp.multithreading.domain.checkout.CheckoutStatus.SUCCESS;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.dolinsky.udemyapp.multithreading.domain.checkout.Cart;
import sk.dolinsky.udemyapp.multithreading.domain.checkout.CheckoutResponse;
import sk.dolinsky.udemyapp.multithreading.util.CommonUtil;
import sk.dolinsky.udemyapp.multithreading.util.DataSet;

class CheckoutServiceImplTest {
  PriceValidatorService priceValidatorService;
  CheckoutService checkoutService;

  @BeforeEach
  void setUp() {
    priceValidatorService = new PriceValidatorService();
    checkoutService = new CheckoutServiceImpl(priceValidatorService);
    CommonUtil.stopWatchReset();
  }

  @Test
  void testCheckout6Items() {
    Cart cart = DataSet.createCart(6);

    CheckoutResponse checkoutResponse = checkoutService.checkout(cart);

    assertEquals(SUCCESS, checkoutResponse.getCheckoutStatus());

  }
}