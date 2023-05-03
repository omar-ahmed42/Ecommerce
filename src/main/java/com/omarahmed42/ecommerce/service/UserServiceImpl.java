package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.omarahmed42.ecommerce.DTO.UserRegistrationDTO;
import com.omarahmed42.ecommerce.DTO.UserResponse;
import com.omarahmed42.ecommerce.event.OnRegistrationEvent;
import com.omarahmed42.ecommerce.exception.EmailAlreadyExistsException;
import com.omarahmed42.ecommerce.exception.MissingFieldException;
import com.omarahmed42.ecommerce.exception.UserNotFoundException;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.repository.UserRepository;

import io.micrometer.core.instrument.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @Override
    @Transactional
    @Secured("hasRole(Role.ADMIN.toString()) || isAnonymous()")
    public void addUser(UserRegistrationDTO userRegistrationDTO) {
        validateUser(userRegistrationDTO);
        User user = modelMapper.map(userRegistrationDTO, User.class);
        if (userRepository.existsByEmail(user.getEmail()))
            throw new EmailAlreadyExistsException("Email " + user.getEmail() + " is already in use");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        applicationEventPublisher.publishEvent(new OnRegistrationEvent(user));
    }

    private void validateUser(UserRegistrationDTO userRegistrationDTO) {
        if (StringUtils.isBlank(userRegistrationDTO.getFirstName()))
            throw new MissingFieldException("First name is missing");
        if (StringUtils.isBlank(userRegistrationDTO.getLastName()))
            throw new MissingFieldException("Last name is missing");
        if (StringUtils.isBlank(userRegistrationDTO.getEmail()))
            throw new MissingFieldException("Email is missing");
        if (StringUtils.isBlank(userRegistrationDTO.getPassword()))
            throw new MissingFieldException("Password is missing");
        if (StringUtils.isBlank(userRegistrationDTO.getPhoneNumber()))
            throw new MissingFieldException("Phone number is missing");
    }

    @Override
    @Transactional
    @Secured("hasRole(Role.ADMIN.toString())")
    public void deleteUser(UUID id) {
        if (ObjectUtils.isEmpty(id))
            throw new MissingFieldException("User id is missing");
        userRepository.delete(userRepository
                .findById(id).orElseThrow(UserNotFoundException::new));
    }

    @Override
    @Transactional
    public void updateUser(UUID userId, UserRegistrationDTO userRegistrationDTO) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(UserNotFoundException::new);
        modelMapper.map(userRegistrationDTO, user);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return modelMapper.map(user, UserResponse.class);
    }
}
