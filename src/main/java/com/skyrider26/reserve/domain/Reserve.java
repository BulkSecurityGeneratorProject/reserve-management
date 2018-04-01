package com.skyrider26.reserve.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.skyrider26.reserve.domain.enumeration.ReserveState;

import com.skyrider26.reserve.domain.enumeration.PaymentMethod;

/**
 * A Reserve.
 */
@Entity
@Table(name = "reserve")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Reserve implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReserveState status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentMethod paymentType;

    @Column(name = "date_created")
    private Instant dateCreated;

    @OneToMany(mappedBy = "reserve")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReserveLine> reservelines = new HashSet<>();

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Admin admin;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReserveState getStatus() {
        return status;
    }

    public Reserve status(ReserveState status) {
        this.status = status;
        return this;
    }

    public void setStatus(ReserveState status) {
        this.status = status;
    }

    public PaymentMethod getPaymentType() {
        return paymentType;
    }

    public Reserve paymentType(PaymentMethod paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public void setPaymentType(PaymentMethod paymentType) {
        this.paymentType = paymentType;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Reserve dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Set<ReserveLine> getReservelines() {
        return reservelines;
    }

    public Reserve reservelines(Set<ReserveLine> reserveLines) {
        this.reservelines = reserveLines;
        return this;
    }

    public Reserve addReserveline(ReserveLine reserveLine) {
        this.reservelines.add(reserveLine);
        reserveLine.setReserve(this);
        return this;
    }

    public Reserve removeReserveline(ReserveLine reserveLine) {
        this.reservelines.remove(reserveLine);
        reserveLine.setReserve(null);
        return this;
    }

    public void setReservelines(Set<ReserveLine> reserveLines) {
        this.reservelines = reserveLines;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Reserve customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Admin getAdmin() {
        return admin;
    }

    public Reserve admin(Admin admin) {
        this.admin = admin;
        return this;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
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
        Reserve reserve = (Reserve) o;
        if (reserve.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reserve.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reserve{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", paymentType='" + getPaymentType() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            "}";
    }
}
