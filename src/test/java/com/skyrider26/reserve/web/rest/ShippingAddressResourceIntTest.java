package com.skyrider26.reserve.web.rest;

import com.skyrider26.reserve.ReserveManagementApp;

import com.skyrider26.reserve.domain.ShippingAddress;
import com.skyrider26.reserve.repository.ShippingAddressRepository;
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
 * Test class for the ShippingAddressResource REST controller.
 *
 * @see ShippingAddressResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReserveManagementApp.class)
public class ShippingAddressResourceIntTest {

    private static final String DEFAULT_ADDRESS_ID = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_ID = "BBBBBBBBBB";

    private static final String DEFAULT_GEOCODE = "AAAAAAAAAA";
    private static final String UPDATED_GEOCODE = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_1 = "AAAAAAAAAA";
    private static final String UPDATED_STREET_1 = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_2 = "AAAAAAAAAA";
    private static final String UPDATED_STREET_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_POBOX = "AAAAAAAAAA";
    private static final String UPDATED_POBOX = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    @Autowired
    private ShippingAddressRepository shippingAddressRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restShippingAddressMockMvc;

    private ShippingAddress shippingAddress;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShippingAddressResource shippingAddressResource = new ShippingAddressResource(shippingAddressRepository);
        this.restShippingAddressMockMvc = MockMvcBuilders.standaloneSetup(shippingAddressResource)
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
    public static ShippingAddress createEntity(EntityManager em) {
        ShippingAddress shippingAddress = new ShippingAddress()
            .addressId(DEFAULT_ADDRESS_ID)
            .geocode(DEFAULT_GEOCODE)
            .street1(DEFAULT_STREET_1)
            .street2(DEFAULT_STREET_2)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .country(DEFAULT_COUNTRY)
            .pobox(DEFAULT_POBOX)
            .postalCode(DEFAULT_POSTAL_CODE);
        return shippingAddress;
    }

    @Before
    public void initTest() {
        shippingAddress = createEntity(em);
    }

