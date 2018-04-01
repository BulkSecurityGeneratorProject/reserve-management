package com.skyrider26.reserve.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ReserveLine.
 */
@Entity
@Table(name = "reserve_line")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReserveLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "shipping_number")
    private String shippingNumber;

    @Column(name = "total")
    private Long total;

    @ManyToOne
    private ShoppingCart shoppingCart;

    @ManyToOne
    private Reserve reserve;

    @ManyToOne
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ReserveLine quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getShippingNumber() {
        return shippingNumber;
    }

    public ReserveLine shippingNumber(String shippingNumber) {
        this.shippingNumber = shippingNumber;
        return this;
    }

    public void setShippingNumber(String shippingNumber) {
        this.shippingNumber = shippingNumber;
    }

    public Long getTotal() {
        return total;
    }

    public ReserveLine total(Long total) {
        this.total = total;
        return this;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public ReserveLine shoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        return this;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Reserve getReserve() {
        return reserve;
    }

    public ReserveLine reserve(Reserve reserve) {
        this.reserve = reserve;
        return this;
    }

    public void setReserve(Reserve reserve) {
        this.reserve = reserve;
    }

    public Product getProduct() {
        return product;
    }

    public ReserveLine product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
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
        ReserveLine reserveLine = (ReserveLine) o;
        if (reserveLine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reserveLine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReserveLine{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", shippingNumber='" + getShippingNumber() + "'" +
            ", total=" + getTotal() +
            "}";
    }
}
