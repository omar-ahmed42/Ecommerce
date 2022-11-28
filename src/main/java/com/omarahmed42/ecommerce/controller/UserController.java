package com.omarahmed42.ecommerce.controller;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.DTO.UserRegistrationDTO;
import com.omarahmed42.ecommerce.exception.UserNotFoundException;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.service.UserService;
import com.omarahmed42.ecommerce.util.UUIDHandler;


@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDTO userRegistrationDTO){
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userRegistrationDTO, User.class);
        user.setActive(false);
        user.setVerified(false);
        user.setBanned(false);
        userService.addUser(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/id=:id")
    @PreAuthorize("hasRole(Role.ADMIN.toString())")
    public ResponseEntity<String> deleteUser(@RequestParam("id") String id){
        if (isEmptyOrNullOrBlank(id)) {
            return ResponseEntity.unprocessableEntity().build();
        }
        try {
            byte[] idBytes = UUIDHandler.getByteArrayFromUUID(UUID.fromString(id));
            userService.deleteUser(idBytes);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException userNotFoundException){
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
    private boolean isEmptyOrNullOrBlank(String id){
        return id == null || id.equals("") || id.equals(" ");
    }
}
