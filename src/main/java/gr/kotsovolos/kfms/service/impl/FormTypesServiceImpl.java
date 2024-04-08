package gr.kotsovolos.kfms.service.impl;

import gr.kotsovolos.kfms.domain.FormTypes;
import gr.kotsovolos.kfms.repository.FormTypesRepository;
import gr.kotsovolos.kfms.service.FormTypesService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link gr.kotsovolos.kfms.domain.FormTypes}.
 */
@Service
@Transactional
public class FormTypesServiceImpl implements FormTypesService {

    private final Logger log = LoggerFactory.getLogger(FormTypesServiceImpl.class);

    private final FormTypesRepository formTypesRepository;

    public FormTypesServiceImpl(FormTypesRepository formTypesRepository) {
        this.formTypesRepository = formTypesRepository;
    }

    @Override
    public FormTypes save(FormTypes formTypes) {
        log.debug("Request to save FormTypes : {}", formTypes);
        return formTypesRepository.save(formTypes);
    }

    @Override
    public FormTypes update(FormTypes formTypes) {
        log.debug("Request to update FormTypes : {}", formTypes);
        return formTypesRepository.save(formTypes);
    }

    @Override
    public Optional<FormTypes> partialUpdate(FormTypes formTypes) {
        log.debug("Request to partially update FormTypes : {}", formTypes);

        return formTypesRepository
            .findById(formTypes.getId())
            .map(existingFormTypes -> {
                if (formTypes.getType() != null) {
                    existingFormTypes.setType(formTypes.getType());
                }
                if (formTypes.getDescription() != null) {
                    existingFormTypes.setDescription(formTypes.getDescription());
                }

                return existingFormTypes;
            })
            .map(formTypesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormTypes> findAll(Pageable pageable) {
        log.debug("Request to get all FormTypes");
        return formTypesRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormTypes> findOne(Long id) {
        log.debug("Request to get FormTypes : {}", id);
        return formTypesRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormTypes : {}", id);
        formTypesRepository.deleteById(id);
    }
}
