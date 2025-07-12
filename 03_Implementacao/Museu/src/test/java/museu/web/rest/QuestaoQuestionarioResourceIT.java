package museu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import museu.IntegrationTest;
import museu.domain.QuestaoQuestionario;
import museu.repository.QuestaoQuestionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuestaoQuestionarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestaoQuestionarioResourceIT {

    private static final String DEFAULT_PERGUNTA = "AAAAAAAAAA";
    private static final String UPDATED_PERGUNTA = "BBBBBBBBBB";

    private static final String DEFAULT_PONTUACAO = "AAAAAAAAAA";
    private static final String UPDATED_PONTUACAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/questao-questionarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestaoQuestionarioRepository questaoQuestionarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestaoQuestionarioMockMvc;

    private QuestaoQuestionario questaoQuestionario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestaoQuestionario createEntity(EntityManager em) {
        QuestaoQuestionario questaoQuestionario = new QuestaoQuestionario().pergunta(DEFAULT_PERGUNTA).pontuacao(DEFAULT_PONTUACAO);
        return questaoQuestionario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestaoQuestionario createUpdatedEntity(EntityManager em) {
        QuestaoQuestionario questaoQuestionario = new QuestaoQuestionario().pergunta(UPDATED_PERGUNTA).pontuacao(UPDATED_PONTUACAO);
        return questaoQuestionario;
    }

    @BeforeEach
    public void initTest() {
        questaoQuestionario = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestaoQuestionario() throws Exception {
        int databaseSizeBeforeCreate = questaoQuestionarioRepository.findAll().size();
        // Create the QuestaoQuestionario
        restQuestaoQuestionarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questaoQuestionario))
            )
            .andExpect(status().isCreated());

        // Validate the QuestaoQuestionario in the database
        List<QuestaoQuestionario> questaoQuestionarioList = questaoQuestionarioRepository.findAll();
        assertThat(questaoQuestionarioList).hasSize(databaseSizeBeforeCreate + 1);
        QuestaoQuestionario testQuestaoQuestionario = questaoQuestionarioList.get(questaoQuestionarioList.size() - 1);
        assertThat(testQuestaoQuestionario.getPergunta()).isEqualTo(DEFAULT_PERGUNTA);
        assertThat(testQuestaoQuestionario.getPontuacao()).isEqualTo(DEFAULT_PONTUACAO);
    }

    @Test
    @Transactional
    void createQuestaoQuestionarioWithExistingId() throws Exception {
        // Create the QuestaoQuestionario with an existing ID
        questaoQuestionario.setId(1L);

        int databaseSizeBeforeCreate = questaoQuestionarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestaoQuestionarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questaoQuestionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestaoQuestionario in the database
        List<QuestaoQuestionario> questaoQuestionarioList = questaoQuestionarioRepository.findAll();
        assertThat(questaoQuestionarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestaoQuestionarios() throws Exception {
        // Initialize the database
        questaoQuestionarioRepository.saveAndFlush(questaoQuestionario);

        // Get all the questaoQuestionarioList
        restQuestaoQuestionarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questaoQuestionario.getId().intValue())))
            .andExpect(jsonPath("$.[*].pergunta").value(hasItem(DEFAULT_PERGUNTA)))
            .andExpect(jsonPath("$.[*].pontuacao").value(hasItem(DEFAULT_PONTUACAO)));
    }

    @Test
    @Transactional
    void getQuestaoQuestionario() throws Exception {
        // Initialize the database
        questaoQuestionarioRepository.saveAndFlush(questaoQuestionario);

        // Get the questaoQuestionario
        restQuestaoQuestionarioMockMvc
            .perform(get(ENTITY_API_URL_ID, questaoQuestionario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questaoQuestionario.getId().intValue()))
            .andExpect(jsonPath("$.pergunta").value(DEFAULT_PERGUNTA))
            .andExpect(jsonPath("$.pontuacao").value(DEFAULT_PONTUACAO));
    }

    @Test
    @Transactional
    void getNonExistingQuestaoQuestionario() throws Exception {
        // Get the questaoQuestionario
        restQuestaoQuestionarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestaoQuestionario() throws Exception {
        // Initialize the database
        questaoQuestionarioRepository.saveAndFlush(questaoQuestionario);

        int databaseSizeBeforeUpdate = questaoQuestionarioRepository.findAll().size();

        // Update the questaoQuestionario
        QuestaoQuestionario updatedQuestaoQuestionario = questaoQuestionarioRepository.findById(questaoQuestionario.getId()).get();
        // Disconnect from session so that the updates on updatedQuestaoQuestionario are not directly saved in db
        em.detach(updatedQuestaoQuestionario);
        updatedQuestaoQuestionario.pergunta(UPDATED_PERGUNTA).pontuacao(UPDATED_PONTUACAO);

        restQuestaoQuestionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuestaoQuestionario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuestaoQuestionario))
            )
            .andExpect(status().isOk());

        // Validate the QuestaoQuestionario in the database
        List<QuestaoQuestionario> questaoQuestionarioList = questaoQuestionarioRepository.findAll();
        assertThat(questaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
        QuestaoQuestionario testQuestaoQuestionario = questaoQuestionarioList.get(questaoQuestionarioList.size() - 1);
        assertThat(testQuestaoQuestionario.getPergunta()).isEqualTo(UPDATED_PERGUNTA);
        assertThat(testQuestaoQuestionario.getPontuacao()).isEqualTo(UPDATED_PONTUACAO);
    }

    @Test
    @Transactional
    void putNonExistingQuestaoQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = questaoQuestionarioRepository.findAll().size();
        questaoQuestionario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestaoQuestionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questaoQuestionario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questaoQuestionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestaoQuestionario in the database
        List<QuestaoQuestionario> questaoQuestionarioList = questaoQuestionarioRepository.findAll();
        assertThat(questaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestaoQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = questaoQuestionarioRepository.findAll().size();
        questaoQuestionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestaoQuestionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questaoQuestionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestaoQuestionario in the database
        List<QuestaoQuestionario> questaoQuestionarioList = questaoQuestionarioRepository.findAll();
        assertThat(questaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestaoQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = questaoQuestionarioRepository.findAll().size();
        questaoQuestionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestaoQuestionarioMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questaoQuestionario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestaoQuestionario in the database
        List<QuestaoQuestionario> questaoQuestionarioList = questaoQuestionarioRepository.findAll();
        assertThat(questaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestaoQuestionarioWithPatch() throws Exception {
        // Initialize the database
        questaoQuestionarioRepository.saveAndFlush(questaoQuestionario);

        int databaseSizeBeforeUpdate = questaoQuestionarioRepository.findAll().size();

        // Update the questaoQuestionario using partial update
        QuestaoQuestionario partialUpdatedQuestaoQuestionario = new QuestaoQuestionario();
        partialUpdatedQuestaoQuestionario.setId(questaoQuestionario.getId());

        restQuestaoQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestaoQuestionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestaoQuestionario))
            )
            .andExpect(status().isOk());

        // Validate the QuestaoQuestionario in the database
        List<QuestaoQuestionario> questaoQuestionarioList = questaoQuestionarioRepository.findAll();
        assertThat(questaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
        QuestaoQuestionario testQuestaoQuestionario = questaoQuestionarioList.get(questaoQuestionarioList.size() - 1);
        assertThat(testQuestaoQuestionario.getPergunta()).isEqualTo(DEFAULT_PERGUNTA);
        assertThat(testQuestaoQuestionario.getPontuacao()).isEqualTo(DEFAULT_PONTUACAO);
    }

    @Test
    @Transactional
    void fullUpdateQuestaoQuestionarioWithPatch() throws Exception {
        // Initialize the database
        questaoQuestionarioRepository.saveAndFlush(questaoQuestionario);

        int databaseSizeBeforeUpdate = questaoQuestionarioRepository.findAll().size();

        // Update the questaoQuestionario using partial update
        QuestaoQuestionario partialUpdatedQuestaoQuestionario = new QuestaoQuestionario();
        partialUpdatedQuestaoQuestionario.setId(questaoQuestionario.getId());

        partialUpdatedQuestaoQuestionario.pergunta(UPDATED_PERGUNTA).pontuacao(UPDATED_PONTUACAO);

        restQuestaoQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestaoQuestionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestaoQuestionario))
            )
            .andExpect(status().isOk());

        // Validate the QuestaoQuestionario in the database
        List<QuestaoQuestionario> questaoQuestionarioList = questaoQuestionarioRepository.findAll();
        assertThat(questaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
        QuestaoQuestionario testQuestaoQuestionario = questaoQuestionarioList.get(questaoQuestionarioList.size() - 1);
        assertThat(testQuestaoQuestionario.getPergunta()).isEqualTo(UPDATED_PERGUNTA);
        assertThat(testQuestaoQuestionario.getPontuacao()).isEqualTo(UPDATED_PONTUACAO);
    }

    @Test
    @Transactional
    void patchNonExistingQuestaoQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = questaoQuestionarioRepository.findAll().size();
        questaoQuestionario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestaoQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questaoQuestionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questaoQuestionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestaoQuestionario in the database
        List<QuestaoQuestionario> questaoQuestionarioList = questaoQuestionarioRepository.findAll();
        assertThat(questaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestaoQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = questaoQuestionarioRepository.findAll().size();
        questaoQuestionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestaoQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questaoQuestionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestaoQuestionario in the database
        List<QuestaoQuestionario> questaoQuestionarioList = questaoQuestionarioRepository.findAll();
        assertThat(questaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestaoQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = questaoQuestionarioRepository.findAll().size();
        questaoQuestionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestaoQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questaoQuestionario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestaoQuestionario in the database
        List<QuestaoQuestionario> questaoQuestionarioList = questaoQuestionarioRepository.findAll();
        assertThat(questaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestaoQuestionario() throws Exception {
        // Initialize the database
        questaoQuestionarioRepository.saveAndFlush(questaoQuestionario);

        int databaseSizeBeforeDelete = questaoQuestionarioRepository.findAll().size();

        // Delete the questaoQuestionario
        restQuestaoQuestionarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, questaoQuestionario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestaoQuestionario> questaoQuestionarioList = questaoQuestionarioRepository.findAll();
        assertThat(questaoQuestionarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
