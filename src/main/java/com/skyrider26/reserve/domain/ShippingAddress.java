package com.skyrider26.reserve.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "shipping_address")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ShippingAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_id")
    private String addressId;

    @Column(name = "geocode")
    private String geocode;

    @Column(name = "street_1")
    private String street1;

    @Column(name = "street_2")
    private String street2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "pobox")
    private String pobox;

    @Column(name = "postal_code")
    private String postalCode;

    @OneToOne
    @JoinColumn(unique = true)
    private Reserve reserve;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressId() {
        return addressId;
    }

    public ShippingAddress addressId(String addressId) {
        this.addressId = addressId;
        return this;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getGeocode() {
        return geocode;
    }

    public ShippingAddress geocode(String geocode) {
        this.geocode = geocode;
        return this;
    }

    public void setGeocode(String geocode) {
        this.geocode = geocode;
    }

    public String getStreet1() {
        return street1;
    }

    public ShippingAddress street1(String street1) {
        this.street1 = street1;
        return this;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public ShippingAddress street2(String street2) {
        this.street2 = street2;
        return this;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public ShippingAddress city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public ShippingAddress state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public ShippingAddress country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPobox() {
        return pobox;
    }

    public ShippingAddress pobox(String pobox) {
        this.pobox = pobox;
        return this;
    }

    public void setPobox(String pobox) {
        this.pobox = pobox;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public ShippingAddress postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Reserve getReserve() {
        return reserve;
    }

    public ShippingAddress reserve(Reserve reserve) {
        this.reserve = reserve;
        return this;
    }

    public void setReserve(Reserve reserve) {
        this.reserve = reserve;
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
        ShippingAddress shippingAddress = (ShippingAddress) o;
        if (shippingAddress.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shippingAddress.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShippingAddress{" +
            "id=" + getId() +
            ", addressId='" + getAddressId() + "'" +
            ", geocode='" + getGeocode() + "'" +
            ", street1='" + getStreet1() + "'" +
            ", street2='" + getStreet2() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", country='" + getCountry() + "'" +
            ", pobox='" + getPobox() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            "}";
    }
}
