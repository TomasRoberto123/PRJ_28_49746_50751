package museu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import museu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InstrumentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Instrumento.class);
        Instrumento instrumento1 = new Instrumento();
        instrumento1.setId(1L);
        Instrumento instrumento2 = new Instrumento();
        instrumento2.setId(instrumento1.getId());
        assertThat(instrumento1).isEqualTo(instrumento2);
        instrumento2.setId(2L);
        assertThat(instrumento1).isNotEqualTo(instrumento2);
        instrumento1.setId(null);
        assertThat(instrumento1).isNotEqualTo(instrumento2);
    }
}
