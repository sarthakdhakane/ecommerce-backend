package com.sarthak.ecommerce.web;
import com.sarthak.ecommerce.dto.ApiDtos.*;
import com.sarthak.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/cart")
public class CartController {
 private final CartService service;public CartController(CartService s){service=s;}
 @GetMapping public CartResponse get(Authentication a){return service.get(a.getName());}
 @PostMapping("/items/{productId}") public CartResponse add(Authentication a,@PathVariable Long productId,@Valid @RequestBody QuantityRequest q){return service.add(a.getName(),productId,q.quantity());}
 @PutMapping("/items/{productId}") public CartResponse update(Authentication a,@PathVariable Long productId,@Valid @RequestBody QuantityRequest q){return service.update(a.getName(),productId,q.quantity());}
 @DeleteMapping("/items/{productId}") public CartResponse remove(Authentication a,@PathVariable Long productId){return service.remove(a.getName(),productId);}
}
