package com.omarahmed42.ecommerce.DTO;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserRegistrationDTO implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
}
