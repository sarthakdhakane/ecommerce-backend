package com.sarthak.ecommerce.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
@Entity @Table(name="products", indexes={@Index(name="idx_products_category",columnList="category_id"),@Index(name="idx_products_name",columnList="name")})
public class Product {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,length=160) private String name;
    @Column(nullable=false,length=3000) private String description;
    @Column(nullable=false,precision=12,scale=2) private BigDecimal price;
    @Column(nullable=false) private int stock;
    @Column(length=500) private String imageUrl;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="category_id",nullable=false) private Category category;
    @Column(nullable=false) private boolean active=true;
    @Column(nullable=false) private Instant createdAt=Instant.now();
    @Version private long version;
    protected Product(){}
    public Product(String name,String description,BigDecimal price,int stock,String imageUrl,Category category){
        this.name=name;this.description=description;this.price=price;this.stock=stock;this.imageUrl=imageUrl;this.category=category;
    }
    public Long getId(){return id;} public String getName(){return name;} public String getDescription(){return description;}
    public BigDecimal getPrice(){return price;} public int getStock(){return stock;} public String getImageUrl(){return imageUrl;}
    public Category getCategory(){return category;} public boolean isActive(){return active;} public Instant getCreatedAt(){return createdAt;}
    public long getVersion(){return version;} public void setName(String v){name=v;} public void setDescription(String v){description=v;}
    public void setPrice(BigDecimal v){price=v;} public void setStock(int v){stock=v;} public void setImageUrl(String v){imageUrl=v;}
    public void setCategory(Category v){category=v;} public void setActive(boolean v){active=v;}
}
