package com.omarahmed42.ecommerce.DTO;

import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
public class BannedUserDTO {
    private BigInteger userId;
    private String banReason;
    private String ip;
    private Timestamp expirationDate;
}