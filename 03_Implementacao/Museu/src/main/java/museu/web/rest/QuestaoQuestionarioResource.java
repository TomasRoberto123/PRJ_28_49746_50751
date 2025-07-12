package museu.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import museu.domain.QuestaoQuestionario;
import museu.repository.QuestaoQuestionarioRepository;
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
 * REST controller for managing {@link museu.domain.QuestaoQuestionario}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class QuestaoQuestionarioResource {

    private final Logger log = LoggerFactory.getLogger(QuestaoQuestionarioResource.class);

    private static final String ENTITY_NAME = "questaoQuestionario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestaoQuestionarioRepository questaoQuestionarioRepository;

    public QuestaoQuestionarioResource(QuestaoQuestionarioRepository questaoQuestionarioRepository) {
        this.questaoQuestionarioRepository = questaoQuestionarioRepository;
    }

    /**
     * {@code GET  /questao-questionarios} : get all the questaoQuestionarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questaoQuestionarios in body.
     */
    @GetMapping("/questao-questionarios")
    public List<QuestaoQuestionario> getAllQuestaoQuestionarios() {
        log.debug("REST request to get all QuestaoQuestionarios");
        return questaoQuestionarioRepository.findAll();
    }

    /**
     * {@code GET  /questao-questionarios/:id} : get the "id" questaoQuestionario.
     *
     * @param id the id of the questaoQuestionario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questaoQuestionario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/questao-questionarios/{id}")
    public ResponseEntity<QuestaoQuestionario> getQuestaoQuestionario(@PathVariable Long id) {
        log.debug("REST request to get QuestaoQuestionario : {}", id);
        Optional<QuestaoQuestionario> questaoQuestionario = questaoQuestionarioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(questaoQuestionario);
    }
}
