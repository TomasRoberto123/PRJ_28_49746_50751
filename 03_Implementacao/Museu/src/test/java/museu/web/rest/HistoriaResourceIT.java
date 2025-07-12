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
import museu.domain.Historia;
import museu.repository.HistoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HistoriaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoriaResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_AUTOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTOR = "BBBBBBBBBB";

    private static final String DEFAULT_CONTEUDO_PATH = "AAAAAAAAAA";
    private static final String UPDATED_CONTEUDO_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/historias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HistoriaRepository historiaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriaMockMvc;

    private Historia historia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historia createEntity(EntityManager em) {
        Historia historia = new Historia().titulo(DEFAULT_TITULO).autor(DEFAULT_AUTOR).conteudoPath(DEFAULT_CONTEUDO_PATH);
        return historia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historia createUpdatedEntity(EntityManager em) {
        Historia historia = new Historia().titulo(UPDATED_TITULO).autor(UPDATED_AUTOR).conteudoPath(UPDATED_CONTEUDO_PATH);
        return historia;
    }

    @BeforeEach
    public void initTest() {
        historia = createEntity(em);
    }

    @Test
    @Transactional
    void createHistoria() throws Exception {
        int databaseSizeBeforeCreate = historiaRepository.findAll().size();
        // Create the Historia
        restHistoriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historia)))
            .andExpect(status().isCreated());

        // Validate the Historia in the database
        List<Historia> historiaList = historiaRepository.findAll();
        assertThat(historiaList).hasSize(databaseSizeBeforeCreate + 1);
        Historia testHistoria = historiaList.get(historiaList.size() - 1);
        assertThat(testHistoria.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testHistoria.getAutor()).isEqualTo(DEFAULT_AUTOR);
        assertThat(testHistoria.getConteudoPath()).isEqualTo(DEFAULT_CONTEUDO_PATH);
    }

    @Test
    @Transactional
    void createHistoriaWithExistingId() throws Exception {
        // Create the Historia with an existing ID
        historia.setId(1L);

        int databaseSizeBeforeCreate = historiaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historia)))
            .andExpect(status().isBadRequest());

        // Validate the Historia in the database
        List<Historia> historiaList = historiaRepository.findAll();
        assertThat(historiaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHistorias() throws Exception {
        // Initialize the database
        historiaRepository.saveAndFlush(historia);

        // Get all the historiaList
        restHistoriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historia.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].autor").value(hasItem(DEFAULT_AUTOR)))
            .andExpect(jsonPath("$.[*].conteudoPath").value(hasItem(DEFAULT_CONTEUDO_PATH)));
    }

    @Test
    @Transactional
    void getHistoria() throws Exception {
        // Initialize the database
        historiaRepository.saveAndFlush(historia);

        // Get the historia
        restHistoriaMockMvc
            .perform(get(ENTITY_API_URL_ID, historia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historia.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.autor").value(DEFAULT_AUTOR))
            .andExpect(jsonPath("$.conteudoPath").value(DEFAULT_CONTEUDO_PATH));
    }

    @Test
    @Transactional
    void getNonExistingHistoria() throws Exception {
        // Get the historia
        restHistoriaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHistoria() throws Exception {
        // Initialize the database
        historiaRepository.saveAndFlush(historia);

        int databaseSizeBeforeUpdate = historiaRepository.findAll().size();

        // Update the historia
        Historia updatedHistoria = historiaRepository.findById(historia.getId()).get();
        // Disconnect from session so that the updates on updatedHistoria are not directly saved in db
        em.detach(updatedHistoria);
        updatedHistoria.titulo(UPDATED_TITULO).autor(UPDATED_AUTOR).conteudoPath(UPDATED_CONTEUDO_PATH);

        restHistoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHistoria.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHistoria))
            )
            .andExpect(status().isOk());

        // Validate the Historia in the database
        List<Historia> historiaList = historiaRepository.findAll();
        assertThat(historiaList).hasSize(databaseSizeBeforeUpdate);
        Historia testHistoria = historiaList.get(historiaList.size() - 1);
        assertThat(testHistoria.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testHistoria.getAutor()).isEqualTo(UPDATED_AUTOR);
        assertThat(testHistoria.getConteudoPath()).isEqualTo(UPDATED_CONTEUDO_PATH);
    }

    @Test
    @Transactional
    void putNonExistingHistoria() throws Exception {
        int databaseSizeBeforeUpdate = historiaRepository.findAll().size();
        historia.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historia.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historia in the database
        List<Historia> historiaList = historiaRepository.findAll();
        assertThat(historiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoria() throws Exception {
        int databaseSizeBeforeUpdate = historiaRepository.findAll().size();
        historia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historia in the database
        List<Historia> historiaList = historiaRepository.findAll();
        assertThat(historiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoria() throws Exception {
        int databaseSizeBeforeUpdate = historiaRepository.findAll().size();
        historia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Historia in the database
        List<Historia> historiaList = historiaRepository.findAll();
        assertThat(historiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoriaWithPatch() throws Exception {
        // Initialize the database
        historiaRepository.saveAndFlush(historia);

        int databaseSizeBeforeUpdate = historiaRepository.findAll().size();

        // Update the historia using partial update
        Historia partialUpdatedHistoria = new Historia();
        partialUpdatedHistoria.setId(historia.getId());

        partialUpdatedHistoria.autor(UPDATED_AUTOR);

        restHistoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoria))
            )
            .andExpect(status().isOk());

        // Validate the Historia in the database
        List<Historia> historiaList = historiaRepository.findAll();
        assertThat(historiaList).hasSize(databaseSizeBeforeUpdate);
        Historia testHistoria = historiaList.get(historiaList.size() - 1);
        assertThat(testHistoria.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testHistoria.getAutor()).isEqualTo(UPDATED_AUTOR);
        assertThat(testHistoria.getConteudoPath()).isEqualTo(DEFAULT_CONTEUDO_PATH);
    }

    @Test
    @Transactional
    void fullUpdateHistoriaWithPatch() throws Exception {
        // Initialize the database
        historiaRepository.saveAndFlush(historia);

        int databaseSizeBeforeUpdate = historiaRepository.findAll().size();

        // Update the historia using partial update
        Historia partialUpdatedHistoria = new Historia();
        partialUpdatedHistoria.setId(historia.getId());

        partialUpdatedHistoria.titulo(UPDATED_TITULO).autor(UPDATED_AUTOR).conteudoPath(UPDATED_CONTEUDO_PATH);

        restHistoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoria))
            )
            .andExpect(status().isOk());

        // Validate the Historia in the database
        List<Historia> historiaList = historiaRepository.findAll();
        assertThat(historiaList).hasSize(databaseSizeBeforeUpdate);
        Historia testHistoria = historiaList.get(historiaList.size() - 1);
        assertThat(testHistoria.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testHistoria.getAutor()).isEqualTo(UPDATED_AUTOR);
        assertThat(testHistoria.getConteudoPath()).isEqualTo(UPDATED_CONTEUDO_PATH);
    }

    @Test
    @Transactional
    void patchNonExistingHistoria() throws Exception {
        int databaseSizeBeforeUpdate = historiaRepository.findAll().size();
        historia.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historia in the database
        List<Historia> historiaList = historiaRepository.findAll();
        assertThat(historiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoria() throws Exception {
        int databaseSizeBeforeUpdate = historiaRepository.findAll().size();
        historia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historia in the database
        List<Historia> historiaList = historiaRepository.findAll();
        assertThat(historiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoria() throws Exception {
        int databaseSizeBeforeUpdate = historiaRepository.findAll().size();
        historia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(historia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Historia in the database
        List<Historia> historiaList = historiaRepository.findAll();
        assertThat(historiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistoria() throws Exception {
        // Initialize the database
        historiaRepository.saveAndFlush(historia);

        int databaseSizeBeforeDelete = historiaRepository.findAll().size();

        // Delete the historia
        restHistoriaMockMvc
            .perform(delete(ENTITY_API_URL_ID, historia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Historia> historiaList = historiaRepository.findAll();
        assertThat(historiaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
