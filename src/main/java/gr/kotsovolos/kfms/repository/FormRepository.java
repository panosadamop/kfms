package gr.kotsovolos.kfms.repository;

import gr.kotsovolos.kfms.domain.Form;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Form entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    @Query("select form from Form form where form.user.login = ?#{principal.preferredUsername}")
    List<Form> findByUserIsCurrentUser();
}
