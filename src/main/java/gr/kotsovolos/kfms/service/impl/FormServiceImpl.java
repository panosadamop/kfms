package gr.kotsovolos.kfms.service.impl;

import gr.kotsovolos.kfms.domain.Form;
import gr.kotsovolos.kfms.repository.FormRepository;
import gr.kotsovolos.kfms.service.FormService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link gr.kotsovolos.kfms.domain.Form}.
 */
@Service
@Transactional
public class FormServiceImpl implements FormService {

    private final Logger log = LoggerFactory.getLogger(FormServiceImpl.class);

    private final FormRepository formRepository;

    public FormServiceImpl(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    @Override
    public Form save(Form form) {
        log.debug("Request to save Form : {}", form);
        return formRepository.save(form);
    }

    @Override
    public Form update(Form form) {
        log.debug("Request to update Form : {}", form);
        return formRepository.save(form);
    }

    @Override
    public Optional<Form> partialUpdate(Form form) {
        log.debug("Request to partially update Form : {}", form);

        return formRepository
            .findById(form.getId())
            .map(existingForm -> {
                if (form.getFormName() != null) {
                    existingForm.setFormName(form.getFormName());
                }
                if (form.getFormDescription() != null) {
                    existingForm.setFormDescription(form.getFormDescription());
                }
                if (form.getFormData() != null) {
                    existingForm.setFormData(form.getFormData());
                }
                if (form.getVersion() != null) {
                    existingForm.setVersion(form.getVersion());
                }
                if (form.getIsDeleted() != null) {
                    existingForm.setIsDeleted(form.getIsDeleted());
                }
                if (form.getIsLocked() != null) {
                    existingForm.setIsLocked(form.getIsLocked());
                }
                if (form.getCreateDate() != null) {
                    existingForm.setCreateDate(form.getCreateDate());
                }
                if (form.getUpdateDate() != null) {
                    existingForm.setUpdateDate(form.getUpdateDate());
                }

                return existingForm;
            })
            .map(formRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Form> findAll(Pageable pageable) {
        log.debug("Request to get all Forms");
        return formRepository.findAll(pageable);
    }

    public Page<Form> findAllWithEagerRelationships(Pageable pageable) {
        return formRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Form> findOne(Long id) {
        log.debug("Request to get Form : {}", id);
        return formRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Form : {}", id);
        formRepository.deleteById(id);
    }
}