    @Test
    @Transactional
    public void createShippingAddress() throws Exception {
        int databaseSizeBeforeCreate = shippingAddressRepository.findAll().size();

        // Create the ShippingAddress
        restShippingAddressMockMvc.perform(post("/api/shipping-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shippingAddress)))
            .andExpect(status().isCreated());

        // Validate the ShippingAddress in the database
        List<ShippingAddress> shippingAddressList = shippingAddressRepository.findAll();
        assertThat(shippingAddressList).hasSize(databaseSizeBeforeCreate + 1);
        ShippingAddress testShippingAddress = shippingAddressList.get(shippingAddressList.size() - 1);
        assertThat(testShippingAddress.getAddressId()).isEqualTo(DEFAULT_ADDRESS_ID);
        assertThat(testShippingAddress.getGeocode()).isEqualTo(DEFAULT_GEOCODE);
        assertThat(testShippingAddress.getStreet1()).isEqualTo(DEFAULT_STREET_1);
        assertThat(testShippingAddress.getStreet2()).isEqualTo(DEFAULT_STREET_2);
        assertThat(testShippingAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testShippingAddress.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testShippingAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testShippingAddress.getPobox()).isEqualTo(DEFAULT_POBOX);
        assertThat(testShippingAddress.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void createShippingAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shippingAddressRepository.findAll().size();

        // Create the ShippingAddress with an existing ID
        shippingAddress.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShippingAddressMockMvc.perform(post("/api/shipping-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shippingAddress)))
            .andExpect(status().isBadRequest());

        // Validate the ShippingAddress in the database
        List<ShippingAddress> shippingAddressList = shippingAddressRepository.findAll();
        assertThat(shippingAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllShippingAddresses() throws Exception {
        // Initialize the database
        shippingAddressRepository.saveAndFlush(shippingAddress);

        // Get all the shippingAddressList
        restShippingAddressMockMvc.perform(get("/api/shipping-addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shippingAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].addressId").value(hasItem(DEFAULT_ADDRESS_ID.toString())))
            .andExpect(jsonPath("$.[*].geocode").value(hasItem(DEFAULT_GEOCODE.toString())))
            .andExpect(jsonPath("$.[*].street1").value(hasItem(DEFAULT_STREET_1.toString())))
            .andExpect(jsonPath("$.[*].street2").value(hasItem(DEFAULT_STREET_2.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].pobox").value(hasItem(DEFAULT_POBOX.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())));
    }

    @Test
    @Transactional
    public void getShippingAddress() throws Exception {
        // Initialize the database
        shippingAddressRepository.saveAndFlush(shippingAddress);

        // Get the shippingAddress
        restShippingAddressMockMvc.perform(get("/api/shipping-addresses/{id}", shippingAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shippingAddress.getId().intValue()))
            .andExpect(jsonPath("$.addressId").value(DEFAULT_ADDRESS_ID.toString()))
            .andExpect(jsonPath("$.geocode").value(DEFAULT_GEOCODE.toString()))
            .andExpect(jsonPath("$.street1").value(DEFAULT_STREET_1.toString()))
            .andExpect(jsonPath("$.street2").value(DEFAULT_STREET_2.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.pobox").value(DEFAULT_POBOX.toString()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingShippingAddress() throws Exception {
        // Get the shippingAddress
        restShippingAddressMockMvc.perform(get("/api/shipping-addresses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShippingAddress() throws Exception {
        // Initialize the database
        shippingAddressRepository.saveAndFlush(shippingAddress);
        int databaseSizeBeforeUpdate = shippingAddressRepository.findAll().size();

        // Update the shippingAddress
        ShippingAddress updatedShippingAddress = shippingAddressRepository.findOne(shippingAddress.getId());
        // Disconnect from session so that the updates on updatedShippingAddress are not directly saved in db
        em.detach(updatedShippingAddress);
        updatedShippingAddress
            .addressId(UPDATED_ADDRESS_ID)
            .geocode(UPDATED_GEOCODE)
            .street1(UPDATED_STREET_1)
            .street2(UPDATED_STREET_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .pobox(UPDATED_POBOX)
            .postalCode(UPDATED_POSTAL_CODE);

        restShippingAddressMockMvc.perform(put("/api/shipping-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShippingAddress)))
            .andExpect(status().isOk());

        // Validate the ShippingAddress in the database
        List<ShippingAddress> shippingAddressList = shippingAddressRepository.findAll();
        assertThat(shippingAddressList).hasSize(databaseSizeBeforeUpdate);
        ShippingAddress testShippingAddress = shippingAddressList.get(shippingAddressList.size() - 1);
        assertThat(testShippingAddress.getAddressId()).isEqualTo(UPDATED_ADDRESS_ID);
        assertThat(testShippingAddress.getGeocode()).isEqualTo(UPDATED_GEOCODE);
        assertThat(testShippingAddress.getStreet1()).isEqualTo(UPDATED_STREET_1);
        assertThat(testShippingAddress.getStreet2()).isEqualTo(UPDATED_STREET_2);
        assertThat(testShippingAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testShippingAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testShippingAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testShippingAddress.getPobox()).isEqualTo(UPDATED_POBOX);
        assertThat(testShippingAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingShippingAddress() throws Exception {
        int databaseSizeBeforeUpdate = shippingAddressRepository.findAll().size();

        // Create the ShippingAddress

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restShippingAddressMockMvc.perform(put("/api/shipping-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shippingAddress)))
            .andExpect(status().isCreated());

        // Validate the ShippingAddress in the database
        List<ShippingAddress> shippingAddressList = shippingAddressRepository.findAll();
        assertThat(shippingAddressList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteShippingAddress() throws Exception {
        // Initialize the database
        shippingAddressRepository.saveAndFlush(shippingAddress);
        int databaseSizeBeforeDelete = shippingAddressRepository.findAll().size();

        // Get the shippingAddress
        restShippingAddressMockMvc.perform(delete("/api/shipping-addresses/{id}", shippingAddress.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ShippingAddress> shippingAddressList = shippingAddressRepository.findAll();
        assertThat(shippingAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShippingAddress.class);
        ShippingAddress shippingAddress1 = new ShippingAddress();
        shippingAddress1.setId(1L);
        ShippingAddress shippingAddress2 = new ShippingAddress();
        shippingAddress2.setId(shippingAddress1.getId());
        assertThat(shippingAddress1).isEqualTo(shippingAddress2);
        shippingAddress2.setId(2L);
        assertThat(shippingAddress1).isNotEqualTo(shippingAddress2);
        shippingAddress1.setId(null);
        assertThat(shippingAddress1).isNotEqualTo(shippingAddress2);
    }
}
