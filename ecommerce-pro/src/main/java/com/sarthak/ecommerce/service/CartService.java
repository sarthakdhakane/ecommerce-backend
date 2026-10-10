package com.sarthak.ecommerce.service;
import com.sarthak.ecommerce.domain.*;
import com.sarthak.ecommerce.dto.ApiDtos.*;
import com.sarthak.ecommerce.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
@Service public class CartService {
 private final CartItemRepository cart; private final ProductRepository products; private final UserRepository users;
 public CartService(CartItemRepository c,ProductRepository p,UserRepository u){cart=c;products=p;users=u;}
 @Transactional public CartResponse add(String email,Long productId,int quantity){
  Product p=products.findById(productId).filter(Product::isActive).orElseThrow(()->new java.util.NoSuchElementException("Product not found"));
  if(p.getStock()<quantity)throw new IllegalArgumentException("Insufficient stock");
  AppUser u=users.findByEmailIgnoreCase(email).orElseThrow();
  CartItem item=cart.findByUserEmailAndProductId(email,productId).orElseGet(()->new CartItem(u,p,0));
  item.setQuantity(item.getQuantity()+quantity);cart.save(item);return get(email);
 }
 @Transactional public CartResponse update(String email,Long productId,int quantity){
  CartItem i=cart.findByUserEmailAndProductId(email,productId).orElseThrow(()->new java.util.NoSuchElementException("Cart item not found"));
  if(i.getProduct().getStock()<quantity)throw new IllegalArgumentException("Insufficient stock");i.setQuantity(quantity);cart.save(i);return get(email);
 }
 @Transactional public CartResponse remove(String email,Long productId){cart.findByUserEmailAndProductId(email,productId).ifPresent(cart::delete);return get(email);}
 @Transactional(readOnly=true) public CartResponse get(String email){
  List<CartLine> lines=cart.findByUserEmailOrderById(email).stream().map(i->new CartLine(i.getProduct().getId(),i.getProduct().getName(),i.getProduct().getPrice(),i.getQuantity(),i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))).toList();
  return new CartResponse(lines,lines.stream().map(CartLine::lineTotal).reduce(BigDecimal.ZERO,BigDecimal::add));
 }
}
