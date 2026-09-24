package com.udea.proyecto.web.rest;

import static com.udea.proyecto.domain.VueloAsserts.*;
import static com.udea.proyecto.web.rest.TestUtil.createUpdateProxyForBean;
import static com.udea.proyecto.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.proyecto.IntegrationTest;
import com.udea.proyecto.domain.Vuelo;
import com.udea.proyecto.repository.VueloRepository;
import com.udea.proyecto.service.VueloService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VueloResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VueloResourceIT {

    private static final String DEFAULT_NUMERO_VUELO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_VUELO = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGEN = "AAAAAAAAAA";
    private static final String UPDATED_ORIGEN = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINO = "AAAAAAAAAA";
    private static final String UPDATED_DESTINO = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_SALIDA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_SALIDA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_PRECIO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/vuelos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VueloRepository vueloRepository;

    @Mock
    private VueloRepository vueloRepositoryMock;

    @Mock
    private VueloService vueloServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVueloMockMvc;

    private Vuelo vuelo;

    private Vuelo insertedVuelo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vuelo createEntity() {
        return new Vuelo()
            .numeroVuelo(DEFAULT_NUMERO_VUELO)
            .origen(DEFAULT_ORIGEN)
            .destino(DEFAULT_DESTINO)
            .fechaSalida(DEFAULT_FECHA_SALIDA)
            .precio(DEFAULT_PRECIO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vuelo createUpdatedEntity() {
        return new Vuelo()
            .numeroVuelo(UPDATED_NUMERO_VUELO)
            .origen(UPDATED_ORIGEN)
            .destino(UPDATED_DESTINO)
            .fechaSalida(UPDATED_FECHA_SALIDA)
            .precio(UPDATED_PRECIO);
    }

    @BeforeEach
    void initTest() {
        vuelo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVuelo != null) {
            vueloRepository.delete(insertedVuelo);
            insertedVuelo = null;
        }
    }

    @Test
    @Transactional
    void createVuelo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vuelo
        var returnedVuelo = om.readValue(
            restVueloMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vuelo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Vuelo.class
        );

        // Validate the Vuelo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVueloUpdatableFieldsEquals(returnedVuelo, getPersistedVuelo(returnedVuelo));

        insertedVuelo = returnedVuelo;
    }

    @Test
    @Transactional
    void createVueloWithExistingId() throws Exception {
        // Create the Vuelo with an existing ID
        vuelo.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVueloMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vuelo)))
            .andExpect(status().isBadRequest());

        // Validate the Vuelo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroVueloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vuelo.setNumeroVuelo(null);

        // Create the Vuelo, which fails.

        restVueloMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vuelo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrigenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vuelo.setOrigen(null);

        // Create the Vuelo, which fails.

        restVueloMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vuelo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDestinoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vuelo.setDestino(null);

        // Create the Vuelo, which fails.

        restVueloMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vuelo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaSalidaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vuelo.setFechaSalida(null);

        // Create the Vuelo, which fails.

        restVueloMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vuelo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vuelo.setPrecio(null);

        // Create the Vuelo, which fails.

        restVueloMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vuelo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVuelos() throws Exception {
        // Initialize the database
        insertedVuelo = vueloRepository.saveAndFlush(vuelo);

        // Get all the vueloList
        restVueloMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vuelo.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroVuelo").value(hasItem(DEFAULT_NUMERO_VUELO)))
            .andExpect(jsonPath("$.[*].origen").value(hasItem(DEFAULT_ORIGEN)))
            .andExpect(jsonPath("$.[*].destino").value(hasItem(DEFAULT_DESTINO)))
            .andExpect(jsonPath("$.[*].fechaSalida").value(hasItem(DEFAULT_FECHA_SALIDA.toString())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(sameNumber(DEFAULT_PRECIO))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVuelosWithEagerRelationshipsIsEnabled() throws Exception {
        when(vueloServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVueloMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vueloServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVuelosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vueloServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVueloMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vueloRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVuelo() throws Exception {
        // Initialize the database
        insertedVuelo = vueloRepository.saveAndFlush(vuelo);

        // Get the vuelo
        restVueloMockMvc
            .perform(get(ENTITY_API_URL_ID, vuelo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vuelo.getId().intValue()))
            .andExpect(jsonPath("$.numeroVuelo").value(DEFAULT_NUMERO_VUELO))
            .andExpect(jsonPath("$.origen").value(DEFAULT_ORIGEN))
            .andExpect(jsonPath("$.destino").value(DEFAULT_DESTINO))
            .andExpect(jsonPath("$.fechaSalida").value(DEFAULT_FECHA_SALIDA.toString()))
            .andExpect(jsonPath("$.precio").value(sameNumber(DEFAULT_PRECIO)));
    }

    @Test
    @Transactional
    void getNonExistingVuelo() throws Exception {
        // Get the vuelo
        restVueloMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVuelo() throws Exception {
        // Initialize the database
        insertedVuelo = vueloRepository.saveAndFlush(vuelo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vuelo
        Vuelo updatedVuelo = vueloRepository.findById(vuelo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVuelo are not directly saved in db
        em.detach(updatedVuelo);
        updatedVuelo
            .numeroVuelo(UPDATED_NUMERO_VUELO)
            .origen(UPDATED_ORIGEN)
            .destino(UPDATED_DESTINO)
            .fechaSalida(UPDATED_FECHA_SALIDA)
            .precio(UPDATED_PRECIO);

        restVueloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVuelo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVuelo))
            )
            .andExpect(status().isOk());

        // Validate the Vuelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVueloToMatchAllProperties(updatedVuelo);
    }

    @Test
    @Transactional
    void putNonExistingVuelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vuelo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVueloMockMvc
            .perform(put(ENTITY_API_URL_ID, vuelo.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vuelo)))
            .andExpect(status().isBadRequest());

        // Validate the Vuelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVuelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vuelo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVueloMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vuelo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vuelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVuelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vuelo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVueloMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vuelo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vuelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVueloWithPatch() throws Exception {
        // Initialize the database
        insertedVuelo = vueloRepository.saveAndFlush(vuelo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vuelo using partial update
        Vuelo partialUpdatedVuelo = new Vuelo();
        partialUpdatedVuelo.setId(vuelo.getId());

        partialUpdatedVuelo.fechaSalida(UPDATED_FECHA_SALIDA);

        restVueloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVuelo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVuelo))
            )
            .andExpect(status().isOk());

        // Validate the Vuelo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVueloUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVuelo, vuelo), getPersistedVuelo(vuelo));
    }

    @Test
    @Transactional
    void fullUpdateVueloWithPatch() throws Exception {
        // Initialize the database
        insertedVuelo = vueloRepository.saveAndFlush(vuelo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vuelo using partial update
        Vuelo partialUpdatedVuelo = new Vuelo();
        partialUpdatedVuelo.setId(vuelo.getId());

        partialUpdatedVuelo
            .numeroVuelo(UPDATED_NUMERO_VUELO)
            .origen(UPDATED_ORIGEN)
            .destino(UPDATED_DESTINO)
            .fechaSalida(UPDATED_FECHA_SALIDA)
            .precio(UPDATED_PRECIO);

        restVueloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVuelo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVuelo))
            )
            .andExpect(status().isOk());

        // Validate the Vuelo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVueloUpdatableFieldsEquals(partialUpdatedVuelo, getPersistedVuelo(partialUpdatedVuelo));
    }

    @Test
    @Transactional
    void patchNonExistingVuelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vuelo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVueloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vuelo.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vuelo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vuelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVuelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vuelo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVueloMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vuelo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vuelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVuelo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vuelo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVueloMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vuelo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vuelo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVuelo() throws Exception {
        // Initialize the database
        insertedVuelo = vueloRepository.saveAndFlush(vuelo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vuelo
        restVueloMockMvc
            .perform(delete(ENTITY_API_URL_ID, vuelo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vueloRepository.count();
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

    protected Vuelo getPersistedVuelo(Vuelo vuelo) {
        return vueloRepository.findById(vuelo.getId()).orElseThrow();
    }

    protected void assertPersistedVueloToMatchAllProperties(Vuelo expectedVuelo) {
        assertVueloAllPropertiesEquals(expectedVuelo, getPersistedVuelo(expectedVuelo));
    }

    protected void assertPersistedVueloToMatchUpdatableProperties(Vuelo expectedVuelo) {
        assertVueloAllUpdatablePropertiesEquals(expectedVuelo, getPersistedVuelo(expectedVuelo));
    }
}
