package com.sarthak.ecommerce.web;
import com.sarthak.ecommerce.dto.ApiDtos.*;
import com.sarthak.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final AuthService service; public AuthController(AuthService s){service=s;}
 @PostMapping("/register") public TokenResponse register(@Valid @RequestBody RegisterRequest r){return service.register(r);}
 @PostMapping("/login") public TokenResponse login(@Valid @RequestBody LoginRequest r){return service.login(r);}
 @PostMapping("/refresh") public TokenResponse refresh(@Valid @RequestBody RefreshRequest r){return service.refresh(r);}
}
