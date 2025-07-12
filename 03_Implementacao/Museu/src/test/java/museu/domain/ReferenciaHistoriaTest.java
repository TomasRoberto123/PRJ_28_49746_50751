package museu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import museu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReferenciaHistoriaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReferenciaHistoria.class);
        ReferenciaHistoria referenciaHistoria1 = new ReferenciaHistoria();
        referenciaHistoria1.setId(1L);
        ReferenciaHistoria referenciaHistoria2 = new ReferenciaHistoria();
        referenciaHistoria2.setId(referenciaHistoria1.getId());
        assertThat(referenciaHistoria1).isEqualTo(referenciaHistoria2);
        referenciaHistoria2.setId(2L);
        assertThat(referenciaHistoria1).isNotEqualTo(referenciaHistoria2);
        referenciaHistoria1.setId(null);
        assertThat(referenciaHistoria1).isNotEqualTo(referenciaHistoria2);
    }
}
