package com.sarthak.ecommerce.repo;
import com.sarthak.ecommerce.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoryRepository extends JpaRepository<Category,Long>{}
