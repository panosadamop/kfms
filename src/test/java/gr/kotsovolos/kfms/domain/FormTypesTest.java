package gr.kotsovolos.kfms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import gr.kotsovolos.kfms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormTypesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormTypes.class);
        FormTypes formTypes1 = new FormTypes();
        formTypes1.setId(1L);
        FormTypes formTypes2 = new FormTypes();
        formTypes2.setId(formTypes1.getId());
        assertThat(formTypes1).isEqualTo(formTypes2);
        formTypes2.setId(2L);
        assertThat(formTypes1).isNotEqualTo(formTypes2);
        formTypes1.setId(null);
        assertThat(formTypes1).isNotEqualTo(formTypes2);
    }
}
