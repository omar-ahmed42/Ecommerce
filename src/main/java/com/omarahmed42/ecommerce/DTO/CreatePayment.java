package com.omarahmed42.ecommerce.DTO;

import java.util.UUID;

import com.google.gson.annotations.SerializedName;

public class CreatePayment {
    @SerializedName("items")
    private CartItemDTO[] items;

    @SerializedName("userId")
    private UUID userId;

    @SerializedName("billingAddress")
    private BillingAddressDTO billingAddress;

    public CartItemDTO[] getItems() {
        return this.items;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public BillingAddressDTO getBillingAddress(){
        return this.billingAddress;
    }
}