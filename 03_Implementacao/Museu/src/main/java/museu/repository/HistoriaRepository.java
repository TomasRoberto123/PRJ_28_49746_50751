package museu.repository;

import museu.domain.Historia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Historia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriaRepository extends JpaRepository<Historia, Long> {}
