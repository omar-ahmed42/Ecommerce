package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.User;

public interface UserService {
    public void addUser(User user);
    public void deleteUser(byte[] id);
    public void updateUser(User user);
}
