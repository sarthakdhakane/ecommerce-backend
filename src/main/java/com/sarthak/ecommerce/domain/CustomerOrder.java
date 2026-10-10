package com.sarthak.ecommerce.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Entity @Table(name="customer_orders", indexes=@Index(name="idx_orders_user_created",columnList="user_id,created_at"))
public class CustomerOrder {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="user_id") private AppUser user;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal total;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private OrderStatus status=OrderStatus.PENDING_PAYMENT;
 @Column(length=255) private String stripeSessionId;
 @Column(nullable=false,unique=true,length=80) private String idempotencyKey;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 @OneToMany(mappedBy="order",cascade=CascadeType.ALL,orphanRemoval=true) private List<OrderItem> items=new ArrayList<>();
 protected CustomerOrder(){}
 public CustomerOrder(AppUser user,BigDecimal total,String key){this.user=user;this.total=total;this.idempotencyKey=key;}
 public void addItem(OrderItem item){items.add(item);item.setOrder(this);}
 public Long getId(){return id;} public AppUser getUser(){return user;} public BigDecimal getTotal(){return total;}
 public OrderStatus getStatus(){return status;} public void setStatus(OrderStatus s){status=s;}
 public String getStripeSessionId(){return stripeSessionId;} public void setStripeSessionId(String s){stripeSessionId=s;}
 public String getIdempotencyKey(){return idempotencyKey;} public Instant getCreatedAt(){return createdAt;} public List<OrderItem> getItems(){return items;}
}
