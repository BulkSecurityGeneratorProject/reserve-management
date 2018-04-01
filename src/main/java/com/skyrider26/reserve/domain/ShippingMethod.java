package com.skyrider26.reserve.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ShippingMethod.
 */
@Entity
@Table(name = "shipping_method")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ShippingMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shipping_id")
    private String shippingId;

    @Column(name = "name")
    private String name;

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

    public String getShippingId() {
        return shippingId;
    }

    public ShippingMethod shippingId(String shippingId) {
        this.shippingId = shippingId;
        return this;
    }

    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }

    public String getName() {
        return name;
    }

    public ShippingMethod name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Reserve getReserve() {
        return reserve;
    }

    public ShippingMethod reserve(Reserve reserve) {
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
        ShippingMethod shippingMethod = (ShippingMethod) o;
        if (shippingMethod.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shippingMethod.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShippingMethod{" +
            "id=" + getId() +
            ", shippingId='" + getShippingId() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
