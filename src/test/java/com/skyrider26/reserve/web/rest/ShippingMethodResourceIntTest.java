package com.skyrider26.reserve.web.rest;

import com.skyrider26.reserve.ReserveManagementApp;

import com.skyrider26.reserve.domain.ShippingMethod;
import com.skyrider26.reserve.repository.ShippingMethodRepository;
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
 * Test class for the ShippingMethodResource REST controller.
 *
 * @see ShippingMethodResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReserveManagementApp.class)
public class ShippingMethodResourceIntTest {

    private static final String DEFAULT_SHIPPING_ID = "AAAAAAAAAA";
    private static final String UPDATED_SHIPPING_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ShippingMethodRepository shippingMethodRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restShippingMethodMockMvc;

    private ShippingMethod shippingMethod;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShippingMethodResource shippingMethodResource = new ShippingMethodResource(shippingMethodRepository);
        this.restShippingMethodMockMvc = MockMvcBuilders.standaloneSetup(shippingMethodResource)
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
    public static ShippingMethod createEntity(EntityManager em) {
        ShippingMethod shippingMethod = new ShippingMethod()
            .shippingId(DEFAULT_SHIPPING_ID)
            .name(DEFAULT_NAME);
        return shippingMethod;
    }

    @Before
    public void initTest() {
        shippingMethod = createEntity(em);
    }

    @Test
    @Transactional
    public void createShippingMethod() throws Exception {
        int databaseSizeBeforeCreate = shippingMethodRepository.findAll().size();

        // Create the ShippingMethod
        restShippingMethodMockMvc.perform(post("/api/shipping-methods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shippingMethod)))
            .andExpect(status().isCreated());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeCreate + 1);
        ShippingMethod testShippingMethod = shippingMethodList.get(shippingMethodList.size() - 1);
        assertThat(testShippingMethod.getShippingId()).isEqualTo(DEFAULT_SHIPPING_ID);
        assertThat(testShippingMethod.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createShippingMethodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shippingMethodRepository.findAll().size();

        // Create the ShippingMethod with an existing ID
        shippingMethod.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShippingMethodMockMvc.perform(post("/api/shipping-methods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shippingMethod)))
            .andExpect(status().isBadRequest());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllShippingMethods() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList
        restShippingMethodMockMvc.perform(get("/api/shipping-methods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shippingMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].shippingId").value(hasItem(DEFAULT_SHIPPING_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getShippingMethod() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get the shippingMethod
        restShippingMethodMockMvc.perform(get("/api/shipping-methods/{id}", shippingMethod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shippingMethod.getId().intValue()))
            .andExpect(jsonPath("$.shippingId").value(DEFAULT_SHIPPING_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingShippingMethod() throws Exception {
        // Get the shippingMethod
        restShippingMethodMockMvc.perform(get("/api/shipping-methods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShippingMethod() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);
        int databaseSizeBeforeUpdate = shippingMethodRepository.findAll().size();

        // Update the shippingMethod
        ShippingMethod updatedShippingMethod = shippingMethodRepository.findOne(shippingMethod.getId());
        // Disconnect from session so that the updates on updatedShippingMethod are not directly saved in db
        em.detach(updatedShippingMethod);
        updatedShippingMethod
            .shippingId(UPDATED_SHIPPING_ID)
            .name(UPDATED_NAME);

        restShippingMethodMockMvc.perform(put("/api/shipping-methods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShippingMethod)))
            .andExpect(status().isOk());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeUpdate);
        ShippingMethod testShippingMethod = shippingMethodList.get(shippingMethodList.size() - 1);
        assertThat(testShippingMethod.getShippingId()).isEqualTo(UPDATED_SHIPPING_ID);
        assertThat(testShippingMethod.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingShippingMethod() throws Exception {
        int databaseSizeBeforeUpdate = shippingMethodRepository.findAll().size();

        // Create the ShippingMethod

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restShippingMethodMockMvc.perform(put("/api/shipping-methods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shippingMethod)))
            .andExpect(status().isCreated());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteShippingMethod() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);
        int databaseSizeBeforeDelete = shippingMethodRepository.findAll().size();

        // Get the shippingMethod
        restShippingMethodMockMvc.perform(delete("/api/shipping-methods/{id}", shippingMethod.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShippingMethod.class);
        ShippingMethod shippingMethod1 = new ShippingMethod();
        shippingMethod1.setId(1L);
        ShippingMethod shippingMethod2 = new ShippingMethod();
        shippingMethod2.setId(shippingMethod1.getId());
        assertThat(shippingMethod1).isEqualTo(shippingMethod2);
        shippingMethod2.setId(2L);
        assertThat(shippingMethod1).isNotEqualTo(shippingMethod2);
        shippingMethod1.setId(null);
        assertThat(shippingMethod1).isNotEqualTo(shippingMethod2);
    }
}
