package com.sarthak.ecommerce.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name="users", indexes=@Index(name="idx_users_email", columnList="email", unique=true))
public class AppUser {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true, length=190) private String email;
    @Column(nullable=false) private String passwordHash;
    @Column(nullable=false, length=80) private String fullName;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private Role role = Role.USER;
    @Column(nullable=false) private Instant createdAt = Instant.now();
    protected AppUser() {}
    public AppUser(String email, String passwordHash, String fullName) { this.email=email; this.passwordHash=passwordHash; this.fullName=fullName; }
    public Long getId(){return id;} public String getEmail(){return email;} public String getPasswordHash(){return passwordHash;}
    public String getFullName(){return fullName;} public Role getRole(){return role;} public Instant getCreatedAt(){return createdAt;}
    public void setRole(Role role){this.role=role;}
}
