package museu.repository;

import java.util.List;
import java.util.Set;
import museu.domain.Instrumento;
import museu.domain.SubTema;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Instrumento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstrumentoRepository extends JpaRepository<Instrumento, Long> {
    List<Instrumento> findAllBySubTemaId(Long id);
}
