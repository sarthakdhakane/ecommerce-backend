package com.sarthak.ecommerce.web;
import com.sarthak.ecommerce.dto.ApiDtos.*;
import com.sarthak.ecommerce.service.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/admin") @PreAuthorize("hasRole('ADMIN')")
public class AdminController {
 private final CategoryService categories;
 public AdminController(CategoryService c){categories=c;}
 @PostMapping("/categories") public CategoryResponse createCategory(@jakarta.validation.Valid @RequestBody CategoryRequest r){return categories.create(r);}
}
