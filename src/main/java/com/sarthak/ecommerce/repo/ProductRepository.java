package com.sarthak.ecommerce.repo;
import com.sarthak.ecommerce.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface ProductRepository extends JpaRepository<Product,Long>{
 @Query("select p from Product p join fetch p.category where p.active=true and (:q is null or lower(p.name) like lower(concat('%',:q,'%')) or lower(p.description) like lower(concat('%',:q,'%'))) and (:categoryId is null or p.category.id=:categoryId)")
 Page<Product> search(@Param("q") String q,@Param("categoryId") Long categoryId,Pageable pageable);
}
