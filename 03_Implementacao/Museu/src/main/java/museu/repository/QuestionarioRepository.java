package museu.repository;

import museu.domain.Questionario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Questionario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionarioRepository extends JpaRepository<Questionario, Long> {}
