package museu.repository;

import java.util.List;
import museu.domain.OpcaoQuestaoQuestionario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OpcaoQuestaoQuestionario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OpcaoQuestaoQuestionarioRepository extends JpaRepository<OpcaoQuestaoQuestionario, Long> {
    List<OpcaoQuestaoQuestionario> findAllByQuestaoQuestionarioId(Long id);
}
