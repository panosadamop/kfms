package gr.kotsovolos.kfms.repository;

import gr.kotsovolos.kfms.domain.FormTypes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FormTypes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormTypesRepository extends JpaRepository<FormTypes, Long> {}
