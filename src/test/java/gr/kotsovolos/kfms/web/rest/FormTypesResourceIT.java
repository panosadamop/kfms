package gr.kotsovolos.kfms.web.rest;

import static gr.kotsovolos.kfms.domain.FormTypesAsserts.*;
import static gr.kotsovolos.kfms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.kotsovolos.kfms.IntegrationTest;
import gr.kotsovolos.kfms.domain.FormTypes;
import gr.kotsovolos.kfms.repository.FormTypesRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FormTypesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormTypesResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/form-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FormTypesRepository formTypesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormTypesMockMvc;

    private FormTypes formTypes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormTypes createEntity(EntityManager em) {
        FormTypes formTypes = new FormTypes().type(DEFAULT_TYPE).description(DEFAULT_DESCRIPTION);
        return formTypes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormTypes createUpdatedEntity(EntityManager em) {
        FormTypes formTypes = new FormTypes().type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);
        return formTypes;
    }

    @BeforeEach
    public void initTest() {
        formTypes = createEntity(em);
    }

    @Test
    @Transactional
    void createFormTypes() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FormTypes
        var returnedFormTypes = om.readValue(
            restFormTypesMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formTypes)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FormTypes.class
        );

        // Validate the FormTypes in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFormTypesUpdatableFieldsEquals(returnedFormTypes, getPersistedFormTypes(returnedFormTypes));
    }

    @Test
    @Transactional
    void createFormTypesWithExistingId() throws Exception {
        // Create the FormTypes with an existing ID
        formTypes.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormTypesMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formTypes)))
            .andExpect(status().isBadRequest());

        // Validate the FormTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        formTypes.setType(null);

        // Create the FormTypes, which fails.

        restFormTypesMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formTypes)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFormTypes() throws Exception {
        // Initialize the database
        formTypesRepository.saveAndFlush(formTypes);

        // Get all the formTypesList
        restFormTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getFormTypes() throws Exception {
        // Initialize the database
        formTypesRepository.saveAndFlush(formTypes);

        // Get the formTypes
        restFormTypesMockMvc
            .perform(get(ENTITY_API_URL_ID, formTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formTypes.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingFormTypes() throws Exception {
        // Get the formTypes
        restFormTypesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormTypes() throws Exception {
        // Initialize the database
        formTypesRepository.saveAndFlush(formTypes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the formTypes
        FormTypes updatedFormTypes = formTypesRepository.findById(formTypes.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFormTypes are not directly saved in db
        em.detach(updatedFormTypes);
        updatedFormTypes.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);

        restFormTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFormTypes.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFormTypes))
            )
            .andExpect(status().isOk());

        // Validate the FormTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFormTypesToMatchAllProperties(updatedFormTypes);
    }

    @Test
    @Transactional
    void putNonExistingFormTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formTypes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formTypes.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(formTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formTypes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(formTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formTypes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormTypesMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formTypes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormTypesWithPatch() throws Exception {
        // Initialize the database
        formTypesRepository.saveAndFlush(formTypes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the formTypes using partial update
        FormTypes partialUpdatedFormTypes = new FormTypes();
        partialUpdatedFormTypes.setId(formTypes.getId());

        partialUpdatedFormTypes.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);

        restFormTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormTypes.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFormTypes))
            )
            .andExpect(status().isOk());

        // Validate the FormTypes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFormTypesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFormTypes, formTypes),
            getPersistedFormTypes(formTypes)
        );
    }

    @Test
    @Transactional
    void fullUpdateFormTypesWithPatch() throws Exception {
        // Initialize the database
        formTypesRepository.saveAndFlush(formTypes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the formTypes using partial update
        FormTypes partialUpdatedFormTypes = new FormTypes();
        partialUpdatedFormTypes.setId(formTypes.getId());

        partialUpdatedFormTypes.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);

        restFormTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormTypes.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFormTypes))
            )
            .andExpect(status().isOk());

        // Validate the FormTypes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFormTypesUpdatableFieldsEquals(partialUpdatedFormTypes, getPersistedFormTypes(partialUpdatedFormTypes));
    }

    @Test
    @Transactional
    void patchNonExistingFormTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formTypes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formTypes.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(formTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formTypes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(formTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formTypes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormTypesMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(formTypes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormTypes() throws Exception {
        // Initialize the database
        formTypesRepository.saveAndFlush(formTypes);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the formTypes
        restFormTypesMockMvc
            .perform(delete(ENTITY_API_URL_ID, formTypes.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return formTypesRepository.count();
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

    protected FormTypes getPersistedFormTypes(FormTypes formTypes) {
        return formTypesRepository.findById(formTypes.getId()).orElseThrow();
    }

    protected void assertPersistedFormTypesToMatchAllProperties(FormTypes expectedFormTypes) {
        assertFormTypesAllPropertiesEquals(expectedFormTypes, getPersistedFormTypes(expectedFormTypes));
    }

    protected void assertPersistedFormTypesToMatchUpdatableProperties(FormTypes expectedFormTypes) {
        assertFormTypesAllUpdatablePropertiesEquals(expectedFormTypes, getPersistedFormTypes(expectedFormTypes));
    }
}
