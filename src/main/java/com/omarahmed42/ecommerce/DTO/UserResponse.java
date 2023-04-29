package com.omarahmed42.ecommerce.DTO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import lombok.Data;

@Data
public class UserResponse implements Serializable {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Timestamp createdAt;
}
