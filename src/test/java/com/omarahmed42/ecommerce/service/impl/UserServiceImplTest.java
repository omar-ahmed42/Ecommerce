package com.omarahmed42.ecommerce.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.omarahmed42.ecommerce.DTO.UserRegistrationDTO;
import com.omarahmed42.ecommerce.event.OnRegistrationEvent;
import com.omarahmed42.ecommerce.exception.EmailAlreadyExistsException;
import com.omarahmed42.ecommerce.exception.MissingFieldException;
import com.omarahmed42.ecommerce.exception.UserNotFoundException;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.repository.UserRepository;
import com.omarahmed42.ecommerce.service.UserService;

@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private UserRepository mockedUserRepository;

    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;

    @SpyBean
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<OnRegistrationEvent> captor;

    private UserRegistrationDTO prepareUserRegistrationDTO() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setFirstName("First");
        userRegistrationDTO.setLastName("Last");
        userRegistrationDTO.setEmail("not.a.real.email@test.imagination");
        userRegistrationDTO.setPassword("user_pass");
        userRegistrationDTO.setPhoneNumber("0123456789");
        return userRegistrationDTO;
    }

    private User prepareUser() {
        User user = new User();
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("not.a.real.email@test.imagination");
        user.setPassword("user_pass");
        user.setPhoneNumber("0123456789");
        user.setActive(false);
        user.setBanned(false);
        return user;
    }

    @Test
    public void addUser_ThrowsEmailAlreadyExistsException() {
        UserRegistrationDTO userRegistration = prepareUserRegistrationDTO();
        when(mockedUserRepository.existsByEmail(userRegistration.getEmail())).thenReturn(true);
        org.junit.jupiter.api.Assertions.assertThrows(EmailAlreadyExistsException.class,
                () -> userService.addUser(userRegistration),
                "Email not.a.real.email@test.imagination is already in use");
    }

    @Test
    public void addUser_EmptyFirstName_ThrowsMissingFieldException() {
        UserRegistrationDTO userRegistration = prepareUserRegistrationDTO();
        userRegistration.setFirstName(null);
        org.junit.jupiter.api.Assertions.assertThrows(MissingFieldException.class,
                () -> userService.addUser(userRegistration), "First name is missing");
    }

    @Test
    public void addUser_UserAddedSuccessfully() {
        UserRegistrationDTO userRegistration = prepareUserRegistrationDTO();

        when(mockedUserRepository.existsByEmail(userRegistration.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("user_pass");

        doNothing().when(applicationEventPublisher).publishEvent(any(OnRegistrationEvent.class));

        userService.addUser(userRegistration);
        verify(userRepository).save(any(User.class));

        User actual = userRepository.findByEmail("not.a.real.email@test.imagination").get();
        User expected = prepareUser();
        expected.setId(actual.getId());

        Assertions
                .assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("bannedUserById", "customersById", "vendorsById", "createdAt")
                .isEqualTo(expected);

    }

    @Test
    public void deleteUser_ThrowsMissingFieldException() {
        org.junit.jupiter.api.Assertions.assertThrows(MissingFieldException.class, () -> userService.deleteUser(null),
                "User id is missing");
    }

    @Test
    public void deleteUser_ThrowsUserNotFoundException() {
        UUID randomId = UUID.randomUUID();
        org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(randomId), "User not found");
    }

    @Test
    public void deleteUser_Successfully() {
        User user = prepareUser();
        user = userRepository.save(user);
        userService.deleteUser(user.getId());
        org.junit.jupiter.api.Assertions.assertTrue(userRepository.findById(user.getId()).isEmpty());
    }

    @Test
    public void getUser_ThrowsUserNotFoundException() {
        UUID randomId = UUID.randomUUID();
        org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.getUser(randomId), "User not found");
    }
}
