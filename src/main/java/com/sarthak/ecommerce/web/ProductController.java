package com.sarthak.ecommerce.web;
import com.sarthak.ecommerce.dto.ApiDtos.*;
import com.sarthak.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/products")
public class ProductController {
 private final ProductService service; public ProductController(ProductService s){service=s;}
 @GetMapping public Page<ProductResponse> search(@RequestParam(required=false) String q,@RequestParam(required=false) Long categoryId,
   @RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size,@RequestParam(defaultValue="createdAt") String sort){return service.search(q,categoryId,page,size,sort);}
 @GetMapping("/{id}") public ProductResponse get(@PathVariable Long id){return service.get(id);}
 @PostMapping @PreAuthorize("hasRole('ADMIN')") public ProductResponse create(@Valid @RequestBody ProductRequest r){return service.create(r);}
 @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ProductResponse update(@PathVariable Long id,@Valid @RequestBody ProductRequest r){return service.update(id,r);}
 @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public void delete(@PathVariable Long id){service.delete(id);}
}
