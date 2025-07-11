package museu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import museu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FotoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Foto.class);
        Foto foto1 = new Foto();
        foto1.setId(1L);
        Foto foto2 = new Foto();
        foto2.setId(foto1.getId());
        assertThat(foto1).isEqualTo(foto2);
        foto2.setId(2L);
        assertThat(foto1).isNotEqualTo(foto2);
        foto1.setId(null);
        assertThat(foto1).isNotEqualTo(foto2);
    }
}
