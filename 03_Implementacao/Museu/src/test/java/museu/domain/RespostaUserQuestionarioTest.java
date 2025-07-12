package museu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import museu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RespostaUserQuestionarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RespostaUserQuestionario.class);
        RespostaUserQuestionario respostaUserQuestionario1 = new RespostaUserQuestionario();
        respostaUserQuestionario1.setId(1L);
        RespostaUserQuestionario respostaUserQuestionario2 = new RespostaUserQuestionario();
        respostaUserQuestionario2.setId(respostaUserQuestionario1.getId());
        assertThat(respostaUserQuestionario1).isEqualTo(respostaUserQuestionario2);
        respostaUserQuestionario2.setId(2L);
        assertThat(respostaUserQuestionario1).isNotEqualTo(respostaUserQuestionario2);
        respostaUserQuestionario1.setId(null);
        assertThat(respostaUserQuestionario1).isNotEqualTo(respostaUserQuestionario2);
    }
}
