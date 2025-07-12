package museu.repository;

import java.util.List;
import java.util.Set;
import museu.domain.SubTema;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SubTema entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubTemaRepository extends JpaRepository<SubTema, Long> {
    Set<SubTema> findAllByCategoriaId(Long id);
}
