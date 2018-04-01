package com.skyrider26.reserve.repository;

import com.skyrider26.reserve.domain.ReserveLine;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ReserveLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReserveLineRepository extends JpaRepository<ReserveLine, Long> {

}
