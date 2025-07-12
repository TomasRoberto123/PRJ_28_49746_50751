package museu.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import museu.domain.OpcaoQuestaoQuestionario;
import museu.repository.OpcaoQuestaoQuestionarioRepository;
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
 * REST controller for managing {@link museu.domain.OpcaoQuestaoQuestionario}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OpcaoQuestaoQuestionarioResource {

    private final Logger log = LoggerFactory.getLogger(OpcaoQuestaoQuestionarioResource.class);

    private static final String ENTITY_NAME = "opcaoQuestaoQuestionario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OpcaoQuestaoQuestionarioRepository opcaoQuestaoQuestionarioRepository;

    public OpcaoQuestaoQuestionarioResource(OpcaoQuestaoQuestionarioRepository opcaoQuestaoQuestionarioRepository) {
        this.opcaoQuestaoQuestionarioRepository = opcaoQuestaoQuestionarioRepository;
    }

    /**
     * {@code GET  /opcao-questao-questionarios} : get all the opcaoQuestaoQuestionarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of opcaoQuestaoQuestionarios in body.
     */
    @GetMapping("/opcao-questao-questionarios")
    public List<OpcaoQuestaoQuestionario> getAllOpcaoQuestaoQuestionarios() {
        log.debug("REST request to get all OpcaoQuestaoQuestionarios");
        return opcaoQuestaoQuestionarioRepository.findAll();
    }

    /**
     * {@code GET  /opcao-questao-questionarios} : get all the opcaoQuestaoQuestionarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of opcaoQuestaoQuestionarios in body.
     */
    @GetMapping("/opcao-questao-questionarios/questao/{id}")
    public List<OpcaoQuestaoQuestionario> getAllOpcaoQuestaoQuestionariosByQuestaoId(@PathVariable Long id) {
        log.debug("REST request to get all OpcaoQuestaoQuestionarios");
        return opcaoQuestaoQuestionarioRepository.findAllByQuestaoQuestionarioId(id);
    }

    /**
     * {@code GET  /opcao-questao-questionarios/:id} : get the "id" opcaoQuestaoQuestionario.
     *
     * @param id the id of the opcaoQuestaoQuestionario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the opcaoQuestaoQuestionario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/opcao-questao-questionarios/{id}")
    public ResponseEntity<OpcaoQuestaoQuestionario> getOpcaoQuestaoQuestionario(@PathVariable Long id) {
        log.debug("REST request to get OpcaoQuestaoQuestionario : {}", id);
        Optional<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionario = opcaoQuestaoQuestionarioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(opcaoQuestaoQuestionario);
    }
}
