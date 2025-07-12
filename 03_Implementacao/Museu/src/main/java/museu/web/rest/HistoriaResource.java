package museu.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import museu.domain.Historia;
import museu.repository.FotoRepository;
import museu.repository.HistoriaRepository;
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
 * REST controller for managing {@link museu.domain.Historia}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HistoriaResource {

    private final Logger log = LoggerFactory.getLogger(HistoriaResource.class);

    private static final String ENTITY_NAME = "historia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoriaRepository historiaRepository;
    private final ReferenciaHistoriaRepository referenciaHistoriaRepository;
    private final FotoRepository fotoRepository;

    public HistoriaResource(
        HistoriaRepository historiaRepository,
        ReferenciaHistoriaRepository referenciaHistoriaRepository,
        FotoRepository fotoRepository
    ) {
        this.historiaRepository = historiaRepository;
        this.referenciaHistoriaRepository = referenciaHistoriaRepository;
        this.fotoRepository = fotoRepository;
    }

    /**
     * {@code GET  /historias} : get all the historias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historias in body.
     */
    @GetMapping("/historias")
    public List<Historia> getAllHistorias() {
        log.debug("REST request to get all Historias");
        return historiaRepository.findAll();
    }

    /**
     * {@code GET  /historias/:id} : get the "id" historia.
     *
     * @param id the id of the historia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/historias/{id}")
    public ResponseEntity<Historia> getHistoria(@PathVariable Long id) {
        log.debug("REST request to get Historia : {}", id);
        Optional<Historia> historia = historiaRepository.findById(id);
        historia.get().setReferenciaHistorias(this.referenciaHistoriaRepository.findAllByHistoriaId(id));
        historia.get().setFotos(this.fotoRepository.findByHistoriaId(id));
        return ResponseUtil.wrapOrNotFound(historia);
    }
}
