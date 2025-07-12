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
import museu.domain.OpcaoQuestaoQuestionario;
import museu.repository.OpcaoQuestaoQuestionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OpcaoQuestaoQuestionarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OpcaoQuestaoQuestionarioResourceIT {

    private static final String DEFAULT_OPCAO_TEXTO = "AAAAAAAAAA";
    private static final String UPDATED_OPCAO_TEXTO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CORRECTA = false;
    private static final Boolean UPDATED_CORRECTA = true;

    private static final String ENTITY_API_URL = "/api/opcao-questao-questionarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OpcaoQuestaoQuestionarioRepository opcaoQuestaoQuestionarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOpcaoQuestaoQuestionarioMockMvc;

    private OpcaoQuestaoQuestionario opcaoQuestaoQuestionario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OpcaoQuestaoQuestionario createEntity(EntityManager em) {
        OpcaoQuestaoQuestionario opcaoQuestaoQuestionario = new OpcaoQuestaoQuestionario()
            .opcaoTexto(DEFAULT_OPCAO_TEXTO)
            .correcta(DEFAULT_CORRECTA);
        return opcaoQuestaoQuestionario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OpcaoQuestaoQuestionario createUpdatedEntity(EntityManager em) {
        OpcaoQuestaoQuestionario opcaoQuestaoQuestionario = new OpcaoQuestaoQuestionario()
            .opcaoTexto(UPDATED_OPCAO_TEXTO)
            .correcta(UPDATED_CORRECTA);
        return opcaoQuestaoQuestionario;
    }

    @BeforeEach
    public void initTest() {
        opcaoQuestaoQuestionario = createEntity(em);
    }

    @Test
    @Transactional
    void createOpcaoQuestaoQuestionario() throws Exception {
        int databaseSizeBeforeCreate = opcaoQuestaoQuestionarioRepository.findAll().size();
        // Create the OpcaoQuestaoQuestionario
        restOpcaoQuestaoQuestionarioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opcaoQuestaoQuestionario))
            )
            .andExpect(status().isCreated());

        // Validate the OpcaoQuestaoQuestionario in the database
        List<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionarioList = opcaoQuestaoQuestionarioRepository.findAll();
        assertThat(opcaoQuestaoQuestionarioList).hasSize(databaseSizeBeforeCreate + 1);
        OpcaoQuestaoQuestionario testOpcaoQuestaoQuestionario = opcaoQuestaoQuestionarioList.get(opcaoQuestaoQuestionarioList.size() - 1);
        assertThat(testOpcaoQuestaoQuestionario.getOpcaoTexto()).isEqualTo(DEFAULT_OPCAO_TEXTO);
        assertThat(testOpcaoQuestaoQuestionario.getCorrecta()).isEqualTo(DEFAULT_CORRECTA);
    }

    @Test
    @Transactional
    void createOpcaoQuestaoQuestionarioWithExistingId() throws Exception {
        // Create the OpcaoQuestaoQuestionario with an existing ID
        opcaoQuestaoQuestionario.setId(1L);

        int databaseSizeBeforeCreate = opcaoQuestaoQuestionarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpcaoQuestaoQuestionarioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opcaoQuestaoQuestionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the OpcaoQuestaoQuestionario in the database
        List<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionarioList = opcaoQuestaoQuestionarioRepository.findAll();
        assertThat(opcaoQuestaoQuestionarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOpcaoQuestaoQuestionarios() throws Exception {
        // Initialize the database
        opcaoQuestaoQuestionarioRepository.saveAndFlush(opcaoQuestaoQuestionario);

        // Get all the opcaoQuestaoQuestionarioList
        restOpcaoQuestaoQuestionarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opcaoQuestaoQuestionario.getId().intValue())))
            .andExpect(jsonPath("$.[*].opcaoTexto").value(hasItem(DEFAULT_OPCAO_TEXTO)))
            .andExpect(jsonPath("$.[*].correcta").value(hasItem(DEFAULT_CORRECTA.booleanValue())));
    }

    @Test
    @Transactional
    void getOpcaoQuestaoQuestionario() throws Exception {
        // Initialize the database
        opcaoQuestaoQuestionarioRepository.saveAndFlush(opcaoQuestaoQuestionario);

        // Get the opcaoQuestaoQuestionario
        restOpcaoQuestaoQuestionarioMockMvc
            .perform(get(ENTITY_API_URL_ID, opcaoQuestaoQuestionario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(opcaoQuestaoQuestionario.getId().intValue()))
            .andExpect(jsonPath("$.opcaoTexto").value(DEFAULT_OPCAO_TEXTO))
            .andExpect(jsonPath("$.correcta").value(DEFAULT_CORRECTA.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingOpcaoQuestaoQuestionario() throws Exception {
        // Get the opcaoQuestaoQuestionario
        restOpcaoQuestaoQuestionarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOpcaoQuestaoQuestionario() throws Exception {
        // Initialize the database
        opcaoQuestaoQuestionarioRepository.saveAndFlush(opcaoQuestaoQuestionario);

        int databaseSizeBeforeUpdate = opcaoQuestaoQuestionarioRepository.findAll().size();

        // Update the opcaoQuestaoQuestionario
        OpcaoQuestaoQuestionario updatedOpcaoQuestaoQuestionario = opcaoQuestaoQuestionarioRepository
            .findById(opcaoQuestaoQuestionario.getId())
            .get();
        // Disconnect from session so that the updates on updatedOpcaoQuestaoQuestionario are not directly saved in db
        em.detach(updatedOpcaoQuestaoQuestionario);
        updatedOpcaoQuestaoQuestionario.opcaoTexto(UPDATED_OPCAO_TEXTO).correcta(UPDATED_CORRECTA);

        restOpcaoQuestaoQuestionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOpcaoQuestaoQuestionario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOpcaoQuestaoQuestionario))
            )
            .andExpect(status().isOk());

        // Validate the OpcaoQuestaoQuestionario in the database
        List<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionarioList = opcaoQuestaoQuestionarioRepository.findAll();
        assertThat(opcaoQuestaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
        OpcaoQuestaoQuestionario testOpcaoQuestaoQuestionario = opcaoQuestaoQuestionarioList.get(opcaoQuestaoQuestionarioList.size() - 1);
        assertThat(testOpcaoQuestaoQuestionario.getOpcaoTexto()).isEqualTo(UPDATED_OPCAO_TEXTO);
        assertThat(testOpcaoQuestaoQuestionario.getCorrecta()).isEqualTo(UPDATED_CORRECTA);
    }

    @Test
    @Transactional
    void putNonExistingOpcaoQuestaoQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = opcaoQuestaoQuestionarioRepository.findAll().size();
        opcaoQuestaoQuestionario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpcaoQuestaoQuestionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, opcaoQuestaoQuestionario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opcaoQuestaoQuestionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the OpcaoQuestaoQuestionario in the database
        List<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionarioList = opcaoQuestaoQuestionarioRepository.findAll();
        assertThat(opcaoQuestaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOpcaoQuestaoQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = opcaoQuestaoQuestionarioRepository.findAll().size();
        opcaoQuestaoQuestionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcaoQuestaoQuestionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opcaoQuestaoQuestionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the OpcaoQuestaoQuestionario in the database
        List<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionarioList = opcaoQuestaoQuestionarioRepository.findAll();
        assertThat(opcaoQuestaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOpcaoQuestaoQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = opcaoQuestaoQuestionarioRepository.findAll().size();
        opcaoQuestaoQuestionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcaoQuestaoQuestionarioMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opcaoQuestaoQuestionario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OpcaoQuestaoQuestionario in the database
        List<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionarioList = opcaoQuestaoQuestionarioRepository.findAll();
        assertThat(opcaoQuestaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOpcaoQuestaoQuestionarioWithPatch() throws Exception {
        // Initialize the database
        opcaoQuestaoQuestionarioRepository.saveAndFlush(opcaoQuestaoQuestionario);

        int databaseSizeBeforeUpdate = opcaoQuestaoQuestionarioRepository.findAll().size();

        // Update the opcaoQuestaoQuestionario using partial update
        OpcaoQuestaoQuestionario partialUpdatedOpcaoQuestaoQuestionario = new OpcaoQuestaoQuestionario();
        partialUpdatedOpcaoQuestaoQuestionario.setId(opcaoQuestaoQuestionario.getId());

        restOpcaoQuestaoQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpcaoQuestaoQuestionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOpcaoQuestaoQuestionario))
            )
            .andExpect(status().isOk());

        // Validate the OpcaoQuestaoQuestionario in the database
        List<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionarioList = opcaoQuestaoQuestionarioRepository.findAll();
        assertThat(opcaoQuestaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
        OpcaoQuestaoQuestionario testOpcaoQuestaoQuestionario = opcaoQuestaoQuestionarioList.get(opcaoQuestaoQuestionarioList.size() - 1);
        assertThat(testOpcaoQuestaoQuestionario.getOpcaoTexto()).isEqualTo(DEFAULT_OPCAO_TEXTO);
        assertThat(testOpcaoQuestaoQuestionario.getCorrecta()).isEqualTo(DEFAULT_CORRECTA);
    }

    @Test
    @Transactional
    void fullUpdateOpcaoQuestaoQuestionarioWithPatch() throws Exception {
        // Initialize the database
        opcaoQuestaoQuestionarioRepository.saveAndFlush(opcaoQuestaoQuestionario);

        int databaseSizeBeforeUpdate = opcaoQuestaoQuestionarioRepository.findAll().size();

        // Update the opcaoQuestaoQuestionario using partial update
        OpcaoQuestaoQuestionario partialUpdatedOpcaoQuestaoQuestionario = new OpcaoQuestaoQuestionario();
        partialUpdatedOpcaoQuestaoQuestionario.setId(opcaoQuestaoQuestionario.getId());

        partialUpdatedOpcaoQuestaoQuestionario.opcaoTexto(UPDATED_OPCAO_TEXTO).correcta(UPDATED_CORRECTA);

        restOpcaoQuestaoQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpcaoQuestaoQuestionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOpcaoQuestaoQuestionario))
            )
            .andExpect(status().isOk());

        // Validate the OpcaoQuestaoQuestionario in the database
        List<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionarioList = opcaoQuestaoQuestionarioRepository.findAll();
        assertThat(opcaoQuestaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
        OpcaoQuestaoQuestionario testOpcaoQuestaoQuestionario = opcaoQuestaoQuestionarioList.get(opcaoQuestaoQuestionarioList.size() - 1);
        assertThat(testOpcaoQuestaoQuestionario.getOpcaoTexto()).isEqualTo(UPDATED_OPCAO_TEXTO);
        assertThat(testOpcaoQuestaoQuestionario.getCorrecta()).isEqualTo(UPDATED_CORRECTA);
    }

    @Test
    @Transactional
    void patchNonExistingOpcaoQuestaoQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = opcaoQuestaoQuestionarioRepository.findAll().size();
        opcaoQuestaoQuestionario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpcaoQuestaoQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, opcaoQuestaoQuestionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(opcaoQuestaoQuestionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the OpcaoQuestaoQuestionario in the database
        List<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionarioList = opcaoQuestaoQuestionarioRepository.findAll();
        assertThat(opcaoQuestaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOpcaoQuestaoQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = opcaoQuestaoQuestionarioRepository.findAll().size();
        opcaoQuestaoQuestionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcaoQuestaoQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(opcaoQuestaoQuestionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the OpcaoQuestaoQuestionario in the database
        List<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionarioList = opcaoQuestaoQuestionarioRepository.findAll();
        assertThat(opcaoQuestaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOpcaoQuestaoQuestionario() throws Exception {
        int databaseSizeBeforeUpdate = opcaoQuestaoQuestionarioRepository.findAll().size();
        opcaoQuestaoQuestionario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcaoQuestaoQuestionarioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(opcaoQuestaoQuestionario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OpcaoQuestaoQuestionario in the database
        List<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionarioList = opcaoQuestaoQuestionarioRepository.findAll();
        assertThat(opcaoQuestaoQuestionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOpcaoQuestaoQuestionario() throws Exception {
        // Initialize the database
        opcaoQuestaoQuestionarioRepository.saveAndFlush(opcaoQuestaoQuestionario);

        int databaseSizeBeforeDelete = opcaoQuestaoQuestionarioRepository.findAll().size();

        // Delete the opcaoQuestaoQuestionario
        restOpcaoQuestaoQuestionarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, opcaoQuestaoQuestionario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionarioList = opcaoQuestaoQuestionarioRepository.findAll();
        assertThat(opcaoQuestaoQuestionarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
