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
import museu.domain.Foto;
import museu.repository.FotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FotoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FotoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGEM_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGEM_PATH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_FIGURA = false;
    private static final Boolean UPDATED_IS_FIGURA = true;

    private static final String DEFAULT_LEGENDA = "AAAAAAAAAA";
    private static final String UPDATED_LEGENDA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fotos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FotoRepository fotoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFotoMockMvc;

    private Foto foto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Foto createEntity(EntityManager em) {
        Foto foto = new Foto().nome(DEFAULT_NOME).imagemPath(DEFAULT_IMAGEM_PATH).isFigura(DEFAULT_IS_FIGURA).legenda(DEFAULT_LEGENDA);
        return foto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Foto createUpdatedEntity(EntityManager em) {
        Foto foto = new Foto().nome(UPDATED_NOME).imagemPath(UPDATED_IMAGEM_PATH).isFigura(UPDATED_IS_FIGURA).legenda(UPDATED_LEGENDA);
        return foto;
    }

    @BeforeEach
    public void initTest() {
        foto = createEntity(em);
    }

    @Test
    @Transactional
    void createFoto() throws Exception {
        int databaseSizeBeforeCreate = fotoRepository.findAll().size();
        // Create the Foto
        restFotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foto)))
            .andExpect(status().isCreated());

        // Validate the Foto in the database
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeCreate + 1);
        Foto testFoto = fotoList.get(fotoList.size() - 1);
        assertThat(testFoto.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFoto.getImagemPath()).isEqualTo(DEFAULT_IMAGEM_PATH);
        assertThat(testFoto.getIsFigura()).isEqualTo(DEFAULT_IS_FIGURA);
        assertThat(testFoto.getLegenda()).isEqualTo(DEFAULT_LEGENDA);
    }

    @Test
    @Transactional
    void createFotoWithExistingId() throws Exception {
        // Create the Foto with an existing ID
        foto.setId(1L);

        int databaseSizeBeforeCreate = fotoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foto)))
            .andExpect(status().isBadRequest());

        // Validate the Foto in the database
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFotos() throws Exception {
        // Initialize the database
        fotoRepository.saveAndFlush(foto);

        // Get all the fotoList
        restFotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].imagemPath").value(hasItem(DEFAULT_IMAGEM_PATH)))
            .andExpect(jsonPath("$.[*].isFigura").value(hasItem(DEFAULT_IS_FIGURA.booleanValue())))
            .andExpect(jsonPath("$.[*].legenda").value(hasItem(DEFAULT_LEGENDA)));
    }

    @Test
    @Transactional
    void getFoto() throws Exception {
        // Initialize the database
        fotoRepository.saveAndFlush(foto);

        // Get the foto
        restFotoMockMvc
            .perform(get(ENTITY_API_URL_ID, foto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(foto.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.imagemPath").value(DEFAULT_IMAGEM_PATH))
            .andExpect(jsonPath("$.isFigura").value(DEFAULT_IS_FIGURA.booleanValue()))
            .andExpect(jsonPath("$.legenda").value(DEFAULT_LEGENDA));
    }

    @Test
    @Transactional
    void getNonExistingFoto() throws Exception {
        // Get the foto
        restFotoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFoto() throws Exception {
        // Initialize the database
        fotoRepository.saveAndFlush(foto);

        int databaseSizeBeforeUpdate = fotoRepository.findAll().size();

        // Update the foto
        Foto updatedFoto = fotoRepository.findById(foto.getId()).get();
        // Disconnect from session so that the updates on updatedFoto are not directly saved in db
        em.detach(updatedFoto);
        updatedFoto.nome(UPDATED_NOME).imagemPath(UPDATED_IMAGEM_PATH).isFigura(UPDATED_IS_FIGURA).legenda(UPDATED_LEGENDA);

        restFotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFoto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFoto))
            )
            .andExpect(status().isOk());

        // Validate the Foto in the database
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeUpdate);
        Foto testFoto = fotoList.get(fotoList.size() - 1);
        assertThat(testFoto.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFoto.getImagemPath()).isEqualTo(UPDATED_IMAGEM_PATH);
        assertThat(testFoto.getIsFigura()).isEqualTo(UPDATED_IS_FIGURA);
        assertThat(testFoto.getLegenda()).isEqualTo(UPDATED_LEGENDA);
    }

    @Test
    @Transactional
    void putNonExistingFoto() throws Exception {
        int databaseSizeBeforeUpdate = fotoRepository.findAll().size();
        foto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Foto in the database
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFoto() throws Exception {
        int databaseSizeBeforeUpdate = fotoRepository.findAll().size();
        foto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Foto in the database
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFoto() throws Exception {
        int databaseSizeBeforeUpdate = fotoRepository.findAll().size();
        foto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFotoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Foto in the database
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFotoWithPatch() throws Exception {
        // Initialize the database
        fotoRepository.saveAndFlush(foto);

        int databaseSizeBeforeUpdate = fotoRepository.findAll().size();

        // Update the foto using partial update
        Foto partialUpdatedFoto = new Foto();
        partialUpdatedFoto.setId(foto.getId());

        partialUpdatedFoto.imagemPath(UPDATED_IMAGEM_PATH).legenda(UPDATED_LEGENDA);

        restFotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoto))
            )
            .andExpect(status().isOk());

        // Validate the Foto in the database
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeUpdate);
        Foto testFoto = fotoList.get(fotoList.size() - 1);
        assertThat(testFoto.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFoto.getImagemPath()).isEqualTo(UPDATED_IMAGEM_PATH);
        assertThat(testFoto.getIsFigura()).isEqualTo(DEFAULT_IS_FIGURA);
        assertThat(testFoto.getLegenda()).isEqualTo(UPDATED_LEGENDA);
    }

    @Test
    @Transactional
    void fullUpdateFotoWithPatch() throws Exception {
        // Initialize the database
        fotoRepository.saveAndFlush(foto);

        int databaseSizeBeforeUpdate = fotoRepository.findAll().size();

        // Update the foto using partial update
        Foto partialUpdatedFoto = new Foto();
        partialUpdatedFoto.setId(foto.getId());

        partialUpdatedFoto.nome(UPDATED_NOME).imagemPath(UPDATED_IMAGEM_PATH).isFigura(UPDATED_IS_FIGURA).legenda(UPDATED_LEGENDA);

        restFotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoto))
            )
            .andExpect(status().isOk());

        // Validate the Foto in the database
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeUpdate);
        Foto testFoto = fotoList.get(fotoList.size() - 1);
        assertThat(testFoto.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFoto.getImagemPath()).isEqualTo(UPDATED_IMAGEM_PATH);
        assertThat(testFoto.getIsFigura()).isEqualTo(UPDATED_IS_FIGURA);
        assertThat(testFoto.getLegenda()).isEqualTo(UPDATED_LEGENDA);
    }

    @Test
    @Transactional
    void patchNonExistingFoto() throws Exception {
        int databaseSizeBeforeUpdate = fotoRepository.findAll().size();
        foto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, foto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Foto in the database
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFoto() throws Exception {
        int databaseSizeBeforeUpdate = fotoRepository.findAll().size();
        foto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Foto in the database
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFoto() throws Exception {
        int databaseSizeBeforeUpdate = fotoRepository.findAll().size();
        foto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFotoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(foto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Foto in the database
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFoto() throws Exception {
        // Initialize the database
        fotoRepository.saveAndFlush(foto);

        int databaseSizeBeforeDelete = fotoRepository.findAll().size();

        // Delete the foto
        restFotoMockMvc
            .perform(delete(ENTITY_API_URL_ID, foto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Foto> fotoList = fotoRepository.findAll();
        assertThat(fotoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
