package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.DTO.UserResponse;
import com.omarahmed42.ecommerce.model.User;

public interface UserService {
    public void addUser(User user);
    public void deleteUser(UUID id);
    public void updateUser(User user);
    public UserResponse getUser(UUID id);
}
