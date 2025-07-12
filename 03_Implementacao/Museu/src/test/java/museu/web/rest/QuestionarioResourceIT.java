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
import museu.domain.Questionario;
import museu.repository.QuestionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuestionarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionarioResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/questionarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionarioRepository questionarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionarioMockMvc;

    private Questionario questionario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questionario createEntity(EntityManager em) {
        Questionario questionario = new Questionario().nome(DEFAULT_NOME).descricao(DEFAULT_DESCRICAO);
        return questionario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questionario createUpdatedEntity(EntityManager em) {
        Questionario questionario = new Questionario().nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);
        return questionario;
    }

    @BeforeEach
    public void initTest() {
        questionario = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionario() throws Exception {
        int databaseSizeBeforeCreate = questionarioRepository.findAll().size();
        // Create the Questionario
        restQuestionarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionario)))
            .andExpect(status().isCreated());

        // Validate the Questionario in the database
        List<Questionario> questionarioList = questionarioRepository.findAll();
        assertThat(questionarioList).hasSize(databaseSizeBeforeCreate + 1);
        Questionario testQuestionario = questionarioList.get(questionarioList.size() - 1);
        assertThat(testQuestionario.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testQuestionario.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createQuestionarioWithExistingId() throws Exception {
        // Create the Questionario with an existing ID
        questionario.setId(1L);

        int databaseSizeBeforeCreate = questionarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionario)))
            .andExpect(status().isBadRequest());

        // Validate the Questionario in the database
        List<Questionario> questionarioList = questionarioRepository.findAll();
        assertThat(questionarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionarios() throws Exception {
        // Initialize the database
        questionarioRepository.saveAndFlush(questionario);

        // Get all the questionarioList
        restQuestionarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getQuestionario() throws Exception {
        // Initialize the database
        questionarioRepository.saveAndFlush(questionario);

        // Get the questionario
        restQuestionarioMockMvc
            .perform(get(ENTITY_API_URL_ID, questionario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionario.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingQuestionario() throws Exception {
        // Get the questionario
        restQuestionarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestionario() throws Exception {
        // Initialize the database
        questionarioRepository.saveAndFlush(questionario);

        int databaseSizeBeforeUpdate = questionarioRepository.findAll().size();

        // Update the questionario
        Questionario updatedQuestionario = questionarioRepository.findById(questionario.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionario are not directly saved in db
        em.detach(updatedQuestionario);
        updatedQuestionario.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);

        restQuestionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuestionario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuestionario))
            )
            .andExpect(status().isOk());

        // Validate the Questionario in the database
        List<Questionario> questionarioList = questionarioRepository.findAll();
        assertThat(questionarioList).hasSize(databaseSizeBeforeUpdate);
        Questionario testQuestionario = questionarioList.get(questionarioList.size() - 1);
        assertThat(testQuestionario.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testQuestionario.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void putNonExistingQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = questionarioRepository.findAll().size();
        questionario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionario in the database
        List<Questionario> questionarioList = questionarioRepository.findAll();
        assertThat(questionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = questionarioRepository.findAll().size();
        questionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionario in the database
        List<Questionario> questionarioList = questionarioRepository.findAll();
        assertThat(questionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = questionarioRepository.findAll().size();
        questionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questionario in the database
        List<Questionario> questionarioList = questionarioRepository.findAll();
        assertThat(questionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionarioWithPatch() throws Exception {
        // Initialize the database
        questionarioRepository.saveAndFlush(questionario);

        int databaseSizeBeforeUpdate = questionarioRepository.findAll().size();

        // Update the questionario using partial update
        Questionario partialUpdatedQuestionario = new Questionario();
        partialUpdatedQuestionario.setId(questionario.getId());

        partialUpdatedQuestionario.descricao(UPDATED_DESCRICAO);

        restQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionario))
            )
            .andExpect(status().isOk());

        // Validate the Questionario in the database
        List<Questionario> questionarioList = questionarioRepository.findAll();
        assertThat(questionarioList).hasSize(databaseSizeBeforeUpdate);
        Questionario testQuestionario = questionarioList.get(questionarioList.size() - 1);
        assertThat(testQuestionario.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testQuestionario.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateQuestionarioWithPatch() throws Exception {
        // Initialize the database
        questionarioRepository.saveAndFlush(questionario);

        int databaseSizeBeforeUpdate = questionarioRepository.findAll().size();

        // Update the questionario using partial update
        Questionario partialUpdatedQuestionario = new Questionario();
        partialUpdatedQuestionario.setId(questionario.getId());

        partialUpdatedQuestionario.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);

        restQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionario))
            )
            .andExpect(status().isOk());

        // Validate the Questionario in the database
        List<Questionario> questionarioList = questionarioRepository.findAll();
        assertThat(questionarioList).hasSize(databaseSizeBeforeUpdate);
        Questionario testQuestionario = questionarioList.get(questionarioList.size() - 1);
        assertThat(testQuestionario.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testQuestionario.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = questionarioRepository.findAll().size();
        questionario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionario in the database
        List<Questionario> questionarioList = questionarioRepository.findAll();
        assertThat(questionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = questionarioRepository.findAll().size();
        questionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionario in the database
        List<Questionario> questionarioList = questionarioRepository.findAll();
        assertThat(questionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = questionarioRepository.findAll().size();
        questionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(questionario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questionario in the database
        List<Questionario> questionarioList = questionarioRepository.findAll();
        assertThat(questionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionario() throws Exception {
        // Initialize the database
        questionarioRepository.saveAndFlush(questionario);

        int databaseSizeBeforeDelete = questionarioRepository.findAll().size();

        // Delete the questionario
        restQuestionarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Questionario> questionarioList = questionarioRepository.findAll();
        assertThat(questionarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
