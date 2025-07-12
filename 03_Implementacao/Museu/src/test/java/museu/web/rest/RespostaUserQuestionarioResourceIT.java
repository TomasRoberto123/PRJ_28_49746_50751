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
import museu.domain.RespostaUserQuestionario;
import museu.repository.RespostaUserQuestionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RespostaUserQuestionarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RespostaUserQuestionarioResourceIT {

    private static final Integer DEFAULT_PONTUACAO = 1;
    private static final Integer UPDATED_PONTUACAO = 2;

    private static final String ENTITY_API_URL = "/api/resposta-user-questionarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RespostaUserQuestionarioRepository respostaUserQuestionarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRespostaUserQuestionarioMockMvc;

    private RespostaUserQuestionario respostaUserQuestionario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RespostaUserQuestionario createEntity(EntityManager em) {
        RespostaUserQuestionario respostaUserQuestionario = new RespostaUserQuestionario().pontuacao(DEFAULT_PONTUACAO);
        return respostaUserQuestionario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RespostaUserQuestionario createUpdatedEntity(EntityManager em) {
        RespostaUserQuestionario respostaUserQuestionario = new RespostaUserQuestionario().pontuacao(UPDATED_PONTUACAO);
        return respostaUserQuestionario;
    }

    @BeforeEach
    public void initTest() {
        respostaUserQuestionario = createEntity(em);
    }

    @Test
    @Transactional
    void createRespostaUserQuestionario() throws Exception {
        int databaseSizeBeforeCreate = respostaUserQuestionarioRepository.findAll().size();
        // Create the RespostaUserQuestionario
        restRespostaUserQuestionarioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(respostaUserQuestionario))
            )
            .andExpect(status().isCreated());

        // Validate the RespostaUserQuestionario in the database
        List<RespostaUserQuestionario> respostaUserQuestionarioList = respostaUserQuestionarioRepository.findAll();
        assertThat(respostaUserQuestionarioList).hasSize(databaseSizeBeforeCreate + 1);
        RespostaUserQuestionario testRespostaUserQuestionario = respostaUserQuestionarioList.get(respostaUserQuestionarioList.size() - 1);
        assertThat(testRespostaUserQuestionario.getPontuacao()).isEqualTo(DEFAULT_PONTUACAO);
    }

    @Test
    @Transactional
    void createRespostaUserQuestionarioWithExistingId() throws Exception {
        // Create the RespostaUserQuestionario with an existing ID
        respostaUserQuestionario.setId(1L);

        int databaseSizeBeforeCreate = respostaUserQuestionarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRespostaUserQuestionarioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(respostaUserQuestionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the RespostaUserQuestionario in the database
        List<RespostaUserQuestionario> respostaUserQuestionarioList = respostaUserQuestionarioRepository.findAll();
        assertThat(respostaUserQuestionarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRespostaUserQuestionarios() throws Exception {
        // Initialize the database
        respostaUserQuestionarioRepository.saveAndFlush(respostaUserQuestionario);

        // Get all the respostaUserQuestionarioList
        restRespostaUserQuestionarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(respostaUserQuestionario.getId().intValue())))
            .andExpect(jsonPath("$.[*].pontuacao").value(hasItem(DEFAULT_PONTUACAO)));
    }

    @Test
    @Transactional
    void getRespostaUserQuestionario() throws Exception {
        // Initialize the database
        respostaUserQuestionarioRepository.saveAndFlush(respostaUserQuestionario);

        // Get the respostaUserQuestionario
        restRespostaUserQuestionarioMockMvc
            .perform(get(ENTITY_API_URL_ID, respostaUserQuestionario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(respostaUserQuestionario.getId().intValue()))
            .andExpect(jsonPath("$.pontuacao").value(DEFAULT_PONTUACAO));
    }

    @Test
    @Transactional
    void getNonExistingRespostaUserQuestionario() throws Exception {
        // Get the respostaUserQuestionario
        restRespostaUserQuestionarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRespostaUserQuestionario() throws Exception {
        // Initialize the database
        respostaUserQuestionarioRepository.saveAndFlush(respostaUserQuestionario);

        int databaseSizeBeforeUpdate = respostaUserQuestionarioRepository.findAll().size();

        // Update the respostaUserQuestionario
        RespostaUserQuestionario updatedRespostaUserQuestionario = respostaUserQuestionarioRepository
            .findById(respostaUserQuestionario.getId())
            .get();
        // Disconnect from session so that the updates on updatedRespostaUserQuestionario are not directly saved in db
        em.detach(updatedRespostaUserQuestionario);
        updatedRespostaUserQuestionario.pontuacao(UPDATED_PONTUACAO);

        restRespostaUserQuestionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRespostaUserQuestionario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRespostaUserQuestionario))
            )
            .andExpect(status().isOk());

        // Validate the RespostaUserQuestionario in the database
        List<RespostaUserQuestionario> respostaUserQuestionarioList = respostaUserQuestionarioRepository.findAll();
        assertThat(respostaUserQuestionarioList).hasSize(databaseSizeBeforeUpdate);
        RespostaUserQuestionario testRespostaUserQuestionario = respostaUserQuestionarioList.get(respostaUserQuestionarioList.size() - 1);
        assertThat(testRespostaUserQuestionario.getPontuacao()).isEqualTo(UPDATED_PONTUACAO);
    }

    @Test
    @Transactional
    void putNonExistingRespostaUserQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = respostaUserQuestionarioRepository.findAll().size();
        respostaUserQuestionario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRespostaUserQuestionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, respostaUserQuestionario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(respostaUserQuestionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the RespostaUserQuestionario in the database
        List<RespostaUserQuestionario> respostaUserQuestionarioList = respostaUserQuestionarioRepository.findAll();
        assertThat(respostaUserQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRespostaUserQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = respostaUserQuestionarioRepository.findAll().size();
        respostaUserQuestionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRespostaUserQuestionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(respostaUserQuestionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the RespostaUserQuestionario in the database
        List<RespostaUserQuestionario> respostaUserQuestionarioList = respostaUserQuestionarioRepository.findAll();
        assertThat(respostaUserQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRespostaUserQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = respostaUserQuestionarioRepository.findAll().size();
        respostaUserQuestionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRespostaUserQuestionarioMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(respostaUserQuestionario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RespostaUserQuestionario in the database
        List<RespostaUserQuestionario> respostaUserQuestionarioList = respostaUserQuestionarioRepository.findAll();
        assertThat(respostaUserQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRespostaUserQuestionarioWithPatch() throws Exception {
        // Initialize the database
        respostaUserQuestionarioRepository.saveAndFlush(respostaUserQuestionario);

        int databaseSizeBeforeUpdate = respostaUserQuestionarioRepository.findAll().size();

        // Update the respostaUserQuestionario using partial update
        RespostaUserQuestionario partialUpdatedRespostaUserQuestionario = new RespostaUserQuestionario();
        partialUpdatedRespostaUserQuestionario.setId(respostaUserQuestionario.getId());

        restRespostaUserQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRespostaUserQuestionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRespostaUserQuestionario))
            )
            .andExpect(status().isOk());

        // Validate the RespostaUserQuestionario in the database
        List<RespostaUserQuestionario> respostaUserQuestionarioList = respostaUserQuestionarioRepository.findAll();
        assertThat(respostaUserQuestionarioList).hasSize(databaseSizeBeforeUpdate);
        RespostaUserQuestionario testRespostaUserQuestionario = respostaUserQuestionarioList.get(respostaUserQuestionarioList.size() - 1);
        assertThat(testRespostaUserQuestionario.getPontuacao()).isEqualTo(DEFAULT_PONTUACAO);
    }

    @Test
    @Transactional
    void fullUpdateRespostaUserQuestionarioWithPatch() throws Exception {
        // Initialize the database
        respostaUserQuestionarioRepository.saveAndFlush(respostaUserQuestionario);

        int databaseSizeBeforeUpdate = respostaUserQuestionarioRepository.findAll().size();

        // Update the respostaUserQuestionario using partial update
        RespostaUserQuestionario partialUpdatedRespostaUserQuestionario = new RespostaUserQuestionario();
        partialUpdatedRespostaUserQuestionario.setId(respostaUserQuestionario.getId());

        partialUpdatedRespostaUserQuestionario.pontuacao(UPDATED_PONTUACAO);

        restRespostaUserQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRespostaUserQuestionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRespostaUserQuestionario))
            )
            .andExpect(status().isOk());

        // Validate the RespostaUserQuestionario in the database
        List<RespostaUserQuestionario> respostaUserQuestionarioList = respostaUserQuestionarioRepository.findAll();
        assertThat(respostaUserQuestionarioList).hasSize(databaseSizeBeforeUpdate);
        RespostaUserQuestionario testRespostaUserQuestionario = respostaUserQuestionarioList.get(respostaUserQuestionarioList.size() - 1);
        assertThat(testRespostaUserQuestionario.getPontuacao()).isEqualTo(UPDATED_PONTUACAO);
    }

    @Test
    @Transactional
    void patchNonExistingRespostaUserQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = respostaUserQuestionarioRepository.findAll().size();
        respostaUserQuestionario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRespostaUserQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, respostaUserQuestionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(respostaUserQuestionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the RespostaUserQuestionario in the database
        List<RespostaUserQuestionario> respostaUserQuestionarioList = respostaUserQuestionarioRepository.findAll();
        assertThat(respostaUserQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRespostaUserQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = respostaUserQuestionarioRepository.findAll().size();
        respostaUserQuestionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRespostaUserQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(respostaUserQuestionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the RespostaUserQuestionario in the database
        List<RespostaUserQuestionario> respostaUserQuestionarioList = respostaUserQuestionarioRepository.findAll();
        assertThat(respostaUserQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRespostaUserQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = respostaUserQuestionarioRepository.findAll().size();
        respostaUserQuestionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRespostaUserQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(respostaUserQuestionario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RespostaUserQuestionario in the database
        List<RespostaUserQuestionario> respostaUserQuestionarioList = respostaUserQuestionarioRepository.findAll();
        assertThat(respostaUserQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRespostaUserQuestionario() throws Exception {
        // Initialize the database
        respostaUserQuestionarioRepository.saveAndFlush(respostaUserQuestionario);

        int databaseSizeBeforeDelete = respostaUserQuestionarioRepository.findAll().size();

        // Delete the respostaUserQuestionario
        restRespostaUserQuestionarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, respostaUserQuestionario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RespostaUserQuestionario> respostaUserQuestionarioList = respostaUserQuestionarioRepository.findAll();
        assertThat(respostaUserQuestionarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
