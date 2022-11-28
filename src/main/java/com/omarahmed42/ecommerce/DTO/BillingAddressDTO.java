package com.omarahmed42.ecommerce.DTO;

import lombok.Data;

@Data
public class BillingAddressDTO {
    private String country;
    private String address;
    private String postalCode;
}
