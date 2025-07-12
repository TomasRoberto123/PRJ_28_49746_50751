package museu.repository;

import java.util.List;
import java.util.Set;
import museu.domain.Foto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Foto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {
    Set<Foto> findByInstrumentoId(Long id);

    Set<Foto> findByHistoriaId(Long id);
}
