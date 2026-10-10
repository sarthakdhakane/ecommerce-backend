package com.sarthak.ecommerce.repo;
import com.sarthak.ecommerce.domain.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface OrderRepository extends JpaRepository<CustomerOrder,Long>{ List<CustomerOrder> findByUserEmailOrderByCreatedAtDesc(String email); Optional<CustomerOrder> findByIdAndUserEmail(Long id,String email); Optional<CustomerOrder> findByIdempotencyKey(String key); Optional<CustomerOrder> findByStripeSessionId(String id); }
