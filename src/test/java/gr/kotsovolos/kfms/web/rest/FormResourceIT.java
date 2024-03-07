package gr.kotsovolos.kfms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import gr.kotsovolos.kfms.IntegrationTest;
import gr.kotsovolos.kfms.domain.Form;
import gr.kotsovolos.kfms.repository.FormRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FormResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormResourceIT {

    private static final String DEFAULT_FORM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FORM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FORM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_FORM_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FORM_DATA = "AAAAAAAAAA";
    private static final String UPDATED_FORM_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final Boolean DEFAULT_IS_LOCKED = false;
    private static final Boolean UPDATED_IS_LOCKED = true;

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/forms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormMockMvc;

    private Form form;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Form createEntity(EntityManager em) {
        Form form = new Form()
            .formName(DEFAULT_FORM_NAME)
            .formDescription(DEFAULT_FORM_DESCRIPTION)
            .formData(DEFAULT_FORM_DATA)
            .version(DEFAULT_VERSION)
            .isDeleted(DEFAULT_IS_DELETED)
            .isLocked(DEFAULT_IS_LOCKED)
            .createDate(DEFAULT_CREATE_DATE)
            .updateDate(DEFAULT_UPDATE_DATE);
        return form;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Form createUpdatedEntity(EntityManager em) {
        Form form = new Form()
            .formName(UPDATED_FORM_NAME)
            .formDescription(UPDATED_FORM_DESCRIPTION)
            .formData(UPDATED_FORM_DATA)
            .version(UPDATED_VERSION)
            .isDeleted(UPDATED_IS_DELETED)
            .isLocked(UPDATED_IS_LOCKED)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE);
        return form;
    }

    @BeforeEach
    public void initTest() {
        form = createEntity(em);
    }

    @Test
    @Transactional
    void createForm() throws Exception {
        int databaseSizeBeforeCreate = formRepository.findAll().size();
        // Create the Form
        restFormMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(form))
            )
            .andExpect(status().isCreated());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeCreate + 1);
        Form testForm = formList.get(formList.size() - 1);
        assertThat(testForm.getFormName()).isEqualTo(DEFAULT_FORM_NAME);
        assertThat(testForm.getFormDescription()).isEqualTo(DEFAULT_FORM_DESCRIPTION);
        assertThat(testForm.getFormData()).isEqualTo(DEFAULT_FORM_DATA);
        assertThat(testForm.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testForm.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testForm.getIsLocked()).isEqualTo(DEFAULT_IS_LOCKED);
        assertThat(testForm.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testForm.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
    }

    @Test
    @Transactional
    void createFormWithExistingId() throws Exception {
        // Create the Form with an existing ID
        form.setId(1L);

        int databaseSizeBeforeCreate = formRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(form))
            )
            .andExpect(status().isBadRequest());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFormNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = formRepository.findAll().size();
        // set the field null
        form.setFormName(null);

        // Create the Form, which fails.

        restFormMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(form))
            )
            .andExpect(status().isBadRequest());

        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = formRepository.findAll().size();
        // set the field null
        form.setVersion(null);

        // Create the Form, which fails.

        restFormMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(form))
            )
            .andExpect(status().isBadRequest());

        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = formRepository.findAll().size();
        // set the field null
        form.setCreateDate(null);

        // Create the Form, which fails.

        restFormMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(form))
            )
            .andExpect(status().isBadRequest());

        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllForms() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList
        restFormMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(form.getId().intValue())))
            .andExpect(jsonPath("$.[*].formName").value(hasItem(DEFAULT_FORM_NAME)))
            .andExpect(jsonPath("$.[*].formDescription").value(hasItem(DEFAULT_FORM_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].formData").value(hasItem(DEFAULT_FORM_DATA.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].isLocked").value(hasItem(DEFAULT_IS_LOCKED.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())));
    }

    @Test
    @Transactional
    void getForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get the form
        restFormMockMvc
            .perform(get(ENTITY_API_URL_ID, form.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(form.getId().intValue()))
            .andExpect(jsonPath("$.formName").value(DEFAULT_FORM_NAME))
            .andExpect(jsonPath("$.formDescription").value(DEFAULT_FORM_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.formData").value(DEFAULT_FORM_DATA.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.isLocked").value(DEFAULT_IS_LOCKED.booleanValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingForm() throws Exception {
        // Get the form
        restFormMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        int databaseSizeBeforeUpdate = formRepository.findAll().size();

        // Update the form
        Form updatedForm = formRepository.findById(form.getId()).get();
        // Disconnect from session so that the updates on updatedForm are not directly saved in db
        em.detach(updatedForm);
        updatedForm
            .formName(UPDATED_FORM_NAME)
            .formDescription(UPDATED_FORM_DESCRIPTION)
            .formData(UPDATED_FORM_DATA)
            .version(UPDATED_VERSION)
            .isDeleted(UPDATED_IS_DELETED)
            .isLocked(UPDATED_IS_LOCKED)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE);

        restFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedForm.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedForm))
            )
            .andExpect(status().isOk());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
        Form testForm = formList.get(formList.size() - 1);
        assertThat(testForm.getFormName()).isEqualTo(UPDATED_FORM_NAME);
        assertThat(testForm.getFormDescription()).isEqualTo(UPDATED_FORM_DESCRIPTION);
        assertThat(testForm.getFormData()).isEqualTo(UPDATED_FORM_DATA);
        assertThat(testForm.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testForm.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testForm.getIsLocked()).isEqualTo(UPDATED_IS_LOCKED);
        assertThat(testForm.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testForm.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, form.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(form))
            )
            .andExpect(status().isBadRequest());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(form))
            )
            .andExpect(status().isBadRequest());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(form))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormWithPatch() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        int databaseSizeBeforeUpdate = formRepository.findAll().size();

        // Update the form using partial update
        Form partialUpdatedForm = new Form();
        partialUpdatedForm.setId(form.getId());

        partialUpdatedForm
            .formName(UPDATED_FORM_NAME)
            .formData(UPDATED_FORM_DATA)
            .isDeleted(UPDATED_IS_DELETED)
            .isLocked(UPDATED_IS_LOCKED)
            .updateDate(UPDATED_UPDATE_DATE);

        restFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForm.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForm))
            )
            .andExpect(status().isOk());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
        Form testForm = formList.get(formList.size() - 1);
        assertThat(testForm.getFormName()).isEqualTo(UPDATED_FORM_NAME);
        assertThat(testForm.getFormDescription()).isEqualTo(DEFAULT_FORM_DESCRIPTION);
        assertThat(testForm.getFormData()).isEqualTo(UPDATED_FORM_DATA);
        assertThat(testForm.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testForm.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testForm.getIsLocked()).isEqualTo(UPDATED_IS_LOCKED);
        assertThat(testForm.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testForm.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFormWithPatch() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        int databaseSizeBeforeUpdate = formRepository.findAll().size();

        // Update the form using partial update
        Form partialUpdatedForm = new Form();
        partialUpdatedForm.setId(form.getId());

        partialUpdatedForm
            .formName(UPDATED_FORM_NAME)
            .formDescription(UPDATED_FORM_DESCRIPTION)
            .formData(UPDATED_FORM_DATA)
            .version(UPDATED_VERSION)
            .isDeleted(UPDATED_IS_DELETED)
            .isLocked(UPDATED_IS_LOCKED)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE);

        restFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForm.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForm))
            )
            .andExpect(status().isOk());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
        Form testForm = formList.get(formList.size() - 1);
        assertThat(testForm.getFormName()).isEqualTo(UPDATED_FORM_NAME);
        assertThat(testForm.getFormDescription()).isEqualTo(UPDATED_FORM_DESCRIPTION);
        assertThat(testForm.getFormData()).isEqualTo(UPDATED_FORM_DATA);
        assertThat(testForm.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testForm.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testForm.getIsLocked()).isEqualTo(UPDATED_IS_LOCKED);
        assertThat(testForm.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testForm.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, form.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(form))
            )
            .andExpect(status().isBadRequest());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(form))
            )
            .andExpect(status().isBadRequest());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(form))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        int databaseSizeBeforeDelete = formRepository.findAll().size();

        // Delete the form
        restFormMockMvc
            .perform(delete(ENTITY_API_URL_ID, form.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
