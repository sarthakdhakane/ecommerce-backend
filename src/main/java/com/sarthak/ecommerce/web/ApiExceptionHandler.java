package com.sarthak.ecommerce.web;
import com.sarthak.ecommerce.dto.ApiDtos.MessageResponse;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(NoSuchElementException.class) ResponseEntity<MessageResponse> notFound(Exception e){return ResponseEntity.status(404).body(new MessageResponse(e.getMessage()));}
 @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<MessageResponse> badRequest(Exception e){return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));}
 @ExceptionHandler(BadCredentialsException.class) ResponseEntity<MessageResponse> unauthorized(Exception e){return ResponseEntity.status(401).body(new MessageResponse("Invalid credentials or token"));}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<MessageResponse> validation(MethodArgumentNotValidException e){return ResponseEntity.badRequest().body(new MessageResponse(e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).findFirst().orElse("Validation failed")));}
}
