package museu.repository;

import java.util.Set;
import museu.domain.ReferenciaHistoria;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReferenciaHistoria entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReferenciaHistoriaRepository extends JpaRepository<ReferenciaHistoria, Long> {
    Set<ReferenciaHistoria> findAllByHistoriaId(Long id);
}
