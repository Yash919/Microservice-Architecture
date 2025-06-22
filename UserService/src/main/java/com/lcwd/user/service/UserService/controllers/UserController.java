package com.lcwd.user.service.UserService.controllers;

import com.lcwd.user.service.UserService.entities.User;
import com.lcwd.user.service.UserService.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User user1 = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    @GetMapping("/{userId}")
    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallBack")
    public ResponseEntity<User> getUser(@PathVariable String userId){
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    // Creating FallBack Method
    public ResponseEntity<User> ratingHotelFallBack(String userId, Exception ex){
        User user = User.builder().email("Dummy@gmail.com").name("Dummy").about("MicroService is down").userId("stew").build();
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    // Implementing Retry
    int retryCount = 1;
    @GetMapping
    //@CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallBack")
    //@Retry(name = "ratingHotelService",fallbackMethod = "ratingHotelFallBack") // For retry I have done UserServiceApplication annotation @EnableAspectJAutoProxy(proxyTargetClass = true) and pom dependency
    @RateLimiter( name = "userRateLimiter", fallbackMethod = "ratingHotelFallBack") // To test you need to download JMeter
    public ResponseEntity<List<User>> getAllUser(){
        System.out.println(retryCount++);
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    // FallBack Method for all users
    public ResponseEntity<List<User>> ratingHotelFallBack(Throwable ex) {
        System.out.println("Fallback is executed due to: " + ex.getMessage());
        List<User> fallbackUsers = new ArrayList<>();
        fallbackUsers.add(User.builder()
                .email("dummy@gmail.com")
                .name("Fallback User")
                .about("Service temporarily unavailable")
                .userId("dummy-id")
                .build());
        return ResponseEntity.ok(fallbackUsers);
    }

}
