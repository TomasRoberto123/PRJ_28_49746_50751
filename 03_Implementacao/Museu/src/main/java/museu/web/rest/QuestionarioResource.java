package museu.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import museu.domain.QuestaoQuestionario;
import museu.domain.Questionario;
import museu.repository.OpcaoQuestaoQuestionarioRepository;
import museu.repository.QuestaoQuestionarioRepository;
import museu.repository.QuestionarioRepository;
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
 * REST controller for managing {@link museu.domain.Questionario}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class QuestionarioResource {

    private final Logger log = LoggerFactory.getLogger(QuestionarioResource.class);

    private static final String ENTITY_NAME = "questionario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionarioRepository questionarioRepository;
    private final QuestaoQuestionarioRepository questaoQuestionarioRepository;
    private final OpcaoQuestaoQuestionarioRepository opcaoQuestaoQuestionarioRepository;

    public QuestionarioResource(
        QuestionarioRepository questionarioRepository,
        QuestaoQuestionarioRepository questaoQuestionarioRepository,
        OpcaoQuestaoQuestionarioRepository opcaoQuestaoQuestionarioRepository
    ) {
        this.questionarioRepository = questionarioRepository;
        this.questaoQuestionarioRepository = questaoQuestionarioRepository;
        this.opcaoQuestaoQuestionarioRepository = opcaoQuestaoQuestionarioRepository;
    }

    /**
     * {@code GET  /questionarios} : get all the questionarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionarios in body.
     */
    @GetMapping("/questionarios")
    public List<Questionario> getAllQuestionarios() {
        log.debug("REST request to get all Questionarios");
        List<Questionario> questionarios = questionarioRepository.findAll();
        Collections.sort(questionarios);
        return questionarios;
    }

    /**
     * {@code GET  /questionarios/:id} : get the "id" questionario.
     *
     * @param id the id of the questionario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/questionarios/{id}")
    public ResponseEntity<Questionario> getQuestionario(@PathVariable Long id) {
        log.debug("REST request to get Questionario : {}", id);
        Optional<Questionario> questionario = questionarioRepository.findById(id);
        questionario.get().setQuestaoQuestionarios(this.questaoQuestionarioRepository.findAllByQuestionarioId(questionario.get().getId()));
        return ResponseUtil.wrapOrNotFound(questionario);
    }
}
