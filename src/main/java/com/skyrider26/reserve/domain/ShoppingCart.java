package com.skyrider26.reserve.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ShoppingCart.
 */
@Entity
@Table(name = "shopping_cart")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created")
    private Instant created;

    @OneToMany(mappedBy = "shoppingCart")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReserveLine> reservelines = new HashSet<>();

    @ManyToOne
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreated() {
        return created;
    }

    public ShoppingCart created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Set<ReserveLine> getReservelines() {
        return reservelines;
    }

    public ShoppingCart reservelines(Set<ReserveLine> reserveLines) {
        this.reservelines = reserveLines;
        return this;
    }

    public ShoppingCart addReserveline(ReserveLine reserveLine) {
        this.reservelines.add(reserveLine);
        reserveLine.setShoppingCart(this);
        return this;
    }

    public ShoppingCart removeReserveline(ReserveLine reserveLine) {
        this.reservelines.remove(reserveLine);
        reserveLine.setShoppingCart(null);
        return this;
    }

    public void setReservelines(Set<ReserveLine> reserveLines) {
        this.reservelines = reserveLines;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ShoppingCart customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
        ShoppingCart shoppingCart = (ShoppingCart) o;
        if (shoppingCart.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shoppingCart.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
