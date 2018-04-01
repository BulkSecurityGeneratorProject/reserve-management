package com.skyrider26.reserve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.skyrider26.reserve.domain.ShippingMethod;

import com.skyrider26.reserve.repository.ShippingMethodRepository;
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

/**
 * REST controller for managing ShippingMethod.
 */
@RestController
@RequestMapping("/api")
public class ShippingMethodResource {

    private final Logger log = LoggerFactory.getLogger(ShippingMethodResource.class);

    private static final String ENTITY_NAME = "shippingMethod";

    private final ShippingMethodRepository shippingMethodRepository;

    public ShippingMethodResource(ShippingMethodRepository shippingMethodRepository) {
        this.shippingMethodRepository = shippingMethodRepository;
    }

    /**
     * POST  /shipping-methods : Create a new shippingMethod.
     *
     * @param shippingMethod the shippingMethod to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shippingMethod, or with status 400 (Bad Request) if the shippingMethod has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/shipping-methods")
    @Timed
    public ResponseEntity<ShippingMethod> createShippingMethod(@RequestBody ShippingMethod shippingMethod) throws URISyntaxException {
        log.debug("REST request to save ShippingMethod : {}", shippingMethod);
        if (shippingMethod.getId() != null) {
            throw new BadRequestAlertException("A new shippingMethod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShippingMethod result = shippingMethodRepository.save(shippingMethod);
        return ResponseEntity.created(new URI("/api/shipping-methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shipping-methods : Updates an existing shippingMethod.
     *
     * @param shippingMethod the shippingMethod to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shippingMethod,
     * or with status 400 (Bad Request) if the shippingMethod is not valid,
     * or with status 500 (Internal Server Error) if the shippingMethod couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/shipping-methods")
    @Timed
    public ResponseEntity<ShippingMethod> updateShippingMethod(@RequestBody ShippingMethod shippingMethod) throws URISyntaxException {
        log.debug("REST request to update ShippingMethod : {}", shippingMethod);
        if (shippingMethod.getId() == null) {
            return createShippingMethod(shippingMethod);
        }
        ShippingMethod result = shippingMethodRepository.save(shippingMethod);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, shippingMethod.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shipping-methods : get all the shippingMethods.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of shippingMethods in body
     */
    @GetMapping("/shipping-methods")
    @Timed
    public List<ShippingMethod> getAllShippingMethods() {
        log.debug("REST request to get all ShippingMethods");
        return shippingMethodRepository.findAll();
        }

    /**
     * GET  /shipping-methods/:id : get the "id" shippingMethod.
     *
     * @param id the id of the shippingMethod to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shippingMethod, or with status 404 (Not Found)
     */
    @GetMapping("/shipping-methods/{id}")
    @Timed
    public ResponseEntity<ShippingMethod> getShippingMethod(@PathVariable Long id) {
        log.debug("REST request to get ShippingMethod : {}", id);
        ShippingMethod shippingMethod = shippingMethodRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(shippingMethod));
    }

    /**
     * DELETE  /shipping-methods/:id : delete the "id" shippingMethod.
     *
     * @param id the id of the shippingMethod to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/shipping-methods/{id}")
    @Timed
    public ResponseEntity<Void> deleteShippingMethod(@PathVariable Long id) {
        log.debug("REST request to delete ShippingMethod : {}", id);
        shippingMethodRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
