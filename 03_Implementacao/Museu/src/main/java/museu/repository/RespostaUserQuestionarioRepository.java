package museu.repository;

import java.util.List;
import museu.domain.RespostaUserQuestionario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RespostaUserQuestionario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RespostaUserQuestionarioRepository extends JpaRepository<RespostaUserQuestionario, Long> {
    @Query(
        "select respostaUserQuestionario from RespostaUserQuestionario respostaUserQuestionario where respostaUserQuestionario.user.login = ?#{principal.username}"
    )
    List<RespostaUserQuestionario> findByUserIsCurrentUser();

    List<RespostaUserQuestionario> findAllByUserId(Long id);
}
