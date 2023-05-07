package com.omarahmed42.ecommerce.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.omarahmed42.ecommerce.DTO.UserRegistrationDTO;
import com.omarahmed42.ecommerce.DTO.UserResponse;
import com.omarahmed42.ecommerce.enums.Role;
import com.omarahmed42.ecommerce.event.OnRegistrationEvent;
import com.omarahmed42.ecommerce.exception.EmailAlreadyExistsException;
import com.omarahmed42.ecommerce.exception.InvalidInputException;
import com.omarahmed42.ecommerce.exception.MissingFieldException;
import com.omarahmed42.ecommerce.exception.UserNotFoundException;
import com.omarahmed42.ecommerce.model.Customer;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.model.Vendor;
import com.omarahmed42.ecommerce.repository.CustomerRepository;
import com.omarahmed42.ecommerce.repository.UserRepository;
import com.omarahmed42.ecommerce.repository.VendorRepository;
import com.omarahmed42.ecommerce.util.UserDetailsUtils;

import io.micrometer.core.instrument.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;
    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            ApplicationEventPublisher applicationEventPublisher, CustomerRepository customerRepository,
            VendorRepository vendorRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') || isAnonymous()")
    public void addUser(UserRegistrationDTO userRegistrationDTO) {
        validateUser(userRegistrationDTO);
        User user = modelMapper.map(userRegistrationDTO, User.class);
        if (userRepository.existsByEmail(user.getEmail()))
            throw new EmailAlreadyExistsException("Email " + user.getEmail() + " is already in use");

        validateRoles(userRegistrationDTO.getAssignedRoles());
        addRoles(user, userRegistrationDTO.getAssignedRoles());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);

        if (userRegistrationDTO.getAssignedRoles().contains(Role.CUSTOMER))
            customerRepository.save(new Customer(user.getId()));
        else if (userRegistrationDTO.getAssignedRoles().contains(Role.UNVERIFIED_VENDOR))
            vendorRepository.save(new Vendor(user.getId()));
        applicationEventPublisher.publishEvent(new OnRegistrationEvent(user));
    }

    private void validateRoles(Set<Role> roles) {
        if (UserDetailsUtils.isAuthenticated() && UserDetailsUtils.hasRole(Role.ADMIN)) {
            List<Role> incompatibleRoles = new LinkedList<>();
            for (Role role : roles) {
                if (role == Role.ADMIN || role == Role.CUSTOMER || role == Role.UNVERIFIED_VENDOR
                        || role == Role.VERIFIED_VENDOR)
                    incompatibleRoles.add(role);
            }
            if (incompatibleRoles.size() >= 2)
                throw new InvalidInputException("Incompatible roles");
        } else if (!UserDetailsUtils.isAuthenticated()) {
            roles.removeIf(role -> role != Role.CUSTOMER && role != Role.UNVERIFIED_VENDOR);
            if (roles.contains(Role.CUSTOMER) && roles.contains(Role.UNVERIFIED_VENDOR))
                throw new InvalidInputException("Incompatible roles");
        }
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
        if (userRegistrationDTO.getAssignedRoles() == null || userRegistrationDTO.getAssignedRoles().isEmpty())
            throw new MissingFieldException("Roles are missing");
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(UUID id) {
        if (ObjectUtils.isEmpty(id))
            throw new MissingFieldException("User id is missing");
        userRepository.delete(userRepository
                .findById(id).orElseThrow(UserNotFoundException::new));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') || principal.user.id == #userId")
    public void updateUser(UUID userId, UserRegistrationDTO userRegistrationDTO) {
        if (UserDetailsUtils.isAuthenticated() && UserDetailsUtils.hasRole(Role.ADMIN))
            validateRoles(userRegistrationDTO.getAssignedRoles());
        else
            userRegistrationDTO.setAssignedRoles(null);

        User user = userRepository
                .findById(userId)
                .orElseThrow(UserNotFoundException::new);
        addRoles(user, userRegistrationDTO.getAssignedRoles());
        modelMapper.map(user, userRegistrationDTO.getAssignedRoles());
        userRepository.save(user);
    }

    private void addRoles(User user, Set<Role> roles) {
        if (roles != null && !roles.isEmpty()) {
            List<com.omarahmed42.ecommerce.model.Role> roleEntities = roles.stream().filter(Objects::nonNull)
                    .map(role -> new com.omarahmed42.ecommerce.model.Role(role.getValue())).toList();
            user.getRoles().addAll(roleEntities);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return modelMapper.map(user, UserResponse.class);
    }
}
