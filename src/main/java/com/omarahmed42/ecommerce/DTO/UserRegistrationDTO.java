package com.omarahmed42.ecommerce.DTO;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.omarahmed42.ecommerce.enums.Role;

import lombok.Data;

@Data
public class UserRegistrationDTO implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    @JsonProperty("roles")
    private Set<Role> assignedRoles;
}
