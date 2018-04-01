package com.skyrider26.reserve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.skyrider26.reserve.domain.Admin;

import com.skyrider26.reserve.repository.AdminRepository;
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
 * REST controller for managing Admin.
 */
@RestController
@RequestMapping("/api")
public class AdminResource {

    private final Logger log = LoggerFactory.getLogger(AdminResource.class);

    private static final String ENTITY_NAME = "admin";

    private final AdminRepository adminRepository;

    public AdminResource(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    /**
     * POST  /admins : Create a new admin.
     *
     * @param admin the admin to create
     * @return the ResponseEntity with status 201 (Created) and with body the new admin, or with status 400 (Bad Request) if the admin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/admins")
    @Timed
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) throws URISyntaxException {
        log.debug("REST request to save Admin : {}", admin);
        if (admin.getId() != null) {
            throw new BadRequestAlertException("A new admin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Admin result = adminRepository.save(admin);
        return ResponseEntity.created(new URI("/api/admins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /admins : Updates an existing admin.
     *
     * @param admin the admin to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated admin,
     * or with status 400 (Bad Request) if the admin is not valid,
     * or with status 500 (Internal Server Error) if the admin couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/admins")
    @Timed
    public ResponseEntity<Admin> updateAdmin(@RequestBody Admin admin) throws URISyntaxException {
        log.debug("REST request to update Admin : {}", admin);
        if (admin.getId() == null) {
            return createAdmin(admin);
        }
        Admin result = adminRepository.save(admin);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, admin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /admins : get all the admins.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of admins in body
     */
    @GetMapping("/admins")
    @Timed
    public List<Admin> getAllAdmins() {
        log.debug("REST request to get all Admins");
        return adminRepository.findAll();
        }

    /**
     * GET  /admins/:id : get the "id" admin.
     *
     * @param id the id of the admin to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the admin, or with status 404 (Not Found)
     */
    @GetMapping("/admins/{id}")
    @Timed
    public ResponseEntity<Admin> getAdmin(@PathVariable Long id) {
        log.debug("REST request to get Admin : {}", id);
        Admin admin = adminRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(admin));
    }

    /**
     * DELETE  /admins/:id : delete the "id" admin.
     *
     * @param id the id of the admin to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/admins/{id}")
    @Timed
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        log.debug("REST request to delete Admin : {}", id);
        adminRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
