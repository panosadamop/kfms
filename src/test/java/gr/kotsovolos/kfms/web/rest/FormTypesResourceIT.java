package gr.kotsovolos.kfms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import gr.kotsovolos.kfms.IntegrationTest;
import gr.kotsovolos.kfms.domain.FormTypes;
import gr.kotsovolos.kfms.repository.FormTypesRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
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
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
        int databaseSizeBeforeCreate = formTypesRepository.findAll().size();
        // Create the FormTypes
        restFormTypesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formTypes))
            )
            .andExpect(status().isCreated());

        // Validate the FormTypes in the database
        List<FormTypes> formTypesList = formTypesRepository.findAll();
        assertThat(formTypesList).hasSize(databaseSizeBeforeCreate + 1);
        FormTypes testFormTypes = formTypesList.get(formTypesList.size() - 1);
        assertThat(testFormTypes.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFormTypes.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createFormTypesWithExistingId() throws Exception {
        // Create the FormTypes with an existing ID
        formTypes.setId(1L);

        int databaseSizeBeforeCreate = formTypesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormTypesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormTypes in the database
        List<FormTypes> formTypesList = formTypesRepository.findAll();
        assertThat(formTypesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = formTypesRepository.findAll().size();
        // set the field null
        formTypes.setType(null);

        // Create the FormTypes, which fails.

        restFormTypesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formTypes))
            )
            .andExpect(status().isBadRequest());

        List<FormTypes> formTypesList = formTypesRepository.findAll();
        assertThat(formTypesList).hasSize(databaseSizeBeforeTest);
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
    void putNewFormTypes() throws Exception {
        // Initialize the database
        formTypesRepository.saveAndFlush(formTypes);

        int databaseSizeBeforeUpdate = formTypesRepository.findAll().size();

        // Update the formTypes
        FormTypes updatedFormTypes = formTypesRepository.findById(formTypes.getId()).get();
        // Disconnect from session so that the updates on updatedFormTypes are not directly saved in db
        em.detach(updatedFormTypes);
        updatedFormTypes.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);

        restFormTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFormTypes.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFormTypes))
            )
            .andExpect(status().isOk());

        // Validate the FormTypes in the database
        List<FormTypes> formTypesList = formTypesRepository.findAll();
        assertThat(formTypesList).hasSize(databaseSizeBeforeUpdate);
        FormTypes testFormTypes = formTypesList.get(formTypesList.size() - 1);
        assertThat(testFormTypes.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFormTypes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingFormTypes() throws Exception {
        int databaseSizeBeforeUpdate = formTypesRepository.findAll().size();
        formTypes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formTypes.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormTypes in the database
        List<FormTypes> formTypesList = formTypesRepository.findAll();
        assertThat(formTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormTypes() throws Exception {
        int databaseSizeBeforeUpdate = formTypesRepository.findAll().size();
        formTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormTypes in the database
        List<FormTypes> formTypesList = formTypesRepository.findAll();
        assertThat(formTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormTypes() throws Exception {
        int databaseSizeBeforeUpdate = formTypesRepository.findAll().size();
        formTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormTypesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formTypes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormTypes in the database
        List<FormTypes> formTypesList = formTypesRepository.findAll();
        assertThat(formTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormTypesWithPatch() throws Exception {
        // Initialize the database
        formTypesRepository.saveAndFlush(formTypes);

        int databaseSizeBeforeUpdate = formTypesRepository.findAll().size();

        // Update the formTypes using partial update
        FormTypes partialUpdatedFormTypes = new FormTypes();
        partialUpdatedFormTypes.setId(formTypes.getId());

        restFormTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormTypes.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormTypes))
            )
            .andExpect(status().isOk());

        // Validate the FormTypes in the database
        List<FormTypes> formTypesList = formTypesRepository.findAll();
        assertThat(formTypesList).hasSize(databaseSizeBeforeUpdate);
        FormTypes testFormTypes = formTypesList.get(formTypesList.size() - 1);
        assertThat(testFormTypes.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFormTypes.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateFormTypesWithPatch() throws Exception {
        // Initialize the database
        formTypesRepository.saveAndFlush(formTypes);

        int databaseSizeBeforeUpdate = formTypesRepository.findAll().size();

        // Update the formTypes using partial update
        FormTypes partialUpdatedFormTypes = new FormTypes();
        partialUpdatedFormTypes.setId(formTypes.getId());

        partialUpdatedFormTypes.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);

        restFormTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormTypes.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormTypes))
            )
            .andExpect(status().isOk());

        // Validate the FormTypes in the database
        List<FormTypes> formTypesList = formTypesRepository.findAll();
        assertThat(formTypesList).hasSize(databaseSizeBeforeUpdate);
        FormTypes testFormTypes = formTypesList.get(formTypesList.size() - 1);
        assertThat(testFormTypes.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFormTypes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingFormTypes() throws Exception {
        int databaseSizeBeforeUpdate = formTypesRepository.findAll().size();
        formTypes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formTypes.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormTypes in the database
        List<FormTypes> formTypesList = formTypesRepository.findAll();
        assertThat(formTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormTypes() throws Exception {
        int databaseSizeBeforeUpdate = formTypesRepository.findAll().size();
        formTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormTypes in the database
        List<FormTypes> formTypesList = formTypesRepository.findAll();
        assertThat(formTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormTypes() throws Exception {
        int databaseSizeBeforeUpdate = formTypesRepository.findAll().size();
        formTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormTypesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formTypes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormTypes in the database
        List<FormTypes> formTypesList = formTypesRepository.findAll();
        assertThat(formTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormTypes() throws Exception {
        // Initialize the database
        formTypesRepository.saveAndFlush(formTypes);

        int databaseSizeBeforeDelete = formTypesRepository.findAll().size();

        // Delete the formTypes
        restFormTypesMockMvc
            .perform(delete(ENTITY_API_URL_ID, formTypes.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormTypes> formTypesList = formTypesRepository.findAll();
        assertThat(formTypesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
