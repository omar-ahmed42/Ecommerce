package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.DTO.UserRegistrationDTO;
import com.omarahmed42.ecommerce.DTO.UserResponse;

public interface UserService {
    public void addUser(UserRegistrationDTO userRegistrationDTO);

    public void deleteUser(UUID id);

    public void updateUser(UUID userId, UserRegistrationDTO userRegistrationDTO);

    public UserResponse getUser(UUID id);
}
