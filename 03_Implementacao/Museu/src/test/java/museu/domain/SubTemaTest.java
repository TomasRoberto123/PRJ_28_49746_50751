package museu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import museu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubTemaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubTema.class);
        SubTema subTema1 = new SubTema();
        subTema1.setId(1L);
        SubTema subTema2 = new SubTema();
        subTema2.setId(subTema1.getId());
        assertThat(subTema1).isEqualTo(subTema2);
        subTema2.setId(2L);
        assertThat(subTema1).isNotEqualTo(subTema2);
        subTema1.setId(null);
        assertThat(subTema1).isNotEqualTo(subTema2);
    }
}
