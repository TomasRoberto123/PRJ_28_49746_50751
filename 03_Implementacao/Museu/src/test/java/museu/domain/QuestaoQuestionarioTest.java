package museu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import museu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestaoQuestionarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestaoQuestionario.class);
        QuestaoQuestionario questaoQuestionario1 = new QuestaoQuestionario();
        questaoQuestionario1.setId(1L);
        QuestaoQuestionario questaoQuestionario2 = new QuestaoQuestionario();
        questaoQuestionario2.setId(questaoQuestionario1.getId());
        assertThat(questaoQuestionario1).isEqualTo(questaoQuestionario2);
        questaoQuestionario2.setId(2L);
        assertThat(questaoQuestionario1).isNotEqualTo(questaoQuestionario2);
        questaoQuestionario1.setId(null);
        assertThat(questaoQuestionario1).isNotEqualTo(questaoQuestionario2);
    }
}
