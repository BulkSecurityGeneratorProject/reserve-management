package com.skyrider26.reserve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.skyrider26.reserve.domain.ShoppingCart;

import com.skyrider26.reserve.repository.ShoppingCartRepository;
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
 * REST controller for managing ShoppingCart.
 */
@RestController
@RequestMapping("/api")
public class ShoppingCartResource {

    private final Logger log = LoggerFactory.getLogger(ShoppingCartResource.class);

    private static final String ENTITY_NAME = "shoppingCart";

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartResource(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    /**
     * POST  /shopping-carts : Create a new shoppingCart.
     *
     * @param shoppingCart the shoppingCart to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shoppingCart, or with status 400 (Bad Request) if the shoppingCart has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/shopping-carts")
    @Timed
    public ResponseEntity<ShoppingCart> createShoppingCart(@RequestBody ShoppingCart shoppingCart) throws URISyntaxException {
        log.debug("REST request to save ShoppingCart : {}", shoppingCart);
        if (shoppingCart.getId() != null) {
            throw new BadRequestAlertException("A new shoppingCart cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShoppingCart result = shoppingCartRepository.save(shoppingCart);
        return ResponseEntity.created(new URI("/api/shopping-carts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shopping-carts : Updates an existing shoppingCart.
     *
     * @param shoppingCart the shoppingCart to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shoppingCart,
     * or with status 400 (Bad Request) if the shoppingCart is not valid,
     * or with status 500 (Internal Server Error) if the shoppingCart couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/shopping-carts")
    @Timed
    public ResponseEntity<ShoppingCart> updateShoppingCart(@RequestBody ShoppingCart shoppingCart) throws URISyntaxException {
        log.debug("REST request to update ShoppingCart : {}", shoppingCart);
        if (shoppingCart.getId() == null) {
            return createShoppingCart(shoppingCart);
        }
        ShoppingCart result = shoppingCartRepository.save(shoppingCart);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, shoppingCart.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shopping-carts : get all the shoppingCarts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of shoppingCarts in body
     */
    @GetMapping("/shopping-carts")
    @Timed
    public List<ShoppingCart> getAllShoppingCarts() {
        log.debug("REST request to get all ShoppingCarts");
        return shoppingCartRepository.findAll();
        }

    /**
     * GET  /shopping-carts/:id : get the "id" shoppingCart.
     *
     * @param id the id of the shoppingCart to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shoppingCart, or with status 404 (Not Found)
     */
    @GetMapping("/shopping-carts/{id}")
    @Timed
    public ResponseEntity<ShoppingCart> getShoppingCart(@PathVariable Long id) {
        log.debug("REST request to get ShoppingCart : {}", id);
        ShoppingCart shoppingCart = shoppingCartRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(shoppingCart));
    }

    /**
     * DELETE  /shopping-carts/:id : delete the "id" shoppingCart.
     *
     * @param id the id of the shoppingCart to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/shopping-carts/{id}")
    @Timed
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable Long id) {
        log.debug("REST request to delete ShoppingCart : {}", id);
        shoppingCartRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
