package museu.repository;

import museu.domain.Bibliografia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Bibliografia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BibliografiaRepository extends JpaRepository<Bibliografia, Long> {}
