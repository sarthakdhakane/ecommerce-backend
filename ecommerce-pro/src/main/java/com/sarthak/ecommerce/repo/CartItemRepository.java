package com.sarthak.ecommerce.repo;
import com.sarthak.ecommerce.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface CartItemRepository extends JpaRepository<CartItem,Long>{ List<CartItem> findByUserEmailOrderById(String email); Optional<CartItem> findByUserEmailAndProductId(String email,Long productId); void deleteByUserEmail(String email); }
