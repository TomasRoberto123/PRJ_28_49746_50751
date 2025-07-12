package museu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import museu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Questionario.class);
        Questionario questionario1 = new Questionario();
        questionario1.setId(1L);
        Questionario questionario2 = new Questionario();
        questionario2.setId(questionario1.getId());
        assertThat(questionario1).isEqualTo(questionario2);
        questionario2.setId(2L);
        assertThat(questionario1).isNotEqualTo(questionario2);
        questionario1.setId(null);
        assertThat(questionario1).isNotEqualTo(questionario2);
    }
}
