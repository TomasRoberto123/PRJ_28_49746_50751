package museu.repository;

import java.util.Set;
import museu.domain.QuestaoQuestionario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the QuestaoQuestionario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestaoQuestionarioRepository extends JpaRepository<QuestaoQuestionario, Long> {
    Set<QuestaoQuestionario> findAllByQuestionarioId(long id);
}
