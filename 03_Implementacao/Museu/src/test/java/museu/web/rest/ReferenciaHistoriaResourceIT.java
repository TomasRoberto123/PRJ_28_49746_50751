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
import museu.domain.ReferenciaHistoria;
import museu.repository.ReferenciaHistoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReferenciaHistoriaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReferenciaHistoriaResourceIT {

    private static final String DEFAULT_ANCORA = "AAAAAAAAAA";
    private static final String UPDATED_ANCORA = "BBBBBBBBBB";

    private static final String DEFAULT_CONTEUDO = "AAAAAAAAAA";
    private static final String UPDATED_CONTEUDO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/referencia-historias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReferenciaHistoriaRepository referenciaHistoriaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReferenciaHistoriaMockMvc;

    private ReferenciaHistoria referenciaHistoria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReferenciaHistoria createEntity(EntityManager em) {
        ReferenciaHistoria referenciaHistoria = new ReferenciaHistoria().ancora(DEFAULT_ANCORA).conteudo(DEFAULT_CONTEUDO);
        return referenciaHistoria;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReferenciaHistoria createUpdatedEntity(EntityManager em) {
        ReferenciaHistoria referenciaHistoria = new ReferenciaHistoria().ancora(UPDATED_ANCORA).conteudo(UPDATED_CONTEUDO);
        return referenciaHistoria;
    }

    @BeforeEach
    public void initTest() {
        referenciaHistoria = createEntity(em);
    }

    @Test
    @Transactional
    void createReferenciaHistoria() throws Exception {
        int databaseSizeBeforeCreate = referenciaHistoriaRepository.findAll().size();
        // Create the ReferenciaHistoria
        restReferenciaHistoriaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referenciaHistoria))
            )
            .andExpect(status().isCreated());

        // Validate the ReferenciaHistoria in the database
        List<ReferenciaHistoria> referenciaHistoriaList = referenciaHistoriaRepository.findAll();
        assertThat(referenciaHistoriaList).hasSize(databaseSizeBeforeCreate + 1);
        ReferenciaHistoria testReferenciaHistoria = referenciaHistoriaList.get(referenciaHistoriaList.size() - 1);
        assertThat(testReferenciaHistoria.getAncora()).isEqualTo(DEFAULT_ANCORA);
        assertThat(testReferenciaHistoria.getConteudo()).isEqualTo(DEFAULT_CONTEUDO);
    }

    @Test
    @Transactional
    void createReferenciaHistoriaWithExistingId() throws Exception {
        // Create the ReferenciaHistoria with an existing ID
        referenciaHistoria.setId(1L);

        int databaseSizeBeforeCreate = referenciaHistoriaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReferenciaHistoriaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referenciaHistoria))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferenciaHistoria in the database
        List<ReferenciaHistoria> referenciaHistoriaList = referenciaHistoriaRepository.findAll();
        assertThat(referenciaHistoriaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReferenciaHistorias() throws Exception {
        // Initialize the database
        referenciaHistoriaRepository.saveAndFlush(referenciaHistoria);

        // Get all the referenciaHistoriaList
        restReferenciaHistoriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(referenciaHistoria.getId().intValue())))
            .andExpect(jsonPath("$.[*].ancora").value(hasItem(DEFAULT_ANCORA)))
            .andExpect(jsonPath("$.[*].conteudo").value(hasItem(DEFAULT_CONTEUDO)));
    }

    @Test
    @Transactional
    void getReferenciaHistoria() throws Exception {
        // Initialize the database
        referenciaHistoriaRepository.saveAndFlush(referenciaHistoria);

        // Get the referenciaHistoria
        restReferenciaHistoriaMockMvc
            .perform(get(ENTITY_API_URL_ID, referenciaHistoria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(referenciaHistoria.getId().intValue()))
            .andExpect(jsonPath("$.ancora").value(DEFAULT_ANCORA))
            .andExpect(jsonPath("$.conteudo").value(DEFAULT_CONTEUDO));
    }

    @Test
    @Transactional
    void getNonExistingReferenciaHistoria() throws Exception {
        // Get the referenciaHistoria
        restReferenciaHistoriaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReferenciaHistoria() throws Exception {
        // Initialize the database
        referenciaHistoriaRepository.saveAndFlush(referenciaHistoria);

        int databaseSizeBeforeUpdate = referenciaHistoriaRepository.findAll().size();

        // Update the referenciaHistoria
        ReferenciaHistoria updatedReferenciaHistoria = referenciaHistoriaRepository.findById(referenciaHistoria.getId()).get();
        // Disconnect from session so that the updates on updatedReferenciaHistoria are not directly saved in db
        em.detach(updatedReferenciaHistoria);
        updatedReferenciaHistoria.ancora(UPDATED_ANCORA).conteudo(UPDATED_CONTEUDO);

        restReferenciaHistoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReferenciaHistoria.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReferenciaHistoria))
            )
            .andExpect(status().isOk());

        // Validate the ReferenciaHistoria in the database
        List<ReferenciaHistoria> referenciaHistoriaList = referenciaHistoriaRepository.findAll();
        assertThat(referenciaHistoriaList).hasSize(databaseSizeBeforeUpdate);
        ReferenciaHistoria testReferenciaHistoria = referenciaHistoriaList.get(referenciaHistoriaList.size() - 1);
        assertThat(testReferenciaHistoria.getAncora()).isEqualTo(UPDATED_ANCORA);
        assertThat(testReferenciaHistoria.getConteudo()).isEqualTo(UPDATED_CONTEUDO);
    }

    @Test
    @Transactional
    void putNonExistingReferenciaHistoria() throws Exception {
        int databaseSizeBeforeUpdate = referenciaHistoriaRepository.findAll().size();
        referenciaHistoria.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferenciaHistoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referenciaHistoria.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(referenciaHistoria))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferenciaHistoria in the database
        List<ReferenciaHistoria> referenciaHistoriaList = referenciaHistoriaRepository.findAll();
        assertThat(referenciaHistoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReferenciaHistoria() throws Exception {
        int databaseSizeBeforeUpdate = referenciaHistoriaRepository.findAll().size();
        referenciaHistoria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferenciaHistoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(referenciaHistoria))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferenciaHistoria in the database
        List<ReferenciaHistoria> referenciaHistoriaList = referenciaHistoriaRepository.findAll();
        assertThat(referenciaHistoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReferenciaHistoria() throws Exception {
        int databaseSizeBeforeUpdate = referenciaHistoriaRepository.findAll().size();
        referenciaHistoria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferenciaHistoriaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referenciaHistoria))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReferenciaHistoria in the database
        List<ReferenciaHistoria> referenciaHistoriaList = referenciaHistoriaRepository.findAll();
        assertThat(referenciaHistoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReferenciaHistoriaWithPatch() throws Exception {
        // Initialize the database
        referenciaHistoriaRepository.saveAndFlush(referenciaHistoria);

        int databaseSizeBeforeUpdate = referenciaHistoriaRepository.findAll().size();

        // Update the referenciaHistoria using partial update
        ReferenciaHistoria partialUpdatedReferenciaHistoria = new ReferenciaHistoria();
        partialUpdatedReferenciaHistoria.setId(referenciaHistoria.getId());

        restReferenciaHistoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferenciaHistoria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReferenciaHistoria))
            )
            .andExpect(status().isOk());

        // Validate the ReferenciaHistoria in the database
        List<ReferenciaHistoria> referenciaHistoriaList = referenciaHistoriaRepository.findAll();
        assertThat(referenciaHistoriaList).hasSize(databaseSizeBeforeUpdate);
        ReferenciaHistoria testReferenciaHistoria = referenciaHistoriaList.get(referenciaHistoriaList.size() - 1);
        assertThat(testReferenciaHistoria.getAncora()).isEqualTo(DEFAULT_ANCORA);
        assertThat(testReferenciaHistoria.getConteudo()).isEqualTo(DEFAULT_CONTEUDO);
    }

    @Test
    @Transactional
    void fullUpdateReferenciaHistoriaWithPatch() throws Exception {
        // Initialize the database
        referenciaHistoriaRepository.saveAndFlush(referenciaHistoria);

        int databaseSizeBeforeUpdate = referenciaHistoriaRepository.findAll().size();

        // Update the referenciaHistoria using partial update
        ReferenciaHistoria partialUpdatedReferenciaHistoria = new ReferenciaHistoria();
        partialUpdatedReferenciaHistoria.setId(referenciaHistoria.getId());

        partialUpdatedReferenciaHistoria.ancora(UPDATED_ANCORA).conteudo(UPDATED_CONTEUDO);

        restReferenciaHistoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferenciaHistoria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReferenciaHistoria))
            )
            .andExpect(status().isOk());

        // Validate the ReferenciaHistoria in the database
        List<ReferenciaHistoria> referenciaHistoriaList = referenciaHistoriaRepository.findAll();
        assertThat(referenciaHistoriaList).hasSize(databaseSizeBeforeUpdate);
        ReferenciaHistoria testReferenciaHistoria = referenciaHistoriaList.get(referenciaHistoriaList.size() - 1);
        assertThat(testReferenciaHistoria.getAncora()).isEqualTo(UPDATED_ANCORA);
        assertThat(testReferenciaHistoria.getConteudo()).isEqualTo(UPDATED_CONTEUDO);
    }

    @Test
    @Transactional
    void patchNonExistingReferenciaHistoria() throws Exception {
        int databaseSizeBeforeUpdate = referenciaHistoriaRepository.findAll().size();
        referenciaHistoria.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferenciaHistoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, referenciaHistoria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(referenciaHistoria))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferenciaHistoria in the database
        List<ReferenciaHistoria> referenciaHistoriaList = referenciaHistoriaRepository.findAll();
        assertThat(referenciaHistoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReferenciaHistoria() throws Exception {
        int databaseSizeBeforeUpdate = referenciaHistoriaRepository.findAll().size();
        referenciaHistoria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferenciaHistoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(referenciaHistoria))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferenciaHistoria in the database
        List<ReferenciaHistoria> referenciaHistoriaList = referenciaHistoriaRepository.findAll();
        assertThat(referenciaHistoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReferenciaHistoria() throws Exception {
        int databaseSizeBeforeUpdate = referenciaHistoriaRepository.findAll().size();
        referenciaHistoria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferenciaHistoriaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(referenciaHistoria))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReferenciaHistoria in the database
        List<ReferenciaHistoria> referenciaHistoriaList = referenciaHistoriaRepository.findAll();
        assertThat(referenciaHistoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReferenciaHistoria() throws Exception {
        // Initialize the database
        referenciaHistoriaRepository.saveAndFlush(referenciaHistoria);

        int databaseSizeBeforeDelete = referenciaHistoriaRepository.findAll().size();

        // Delete the referenciaHistoria
        restReferenciaHistoriaMockMvc
            .perform(delete(ENTITY_API_URL_ID, referenciaHistoria.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReferenciaHistoria> referenciaHistoriaList = referenciaHistoriaRepository.findAll();
        assertThat(referenciaHistoriaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
