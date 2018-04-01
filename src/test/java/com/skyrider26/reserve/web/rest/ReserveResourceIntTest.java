package com.skyrider26.reserve.web.rest;

import com.skyrider26.reserve.ReserveManagementApp;

import com.skyrider26.reserve.domain.Reserve;
import com.skyrider26.reserve.repository.ReserveRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.skyrider26.reserve.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.skyrider26.reserve.domain.enumeration.ReserveState;
import com.skyrider26.reserve.domain.enumeration.PaymentMethod;
/**
 * Test class for the ReserveResource REST controller.
 *
 * @see ReserveResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReserveManagementApp.class)
public class ReserveResourceIntTest {

    private static final ReserveState DEFAULT_STATUS = ReserveState.NEW;
    private static final ReserveState UPDATED_STATUS = ReserveState.PACKED;

    private static final PaymentMethod DEFAULT_PAYMENT_TYPE = PaymentMethod.TRANSFER;
    private static final PaymentMethod UPDATED_PAYMENT_TYPE = PaymentMethod.CREDIT_CARD;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ReserveRepository reserveRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReserveMockMvc;

    private Reserve reserve;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReserveResource reserveResource = new ReserveResource(reserveRepository);
        this.restReserveMockMvc = MockMvcBuilders.standaloneSetup(reserveResource)
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
    public static Reserve createEntity(EntityManager em) {
        Reserve reserve = new Reserve()
            .status(DEFAULT_STATUS)
            .paymentType(DEFAULT_PAYMENT_TYPE)
            .dateCreated(DEFAULT_DATE_CREATED);
        return reserve;
    }

    @Before
    public void initTest() {
        reserve = createEntity(em);
    }

    @Test
    @Transactional
    public void createReserve() throws Exception {
        int databaseSizeBeforeCreate = reserveRepository.findAll().size();

        // Create the Reserve
        restReserveMockMvc.perform(post("/api/reserves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reserve)))
            .andExpect(status().isCreated());

        // Validate the Reserve in the database
        List<Reserve> reserveList = reserveRepository.findAll();
        assertThat(reserveList).hasSize(databaseSizeBeforeCreate + 1);
        Reserve testReserve = reserveList.get(reserveList.size() - 1);
        assertThat(testReserve.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReserve.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testReserve.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
    }

    @Test
    @Transactional
    public void createReserveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reserveRepository.findAll().size();

        // Create the Reserve with an existing ID
        reserve.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReserveMockMvc.perform(post("/api/reserves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reserve)))
            .andExpect(status().isBadRequest());

        // Validate the Reserve in the database
        List<Reserve> reserveList = reserveRepository.findAll();
        assertThat(reserveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = reserveRepository.findAll().size();
        // set the field null
        reserve.setStatus(null);

        // Create the Reserve, which fails.

        restReserveMockMvc.perform(post("/api/reserves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reserve)))
            .andExpect(status().isBadRequest());

        List<Reserve> reserveList = reserveRepository.findAll();
        assertThat(reserveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = reserveRepository.findAll().size();
        // set the field null
        reserve.setPaymentType(null);

        // Create the Reserve, which fails.

        restReserveMockMvc.perform(post("/api/reserves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reserve)))
            .andExpect(status().isBadRequest());

        List<Reserve> reserveList = reserveRepository.findAll();
        assertThat(reserveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReserves() throws Exception {
        // Initialize the database
        reserveRepository.saveAndFlush(reserve);

        // Get all the reserveList
        restReserveMockMvc.perform(get("/api/reserves?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reserve.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())));
    }

    @Test
    @Transactional
    public void getReserve() throws Exception {
        // Initialize the database
        reserveRepository.saveAndFlush(reserve);

        // Get the reserve
        restReserveMockMvc.perform(get("/api/reserves/{id}", reserve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reserve.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReserve() throws Exception {
        // Get the reserve
        restReserveMockMvc.perform(get("/api/reserves/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReserve() throws Exception {
        // Initialize the database
        reserveRepository.saveAndFlush(reserve);
        int databaseSizeBeforeUpdate = reserveRepository.findAll().size();

        // Update the reserve
        Reserve updatedReserve = reserveRepository.findOne(reserve.getId());
        // Disconnect from session so that the updates on updatedReserve are not directly saved in db
        em.detach(updatedReserve);
        updatedReserve
            .status(UPDATED_STATUS)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .dateCreated(UPDATED_DATE_CREATED);

        restReserveMockMvc.perform(put("/api/reserves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReserve)))
            .andExpect(status().isOk());

        // Validate the Reserve in the database
        List<Reserve> reserveList = reserveRepository.findAll();
        assertThat(reserveList).hasSize(databaseSizeBeforeUpdate);
        Reserve testReserve = reserveList.get(reserveList.size() - 1);
        assertThat(testReserve.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReserve.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testReserve.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void updateNonExistingReserve() throws Exception {
        int databaseSizeBeforeUpdate = reserveRepository.findAll().size();

        // Create the Reserve

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReserveMockMvc.perform(put("/api/reserves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reserve)))
            .andExpect(status().isCreated());

        // Validate the Reserve in the database
        List<Reserve> reserveList = reserveRepository.findAll();
        assertThat(reserveList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReserve() throws Exception {
        // Initialize the database
        reserveRepository.saveAndFlush(reserve);
        int databaseSizeBeforeDelete = reserveRepository.findAll().size();

        // Get the reserve
        restReserveMockMvc.perform(delete("/api/reserves/{id}", reserve.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Reserve> reserveList = reserveRepository.findAll();
        assertThat(reserveList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reserve.class);
        Reserve reserve1 = new Reserve();
        reserve1.setId(1L);
        Reserve reserve2 = new Reserve();
        reserve2.setId(reserve1.getId());
        assertThat(reserve1).isEqualTo(reserve2);
        reserve2.setId(2L);
        assertThat(reserve1).isNotEqualTo(reserve2);
        reserve1.setId(null);
        assertThat(reserve1).isNotEqualTo(reserve2);
    }
}
