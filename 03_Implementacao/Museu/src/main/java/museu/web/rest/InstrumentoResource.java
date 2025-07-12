package museu.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import museu.domain.Instrumento;
import museu.repository.FotoRepository;
import museu.repository.InstrumentoRepository;
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
 * REST controller for managing {@link museu.domain.Instrumento}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InstrumentoResource {

    private final Logger log = LoggerFactory.getLogger(InstrumentoResource.class);

    private static final String ENTITY_NAME = "instrumento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstrumentoRepository instrumentoRepository;
    private final FotoRepository fotoRepository;

    public InstrumentoResource(InstrumentoRepository instrumentoRepository, FotoRepository fotoRepository) {
        this.instrumentoRepository = instrumentoRepository;
        this.fotoRepository = fotoRepository;
    }

    /**
     * {@code GET  /instrumentos} : get all the instrumentos.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of instrumentos in body.
     */
    @GetMapping("/instrumentos")
    public List<Instrumento> getAllInstrumentos(@RequestParam(required = false) String filter) {
        //        if ("questaoquestionario-is-null".equals(filter)) {
        //            log.debug("REST request to get all Instrumentos where questaoQuestionario is null");
        //            return StreamSupport
        //                .stream(instrumentoRepository.findAll().spliterator(), false)
        //                .filter(instrumento -> instrumento.getQuestaoQuestionario() == null)
        //                .collect(Collectors.toList());
        //        }
        log.debug("REST request to get all Instrumentos");
        List<Instrumento> instrumentos = instrumentoRepository.findAll();
        instrumentos.stream().forEach(item -> item.setFotos(this.fotoRepository.findByInstrumentoId(item.getId())));

        return instrumentos;
    }

    /**
     * {@code GET  /instrumentos/:id} : get the "id" instrumento.
     *
     * @param id the id of the instrumento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the instrumento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/instrumentos/{id}")
    public ResponseEntity<Instrumento> getInstrumento(@PathVariable Long id) {
        log.debug("REST request to get Instrumento : {}", id);
        Optional<Instrumento> instrumento = instrumentoRepository.findById(id);
        instrumento.get().setFotos(this.fotoRepository.findByInstrumentoId(id));
        return ResponseUtil.wrapOrNotFound(instrumento);
    }

    @GetMapping("/instrumentos/subtema/{id}")
    public List<Instrumento> getInstrumentoBySubtemaId(@PathVariable Long id) {
        log.debug("REST request to get Instrumento : {}", id);
        List<Instrumento> instrumentos = instrumentoRepository.findAllBySubTemaId(id);
        instrumentos.stream().forEach(item -> item.setFotos(this.fotoRepository.findByInstrumentoId(item.getId())));
        return instrumentos;
    }
}
