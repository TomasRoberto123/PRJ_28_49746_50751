package museu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import museu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BibliografiaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bibliografia.class);
        Bibliografia bibliografia1 = new Bibliografia();
        bibliografia1.setId(1L);
        Bibliografia bibliografia2 = new Bibliografia();
        bibliografia2.setId(bibliografia1.getId());
        assertThat(bibliografia1).isEqualTo(bibliografia2);
        bibliografia2.setId(2L);
        assertThat(bibliografia1).isNotEqualTo(bibliografia2);
        bibliografia1.setId(null);
        assertThat(bibliografia1).isNotEqualTo(bibliografia2);
    }
}
