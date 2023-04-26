package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.model.BannedUser;

public interface BannedUserService {
    void addBannedUser(BannedUser bannedUser);
    void deleteBannedUser(UUID id);
    void updateBannedUser(BannedUser bannedUser);

    BannedUser getBannedUser(UUID id);
}
