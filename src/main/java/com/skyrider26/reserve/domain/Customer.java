package com.skyrider26.reserve.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "billing_address")
    private String billingAddress;

    @NotNull
    @Column(name = "delivery_address_1", nullable = false)
    private String deliveryAddress1;

    @NotNull
    @Column(name = "delivery_address_2", nullable = false)
    private String deliveryAddress2;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reserve> reserves = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ShoppingCart> shoppingcarts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Customer firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Customer lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public Customer address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public Customer billingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getDeliveryAddress1() {
        return deliveryAddress1;
    }

    public Customer deliveryAddress1(String deliveryAddress1) {
        this.deliveryAddress1 = deliveryAddress1;
        return this;
    }

    public void setDeliveryAddress1(String deliveryAddress1) {
        this.deliveryAddress1 = deliveryAddress1;
    }

    public String getDeliveryAddress2() {
        return deliveryAddress2;
    }

    public Customer deliveryAddress2(String deliveryAddress2) {
        this.deliveryAddress2 = deliveryAddress2;
        return this;
    }

    public void setDeliveryAddress2(String deliveryAddress2) {
        this.deliveryAddress2 = deliveryAddress2;
    }

    public Set<Reserve> getReserves() {
        return reserves;
    }

    public Customer reserves(Set<Reserve> reserves) {
        this.reserves = reserves;
        return this;
    }

    public Customer addReserve(Reserve reserve) {
        this.reserves.add(reserve);
        reserve.setCustomer(this);
        return this;
    }

    public Customer removeReserve(Reserve reserve) {
        this.reserves.remove(reserve);
        reserve.setCustomer(null);
        return this;
    }

    public void setReserves(Set<Reserve> reserves) {
        this.reserves = reserves;
    }

    public Set<ShoppingCart> getShoppingcarts() {
        return shoppingcarts;
    }

    public Customer shoppingcarts(Set<ShoppingCart> shoppingCarts) {
        this.shoppingcarts = shoppingCarts;
        return this;
    }

    public Customer addShoppingcart(ShoppingCart shoppingCart) {
        this.shoppingcarts.add(shoppingCart);
        shoppingCart.setCustomer(this);
        return this;
    }

    public Customer removeShoppingcart(ShoppingCart shoppingCart) {
        this.shoppingcarts.remove(shoppingCart);
        shoppingCart.setCustomer(null);
        return this;
    }

    public void setShoppingcarts(Set<ShoppingCart> shoppingCarts) {
        this.shoppingcarts = shoppingCarts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        if (customer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", address='" + getAddress() + "'" +
            ", billingAddress='" + getBillingAddress() + "'" +
            ", deliveryAddress1='" + getDeliveryAddress1() + "'" +
            ", deliveryAddress2='" + getDeliveryAddress2() + "'" +
            "}";
    }
}
