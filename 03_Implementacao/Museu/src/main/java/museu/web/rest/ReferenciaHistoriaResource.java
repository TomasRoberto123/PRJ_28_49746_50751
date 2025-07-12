package museu.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import museu.domain.ReferenciaHistoria;
import museu.repository.ReferenciaHistoriaRepository;
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
 * REST controller for managing {@link museu.domain.ReferenciaHistoria}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReferenciaHistoriaResource {

    private final Logger log = LoggerFactory.getLogger(ReferenciaHistoriaResource.class);

    private static final String ENTITY_NAME = "referenciaHistoria";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReferenciaHistoriaRepository referenciaHistoriaRepository;

    public ReferenciaHistoriaResource(ReferenciaHistoriaRepository referenciaHistoriaRepository) {
        this.referenciaHistoriaRepository = referenciaHistoriaRepository;
    }

    /**
     * {@code GET  /referencia-historias} : get all the referenciaHistorias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of referenciaHistorias in body.
     */
    @GetMapping("/referencia-historias")
    public List<ReferenciaHistoria> getAllReferenciaHistorias() {
        log.debug("REST request to get all ReferenciaHistorias");
        return referenciaHistoriaRepository.findAll();
    }

    /**
     * {@code GET  /referencia-historias/:id} : get the "id" referenciaHistoria.
     *
     * @param id the id of the referenciaHistoria to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referenciaHistoria, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/referencia-historias/{id}")
    public ResponseEntity<ReferenciaHistoria> getReferenciaHistoria(@PathVariable Long id) {
        log.debug("REST request to get ReferenciaHistoria : {}", id);
        Optional<ReferenciaHistoria> referenciaHistoria = referenciaHistoriaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(referenciaHistoria);
    }
}
