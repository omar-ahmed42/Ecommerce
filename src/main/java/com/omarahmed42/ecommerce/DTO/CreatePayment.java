package com.omarahmed42.ecommerce.DTO;

import java.math.BigInteger;

import com.google.gson.annotations.SerializedName;

public class CreatePayment {
    @SerializedName("items")
    CartItemDTO[] items;

    @SerializedName("userId")
    BigInteger userId;

    @SerializedName("billingAddress")
    BillingAddressDTO billingAddress;

    public CartItemDTO[] getItems() {
        return this.items;
    }

    public BigInteger getUserId() {
        return this.userId;
    }

    public BillingAddressDTO getBillingAddress(){
        return this.billingAddress;
    }
}