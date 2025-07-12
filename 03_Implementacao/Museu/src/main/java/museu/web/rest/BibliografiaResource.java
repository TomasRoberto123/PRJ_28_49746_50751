package museu.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import museu.domain.Bibliografia;
import museu.repository.BibliografiaRepository;
import museu.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link museu.domain.Bibliografia}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BibliografiaResource {

    private final Logger log = LoggerFactory.getLogger(BibliografiaResource.class);

    private static final String ENTITY_NAME = "bibliografia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BibliografiaRepository bibliografiaRepository;

    public BibliografiaResource(BibliografiaRepository bibliografiaRepository) {
        this.bibliografiaRepository = bibliografiaRepository;
    }

    /**
     * {@code GET  /bibliografias} : get all the bibliografias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bibliografias in body.
     */
    @GetMapping("/bibliografias")
    public List<Bibliografia> getAllBibliografias() {
        log.debug("REST request to get all Bibliografias");
        return bibliografiaRepository.findAll();
    }

    /**
     * {@code GET  /bibliografias/:id} : get the "id" bibliografia.
     *
     * @param id the id of the bibliografia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bibliografia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bibliografias/{id}")
    public ResponseEntity<Bibliografia> getBibliografia(@PathVariable Long id) {
        log.debug("REST request to get Bibliografia : {}", id);
        Optional<Bibliografia> bibliografia = bibliografiaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bibliografia);
    }
}
