package com.skyrider26.reserve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.skyrider26.reserve.domain.Product;
import com.skyrider26.reserve.domain.ReserveLine;

import com.skyrider26.reserve.domain.enumeration.ReserveState;
import com.skyrider26.reserve.repository.ReserveLineRepository;
import com.skyrider26.reserve.web.rest.errors.BadRequestAlertException;
import com.skyrider26.reserve.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.skyrider26.reserve.repository.ProductRepository;

/**
 * REST controller for managing ReserveLine.
 */
@RestController
@RequestMapping("/api")
public class ReserveLineResource {

    private final Logger log = LoggerFactory.getLogger(ReserveLineResource.class);

    private static final String ENTITY_NAME = "reserveLine";

    private final ReserveLineRepository reserveLineRepository;

    private ProductRepository productRepository;

    public ReserveLineResource(ReserveLineRepository reserveLineRepository, ProductRepository productRepository) {
        this.reserveLineRepository = reserveLineRepository;
        this.productRepository = productRepository;
    }

    /**
     * POST  /reserve-lines : Create a new reserveLine.
     *
     * @param reserveLine the reserveLine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reserveLine, or with status 400 (Bad Request) if the reserveLine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reserve-lines")
    @Timed
    public ResponseEntity<ReserveLine> createReserveLine(@RequestBody ReserveLine reserveLine) throws URISyntaxException {
        log.debug("REST request to save ReserveLine : {}", reserveLine);
        if (reserveLine.getId() != null) {
            throw new BadRequestAlertException("A new reserveLine cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Product product = productRepository.findOneWithEagerRelationships(reserveLine.getProduct().getId());

        if (reserveLine.getQuantity() <= product.getStock()){

            if (reserveLine.getReserve().getStatus().equals(ReserveState.DELIVERED)){
                UUID uniqueKey = UUID.randomUUID();
                System.out.println ("random ship number"+ uniqueKey);

                reserveLine.setShippingNumber(uniqueKey.toString());

                //reduce stock
                reserveLine.getProduct().setStock(product.getStock() - reserveLine.getQuantity());
            }

            reserveLine.setTotal(reserveLine.getQuantity() * product.getUnitPrice());
            ReserveLine result = reserveLineRepository.save(reserveLine);

            return ResponseEntity.created(new URI("/api/reserve-lines/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
        }else{
            throw new BadRequestAlertException("Doesn't have enough Stock, please try again", ENTITY_NAME, "idexists");
        }
    }

    /**
     * PUT  /reserve-lines : Updates an existing reserveLine.
     *
     * @param reserveLine the reserveLine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reserveLine,
     * or with status 400 (Bad Request) if the reserveLine is not valid,
     * or with status 500 (Internal Server Error) if the reserveLine couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reserve-lines")
    @Timed
    public ResponseEntity<ReserveLine> updateReserveLine(@RequestBody ReserveLine reserveLine) throws URISyntaxException {
        log.debug("REST request to update ReserveLine : {}", reserveLine);
        if (reserveLine.getId() == null) {
            return createReserveLine(reserveLine);
        }
        ReserveLine result = reserveLineRepository.save(reserveLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reserveLine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reserve-lines : get all the reserveLines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reserveLines in body
     */
    @GetMapping("/reserve-lines")
    @Timed
    public List<ReserveLine> getAllReserveLines() {
        log.debug("REST request to get all ReserveLines");
        return reserveLineRepository.findAll();
    }

    /**
     * GET  /reserve-lines/:id : get the "id" reserveLine.
     *
     * @param id the id of the reserveLine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reserveLine, or with status 404 (Not Found)
     */
    @GetMapping("/reserve-lines/{id}")
    @Timed
    public ResponseEntity<ReserveLine> getReserveLine(@PathVariable Long id) {
        log.debug("REST request to get ReserveLine : {}", id);
        ReserveLine reserveLine = reserveLineRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reserveLine));
    }

    /**
     * DELETE  /reserve-lines/:id : delete the "id" reserveLine.
     *
     * @param id the id of the reserveLine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reserve-lines/{id}")
    @Timed
    public ResponseEntity<Void> deleteReserveLine(@PathVariable Long id) {
        log.debug("REST request to delete ReserveLine : {}", id);
        reserveLineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
