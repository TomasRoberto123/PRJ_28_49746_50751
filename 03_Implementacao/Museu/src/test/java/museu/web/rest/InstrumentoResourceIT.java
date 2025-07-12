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
import museu.domain.Instrumento;
import museu.repository.InstrumentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InstrumentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InstrumentoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_LOCALIZACAO = "AAAAAAAAAA";
    private static final String UPDATED_LOCALIZACAO = "BBBBBBBBBB";

    private static final String DEFAULT_FABRICANTE = "AAAAAAAAAA";
    private static final String UPDATED_FABRICANTE = "BBBBBBBBBB";

    private static final String DEFAULT_DIMENSOES = "AAAAAAAAAA";
    private static final String UPDATED_DIMENSOES = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO_PATH = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/instrumentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInstrumentoMockMvc;

    private Instrumento instrumento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instrumento createEntity(EntityManager em) {
        Instrumento instrumento = new Instrumento()
            .nome(DEFAULT_NOME)
            .codigo(DEFAULT_CODIGO)
            .localizacao(DEFAULT_LOCALIZACAO)
            .fabricante(DEFAULT_FABRICANTE)
            .dimensoes(DEFAULT_DIMENSOES)
            .descricaoPath(DEFAULT_DESCRICAO_PATH);
        return instrumento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instrumento createUpdatedEntity(EntityManager em) {
        Instrumento instrumento = new Instrumento()
            .nome(UPDATED_NOME)
            .codigo(UPDATED_CODIGO)
            .localizacao(UPDATED_LOCALIZACAO)
            .fabricante(UPDATED_FABRICANTE)
            .dimensoes(UPDATED_DIMENSOES)
            .descricaoPath(UPDATED_DESCRICAO_PATH);
        return instrumento;
    }

    @BeforeEach
    public void initTest() {
        instrumento = createEntity(em);
    }

    @Test
    @Transactional
    void createInstrumento() throws Exception {
        int databaseSizeBeforeCreate = instrumentoRepository.findAll().size();
        // Create the Instrumento
        restInstrumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instrumento)))
            .andExpect(status().isCreated());

        // Validate the Instrumento in the database
        List<Instrumento> instrumentoList = instrumentoRepository.findAll();
        assertThat(instrumentoList).hasSize(databaseSizeBeforeCreate + 1);
        Instrumento testInstrumento = instrumentoList.get(instrumentoList.size() - 1);
        assertThat(testInstrumento.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testInstrumento.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testInstrumento.getLocalizacao()).isEqualTo(DEFAULT_LOCALIZACAO);
        assertThat(testInstrumento.getFabricante()).isEqualTo(DEFAULT_FABRICANTE);
        assertThat(testInstrumento.getDimensoes()).isEqualTo(DEFAULT_DIMENSOES);
        assertThat(testInstrumento.getDescricaoPath()).isEqualTo(DEFAULT_DESCRICAO_PATH);
    }

    @Test
    @Transactional
    void createInstrumentoWithExistingId() throws Exception {
        // Create the Instrumento with an existing ID
        instrumento.setId(1L);

        int databaseSizeBeforeCreate = instrumentoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstrumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instrumento)))
            .andExpect(status().isBadRequest());

        // Validate the Instrumento in the database
        List<Instrumento> instrumentoList = instrumentoRepository.findAll();
        assertThat(instrumentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInstrumentos() throws Exception {
        // Initialize the database
        instrumentoRepository.saveAndFlush(instrumento);

        // Get all the instrumentoList
        restInstrumentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instrumento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].localizacao").value(hasItem(DEFAULT_LOCALIZACAO)))
            .andExpect(jsonPath("$.[*].fabricante").value(hasItem(DEFAULT_FABRICANTE)))
            .andExpect(jsonPath("$.[*].dimensoes").value(hasItem(DEFAULT_DIMENSOES)))
            .andExpect(jsonPath("$.[*].descricaoPath").value(hasItem(DEFAULT_DESCRICAO_PATH)));
    }

    @Test
    @Transactional
    void getInstrumento() throws Exception {
        // Initialize the database
        instrumentoRepository.saveAndFlush(instrumento);

        // Get the instrumento
        restInstrumentoMockMvc
            .perform(get(ENTITY_API_URL_ID, instrumento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(instrumento.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.localizacao").value(DEFAULT_LOCALIZACAO))
            .andExpect(jsonPath("$.fabricante").value(DEFAULT_FABRICANTE))
            .andExpect(jsonPath("$.dimensoes").value(DEFAULT_DIMENSOES))
            .andExpect(jsonPath("$.descricaoPath").value(DEFAULT_DESCRICAO_PATH));
    }

    @Test
    @Transactional
    void getNonExistingInstrumento() throws Exception {
        // Get the instrumento
        restInstrumentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInstrumento() throws Exception {
        // Initialize the database
        instrumentoRepository.saveAndFlush(instrumento);

        int databaseSizeBeforeUpdate = instrumentoRepository.findAll().size();

        // Update the instrumento
        Instrumento updatedInstrumento = instrumentoRepository.findById(instrumento.getId()).get();
        // Disconnect from session so that the updates on updatedInstrumento are not directly saved in db
        em.detach(updatedInstrumento);
        updatedInstrumento
            .nome(UPDATED_NOME)
            .codigo(UPDATED_CODIGO)
            .localizacao(UPDATED_LOCALIZACAO)
            .fabricante(UPDATED_FABRICANTE)
            .dimensoes(UPDATED_DIMENSOES)
            .descricaoPath(UPDATED_DESCRICAO_PATH);

        restInstrumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInstrumento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInstrumento))
            )
            .andExpect(status().isOk());

        // Validate the Instrumento in the database
        List<Instrumento> instrumentoList = instrumentoRepository.findAll();
        assertThat(instrumentoList).hasSize(databaseSizeBeforeUpdate);
        Instrumento testInstrumento = instrumentoList.get(instrumentoList.size() - 1);
        assertThat(testInstrumento.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testInstrumento.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testInstrumento.getLocalizacao()).isEqualTo(UPDATED_LOCALIZACAO);
        assertThat(testInstrumento.getFabricante()).isEqualTo(UPDATED_FABRICANTE);
        assertThat(testInstrumento.getDimensoes()).isEqualTo(UPDATED_DIMENSOES);
        assertThat(testInstrumento.getDescricaoPath()).isEqualTo(UPDATED_DESCRICAO_PATH);
    }

    @Test
    @Transactional
    void putNonExistingInstrumento() throws Exception {
        int databaseSizeBeforeUpdate = instrumentoRepository.findAll().size();
        instrumento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstrumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, instrumento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(instrumento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instrumento in the database
        List<Instrumento> instrumentoList = instrumentoRepository.findAll();
        assertThat(instrumentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInstrumento() throws Exception {
        int databaseSizeBeforeUpdate = instrumentoRepository.findAll().size();
        instrumento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstrumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(instrumento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instrumento in the database
        List<Instrumento> instrumentoList = instrumentoRepository.findAll();
        assertThat(instrumentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInstrumento() throws Exception {
        int databaseSizeBeforeUpdate = instrumentoRepository.findAll().size();
        instrumento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstrumentoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instrumento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Instrumento in the database
        List<Instrumento> instrumentoList = instrumentoRepository.findAll();
        assertThat(instrumentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInstrumentoWithPatch() throws Exception {
        // Initialize the database
        instrumentoRepository.saveAndFlush(instrumento);

        int databaseSizeBeforeUpdate = instrumentoRepository.findAll().size();

        // Update the instrumento using partial update
        Instrumento partialUpdatedInstrumento = new Instrumento();
        partialUpdatedInstrumento.setId(instrumento.getId());

        partialUpdatedInstrumento
            .codigo(UPDATED_CODIGO)
            .localizacao(UPDATED_LOCALIZACAO)
            .dimensoes(UPDATED_DIMENSOES)
            .descricaoPath(UPDATED_DESCRICAO_PATH);

        restInstrumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstrumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstrumento))
            )
            .andExpect(status().isOk());

        // Validate the Instrumento in the database
        List<Instrumento> instrumentoList = instrumentoRepository.findAll();
        assertThat(instrumentoList).hasSize(databaseSizeBeforeUpdate);
        Instrumento testInstrumento = instrumentoList.get(instrumentoList.size() - 1);
        assertThat(testInstrumento.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testInstrumento.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testInstrumento.getLocalizacao()).isEqualTo(UPDATED_LOCALIZACAO);
        assertThat(testInstrumento.getFabricante()).isEqualTo(DEFAULT_FABRICANTE);
        assertThat(testInstrumento.getDimensoes()).isEqualTo(UPDATED_DIMENSOES);
        assertThat(testInstrumento.getDescricaoPath()).isEqualTo(UPDATED_DESCRICAO_PATH);
    }

    @Test
    @Transactional
    void fullUpdateInstrumentoWithPatch() throws Exception {
        // Initialize the database
        instrumentoRepository.saveAndFlush(instrumento);

        int databaseSizeBeforeUpdate = instrumentoRepository.findAll().size();

        // Update the instrumento using partial update
        Instrumento partialUpdatedInstrumento = new Instrumento();
        partialUpdatedInstrumento.setId(instrumento.getId());

        partialUpdatedInstrumento
            .nome(UPDATED_NOME)
            .codigo(UPDATED_CODIGO)
            .localizacao(UPDATED_LOCALIZACAO)
            .fabricante(UPDATED_FABRICANTE)
            .dimensoes(UPDATED_DIMENSOES)
            .descricaoPath(UPDATED_DESCRICAO_PATH);

        restInstrumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstrumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstrumento))
            )
            .andExpect(status().isOk());

        // Validate the Instrumento in the database
        List<Instrumento> instrumentoList = instrumentoRepository.findAll();
        assertThat(instrumentoList).hasSize(databaseSizeBeforeUpdate);
        Instrumento testInstrumento = instrumentoList.get(instrumentoList.size() - 1);
        assertThat(testInstrumento.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testInstrumento.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testInstrumento.getLocalizacao()).isEqualTo(UPDATED_LOCALIZACAO);
        assertThat(testInstrumento.getFabricante()).isEqualTo(UPDATED_FABRICANTE);
        assertThat(testInstrumento.getDimensoes()).isEqualTo(UPDATED_DIMENSOES);
        assertThat(testInstrumento.getDescricaoPath()).isEqualTo(UPDATED_DESCRICAO_PATH);
    }

    @Test
    @Transactional
    void patchNonExistingInstrumento() throws Exception {
        int databaseSizeBeforeUpdate = instrumentoRepository.findAll().size();
        instrumento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstrumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, instrumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(instrumento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instrumento in the database
        List<Instrumento> instrumentoList = instrumentoRepository.findAll();
        assertThat(instrumentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInstrumento() throws Exception {
        int databaseSizeBeforeUpdate = instrumentoRepository.findAll().size();
        instrumento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstrumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(instrumento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instrumento in the database
        List<Instrumento> instrumentoList = instrumentoRepository.findAll();
        assertThat(instrumentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInstrumento() throws Exception {
        int databaseSizeBeforeUpdate = instrumentoRepository.findAll().size();
        instrumento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstrumentoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(instrumento))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Instrumento in the database
        List<Instrumento> instrumentoList = instrumentoRepository.findAll();
        assertThat(instrumentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInstrumento() throws Exception {
        // Initialize the database
        instrumentoRepository.saveAndFlush(instrumento);

        int databaseSizeBeforeDelete = instrumentoRepository.findAll().size();

        // Delete the instrumento
        restInstrumentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, instrumento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Instrumento> instrumentoList = instrumentoRepository.findAll();
        assertThat(instrumentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
