package museu.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import museu.domain.Foto;
import museu.repository.FotoRepository;
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
 * REST controller for managing {@link museu.domain.Foto}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FotoResource {

    private final Logger log = LoggerFactory.getLogger(FotoResource.class);

    private static final String ENTITY_NAME = "foto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FotoRepository fotoRepository;

    public FotoResource(FotoRepository fotoRepository) {
        this.fotoRepository = fotoRepository;
    }

    /**
     * {@code GET  /fotos} : get all the fotos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fotos in body.
     */
    @GetMapping("/fotos")
    public List<Foto> getAllFotos() {
        log.debug("REST request to get all Fotos");
        return fotoRepository.findAll();
    }

    /**
     * {@code GET  /fotos} : get all the fotos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fotos in body.
     */
    @GetMapping("/fotos/instrumento/:idInstrumento")
    public Set<Foto> getAllFotosByInstrumentoId(@PathVariable Long idInstrumento) {
        log.debug("REST request to get all Fotos");
        return fotoRepository.findByInstrumentoId(idInstrumento);
    }

    /**
     * {@code GET  /fotos/:id} : get the "id" foto.
     *
     * @param id the id of the foto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the foto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fotos/{id}")
    public ResponseEntity<Foto> getFoto(@PathVariable Long id) {
        log.debug("REST request to get Foto : {}", id);
        Optional<Foto> foto = fotoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(foto);
    }
}
