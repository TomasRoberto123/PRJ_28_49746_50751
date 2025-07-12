package museu.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import museu.domain.SubTema;
import museu.repository.FotoRepository;
import museu.repository.InstrumentoRepository;
import museu.repository.SubTemaRepository;
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
 * REST controller for managing {@link museu.domain.SubTema}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SubTemaResource {

    private final Logger log = LoggerFactory.getLogger(SubTemaResource.class);

    private static final String ENTITY_NAME = "subTema";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubTemaRepository subTemaRepository;
    private final InstrumentoRepository instrumentoRepository;
    private final FotoRepository fotoRepository;

    public SubTemaResource(
        SubTemaRepository subTemaRepository,
        InstrumentoRepository instrumentoRepository,
        FotoRepository fotoRepository
    ) {
        this.subTemaRepository = subTemaRepository;
        this.instrumentoRepository = instrumentoRepository;
        this.fotoRepository = fotoRepository;
    }

    /**
     * {@code GET  /sub-temas} : get all the subTemas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subTemas in body.
     */
    @GetMapping("/sub-temas")
    public List<SubTema> getAllSubTemas() {
        log.debug("REST request to get all SubTemas");
        return subTemaRepository.findAll();
    }

    /**
     * {@code GET  /sub-temas/:id} : get the "id" subTema.
     *
     * @param id the id of the subTema to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subTema, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sub-temas/{id}")
    public ResponseEntity<SubTema> getSubTema(@PathVariable Long id) {
        log.debug("REST request to get SubTema : {}", id);
        Optional<SubTema> subTema = subTemaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(subTema);
    }

    @GetMapping("/sub-temas/categoria/{id}")
    public Set<SubTema> getSubTemaByCategoriaId(@PathVariable Long id) {
        log.debug("REST request to get SubTema : {}", id);
        return subTemaRepository.findAllByCategoriaId(id);
    }
}
