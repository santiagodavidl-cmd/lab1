package com.udea.proyecto.web.rest;

import static com.udea.proyecto.domain.AvionAsserts.*;
import static com.udea.proyecto.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.proyecto.IntegrationTest;
import com.udea.proyecto.domain.Avion;
import com.udea.proyecto.repository.AvionRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AvionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AvionResourceIT {

    private static final String DEFAULT_MODELO = "AAAAAAAAAA";
    private static final String UPDATED_MODELO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACIDAD = 10;
    private static final Integer UPDATED_CAPACIDAD = 11;

    private static final String DEFAULT_MATRICULA = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/avions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AvionRepository avionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAvionMockMvc;

    private Avion avion;

    private Avion insertedAvion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Avion createEntity() {
        return new Avion().modelo(DEFAULT_MODELO).capacidad(DEFAULT_CAPACIDAD).matricula(DEFAULT_MATRICULA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Avion createUpdatedEntity() {
        return new Avion().modelo(UPDATED_MODELO).capacidad(UPDATED_CAPACIDAD).matricula(UPDATED_MATRICULA);
    }

    @BeforeEach
    void initTest() {
        avion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAvion != null) {
            avionRepository.delete(insertedAvion);
            insertedAvion = null;
        }
    }

    @Test
    @Transactional
    void createAvion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Avion
        var returnedAvion = om.readValue(
            restAvionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avion)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Avion.class
        );

        // Validate the Avion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAvionUpdatableFieldsEquals(returnedAvion, getPersistedAvion(returnedAvion));

        insertedAvion = returnedAvion;
    }

    @Test
    @Transactional
    void createAvionWithExistingId() throws Exception {
        // Create the Avion with an existing ID
        avion.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avion)))
            .andExpect(status().isBadRequest());

        // Validate the Avion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkModeloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        avion.setModelo(null);

        // Create the Avion, which fails.

        restAvionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avion)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCapacidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        avion.setCapacidad(null);

        // Create the Avion, which fails.

        restAvionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avion)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMatriculaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        avion.setMatricula(null);

        // Create the Avion, which fails.

        restAvionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avion)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAvions() throws Exception {
        // Initialize the database
        insertedAvion = avionRepository.saveAndFlush(avion);

        // Get all the avionList
        restAvionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avion.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO)))
            .andExpect(jsonPath("$.[*].capacidad").value(hasItem(DEFAULT_CAPACIDAD)))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)));
    }

    @Test
    @Transactional
    void getAvion() throws Exception {
        // Initialize the database
        insertedAvion = avionRepository.saveAndFlush(avion);

        // Get the avion
        restAvionMockMvc
            .perform(get(ENTITY_API_URL_ID, avion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(avion.getId().intValue()))
            .andExpect(jsonPath("$.modelo").value(DEFAULT_MODELO))
            .andExpect(jsonPath("$.capacidad").value(DEFAULT_CAPACIDAD))
            .andExpect(jsonPath("$.matricula").value(DEFAULT_MATRICULA));
    }

    @Test
    @Transactional
    void getNonExistingAvion() throws Exception {
        // Get the avion
        restAvionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAvion() throws Exception {
        // Initialize the database
        insertedAvion = avionRepository.saveAndFlush(avion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the avion
        Avion updatedAvion = avionRepository.findById(avion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAvion are not directly saved in db
        em.detach(updatedAvion);
        updatedAvion.modelo(UPDATED_MODELO).capacidad(UPDATED_CAPACIDAD).matricula(UPDATED_MATRICULA);

        restAvionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAvion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAvion))
            )
            .andExpect(status().isOk());

        // Validate the Avion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAvionToMatchAllProperties(updatedAvion);
    }

    @Test
    @Transactional
    void putNonExistingAvion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvionMockMvc
            .perform(put(ENTITY_API_URL_ID, avion.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avion)))
            .andExpect(status().isBadRequest());

        // Validate the Avion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAvion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(avion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAvion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(avion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Avion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAvionWithPatch() throws Exception {
        // Initialize the database
        insertedAvion = avionRepository.saveAndFlush(avion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the avion using partial update
        Avion partialUpdatedAvion = new Avion();
        partialUpdatedAvion.setId(avion.getId());

        partialUpdatedAvion.capacidad(UPDATED_CAPACIDAD).matricula(UPDATED_MATRICULA);

        restAvionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAvion))
            )
            .andExpect(status().isOk());

        // Validate the Avion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAvionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAvion, avion), getPersistedAvion(avion));
    }

    @Test
    @Transactional
    void fullUpdateAvionWithPatch() throws Exception {
        // Initialize the database
        insertedAvion = avionRepository.saveAndFlush(avion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the avion using partial update
        Avion partialUpdatedAvion = new Avion();
        partialUpdatedAvion.setId(avion.getId());

        partialUpdatedAvion.modelo(UPDATED_MODELO).capacidad(UPDATED_CAPACIDAD).matricula(UPDATED_MATRICULA);

        restAvionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAvion))
            )
            .andExpect(status().isOk());

        // Validate the Avion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAvionUpdatableFieldsEquals(partialUpdatedAvion, getPersistedAvion(partialUpdatedAvion));
    }

    @Test
    @Transactional
    void patchNonExistingAvion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, avion.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(avion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAvion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(avion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAvion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        avion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(avion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Avion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAvion() throws Exception {
        // Initialize the database
        insertedAvion = avionRepository.saveAndFlush(avion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the avion
        restAvionMockMvc
            .perform(delete(ENTITY_API_URL_ID, avion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return avionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Avion getPersistedAvion(Avion avion) {
        return avionRepository.findById(avion.getId()).orElseThrow();
    }

    protected void assertPersistedAvionToMatchAllProperties(Avion expectedAvion) {
        assertAvionAllPropertiesEquals(expectedAvion, getPersistedAvion(expectedAvion));
    }

    protected void assertPersistedAvionToMatchUpdatableProperties(Avion expectedAvion) {
        assertAvionAllUpdatablePropertiesEquals(expectedAvion, getPersistedAvion(expectedAvion));
    }
}
