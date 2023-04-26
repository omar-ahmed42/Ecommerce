package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.exception.BannedUserNotFoundException;
import com.omarahmed42.ecommerce.exception.UserNotFoundException;
import com.omarahmed42.ecommerce.model.BannedUser;
import com.omarahmed42.ecommerce.repository.BannedUserRepository;
import com.omarahmed42.ecommerce.repository.UserRepository;

@Service
public class BannedUserServiceImpl implements BannedUserService {

    private final BannedUserRepository bannedUserRepository;
    private final UserRepository userRepository;

    public BannedUserServiceImpl(BannedUserRepository bannedUserRepository, UserRepository userRepository) {
        this.bannedUserRepository = bannedUserRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void addBannedUser(BannedUser bannedUser) {
        userRepository
                .findById(bannedUser.getUserId())
                .ifPresentOrElse(
                        presentUser -> {
                            presentUser.setBanned(true);
                            userRepository.save(presentUser);
                            bannedUserRepository.save(bannedUser);
                        },
                        () -> {
                            throw new UserNotFoundException("User not found");
                        });
    }

    @Transactional
    @Override
    public void deleteBannedUser(UUID id) {
        bannedUserRepository
                .findById(id)
                .ifPresentOrElse(
                        presentBannedUser -> {
                            userRepository
                                    .findById(presentBannedUser.getUserId())
                                    .ifPresent(presentUser -> {
                                        presentUser.setBanned(false);
                                        userRepository.save(presentUser);
                                    });
                            bannedUserRepository.delete(presentBannedUser);
                        },
                        () -> {
                            throw new BannedUserNotFoundException("Banned user not found");
                        });
    }

    @Transactional
    @Override
    public void updateBannedUser(BannedUser bannedUser) {
        bannedUserRepository
                .findById(bannedUser.getUserId())
                .ifPresentOrElse(
                        presentBannedUser -> {
                            bannedUserRepository.save(bannedUser);
                        },
                        () -> {
                            throw new BannedUserNotFoundException("Banned user not found");
                        });
    }

    @Override
    public BannedUser getBannedUser(UUID id) {
        return bannedUserRepository
                .findById(id)
                .orElseThrow(() -> new BannedUserNotFoundException("Banned user not found"));
    }
}
