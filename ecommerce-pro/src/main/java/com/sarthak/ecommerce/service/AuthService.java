package com.sarthak.ecommerce.service;

import com.sarthak.ecommerce.domain.AppUser;
import com.sarthak.ecommerce.dto.ApiDtos.*;
import com.sarthak.ecommerce.repo.UserRepository;
import com.sarthak.ecommerce.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import java.util.List;
import java.util.Locale;

@Service
public class AuthService {
 private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
 public AuthService(UserRepository users,PasswordEncoder encoder,JwtService jwt){this.users=users;this.encoder=encoder;this.jwt=jwt;}
 @Transactional public TokenResponse register(RegisterRequest req){
  String email=req.email().trim().toLowerCase(Locale.ROOT);
  if(users.existsByEmailIgnoreCase(email)) throw new IllegalArgumentException("Email is already registered");
  AppUser user=users.save(new AppUser(email,encoder.encode(req.password()),req.fullName().trim()));
  return tokens(user);
 }
 @Transactional(readOnly=true) public TokenResponse login(LoginRequest req){
  AppUser user=users.findByEmailIgnoreCase(req.email().trim()).orElseThrow(()->new BadCredentialsException("Invalid email or password"));
  if(!encoder.matches(req.password(),user.getPasswordHash())) throw new BadCredentialsException("Invalid email or password");
  return tokens(user);
 }
 @Transactional(readOnly=true) public TokenResponse refresh(RefreshRequest req){
  try {
   var claims=jwt.parse(req.refreshToken());
   if(!"refresh".equals(claims.get("type",String.class))) throw new BadCredentialsException("Invalid refresh token");
   AppUser user=users.findByEmailIgnoreCase(claims.getSubject()).orElseThrow(()->new BadCredentialsException("Invalid refresh token"));
   return tokens(user);
  } catch (io.jsonwebtoken.JwtException e) { throw new BadCredentialsException("Invalid refresh token"); }
 }
 private TokenResponse tokens(AppUser user){return new TokenResponse(jwt.accessToken(user.getEmail(),List.of(user.getRole().name())),jwt.refreshToken(user.getEmail()),"Bearer");}
}
