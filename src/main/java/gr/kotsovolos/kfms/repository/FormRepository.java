package gr.kotsovolos.kfms.repository;

import gr.kotsovolos.kfms.domain.Form;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Form entity.
 */
@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    default Optional<Form> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Form> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Form> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select form from Form form left join fetch form.formType", countQuery = "select count(form) from Form form")
    Page<Form> findAllWithToOneRelationships(Pageable pageable);

    @Query("select form from Form form left join fetch form.formType")
    List<Form> findAllWithToOneRelationships();

    @Query("select form from Form form left join fetch form.formType where form.id =:id")
    Optional<Form> findOneWithToOneRelationships(@Param("id") Long id);
}
