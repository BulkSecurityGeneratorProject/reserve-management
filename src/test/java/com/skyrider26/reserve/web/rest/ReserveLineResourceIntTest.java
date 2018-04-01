package com.skyrider26.reserve.web.rest;

import com.skyrider26.reserve.ReserveManagementApp;

import com.skyrider26.reserve.domain.ReserveLine;
import com.skyrider26.reserve.repository.ProductRepository;
import com.skyrider26.reserve.repository.ReserveLineRepository;
import com.skyrider26.reserve.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.skyrider26.reserve.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReserveLineResource REST controller.
 *
 * @see ReserveLineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReserveManagementApp.class)
public class ReserveLineResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String DEFAULT_SHIPPING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SHIPPING_NUMBER = "BBBBBBBBBB";

    private static final Long DEFAULT_TOTAL = 1L;
    private static final Long UPDATED_TOTAL = 2L;

    @Autowired
    private ReserveLineRepository reserveLineRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReserveLineMockMvc;

    private ReserveLine reserveLine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReserveLineResource reserveLineResource = new ReserveLineResource(reserveLineRepository, productRepository);
        this.restReserveLineMockMvc = MockMvcBuilders.standaloneSetup(reserveLineResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReserveLine createEntity(EntityManager em) {
        ReserveLine reserveLine = new ReserveLine()
            .quantity(DEFAULT_QUANTITY)
            .shippingNumber(DEFAULT_SHIPPING_NUMBER)
            .total(DEFAULT_TOTAL);
        return reserveLine;
    }

    @Before
    public void initTest() {
        reserveLine = createEntity(em);
    }

    @Test
    @Transactional
    public void createReserveLine() throws Exception {
        int databaseSizeBeforeCreate = reserveLineRepository.findAll().size();

        // Create the ReserveLine
        restReserveLineMockMvc.perform(post("/api/reserve-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reserveLine)))
            .andExpect(status().isCreated());

        // Validate the ReserveLine in the database
        List<ReserveLine> reserveLineList = reserveLineRepository.findAll();
        assertThat(reserveLineList).hasSize(databaseSizeBeforeCreate + 1);
        ReserveLine testReserveLine = reserveLineList.get(reserveLineList.size() - 1);
        assertThat(testReserveLine.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testReserveLine.getShippingNumber()).isEqualTo(DEFAULT_SHIPPING_NUMBER);
        assertThat(testReserveLine.getTotal()).isEqualTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    public void createReserveLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reserveLineRepository.findAll().size();

        // Create the ReserveLine with an existing ID
        reserveLine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReserveLineMockMvc.perform(post("/api/reserve-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reserveLine)))
            .andExpect(status().isBadRequest());

        // Validate the ReserveLine in the database
        List<ReserveLine> reserveLineList = reserveLineRepository.findAll();
        assertThat(reserveLineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReserveLines() throws Exception {
        // Initialize the database
        reserveLineRepository.saveAndFlush(reserveLine);

        // Get all the reserveLineList
        restReserveLineMockMvc.perform(get("/api/reserve-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reserveLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].shippingNumber").value(hasItem(DEFAULT_SHIPPING_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())));
    }

    @Test
    @Transactional
    public void getReserveLine() throws Exception {
        // Initialize the database
        reserveLineRepository.saveAndFlush(reserveLine);

        // Get the reserveLine
        restReserveLineMockMvc.perform(get("/api/reserve-lines/{id}", reserveLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reserveLine.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.shippingNumber").value(DEFAULT_SHIPPING_NUMBER.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingReserveLine() throws Exception {
        // Get the reserveLine
        restReserveLineMockMvc.perform(get("/api/reserve-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReserveLine() throws Exception {
        // Initialize the database
        reserveLineRepository.saveAndFlush(reserveLine);
        int databaseSizeBeforeUpdate = reserveLineRepository.findAll().size();

        // Update the reserveLine
        ReserveLine updatedReserveLine = reserveLineRepository.findOne(reserveLine.getId());
        // Disconnect from session so that the updates on updatedReserveLine are not directly saved in db
        em.detach(updatedReserveLine);
        updatedReserveLine
            .quantity(UPDATED_QUANTITY)
            .shippingNumber(UPDATED_SHIPPING_NUMBER)
            .total(UPDATED_TOTAL);

        restReserveLineMockMvc.perform(put("/api/reserve-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReserveLine)))
            .andExpect(status().isOk());

        // Validate the ReserveLine in the database
        List<ReserveLine> reserveLineList = reserveLineRepository.findAll();
        assertThat(reserveLineList).hasSize(databaseSizeBeforeUpdate);
        ReserveLine testReserveLine = reserveLineList.get(reserveLineList.size() - 1);
        assertThat(testReserveLine.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testReserveLine.getShippingNumber()).isEqualTo(UPDATED_SHIPPING_NUMBER);
        assertThat(testReserveLine.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void updateNonExistingReserveLine() throws Exception {
        int databaseSizeBeforeUpdate = reserveLineRepository.findAll().size();

        // Create the ReserveLine

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReserveLineMockMvc.perform(put("/api/reserve-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reserveLine)))
            .andExpect(status().isCreated());

        // Validate the ReserveLine in the database
        List<ReserveLine> reserveLineList = reserveLineRepository.findAll();
        assertThat(reserveLineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReserveLine() throws Exception {
        // Initialize the database
        reserveLineRepository.saveAndFlush(reserveLine);
        int databaseSizeBeforeDelete = reserveLineRepository.findAll().size();

        // Get the reserveLine
        restReserveLineMockMvc.perform(delete("/api/reserve-lines/{id}", reserveLine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReserveLine> reserveLineList = reserveLineRepository.findAll();
        assertThat(reserveLineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReserveLine.class);
        ReserveLine reserveLine1 = new ReserveLine();
        reserveLine1.setId(1L);
        ReserveLine reserveLine2 = new ReserveLine();
        reserveLine2.setId(reserveLine1.getId());
        assertThat(reserveLine1).isEqualTo(reserveLine2);
        reserveLine2.setId(2L);
        assertThat(reserveLine1).isNotEqualTo(reserveLine2);
        reserveLine1.setId(null);
        assertThat(reserveLine1).isNotEqualTo(reserveLine2);
    }
}
