package com.skyrider26.reserve.repository;

import com.skyrider26.reserve.domain.ShippingMethod;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ShippingMethod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Long> {

}
