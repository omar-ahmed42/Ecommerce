package com.omarahmed42.ecommerce.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.DTO.UserRegistrationDTO;
import com.omarahmed42.ecommerce.DTO.UserResponse;
import com.omarahmed42.ecommerce.service.UserService;

@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> register(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        userService.addUser(userRegistrationDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") UUID id,
            @RequestBody UserRegistrationDTO userRegistrationDTO) {
        userService.updateUser(id, userRegistrationDTO);
        return ResponseEntity.noContent().build();
    }
}
