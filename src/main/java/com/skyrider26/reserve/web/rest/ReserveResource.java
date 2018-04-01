package com.skyrider26.reserve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.skyrider26.reserve.domain.Reserve;

import com.skyrider26.reserve.repository.ReserveRepository;
import com.skyrider26.reserve.web.rest.errors.BadRequestAlertException;
import com.skyrider26.reserve.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Reserve.
 */
@RestController
@RequestMapping("/api")
public class ReserveResource {

    private final Logger log = LoggerFactory.getLogger(ReserveResource.class);

    private static final String ENTITY_NAME = "reserve";

    private final ReserveRepository reserveRepository;

    public ReserveResource(ReserveRepository reserveRepository) {
        this.reserveRepository = reserveRepository;
    }

    /**
     * POST  /reserves : Create a new reserve.
     *
     * @param reserve the reserve to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reserve, or with status 400 (Bad Request) if the reserve has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reserves")
    @Timed
    public ResponseEntity<Reserve> createReserve(@Valid @RequestBody Reserve reserve) throws URISyntaxException {
        log.debug("REST request to save Reserve : {}", reserve);
        if (reserve.getId() != null) {
            throw new BadRequestAlertException("A new reserve cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Reserve result = reserveRepository.save(reserve);
        return ResponseEntity.created(new URI("/api/reserves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reserves : Updates an existing reserve.
     *
     * @param reserve the reserve to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reserve,
     * or with status 400 (Bad Request) if the reserve is not valid,
     * or with status 500 (Internal Server Error) if the reserve couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reserves")
    @Timed
    public ResponseEntity<Reserve> updateReserve(@Valid @RequestBody Reserve reserve) throws URISyntaxException {
        log.debug("REST request to update Reserve : {}", reserve);
        if (reserve.getId() == null) {
            return createReserve(reserve);
        }
        Reserve result = reserveRepository.save(reserve);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reserve.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reserves : get all the reserves.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reserves in body
     */
    @GetMapping("/reserves")
    @Timed
    public List<Reserve> getAllReserves() {
        log.debug("REST request to get all Reserves");
        return reserveRepository.findAll();
        }

    /**
     * GET  /reserves/:id : get the "id" reserve.
     *
     * @param id the id of the reserve to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reserve, or with status 404 (Not Found)
     */
    @GetMapping("/reserves/{id}")
    @Timed
    public ResponseEntity<Reserve> getReserve(@PathVariable Long id) {
        log.debug("REST request to get Reserve : {}", id);
        Reserve reserve = reserveRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reserve));
    }

    /**
     * DELETE  /reserves/:id : delete the "id" reserve.
     *
     * @param id the id of the reserve to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reserves/{id}")
    @Timed
    public ResponseEntity<Void> deleteReserve(@PathVariable Long id) {
        log.debug("REST request to delete Reserve : {}", id);
        reserveRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
