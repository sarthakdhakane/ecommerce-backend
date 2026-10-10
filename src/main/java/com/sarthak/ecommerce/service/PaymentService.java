package com.sarthak.ecommerce.service;

import com.sarthak.ecommerce.domain.*;
import com.sarthak.ecommerce.dto.ApiDtos.*;
import com.sarthak.ecommerce.repo.*;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {
 private final CartItemRepository cart; private final UserRepository users; private final ProductRepository products; private final OrderRepository orders;
 private final String secretKey,baseUrl;
 public PaymentService(CartItemRepository c,UserRepository u,ProductRepository p,OrderRepository o,
   @Value("${stripe.secret-key:}") String secretKey,@Value("${app.base-url:http://localhost:8080}") String baseUrl){
  cart=c;users=u;products=p;orders=o;this.secretKey=secretKey;this.baseUrl=baseUrl;
 }
 @Transactional
 public CheckoutResponse checkout(String email,String key) {
  var existing=orders.findByIdempotencyKey(key);
  if(existing.isPresent()) {var o=existing.get();return new CheckoutResponse(o.getId(),null,o.getStatus().name(),o.getTotal());}
  var user=users.findByEmailIgnoreCase(email).orElseThrow();
  var items=cart.findByUserEmailOrderById(email);
  if(items.isEmpty())throw new IllegalArgumentException("Cart is empty");
  BigDecimal total=BigDecimal.ZERO;
  for(var i:items){if(i.getProduct().getStock()<i.getQuantity())throw new IllegalArgumentException("Insufficient stock for "+i.getProduct().getName());total=total.add(i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())));}
  CustomerOrder order=new CustomerOrder(user,total,key);
  for(var i:items) order.addItem(new OrderItem(i.getProduct().getId(),i.getProduct().getName(),i.getQuantity(),i.getProduct().getPrice()));
  order=orders.saveAndFlush(order);
  if(secretKey==null||secretKey.isBlank()) return new CheckoutResponse(order.getId(),null,"PENDING_PAYMENT_DEMO",total);
  try {
   Stripe.apiKey=secretKey;
   SessionCreateParams.Builder params=SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl(baseUrl+"/payment/success?session_id={CHECKOUT_SESSION_ID}")
    .setCancelUrl(baseUrl+"/payment/cancel?order_id="+order.getId())
    .putMetadata("orderId",order.getId().toString());
   for(var i:items) params.addLineItem(SessionCreateParams.LineItem.builder().setQuantity((long)i.getQuantity())
    .setPriceData(SessionCreateParams.LineItem.PriceData.builder().setCurrency("inr")
     .setUnitAmount(i.getProduct().getPrice().movePointRight(2).longValueExact())
     .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder().setName(i.getProduct().getName()).build()).build()).build());
   Session session=Session.create(params.build());
   order.setStripeSessionId(session.getId());orders.save(order);
   return new CheckoutResponse(order.getId(),session.getUrl(),order.getStatus().name(),total);
  } catch(Exception e){throw new IllegalStateException("Unable to create Stripe checkout session. Verify Stripe configuration.",e);}
 }
 @Transactional public void handleCheckoutCompleted(String sessionId,String orderId){
  CustomerOrder o=orders.findByStripeSessionId(sessionId).orElseGet(()->orders.findById(Long.valueOf(orderId)).orElseThrow());
  if(o.getStatus()==OrderStatus.PAID)return;
  for(var item:o.getItems()){
   Product p=products.findById(item.getProductId()).orElseThrow();
   if(p.getStock()<item.getQuantity())throw new IllegalStateException("Stock changed before payment confirmation");
   p.setStock(p.getStock()-item.getQuantity());
  }
  o.setStatus(OrderStatus.PAID);orders.save(o);cart.deleteByUserEmail(o.getUser().getEmail());
 }
}
