package gr.kotsovolos.kfms.service;

import gr.kotsovolos.kfms.domain.Form;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link gr.kotsovolos.kfms.domain.Form}.
 */
public interface FormService {
    /**
     * Save a form.
     *
     * @param form the entity to save.
     * @return the persisted entity.
     */
    Form save(Form form);

    /**
     * Updates a form.
     *
     * @param form the entity to update.
     * @return the persisted entity.
     */
    Form update(Form form);

    /**
     * Partially updates a form.
     *
     * @param form the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Form> partialUpdate(Form form);

    /**
     * Get all the forms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Form> findAll(Pageable pageable);

    /**
     * Get all the forms with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Form> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" form.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Form> findOne(Long id);

    /**
     * Delete the "id" form.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
