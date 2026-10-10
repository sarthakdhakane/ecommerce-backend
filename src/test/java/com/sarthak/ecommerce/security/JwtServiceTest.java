package com.sarthak.ecommerce.security;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
 private final JwtService service = new JwtService("0123456789abcdef0123456789abcdef",15,7);
 @Test void accessTokenContainsSubjectAndType() {
  var claims=service.parse(service.accessToken("test@example.com", List.of("USER")));
  assertEquals("test@example.com",claims.getSubject());
  assertEquals("access",claims.get("type"));
  assertEquals(List.of("USER"),claims.get("roles"));
 }
 @Test void refreshTokenIsDistinctType() {
  var claims=service.parse(service.refreshToken("test@example.com"));
  assertEquals("refresh",claims.get("type"));
 }
}
