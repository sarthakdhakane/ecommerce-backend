package com.sarthak.ecommerce.domain;
import jakarta.persistence.*;
@Entity @Table(name="categories", uniqueConstraints=@UniqueConstraint(columnNames="name"))
public class Category {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,length=80) private String name;
    @Column(length=500) private String description;
    protected Category(){}
    public Category(String name,String description){this.name=name;this.description=description;}
    public Long getId(){return id;} public String getName(){return name;} public String getDescription(){return description;}
    public void setName(String n){name=n;} public void setDescription(String d){description=d;}
}
