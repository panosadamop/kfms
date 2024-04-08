package gr.kotsovolos.kfms.web.rest;

import gr.kotsovolos.kfms.domain.FormTypes;
import gr.kotsovolos.kfms.repository.FormTypesRepository;
import gr.kotsovolos.kfms.service.FormTypesService;
import gr.kotsovolos.kfms.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link gr.kotsovolos.kfms.domain.FormTypes}.
 */
@RestController
@RequestMapping("/api/form-types")
public class FormTypesResource {

    private final Logger log = LoggerFactory.getLogger(FormTypesResource.class);

    private static final String ENTITY_NAME = "formTypes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormTypesService formTypesService;

    private final FormTypesRepository formTypesRepository;

    public FormTypesResource(FormTypesService formTypesService, FormTypesRepository formTypesRepository) {
        this.formTypesService = formTypesService;
        this.formTypesRepository = formTypesRepository;
    }

    /**
     * {@code POST  /form-types} : Create a new formTypes.
     *
     * @param formTypes the formTypes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formTypes, or with status {@code 400 (Bad Request)} if the formTypes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FormTypes> createFormTypes(@Valid @RequestBody FormTypes formTypes) throws URISyntaxException {
        log.debug("REST request to save FormTypes : {}", formTypes);
        if (formTypes.getId() != null) {
            throw new BadRequestAlertException("A new formTypes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        formTypes = formTypesService.save(formTypes);
        return ResponseEntity.created(new URI("/api/form-types/" + formTypes.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, formTypes.getId().toString()))
            .body(formTypes);
    }

    /**
     * {@code PUT  /form-types/:id} : Updates an existing formTypes.
     *
     * @param id the id of the formTypes to save.
     * @param formTypes the formTypes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formTypes,
     * or with status {@code 400 (Bad Request)} if the formTypes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formTypes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FormTypes> updateFormTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FormTypes formTypes
    ) throws URISyntaxException {
        log.debug("REST request to update FormTypes : {}, {}", id, formTypes);
        if (formTypes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formTypes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        formTypes = formTypesService.update(formTypes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formTypes.getId().toString()))
            .body(formTypes);
    }

    /**
     * {@code PATCH  /form-types/:id} : Partial updates given fields of an existing formTypes, field will ignore if it is null
     *
     * @param id the id of the formTypes to save.
     * @param formTypes the formTypes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formTypes,
     * or with status {@code 400 (Bad Request)} if the formTypes is not valid,
     * or with status {@code 404 (Not Found)} if the formTypes is not found,
     * or with status {@code 500 (Internal Server Error)} if the formTypes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormTypes> partialUpdateFormTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FormTypes formTypes
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormTypes partially : {}, {}", id, formTypes);
        if (formTypes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formTypes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormTypes> result = formTypesService.partialUpdate(formTypes);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formTypes.getId().toString())
        );
    }

    /**
     * {@code GET  /form-types} : get all the formTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FormTypes>> getAllFormTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FormTypes");
        Page<FormTypes> page = formTypesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /form-types/:id} : get the "id" formTypes.
     *
     * @param id the id of the formTypes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formTypes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FormTypes> getFormTypes(@PathVariable("id") Long id) {
        log.debug("REST request to get FormTypes : {}", id);
        Optional<FormTypes> formTypes = formTypesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formTypes);
    }

    /**
     * {@code DELETE  /form-types/:id} : delete the "id" formTypes.
     *
     * @param id the id of the formTypes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormTypes(@PathVariable("id") Long id) {
        log.debug("REST request to delete FormTypes : {}", id);
        formTypesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
