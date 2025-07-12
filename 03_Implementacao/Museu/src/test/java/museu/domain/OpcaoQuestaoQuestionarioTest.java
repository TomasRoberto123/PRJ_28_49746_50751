package museu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import museu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OpcaoQuestaoQuestionarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OpcaoQuestaoQuestionario.class);
        OpcaoQuestaoQuestionario opcaoQuestaoQuestionario1 = new OpcaoQuestaoQuestionario();
        opcaoQuestaoQuestionario1.setId(1L);
        OpcaoQuestaoQuestionario opcaoQuestaoQuestionario2 = new OpcaoQuestaoQuestionario();
        opcaoQuestaoQuestionario2.setId(opcaoQuestaoQuestionario1.getId());
        assertThat(opcaoQuestaoQuestionario1).isEqualTo(opcaoQuestaoQuestionario2);
        opcaoQuestaoQuestionario2.setId(2L);
        assertThat(opcaoQuestaoQuestionario1).isNotEqualTo(opcaoQuestaoQuestionario2);
        opcaoQuestaoQuestionario1.setId(null);
        assertThat(opcaoQuestaoQuestionario1).isNotEqualTo(opcaoQuestaoQuestionario2);
    }
}
