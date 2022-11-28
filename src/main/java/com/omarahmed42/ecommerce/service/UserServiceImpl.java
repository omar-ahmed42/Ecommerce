package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.event.OnRegistrationEvent;
import com.omarahmed42.ecommerce.exception.EmailAlreadyExistsException;
import com.omarahmed42.ecommerce.exception.UserNotFoundException;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    @Override
    public void addUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw new EmailAlreadyExistsException("Email " + user.getEmail() + " is already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        applicationEventPublisher.publishEvent(new OnRegistrationEvent(user));
    }

    @Transactional
    @Override
    public void deleteUser(byte[] id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new UserNotFoundException("User with id " + Arrays.toString(id) + " doesn't exist");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        if (!userRepository.existsById(user.getId())){
            throw new UserNotFoundException("User doesn't exist");
        }

        userRepository.save(user);
    }
}
