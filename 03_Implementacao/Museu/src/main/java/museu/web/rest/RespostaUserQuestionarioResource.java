package museu.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import museu.domain.RespostaUserQuestionario;
import museu.repository.RespostaUserQuestionarioRepository;
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
 * REST controller for managing {@link museu.domain.RespostaUserQuestionario}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RespostaUserQuestionarioResource {

    private final Logger log = LoggerFactory.getLogger(RespostaUserQuestionarioResource.class);

    private static final String ENTITY_NAME = "respostaUserQuestionario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RespostaUserQuestionarioRepository respostaUserQuestionarioRepository;

    public RespostaUserQuestionarioResource(RespostaUserQuestionarioRepository respostaUserQuestionarioRepository) {
        this.respostaUserQuestionarioRepository = respostaUserQuestionarioRepository;
    }

    /**
     * {@code GET  /resposta-user-questionarios} : get all the respostaUserQuestionarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of respostaUserQuestionarios in body.
     */
    @GetMapping("/resposta-user-questionarios")
    public List<RespostaUserQuestionario> getAllRespostaUserQuestionarios() {
        log.debug("REST request to get all RespostaUserQuestionarios");
        return respostaUserQuestionarioRepository.findAll();
    }

    /**
     * {@code GET  /resposta-user-questionarios} : get all the respostaUserQuestionarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of respostaUserQuestionarios in body.
     */
    @GetMapping("/resposta-user-questionarios/user")
    public List<RespostaUserQuestionario> getAllRespostaUserQuestionariosByUserId() {
        log.debug("REST request to get all RespostaUserQuestionarios");
        return respostaUserQuestionarioRepository.findByUserIsCurrentUser();
    }

    /**
     * {@code GET  /resposta-user-questionarios/:id} : get the "id" respostaUserQuestionario.
     *
     * @param id the id of the respostaUserQuestionario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the respostaUserQuestionario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resposta-user-questionarios/{id}")
    public ResponseEntity<RespostaUserQuestionario> getRespostaUserQuestionario(@PathVariable Long id) {
        log.debug("REST request to get RespostaUserQuestionario : {}", id);
        Optional<RespostaUserQuestionario> respostaUserQuestionario = respostaUserQuestionarioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(respostaUserQuestionario);
    }

    /**
     * {@code POST  /resposta-user-questionarios} : Create a new respostaUserQuestionario.
     *
     * @param respostaUserQuestionario the respostaUserQuestionario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new respostaUserQuestionario, or with status {@code 400 (Bad Request)} if the respostaUserQuestionario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resposta-user-questionarios")
    public ResponseEntity<RespostaUserQuestionario> createRespostaUserQuestionario(
        @RequestBody RespostaUserQuestionario respostaUserQuestionario
    ) throws URISyntaxException {
        log.debug("REST request to save RespostaUserQuestionario : {}", respostaUserQuestionario);
        if (respostaUserQuestionario.getId() != null) {
            throw new BadRequestAlertException("A new respostaUserQuestionario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        List<RespostaUserQuestionario> existingRespostUserQuestionario = respostaUserQuestionarioRepository.findByUserIsCurrentUser();
        for (RespostaUserQuestionario r : existingRespostUserQuestionario) {
            if (r.getQuestionario().equals(respostaUserQuestionario.getQuestionario())) {
                RespostaUserQuestionario newResposta = new RespostaUserQuestionario();
                newResposta.setId(r.getId());
                newResposta.setPontuacao(respostaUserQuestionario.getPontuacao());
                newResposta.setQuestionario(r.getQuestionario());
                newResposta.setUser(r.getUser());

                if (newResposta.getPontuacao() > r.getPontuacao()) {
                    RespostaUserQuestionario result = respostaUserQuestionarioRepository.save(newResposta);
                    return ResponseEntity
                        .ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } else {
                    RespostaUserQuestionario result = respostaUserQuestionarioRepository.save(r);
                    return ResponseEntity
                        .ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                }
            }
        }
        RespostaUserQuestionario result = respostaUserQuestionarioRepository.save(respostaUserQuestionario);
        return ResponseEntity
            .created(new URI("/api/resposta-user-questionarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
