package gr.kotsovolos.kfms.service;

import gr.kotsovolos.kfms.domain.FormTypes;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link gr.kotsovolos.kfms.domain.FormTypes}.
 */
public interface FormTypesService {
    /**
     * Save a formTypes.
     *
     * @param formTypes the entity to save.
     * @return the persisted entity.
     */
    FormTypes save(FormTypes formTypes);

    /**
     * Updates a formTypes.
     *
     * @param formTypes the entity to update.
     * @return the persisted entity.
     */
    FormTypes update(FormTypes formTypes);

    /**
     * Partially updates a formTypes.
     *
     * @param formTypes the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormTypes> partialUpdate(FormTypes formTypes);

    /**
     * Get all the formTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormTypes> findAll(Pageable pageable);

    /**
     * Get the "id" formTypes.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormTypes> findOne(Long id);

    /**
     * Delete the "id" formTypes.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
