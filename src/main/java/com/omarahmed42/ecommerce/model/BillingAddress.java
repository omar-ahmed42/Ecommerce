package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "billing_address", schema = "ecommerce")
public class BillingAddress implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private byte[] id;

    @Basic
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Basic
    @Column(name = "address", nullable = false, length = -1)
    private String address;

    @Basic
    @Column(name = "postal_code", nullable = true, length = 10)
    private String postalCode;

    @OneToMany(mappedBy = "billingAddressByBillingAddressId", fetch = FetchType.LAZY)
    private Collection<Customer> customersById;

    @OneToMany(mappedBy = "billingAddressByBillingAddressId", fetch = FetchType.LAZY)
    private Collection<Orders> ordersById;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BillingAddress that = (BillingAddress) o;
        return Arrays.equals(id, that.id) && Objects.equals(country, that.country)
                && Objects.equals(address, that.address) && Objects.equals(postalCode, that.postalCode);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(country, address, postalCode);
        result = 31 * result + Arrays.hashCode(id);
        return result;
    }
}
