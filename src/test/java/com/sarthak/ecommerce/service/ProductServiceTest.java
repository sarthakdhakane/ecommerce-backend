package com.sarthak.ecommerce.service;

import com.sarthak.ecommerce.domain.Category;
import com.sarthak.ecommerce.domain.Product;
import com.sarthak.ecommerce.repo.CategoryRepository;
import com.sarthak.ecommerce.repo.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {
 @Test void getMapsProductToResponse() {
  ProductRepository products=mock(ProductRepository.class); CategoryRepository categories=mock(CategoryRepository.class);
  Category c=new Category("Books","Books"); Product p=new Product("Java","Java book",new BigDecimal("499.00"),3,null,c);
  when(products.findById(1L)).thenReturn(Optional.of(p));
  ProductService service=new ProductService(products,categories);
  var response=service.get(1L);
  assertEquals("Java",response.name());assertEquals(new BigDecimal("499.00"),response.price());
 }
}
