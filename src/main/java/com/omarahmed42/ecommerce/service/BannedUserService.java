package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.BannedUser;

public interface BannedUserService {
    void addBannedUser(BannedUser bannedUser);
    void deleteBannedUser(byte[] id);
    void updateBannedUser(BannedUser bannedUser);

    BannedUser getBannedUser(byte[] id);
}
