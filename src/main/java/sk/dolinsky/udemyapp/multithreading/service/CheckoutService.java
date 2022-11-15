package sk.dolinsky.udemyapp.multithreading.service;

import sk.dolinsky.udemyapp.multithreading.domain.checkout.Cart;
import sk.dolinsky.udemyapp.multithreading.domain.checkout.CheckoutResponse;

public interface CheckoutService {

  CheckoutResponse checkout(Cart cart);

}
