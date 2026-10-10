package com.sarthak.ecommerce.web;
import com.sarthak.ecommerce.dto.ApiDtos.*;
import com.sarthak.ecommerce.domain.CustomerOrder;
import com.sarthak.ecommerce.repo.OrderRepository;
import com.sarthak.ecommerce.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/orders")
public class OrderController {
 private final PaymentService payments; private final OrderRepository orders;
 public OrderController(PaymentService p,OrderRepository o){payments=p;orders=o;}
 @PostMapping("/checkout") public CheckoutResponse checkout(Authentication a,@Valid @RequestBody CheckoutRequest r){return payments.checkout(a.getName(),r.idempotencyKey());}
 @GetMapping public java.util.List<OrderResponse> mine(Authentication a){return orders.findByUserEmailOrderByCreatedAtDesc(a.getName()).stream().map(this::dto).toList();}
 @GetMapping("/{id}") public OrderResponse get(Authentication a,@PathVariable Long id){return dto(orders.findByIdAndUserEmail(id,a.getName()).orElseThrow(()->new java.util.NoSuchElementException("Order not found")));}
 private OrderResponse dto(CustomerOrder o){return new OrderResponse(o.getId(),o.getStatus().name(),o.getTotal(),o.getCreatedAt().toString(),o.getItems().stream().map(i->new OrderItemResponse(i.getProductId(),i.getProductName(),i.getQuantity(),i.getUnitPrice())).toList());}
}
