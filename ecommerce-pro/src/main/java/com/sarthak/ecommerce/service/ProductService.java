package com.sarthak.ecommerce.service;

import com.sarthak.ecommerce.domain.*;
import com.sarthak.ecommerce.dto.ApiDtos.*;
import com.sarthak.ecommerce.repo.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
 private final ProductRepository products; private final CategoryRepository categories;
 public ProductService(ProductRepository p,CategoryRepository c){products=p;categories=c;}
 @Transactional(readOnly=true)
 public Page<ProductResponse> search(String q,Long categoryId,int page,int size,String sort){
  if(page<0) throw new IllegalArgumentException("page must be >= 0");
  if(size<1||size>100) throw new IllegalArgumentException("size must be between 1 and 100");
  String sortField=java.util.Set.of("name","price","createdAt").contains(sort)?sort:"createdAt";
  return products.search(q==null||q.isBlank()?null:q.trim(),categoryId,PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,sortField)))
   .map(this::dto);
 }
 @Cacheable(cacheNames="productById",key="#id")
 @Transactional(readOnly=true) public ProductResponse get(Long id){return dto(products.findById(id).filter(Product::isActive).orElseThrow(()->new java.util.NoSuchElementException("Product not found")));}
 @Transactional @CacheEvict(cacheNames="productById",allEntries=true)
 public ProductResponse create(ProductRequest r){Category c=categories.findById(r.categoryId()).orElseThrow(()->new java.util.NoSuchElementException("Category not found"));return dto(products.save(new Product(r.name(),r.description(),r.price(),r.stock(),r.imageUrl(),c)));}
 @Transactional @CacheEvict(cacheNames="productById",allEntries=true)
 public ProductResponse update(Long id,ProductRequest r){Product p=products.findById(id).orElseThrow(()->new java.util.NoSuchElementException("Product not found"));Category c=categories.findById(r.categoryId()).orElseThrow(()->new java.util.NoSuchElementException("Category not found"));p.setName(r.name());p.setDescription(r.description());p.setPrice(r.price());p.setStock(r.stock());p.setImageUrl(r.imageUrl());p.setCategory(c);return dto(p);}
 @Transactional @CacheEvict(cacheNames="productById",allEntries=true)
 public void delete(Long id){Product p=products.findById(id).orElseThrow(()->new java.util.NoSuchElementException("Product not found"));p.setActive(false);}
 public ProductResponse dto(Product p){return new ProductResponse(p.getId(),p.getName(),p.getDescription(),p.getPrice(),p.getStock(),p.getImageUrl(),p.getCategory().getId(),p.getCategory().getName());}
}
