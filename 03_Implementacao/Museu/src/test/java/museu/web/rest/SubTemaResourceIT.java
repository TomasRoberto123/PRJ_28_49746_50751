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
import museu.domain.SubTema;
import museu.repository.SubTemaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SubTemaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubTemaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sub-temas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubTemaRepository subTemaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubTemaMockMvc;

    private SubTema subTema;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubTema createEntity(EntityManager em) {
        SubTema subTema = new SubTema().nome(DEFAULT_NOME).descricao(DEFAULT_DESCRICAO);
        return subTema;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubTema createUpdatedEntity(EntityManager em) {
        SubTema subTema = new SubTema().nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);
        return subTema;
    }

    @BeforeEach
    public void initTest() {
        subTema = createEntity(em);
    }

    @Test
    @Transactional
    void createSubTema() throws Exception {
        int databaseSizeBeforeCreate = subTemaRepository.findAll().size();
        // Create the SubTema
        restSubTemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subTema)))
            .andExpect(status().isCreated());

        // Validate the SubTema in the database
        List<SubTema> subTemaList = subTemaRepository.findAll();
        assertThat(subTemaList).hasSize(databaseSizeBeforeCreate + 1);
        SubTema testSubTema = subTemaList.get(subTemaList.size() - 1);
        assertThat(testSubTema.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testSubTema.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createSubTemaWithExistingId() throws Exception {
        // Create the SubTema with an existing ID
        subTema.setId(1L);

        int databaseSizeBeforeCreate = subTemaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubTemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subTema)))
            .andExpect(status().isBadRequest());

        // Validate the SubTema in the database
        List<SubTema> subTemaList = subTemaRepository.findAll();
        assertThat(subTemaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSubTemas() throws Exception {
        // Initialize the database
        subTemaRepository.saveAndFlush(subTema);

        // Get all the subTemaList
        restSubTemaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subTema.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getSubTema() throws Exception {
        // Initialize the database
        subTemaRepository.saveAndFlush(subTema);

        // Get the subTema
        restSubTemaMockMvc
            .perform(get(ENTITY_API_URL_ID, subTema.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subTema.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingSubTema() throws Exception {
        // Get the subTema
        restSubTemaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubTema() throws Exception {
        // Initialize the database
        subTemaRepository.saveAndFlush(subTema);

        int databaseSizeBeforeUpdate = subTemaRepository.findAll().size();

        // Update the subTema
        SubTema updatedSubTema = subTemaRepository.findById(subTema.getId()).get();
        // Disconnect from session so that the updates on updatedSubTema are not directly saved in db
        em.detach(updatedSubTema);
        updatedSubTema.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);

        restSubTemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubTema.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSubTema))
            )
            .andExpect(status().isOk());

        // Validate the SubTema in the database
        List<SubTema> subTemaList = subTemaRepository.findAll();
        assertThat(subTemaList).hasSize(databaseSizeBeforeUpdate);
        SubTema testSubTema = subTemaList.get(subTemaList.size() - 1);
        assertThat(testSubTema.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testSubTema.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void putNonExistingSubTema() throws Exception {
        int databaseSizeBeforeUpdate = subTemaRepository.findAll().size();
        subTema.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubTemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subTema.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subTema))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubTema in the database
        List<SubTema> subTemaList = subTemaRepository.findAll();
        assertThat(subTemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubTema() throws Exception {
        int databaseSizeBeforeUpdate = subTemaRepository.findAll().size();
        subTema.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubTemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subTema))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubTema in the database
        List<SubTema> subTemaList = subTemaRepository.findAll();
        assertThat(subTemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubTema() throws Exception {
        int databaseSizeBeforeUpdate = subTemaRepository.findAll().size();
        subTema.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubTemaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subTema)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubTema in the database
        List<SubTema> subTemaList = subTemaRepository.findAll();
        assertThat(subTemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubTemaWithPatch() throws Exception {
        // Initialize the database
        subTemaRepository.saveAndFlush(subTema);

        int databaseSizeBeforeUpdate = subTemaRepository.findAll().size();

        // Update the subTema using partial update
        SubTema partialUpdatedSubTema = new SubTema();
        partialUpdatedSubTema.setId(subTema.getId());

        partialUpdatedSubTema.descricao(UPDATED_DESCRICAO);

        restSubTemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubTema.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubTema))
            )
            .andExpect(status().isOk());

        // Validate the SubTema in the database
        List<SubTema> subTemaList = subTemaRepository.findAll();
        assertThat(subTemaList).hasSize(databaseSizeBeforeUpdate);
        SubTema testSubTema = subTemaList.get(subTemaList.size() - 1);
        assertThat(testSubTema.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testSubTema.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateSubTemaWithPatch() throws Exception {
        // Initialize the database
        subTemaRepository.saveAndFlush(subTema);

        int databaseSizeBeforeUpdate = subTemaRepository.findAll().size();

        // Update the subTema using partial update
        SubTema partialUpdatedSubTema = new SubTema();
        partialUpdatedSubTema.setId(subTema.getId());

        partialUpdatedSubTema.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);

        restSubTemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubTema.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubTema))
            )
            .andExpect(status().isOk());

        // Validate the SubTema in the database
        List<SubTema> subTemaList = subTemaRepository.findAll();
        assertThat(subTemaList).hasSize(databaseSizeBeforeUpdate);
        SubTema testSubTema = subTemaList.get(subTemaList.size() - 1);
        assertThat(testSubTema.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testSubTema.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingSubTema() throws Exception {
        int databaseSizeBeforeUpdate = subTemaRepository.findAll().size();
        subTema.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubTemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subTema.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subTema))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubTema in the database
        List<SubTema> subTemaList = subTemaRepository.findAll();
        assertThat(subTemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubTema() throws Exception {
        int databaseSizeBeforeUpdate = subTemaRepository.findAll().size();
        subTema.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubTemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subTema))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubTema in the database
        List<SubTema> subTemaList = subTemaRepository.findAll();
        assertThat(subTemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubTema() throws Exception {
        int databaseSizeBeforeUpdate = subTemaRepository.findAll().size();
        subTema.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubTemaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(subTema)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubTema in the database
        List<SubTema> subTemaList = subTemaRepository.findAll();
        assertThat(subTemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubTema() throws Exception {
        // Initialize the database
        subTemaRepository.saveAndFlush(subTema);

        int databaseSizeBeforeDelete = subTemaRepository.findAll().size();

        // Delete the subTema
        restSubTemaMockMvc
            .perform(delete(ENTITY_API_URL_ID, subTema.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubTema> subTemaList = subTemaRepository.findAll();
        assertThat(subTemaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
