package com.sarthak.ecommerce.service;
import com.sarthak.ecommerce.domain.Category;
import com.sarthak.ecommerce.dto.ApiDtos.*;
import com.sarthak.ecommerce.repo.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service public class CategoryService {
 private final CategoryRepository repo;
 public CategoryService(CategoryRepository r){repo=r;}
 @Transactional(readOnly=true) public List<CategoryResponse> all(){return repo.findAll().stream().map(c->new CategoryResponse(c.getId(),c.getName(),c.getDescription())).toList();}
 @Transactional public CategoryResponse create(CategoryRequest r){Category c=repo.save(new Category(r.name(),r.description()));return new CategoryResponse(c.getId(),c.getName(),c.getDescription());}
}
