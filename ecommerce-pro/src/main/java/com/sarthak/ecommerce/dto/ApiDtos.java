package com.sarthak.ecommerce.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public final class ApiDtos {
 private ApiDtos(){}
 public record RegisterRequest(@NotBlank @Email @Size(max=190) String email,@NotBlank @Size(min=10,max=72) String password,@NotBlank @Size(max=80) String fullName){}
 public record LoginRequest(@NotBlank @Email String email,@NotBlank String password){}
 public record TokenResponse(String accessToken,String refreshToken,String tokenType){}
 public record RefreshRequest(@NotBlank String refreshToken){}
 public record ProductRequest(@NotBlank @Size(max=160) String name,@NotBlank @Size(max=3000) String description,@NotNull @DecimalMin("0.01") BigDecimal price,@Min(0) @Max(1000000) int stock,@Size(max=500) String imageUrl,@NotNull Long categoryId){}
 public record ProductResponse(Long id,String name,String description,BigDecimal price,int stock,String imageUrl,Long categoryId,String categoryName){}
 public record CategoryRequest(@NotBlank @Size(max=80) String name,@Size(max=500) String description){}
 public record CategoryResponse(Long id,String name,String description){}
 public record QuantityRequest(@Min(1) @Max(1000) int quantity){}
 public record CartLine(Long productId,String productName,BigDecimal unitPrice,int quantity,BigDecimal lineTotal){}
 public record CartResponse(List<CartLine> items,BigDecimal total){}
 public record CheckoutRequest(@NotBlank @Size(max=80) String idempotencyKey){}
 public record CheckoutResponse(Long orderId,String checkoutUrl,String status,BigDecimal total){}
 public record OrderItemResponse(Long productId,String productName,int quantity,BigDecimal unitPrice){}
 public record OrderResponse(Long id,String status,BigDecimal total,String createdAt,List<OrderItemResponse> items){}
 public record MessageResponse(String message){}
}
