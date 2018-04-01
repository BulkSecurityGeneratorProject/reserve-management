package com.skyrider26.reserve.repository;

import com.skyrider26.reserve.domain.Reserve;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Reserve entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long> {

}
