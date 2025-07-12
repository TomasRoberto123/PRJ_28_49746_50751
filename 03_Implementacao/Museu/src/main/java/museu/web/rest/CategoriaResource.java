package museu.web.rest;

import com.sun.mail.imap.protocol.Item;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import museu.domain.Categoria;
import museu.domain.Instrumento;
import museu.domain.SubTema;
import museu.repository.CategoriaRepository;
import museu.repository.InstrumentoRepository;
import museu.repository.SubTemaRepository;
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
 * REST controller for managing {@link museu.domain.Categoria}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CategoriaResource {

    private final Logger log = LoggerFactory.getLogger(CategoriaResource.class);

    private static final String ENTITY_NAME = "categoria";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoriaRepository categoriaRepository;
    private final SubTemaRepository subTemaRepository;

    private final InstrumentoRepository instrumentoRepository;

    public CategoriaResource(
        CategoriaRepository categoriaRepository,
        SubTemaRepository subTemaRepository,
        InstrumentoRepository instrumentoRepository
    ) {
        this.categoriaRepository = categoriaRepository;
        this.subTemaRepository = subTemaRepository;
        this.instrumentoRepository = instrumentoRepository;
    }

    /**
     * {@code GET  /categorias} : get all the categorias.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categorias in body.
     */
    @GetMapping("/categorias")
    public List<Categoria> getAllCategorias(@RequestParam(required = false) String filter) {
        log.debug("REST request to get all Categorias");
        List<Categoria> categorias = categoriaRepository.findAll();
        categorias.stream().forEach(item -> item.setSubTemas(this.subTemaRepository.findAllByCategoriaId(item.getId())));
        return categorias;
    }

    /**
     * {@code GET  /categorias/:id} : get the "id" categoria.
     *
     * @param id the id of the categoria to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoria, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categorias/{id}")
    public ResponseEntity<Categoria> getCategoria(@PathVariable Long id) {
        log.debug("REST request to get Categoria : {}", id);
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        categoria.get().setSubTemas(this.subTemaRepository.findAllByCategoriaId(id));
        return ResponseUtil.wrapOrNotFound(categoria);
    }
}
