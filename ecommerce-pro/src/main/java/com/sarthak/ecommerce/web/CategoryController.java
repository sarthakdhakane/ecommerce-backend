package com.sarthak.ecommerce.web;
import com.sarthak.ecommerce.dto.ApiDtos.*;
import com.sarthak.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/categories")
public class CategoryController {
 private final CategoryService service;public CategoryController(CategoryService s){service=s;}
 @GetMapping public List<CategoryResponse> all(){return service.all();}
 @PostMapping @PreAuthorize("hasRole('ADMIN')") public CategoryResponse create(@Valid @RequestBody CategoryRequest r){return service.create(r);}
}
